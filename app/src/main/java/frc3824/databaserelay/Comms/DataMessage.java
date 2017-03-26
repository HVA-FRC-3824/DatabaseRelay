package frc3824.databaserelay.Comms;

import org.json.JSONObject;

/**
 * @author frc3824
 * Created: 3/26/17
 */

public class DataMessage extends MessageBase {
    private String mType;
    private JSONObject mData;

    public DataMessage(String type,JSONObject data) {
        mType = type;
        mData = data;
    }

    @Override
    public String getType() {
        return mType;
    }

    @Override
    public JSONObject getData() {
        return mData;
    }
}
