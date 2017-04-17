package frc3824.databaserelay.Comms;

/**
 * @author frc3824
 * Created: 4/17/17
 */

public interface ResponseListener {

    void matchCompleted(int match_number);

    void teamCompleted(int team_number);

    void finalRunCompleted();
}
