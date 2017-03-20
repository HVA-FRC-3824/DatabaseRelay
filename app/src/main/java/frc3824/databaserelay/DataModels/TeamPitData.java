package frc3824.databaserelay.DataModels;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;

/**
 * @author frc3824
 * Created: 8/20/16
 *
 * Data collected by pit scouts
 */
@IgnoreExtraProperties
public class TeamPitData {

    private final static String TAG = "TeamPitData";

    /*
        Pit Scouting
    */
    public int team_number;
    public String scout_name;

    // Robot Image
    public int robot_image_default;
    public ArrayList<String> robot_image_filepaths;
    public ArrayList<String> robot_image_urls;

    // Dimensions
    public double weight;
    public double width;
    public double length;
    public double height;

    // Miscellaneous

    public String programming_language;
    public String drive_train;
    public int cims;
    public int max_hopper_load;
    public String chosen_volume;


    // Notes
    public String notes;

    public TeamPitData(){
        robot_image_filepaths = new ArrayList<>();
        robot_image_urls = new ArrayList<>();
        robot_image_default = -1;
    }
}
