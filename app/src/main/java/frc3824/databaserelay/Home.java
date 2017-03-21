package frc3824.databaserelay;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

import frc3824.databaserelay.Comms.ServerConnectionStateListener;
import frc3824.databaserelay.Comms.ServerConnectionStatusBroadcastReceiver;

public class Home extends Activity implements ServerConnectionStateListener {

    private static final String TAG = "Home";

    private String mEventKey;
    private boolean mIsRunning;
    private View mConnectionStateView;
    private ServerConnectionStatusBroadcastReceiver mSbr;

    @Override
    protected void onStart() {
        super.onStart();
        mIsRunning = true;
        Log.i(TAG, "onStart");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(TAG, "onRestart");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Database.getInstance();

        ((TextView)findViewById(R.id.version)).setText(String.format("Version: %s", Constants.VERSION));

        final AutoCompleteTextView event_key = (AutoCompleteTextView)findViewById(R.id.event);

        final SharedPreferences sp = getSharedPreferences(Constants.APP_DATA, Context.MODE_PRIVATE);
        mEventKey = sp.getString(Constants.EVENT_KEY, "");
        event_key.setText(mEventKey);

        event_key.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(Database.getEvents().contains(s.toString()))
                {
                    mEventKey = s.toString();
                    Database.getInstance(mEventKey);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString(Constants.EVENT_KEY, mEventKey);
                    editor.commit();
                    event_key.setBackground(null);
                }
                else
                {
                    event_key.setBackgroundColor(Color.RED);
                }
            }
        });

        ArrayList<String> events = new ArrayList<>(Database.getEvents());
        ArrayAdapter<String> aa = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, events);
        event_key.setAdapter(aa);

        setupUi(this, findViewById(android.R.id.content));

        mConnectionStateView = findViewById(R.id.connection_state);
        mSbr = new ServerConnectionStatusBroadcastReceiver(this, this);

        Log.i(TAG, "onCreate");
    }

    @Override
    protected void onPause() {
        Log.i(TAG, "onPause");
        mIsRunning = false;
        super.onPause();
    }

    @Override
    protected void onResume() {
        Log.i(TAG, "onResume ");
        super.onResume();
        mIsRunning = true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mSbr);
    }

    public static void setupUi(final Activity activity, View view) {
        // Setup touch listener for non-textbox views to hide the keyboard.
        if(!(view instanceof AutoCompleteTextView) && !(view instanceof EditText)){
            view.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    hideSoftKeyboard(activity);
                    return false;
                }
            });
        }

        // If layout is a container, iterate over children and seed recursion.
        if(view instanceof ViewGroup) {
            int childCount = ((ViewGroup) view).getChildCount();
            for(int i = 0; i < childCount; i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setupUi(activity, innerView);
            }
        }
    }

    private static void hideSoftKeyboard(Activity activity) {
        if(activity.getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager)activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        }
    }

    public void startBadConnectionAnimation() {
        Animation animation = new ScaleAnimation(1, 1, 1, 20, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0);
        animation.setDuration(200);
        animation.setInterpolator(new LinearInterpolator());
        animation.setRepeatCount(Animation.INFINITE);
        animation.setRepeatMode(Animation.REVERSE);
        mConnectionStateView.startAnimation(animation);
    }

    public void stopBadConnectionAnimation() {
        mConnectionStateView.clearAnimation();
    }

    @Override
    public void serverConnected() {
        Log.i(TAG, "Robot Connected");
        mConnectionStateView.setBackgroundColor(ContextCompat.getColor(this, R.color.bright_green));
        stopBadConnectionAnimation();
    }

    @Override
    public void serverDisconnected() {
        mConnectionStateView.setBackgroundColor(ContextCompat.getColor(this, R.color.bright_red));
        startBadConnectionAnimation();
        playAirhorn();
    }

    public void playAirhorn() {
        MediaPlayer mp = MediaPlayer.create(this, R.raw.airhorn);
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.reset();
                mp.release();
            }
        });
        mp.start();
    }
}
