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
    String VERSION = "2.3.1";

    interface Comms{

        String HOST = "localhost";
        int PORT = 38240;

        int CONNECTOR_SLEEP = 100; // 1 second
        int HEARTBEAT_THRESHOLD = 5 * 1000; // 5 second
        int SEND_HEARTBEAT_PERIOD = 10 * 60 * 1000; // 10 minutes

        interface Message_Type {
            String HEARTBEAT = "heartbeat";
        }
    }
}
