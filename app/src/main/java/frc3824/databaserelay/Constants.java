package frc3824.databaserelay;

/**
 * @author frc3824
 * Created: 3/9/17
 */

public interface Constants {

    /**
     Version number changing rules
     - right most number get changed for major changes (after alpha version is finished)
     - middle number is changed after events
     - left most number is changed after the season
     */
    String VERSION = "1.0.0";

    String EVENT_KEY = "event_key";
    String APP_DATA = "appData";

    interface Comms{

        String HOST = "localhost";
        int PORT = 38240;

        int CONNECTOR_SLEEP = 100;
        int HEARTBEAT_THRESHOLD = 800;
        int SEND_HEARTBEAT_PERIOD = 100;

        interface Message_Type {
            String HEARTBEAT = "heartbeat";
        }
    }
}
