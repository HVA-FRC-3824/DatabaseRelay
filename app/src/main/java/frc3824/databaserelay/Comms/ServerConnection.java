package frc3824.databaserelay.Comms;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

import frc3824.databaserelay.Constants;

/**
 * @author frc3824
 *         Created: 3/20/17
 */

public class ServerConnection {

    private static ServerConnection mSingleton = null;

    private static final String TAG = "ServerConnection";

    private int m_port;
    private String m_host;
    private Context m_context;
    private boolean m_running = true;
    private boolean m_connected = false;
    volatile private Socket m_socket;
    private Thread m_connect_thread, m_read_thread, m_write_thread;

    private long m_last_heartbeat_sent_at = System.currentTimeMillis();
    private long m_last_heartbeat_rcvd_at = 0;

    private ArrayBlockingQueue<MessageBase> mToSend = new ArrayBlockingQueue<>(30);


    protected class WriteThread implements Runnable {
        private static final String TAG = "WriteThread";

        @Override
        public void run() {
            while (m_running) {
                MessageBase nextToSend = null;
                try {
                    nextToSend = mToSend.poll(250, TimeUnit.MILLISECONDS);
                } catch (InterruptedException e) {
                    Log.e(TAG, "Couldn't poll queue");
                }

                if (nextToSend == null) {
                    continue;
                }

                sendToWire(nextToSend);
            }
        }
    }

    protected class ReadThread implements Runnable {
        private static final String TAG = "ReadThread";

        public void handleMessage(MessageBase message) {

            switch (message.getType()){
                case Constants.Comms.Message_Type.HEARTBEAT:
                    m_last_heartbeat_rcvd_at = System.currentTimeMillis();
                    break;
            }

            Log.w(TAG, message.getType() + " " + message.getMessage());
        }

        @Override
        public void run() {
            while (m_running) if (m_socket != null || m_connected) {
                BufferedReader reader;
                try {
                    InputStream is = m_socket.getInputStream();
                    reader = new BufferedReader(new InputStreamReader(is));
                } catch (IOException e) {
                    Log.e("ReadThread", "Could not get input stream");
                    continue;
                } catch (NullPointerException npe) {
                    Log.e("ReadThread", "socket was null");
                    continue;
                }
                String json_message = null;
                try {
                    json_message = reader.readLine();
                } catch (IOException e) {
                }
                if (json_message != null) {
                    OffWireMessage parsedMessage = new OffWireMessage(json_message);
                    if (parsedMessage.isValid()) {
                        handleMessage(parsedMessage);
                    }
                } else {
                    try {
                        Thread.sleep(100, 0);
                    } catch (InterruptedException e) {
                    }
                }
            }
        }
    }

    protected class ConnectionMonitor implements Runnable {
        @Override
        public void run() {
            while (m_running) {
                try {
                    if (m_socket == null || !m_socket.isConnected() && !m_connected) {
                        tryConnect();
                        Thread.sleep(250, 0);
                    }

                    long now = System.currentTimeMillis();

                    if (now - m_last_heartbeat_sent_at > Constants.Comms.SEND_HEARTBEAT_PERIOD) {
                        send(HeartbeatMessage.getInstance());
                        m_last_heartbeat_sent_at = now;
                    }

                    if (Math.abs(m_last_heartbeat_rcvd_at - m_last_heartbeat_sent_at) > Constants.Comms.HEARTBEAT_THRESHOLD && m_connected) {
                        m_connected = false;
                        broadcastRobotDisconnected();
                    }
                    if (Math.abs(m_last_heartbeat_rcvd_at - m_last_heartbeat_sent_at) < Constants.Comms.HEARTBEAT_THRESHOLD && !m_connected) {
                        m_connected = true;
                        broadcastRobotConnected();
                    }

                    Thread.sleep(Constants.Comms.CONNECTOR_SLEEP, 0);
                } catch (InterruptedException e) {
                }
            }
        }
    }

    public static ServerConnection getInstance() throws Exception {
        if(mSingleton == null){
            throw new Exception();
        }
        return mSingleton;
    }

    public static ServerConnection getInstance(Context context){
        if(mSingleton == null){
            mSingleton = new ServerConnection(context);
        }
        return mSingleton;
    }

    public static ServerConnection getInstance(Context context, String host, int port){
        if(mSingleton == null){
            mSingleton = new ServerConnection(context);
        }
        return mSingleton;
    }

    private ServerConnection(Context context, String host, int port) {
        m_context = context;
        m_host = host;
        m_port = port;
    }

    private ServerConnection(Context context) {
        this(context, Constants.Comms.HOST, Constants.Comms.PORT);
    }

    synchronized private void tryConnect() {
        if (m_socket == null) {
            try {
                m_socket = new Socket(m_host, m_port);
                m_socket.setSoTimeout(100);
            } catch (IOException e) {
                Log.w(TAG, "Could not connect");
                m_socket = null;
            }
        }
    }

    synchronized public void stop() {
        m_running = false;
        if (m_connect_thread != null && m_connect_thread.isAlive()) {
            try {
                m_connect_thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if (m_write_thread != null && m_write_thread.isAlive()) {
            try {
                m_write_thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if (m_read_thread != null && m_read_thread.isAlive()) {
            try {
                m_read_thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    synchronized public void start() {
        m_running = true;

        if (m_write_thread == null || !m_write_thread.isAlive()) {
            m_write_thread = new Thread(new WriteThread());
            m_write_thread.start();
        }

        if (m_read_thread == null || !m_read_thread.isAlive()) {
            m_read_thread = new Thread(new ReadThread());
            m_read_thread.start();
        }

        if (m_connect_thread == null || !m_connect_thread.isAlive()) {
            m_connect_thread = new Thread(new ConnectionMonitor());
            m_connect_thread.start();
        }
    }

    synchronized public void restart() {
        stop();
        start();
    }

    synchronized public boolean isConnected() {
        return m_socket != null && m_socket.isConnected() && m_connected;
    }

    private synchronized boolean sendToWire(MessageBase message) {
        String toSend = message.toJson() + "\n";
        if (m_socket != null && m_socket.isConnected()) {
            try {
                OutputStream os = m_socket.getOutputStream();
                os.write(toSend.getBytes());
                return true;
            } catch (IOException e) {
                Log.w("RobotConnection", "Could not send data to socket, try to reconnect");
                m_socket = null;
            }
        }
        return false;
    }

    public synchronized boolean send(MessageBase message) {
        return mToSend.offer(message);
    }

    public void broadcastRobotConnected() {
        Intent i = new Intent(ServerConnectionStatusBroadcastReceiver.ACTION_SERVER_CONNECTED);
        m_context.sendBroadcast(i);
    }

    public void broadcastRobotDisconnected() {
        Intent i = new Intent(ServerConnectionStatusBroadcastReceiver.ACTION_SERVER_DISCONNECTED);
        m_context.sendBroadcast(i);
    }
}
