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

    int PORT = 38240;

    interface Comms{
        int SENDING = 0;
        int RECEIVING = 1;

        int CHUNK_SIZE = 4192;
        int HEADER_MSB = 0x10;
        int HEADER_LSB = 0x55;

        interface Message_Type {
            int DATA_SENT_OK = 0x00;
            int DATA_RECEIVED = 0x02;
            int SENDING_DATA = 0x04;

            int DIGEST_DID_NOT_MATCH = 0x50;
            int COULD_NOT_CONNECT = 0x51;
            int INVALID_HEADER = 0x52;
        }
    }

    interface Server_Log_Colors {
        String RED = "red";
        String BLUE = "blue";
        String BLACK = "black";
        String GREEN = "green";
        String YELLOW = "yellow";
        String WHITE = "white";
    }
}
