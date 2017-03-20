package frc3824.databaserelay.DataModels;
import java.util.ArrayList;

/**
 * @author frc3824
 * Created: 3/8/17
 */

public class MatchPilotData {
    public MatchPilotData(){
        teams = new ArrayList<>();
    }

    public int match_number;
    public long last_modified;
    public ArrayList<MatchTeamPilotData> teams;
}
