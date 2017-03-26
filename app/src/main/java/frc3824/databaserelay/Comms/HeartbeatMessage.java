package frc3824.databaserelay.Comms;

import org.json.JSONObject;

import frc3824.databaserelay.Constants;

/**
 * @author frc3824
 * Created: 3/20/17
 */

public class HeartbeatMessage extends MessageBase{

    static HeartbeatMessage m_singleton = null;

    public static HeartbeatMessage getInstance() {
        if(m_singleton == null){
            m_singleton = new HeartbeatMessage();
        }
        return m_singleton;
    }

    @Override
    public String getType() {
        return Constants.Comms.Message_Type.HEARTBEAT;
    }

    @Override
    public JSONObject getData() {
        return new JSONObject();
    }
}
