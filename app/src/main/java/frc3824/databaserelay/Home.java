package frc3824.databaserelay;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Set;

public class Home extends AppCompatActivity {

    private String mEventKey;
    private SocketThread mSocket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ((TextView)findViewById(R.id.version)).setText(String.format("Version: %s", Constants.VERSION));

        final AutoCompleteTextView event_key = (AutoCompleteTextView)findViewById(R.id.event);

        final SharedPreferences sp = getSharedPreferences(Constants.APP_DATA, Context.MODE_PRIVATE);
        mEventKey = sp.getString(Constants.EVENT_KEY, "");
        event_key.setText(mEventKey);

        Socket socket = null;
        try {
            socket = new Socket("localhost", Constants.PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }

        mSocket = new SocketThread(socket);
        mSocket.start();

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

        // Setup the database or reload it (to make the schedule and button list work)
        if(mEventKey != "") {
            Database.getInstance(mEventKey).setSocket(mSocket);;
        }
        else
        {
            Database.getInstance().setSocket(mSocket);;
        }
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
}
