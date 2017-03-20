package frc3824.databaserelay.DataModels;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;

/**
 * @author frc3824
 * Created: 8/13/16
 *
 * Data collected by match scouts
 */
@IgnoreExtraProperties
public class TeamMatchData {

    private final static String TAG = "TeamMatchData";

    public int match_number;
    public int team_number;
    public String alliance_color;
    public int alliance_number;
    public String scout_name;
    public int total_points;

    // Autonomous
    public String auto_start_position;
    public boolean auto_baseline;
    public ArrayList<Gear> auto_gears;
    public int auto_high_goal_made;
    public int auto_high_goal_missed;
    public int auto_high_goal_correction;
    public int auto_low_goal_made;
    public int auto_low_goal_missed;
    public int auto_low_goal_correction;
    public int auto_hoppers;
    public int auto_points;

    // Teleop
    public ArrayList<Gear> teleop_gears;
    public int teleop_high_goal_made;
    public int teleop_high_goal_missed;
    public int teleop_high_goal_correction;
    public int teleop_low_goal_made;
    public int teleop_low_goal_missed;
    public int teleop_low_goal_correction;
    public int teleop_hoppers;
    public int teleop_picked_up_gears;
    public int teleop_points;

    // Endgame
    public String endgame_climb;
    public String endgame_climb_time;
    public int endgame_points;

    //Post Match
    public boolean no_show;
    public boolean stopped_moving;
    public boolean dq;
    public String notes;

    public boolean tags_blocked_shots;
    public boolean tags_pinned_robot;
    public boolean tags_defended_loading_station;
    public boolean tags_defended_airship;
    public boolean tags_broke;
    public boolean tags_dumped_all_hoppers;

    // Fouls
    public int fouls;
    public int tech_fouls;
    public boolean yellow_card;
    public boolean red_card;

    public TeamMatchData() {
        auto_gears = new ArrayList<>();
        teleop_gears = new ArrayList<>();
    }
}
