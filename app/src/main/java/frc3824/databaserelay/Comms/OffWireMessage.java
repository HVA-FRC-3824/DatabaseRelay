package frc3824.databaserelay.Comms;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author frc3824
 * Created: 3/20/17
 */

public class OffWireMessage extends MessageBase {

    private String mType;
    private JSONObject mData = new JSONObject();
    private boolean mValid = false;

    public OffWireMessage(String message) {
        try {
            JSONObject reader = new JSONObject(message);
            mType = reader.getString("type");
            mData = reader.getJSONObject("data");
            mValid = true;
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public boolean isValid() {
        return mValid;
    }

    @Override
    public String getType() {
        return mType == null ? "unknown" : mType;
    }

    @Override
    public JSONObject getData() {
        return mData;
    }
}
