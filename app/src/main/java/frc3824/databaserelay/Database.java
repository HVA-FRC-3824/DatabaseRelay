package frc3824.databaserelay;

import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.JsonObject;

import java.util.HashSet;
import java.util.Set;

import frc3824.databaserelay.DataModels.MatchPilotData;
import frc3824.databaserelay.DataModels.SuperMatchData;

import frc3824.databaserelay.DataModels.TeamMatchData;


/**
 * @author frc3824
 *         Created: 3/9/17
 */

public class Database {


    private final static String TAG = "Database";

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mRootRef;

    //region Database References
    private DatabaseReference mEventRef;
    private DatabaseReference mSuperMatchDataRef;
    private DatabaseReference mMatchPilotRef;
    private DatabaseReference mPartialMatchRef;
    //endregion

    private static Set<String> mEvents;

    private String mEventKey;

    private SocketThread mSocket;

    private static Database mSingleton;

    public static Database getInstance(String eventKey) {
        if (mSingleton == null) {
            mSingleton = new Database();
        }

        mSingleton.setEventKey(eventKey);
        return mSingleton;
    }

    public static Database getInstance() {
        if (mSingleton == null) {
            mSingleton = new Database();
        }

        return mSingleton;
    }

    private Database() {
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mFirebaseDatabase.setPersistenceEnabled(true);
        mRootRef = mFirebaseDatabase.getReference();

        mEvents = new HashSet<>();

        //Root reference's children are the events
        mRootRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.v(TAG, "onChildAdded: " + dataSnapshot.getKey());
                mEvents.add(dataSnapshot.getKey());
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Log.v(TAG, "onChildChanged: " + dataSnapshot.getKey());
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.v(TAG, "onChildRemoved: " + dataSnapshot.getKey());
                mEvents.remove(dataSnapshot.getKey());
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                Log.v(TAG, "onChildMoved: " + dataSnapshot.getKey());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.v(TAG, "onCancelled");
            }
        });
    }

    private void setEventKey(String eventKey) {
        if (eventKey == "" || mEventKey == eventKey)
            return;

        mEventRef = mRootRef.child(eventKey);

        //region Setup references and maps
        //region Partial Matches
        mPartialMatchRef = mEventRef.child("partial_match");
        mPartialMatchRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.v(TAG, "partial_match.onChildAdded: " + dataSnapshot.getKey());
                TeamMatchData tmd = dataSnapshot.getValue(TeamMatchData.class);
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("update_type", "match");
                jsonObject.addProperty("match_number", tmd.match_number);
                jsonObject.addProperty("team_number", tmd.team_number);
                mSocket.write(jsonObject.getAsString());
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Log.v(TAG, "partial_match.onChildChanged: " + dataSnapshot.getKey());
                TeamMatchData tmd = dataSnapshot.getValue(TeamMatchData.class);
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("update_type", "match");
                jsonObject.addProperty("match_number", tmd.match_number);
                jsonObject.addProperty("team_number", tmd.team_number);
                mSocket.write(jsonObject.getAsString());
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.v(TAG, "partial_match.onChildRemoved: " + dataSnapshot.getKey());
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                Log.v(TAG, "partial_match.onChildMoved: " + dataSnapshot.getKey());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.v(TAG, "partial_match.onCancelled");
            }
        });
        //endregion

        //region Super
        mSuperMatchDataRef = mEventRef.child("super_match");
        mSuperMatchDataRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.v(TAG, "super_match.onChildAdded: " + dataSnapshot.getKey());
                SuperMatchData smd = dataSnapshot.getValue(SuperMatchData.class);
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("update_type", "super");
                jsonObject.addProperty("match_number", smd.match_number);
                mSocket.write(jsonObject.getAsString());
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Log.v(TAG, "super_match.onChildChanged: " + dataSnapshot.getKey());
                SuperMatchData smd = dataSnapshot.getValue(SuperMatchData.class);
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("update_type", "super");
                jsonObject.addProperty("match_number", smd.match_number);
                mSocket.write(jsonObject.getAsString());
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.v(TAG, "super_match.onChildRemoved: " + dataSnapshot.getKey());
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                Log.v(TAG, "super_match.onChildMoved: " + dataSnapshot.getKey());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "super_match.onCancelled: ");
            }
        });
        //endregion

        //region Match Pilot
        mMatchPilotRef = mEventRef.child("pilot").child("match");
        mMatchPilotRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.v(TAG, "pilot.match.onChildAdded: " + dataSnapshot.getKey());
                MatchPilotData mpd = dataSnapshot.getValue(MatchPilotData.class);

                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("update_type", "pilot");
                jsonObject.addProperty("match_number", mpd.match_number);
                mSocket.write(jsonObject.getAsString());
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Log.v(TAG, "pilot.match.onChildChanged: " + dataSnapshot.getKey());
                MatchPilotData mpd = dataSnapshot.getValue(MatchPilotData.class);

                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("update_type", "pilot");
                jsonObject.addProperty("match_number", mpd.match_number);
                mSocket.write(jsonObject.getAsString());
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.v(TAG, "pilot.match.onChildRemoved: " + dataSnapshot.getKey());
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                Log.v(TAG, "pilot.match.onChildMoved");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.v(TAG, "pilot.match.onCancelled");
            }
        });
        //endregion
        //endregion

        mEventKey = eventKey;
    }

    public static Set<String> getEvents() {
        return mEvents;
    }

    public void setSocket(SocketThread socket) {
        mSocket = socket;
        setEventKey(mEventKey);
    }

}