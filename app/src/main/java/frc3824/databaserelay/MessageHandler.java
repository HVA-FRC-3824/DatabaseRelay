package frc3824.databaserelay;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;


import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author frc3824
 * Created: 3/9/17
 */

class MessageHandler extends Handler{

    private final static String TAG = "MessageHandler";

    private SocketThread mSocket;

    public MessageHandler(Looper looper, SocketThread socketThread) {
        super(looper);
        mSocket = socketThread;
    }

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case Constants.Comms.Message_Type.COULD_NOT_CONNECT:
                displayText("Could not connect", Constants.Server_Log_Colors.RED);
                break;
            case Constants.Comms.Message_Type.DATA_SENT_OK:
                displayText("Data sent ok", Constants.Server_Log_Colors.GREEN);
                break;
            case Constants.Comms.Message_Type.SENDING_DATA:
                displayText("Sending data", Constants.Server_Log_Colors.BLUE);
                break;
            case Constants.Comms.Message_Type.DIGEST_DID_NOT_MATCH:
                displayText("Digest did not match", Constants.Server_Log_Colors.RED);
                break;
            case Constants.Comms.Message_Type.INVALID_HEADER:
                displayText("Invalid header", Constants.Server_Log_Colors.RED);
                break;
            case Constants.Comms.Message_Type.DATA_RECEIVED:
                String message = new String((byte[]) msg.obj);
                displayText(message);
                try {
                    JSONObject json = new JSONObject(message);
                    if(json.has("message_type")) {
                        switch (json.getString("message_type")){
                            // Heartbeat
                            case "ping":
                                mSocket.write("{\"message_type\": \"pong\"}");
                                break;
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    public void displayText(String message){
        Log.i(TAG, message);
    }

    public void displayText(String message, String color){
        Log.i(TAG, message);
    }
}
