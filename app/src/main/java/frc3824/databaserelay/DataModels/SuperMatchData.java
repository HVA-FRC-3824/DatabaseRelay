package frc3824.databaserelay.DataModels;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author frc3824
 * Created: 8/13/16
 *
 * Class to store the data collected by the super scout during a match
 */
public class SuperMatchData {

    private final static String TAG = "SuperMatchData";

    public int match_number;
    public String scout_name;

    // Qualitative

    // Speed
    public Map<Integer, Integer> blue_speed;
    public Map<Integer, Integer> red_speed;

    // Torque (Pushing Power)
    public Map<Integer, Integer> blue_torque;
    public Map<Integer, Integer> red_torque;

    // Control
    public Map<Integer, Integer> blue_control;
    public Map<Integer, Integer> red_control;

    // Defense
    public Map<Integer, Integer> blue_defense;
    public Map<Integer, Integer> red_defense;

    //Notes
    public String notes;

    public SuperMatchData() {
        blue_speed = new HashMap<>();
        red_speed = new HashMap<>();

        blue_torque = new HashMap<>();
        red_torque = new HashMap<>();

        blue_control = new HashMap<>();
        red_control = new HashMap<>();

        blue_defense = new HashMap<>();
        red_defense = new HashMap<>();
    }
}
