package frc3824.databaserelay.Comms;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author frc3824
 * Created: 3/20/17
 */

public abstract class MessageBase {

    private static final String TAG = "MessageBase";

    public abstract String getType();

    public abstract JSONObject getData();

    public String toJson() {
        JSONObject j = new JSONObject();
        try {
            j.put("type", getType());
            j.put("data", getData());
        } catch (JSONException e) {
            Log.e(TAG, "Could not encode JSON");
        }
        return j.toString();
    }
}
