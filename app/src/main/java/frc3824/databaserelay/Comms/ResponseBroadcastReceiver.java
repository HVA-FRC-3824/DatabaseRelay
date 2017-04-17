package frc3824.databaserelay.Comms;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

/**
 * @author frc3824
 * Created: 4/17/17
 */

public class ResponseBroadcastReceiver extends BroadcastReceiver {

    public static final String ACTION_MATCH_COMPLETED = "action_match_completed";
    public static final String ACTION_TEAM_COMPLETED = "action_team_completed";
    public static final String ACTION_FINAL_RUN_COMPLETED = "action_final_run_completed";

    private ResponseListener m_listener;

    public ResponseBroadcastReceiver(Context context, ResponseListener listener) {
        this.m_listener = listener;

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_MATCH_COMPLETED);
        intentFilter.addAction(ACTION_TEAM_COMPLETED);
        intentFilter.addAction(ACTION_FINAL_RUN_COMPLETED);
        context.registerReceiver(this, intentFilter);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (ACTION_MATCH_COMPLETED.equals(intent.getAction())) {
            m_listener.matchCompleted(intent.getIntExtra("match_number", -1));
        } else if (ACTION_TEAM_COMPLETED.equals(intent.getAction())) {
            m_listener.teamCompleted(intent.getIntExtra("team_number", -1));
        } else if (ACTION_FINAL_RUN_COMPLETED.equals(intent.getAction())) {
            m_listener.finalRunCompleted();
        }
    }
}
