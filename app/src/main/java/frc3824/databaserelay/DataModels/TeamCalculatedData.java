package frc3824.databaserelay.DataModels;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * @author frc3824
 * Created: 8/20/16
 * Aggregated data for a specific team
 */
@IgnoreExtraProperties
public class TeamCalculatedData {

    private final static String TAG = "TeamCalculatedData";

    /*
        Calculated Data
    */

    public int team_number;

    public LowLevelStats total_points;

    // Autonomous
    public int auto_start_position_far;
    public int auto_start_position_center;
    public int auto_start_position_near;
    public LowLevelStats auto_baseline;
    public LowLevelStats auto_total_gears_placed;
    public LowLevelStats auto_near_gears_placed;
    public LowLevelStats auto_center_gears_placed;
    public LowLevelStats auto_far_gears_placed;
    public LowLevelStats auto_total_gears_dropped;
    public LowLevelStats auto_near_gears_dropped;
    public LowLevelStats auto_center_gears_dropped;
    public LowLevelStats auto_far_gears_dropped;
    public LowLevelStats auto_high_goal_made;
    public LowLevelStats auto_high_goal_missed;
    public LowLevelStats auto_low_goal_made;
    public LowLevelStats auto_low_goal_missed;
    public LowLevelStats auto_hoppers;
    public LowLevelStats auto_points;

    // Teleop
    public LowLevelStats teleop_total_gears_placed;
    public LowLevelStats teleop_near_gears_placed;
    public LowLevelStats teleop_center_gears_placed;
    public LowLevelStats teleop_far_gears_placed;
    public LowLevelStats teleop_total_gears_dropped;
    public LowLevelStats teleop_near_gears_dropped;
    public LowLevelStats teleop_center_gears_dropped;
    public LowLevelStats teleop_far_gears_dropped;
    public LowLevelStats teleop_high_goal_made;
    public LowLevelStats teleop_high_goal_missed;
    public LowLevelStats teleop_low_goal_made;
    public LowLevelStats teleop_low_goal_missed;
    public LowLevelStats teleop_hoppers;
    public LowLevelStats teleop_picked_up_gears;
    public LowLevelStats teleop_points;

    // Endgame
    public LowLevelStats endgame_climb_successful;
    public LowLevelStats endgame_climb_robot_fell;
    public LowLevelStats endgame_climb_did_not_finish_in_time;
    public LowLevelStats endgame_climb_no_attempt;
    public LowLevelStats endgame_climb_credited_through_foul;
    public LowLevelStats endgame_climb_time;
    public LowLevelStats endgame_points;

    // Post Match
    public LowLevelStats no_show;
    public LowLevelStats stopped_moving;
    public LowLevelStats dq;

    // Fouls
    public LowLevelStats fouls;
    public LowLevelStats tech_fouls;
    public LowLevelStats yellow_cards;
    public LowLevelStats red_cards;

    // Qualitative
    public double zscore_speed;
    public int rank_speed;

    public double zscore_torque;
    public int rank_torque;

    public double zscore_control;
    public int rank_control;

    public double zscore_defense;
    public int rank_defense;

    public LowLevelStats pilot_ratings;

    public TeamCalculatedData() {
        auto_baseline = new LowLevelStats();
        auto_total_gears_placed = new LowLevelStats();
        auto_total_gears_dropped = new LowLevelStats();
        auto_near_gears_placed = new LowLevelStats();
        auto_near_gears_dropped = new LowLevelStats();
        auto_center_gears_placed = new LowLevelStats();
        auto_center_gears_dropped = new LowLevelStats();
        auto_far_gears_placed = new LowLevelStats();
        auto_far_gears_dropped = new LowLevelStats();
        auto_high_goal_made = new LowLevelStats();
        auto_high_goal_missed = new LowLevelStats();
        auto_low_goal_made = new LowLevelStats();
        auto_low_goal_missed = new LowLevelStats();
        auto_hoppers = new LowLevelStats();
        auto_points = new LowLevelStats();

        teleop_total_gears_placed = new LowLevelStats();
        teleop_total_gears_dropped = new LowLevelStats();
        teleop_near_gears_placed = new LowLevelStats();
        teleop_near_gears_dropped = new LowLevelStats();
        teleop_center_gears_placed = new LowLevelStats();
        teleop_center_gears_dropped = new LowLevelStats();
        teleop_far_gears_placed = new LowLevelStats();
        teleop_far_gears_dropped = new LowLevelStats();
        teleop_high_goal_made = new LowLevelStats();
        teleop_high_goal_missed = new LowLevelStats();
        teleop_low_goal_made = new LowLevelStats();
        teleop_low_goal_missed = new LowLevelStats();
        teleop_hoppers = new LowLevelStats();
        teleop_picked_up_gears = new LowLevelStats();
        teleop_points = new LowLevelStats();

        endgame_climb_successful = new LowLevelStats();
        endgame_climb_robot_fell = new LowLevelStats();
        endgame_climb_credited_through_foul = new LowLevelStats();
        endgame_climb_did_not_finish_in_time = new LowLevelStats();
        endgame_climb_no_attempt = new LowLevelStats();
        endgame_climb_time = new LowLevelStats();
        endgame_points = new LowLevelStats();

        no_show = new LowLevelStats();
        stopped_moving = new LowLevelStats();
        dq = new LowLevelStats();

        fouls = new LowLevelStats();
        tech_fouls = new LowLevelStats();
        yellow_cards = new LowLevelStats();
        red_cards = new LowLevelStats();

        pilot_ratings = new LowLevelStats();
    }
}