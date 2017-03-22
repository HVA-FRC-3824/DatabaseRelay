package frc3824.databaserelay;

import android.os.AsyncTask;
import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import frc3824.databaserelay.Comms.MessageBase;
import frc3824.databaserelay.Comms.ServerConnection;


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

    private static Database mSingleton = null;

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
        if (eventKey.isEmpty() || mEventKey == eventKey)
            return;

        mEventRef = mRootRef.child(eventKey);

        //region Setup references and maps
        //region Partial Matches
        mPartialMatchRef = mEventRef.child("partial_match");
        mPartialMatchRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.v(TAG, "partial_match.onChildAdded: " + dataSnapshot.getKey());
                if(dataSnapshot.getKey().equals("scout_names")){
                    return;
                }
                final int match_number = dataSnapshot.child("match_number").getValue(Integer.class);
                final int team_number = dataSnapshot.child("team_number").getValue(Integer.class);

                MessageBase message = new MessageBase() {
                    @Override
                    public String getType() {
                        return "match";
                    }

                    @Override
                    public JSONObject getData() {
                        JSONObject o = new JSONObject();
                        try {
                            o.put("match_number", match_number);
                            o.put("team_number", team_number);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        return o;
                    }
                };
                try {
                    ServerConnection.getInstance().send(message);
                } catch (Exception e){
                    e.getMessage();
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Log.v(TAG, "partial_match.onChildChanged: " + dataSnapshot.getKey());
                if(dataSnapshot.getKey().equals("scout_names")){
                    return;
                }
                final int match_number = dataSnapshot.child("match_number").getValue(Integer.class);
                final int team_number = dataSnapshot.child("team_number").getValue(Integer.class);
                MessageBase message = new MessageBase() {
                    @Override
                    public String getType() {
                        return "match";
                    }

                    @Override
                    public JSONObject getData() {
                        JSONObject o = new JSONObject();
                        try {
                            o.put("match_number", match_number);
                            o.put("team_number", team_number);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        return o;
                    }
                };
                try {
                    ServerConnection.getInstance().send(message);
                } catch (Exception e){
                    e.getMessage();
                }
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
                final int match_number = dataSnapshot.child("match_number").getValue(Integer.class);
                MessageBase message = new MessageBase() {
                    @Override
                    public String getType() {
                        return "super";
                    }

                    @Override
                    public JSONObject getData() {
                        JSONObject o = new JSONObject();
                        try {
                            o.put("match_number", match_number);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        return o;
                    }
                };
                try {
                    ServerConnection.getInstance().send(message);
                } catch (Exception e){
                    e.getMessage();
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Log.v(TAG, "super_match.onChildChanged: " + dataSnapshot.getKey());
                final int match_number = dataSnapshot.child("match_number").getValue(Integer.class);
                MessageBase message = new MessageBase() {
                    @Override
                    public String getType() {
                        return "super";
                    }

                    @Override
                    public JSONObject getData() {
                        JSONObject o = new JSONObject();
                        try {
                            o.put("match_number", match_number);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        return o;
                    }
                };
                try {
                    ServerConnection.getInstance().send(message);
                } catch (Exception e){
                    e.getMessage();
                }
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
                final int match_number = dataSnapshot.child("match_number").getValue(Integer.class);

                MessageBase message = new MessageBase() {
                    @Override
                    public String getType() {
                        return "pilot";
                    }

                    @Override
                    public JSONObject getData() {
                        JSONObject o = new JSONObject();
                        try {
                            o.put("match_number", match_number);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        return o;
                    }
                };
                try {
                    ServerConnection.getInstance().send(message);
                } catch (Exception e){
                    e.getMessage();
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Log.v(TAG, "pilot.match.onChildChanged: " + dataSnapshot.getKey());
                final int match_number = dataSnapshot.child("match_number").getValue(Integer.class);

                MessageBase message = new MessageBase() {
                    @Override
                    public String getType() {
                        return "pilot";
                    }

                    @Override
                    public JSONObject getData() {
                        JSONObject o = new JSONObject();
                        try {
                            o.put("match_number", match_number);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        return o;
                    }
                };
                try {
                    ServerConnection.getInstance().send(message);
                } catch (Exception e){
                    e.getMessage();
                }
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
}