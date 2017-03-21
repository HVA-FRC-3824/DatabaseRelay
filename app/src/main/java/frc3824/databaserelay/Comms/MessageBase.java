package frc3824.databaserelay.Comms;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author frc3824
 * Created: 3/20/17
 */

public abstract class MessageBase {

    public abstract String getType();

    public abstract String getMessage();

    public String toJson() {
        JSONObject j = new JSONObject();
        try {
            j.put("type", getType());
            j.put("message", getMessage());
        } catch (JSONException e) {
            Log.e("MessageBase", "Could not encode JSON");
        }
        return j.toString();
    }
}
