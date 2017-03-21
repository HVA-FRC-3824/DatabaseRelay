package frc3824.databaserelay.Comms;

/**
 * @author frc3824
 * Created: 3/20/17
 */

public interface ServerConnectionStateListener {
    void serverConnected();

    void serverDisconnected();
}
