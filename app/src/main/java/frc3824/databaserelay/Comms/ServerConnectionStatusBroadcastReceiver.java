package frc3824.databaserelay.Comms;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

/**
 * @author frc3824
 * Created: 3/20/17
 */

public class ServerConnectionStatusBroadcastReceiver extends BroadcastReceiver {
    public static final String ACTION_SERVER_CONNECTED = "action_server_connected";
    public static final String ACTION_SERVER_DISCONNECTED = "action_server_disconnected";

    private ServerConnectionStateListener m_listener;

    public ServerConnectionStatusBroadcastReceiver(Context context, ServerConnectionStateListener listener) {
        this.m_listener = listener;

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_SERVER_CONNECTED);
        intentFilter.addAction(ACTION_SERVER_DISCONNECTED);
        context.registerReceiver(this, intentFilter);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (ACTION_SERVER_CONNECTED.equals(intent.getAction())) {
            m_listener.serverConnected();
        } else if (ACTION_SERVER_DISCONNECTED.equals(intent.getAction())) {
            m_listener.serverDisconnected();
        }
    }
}
