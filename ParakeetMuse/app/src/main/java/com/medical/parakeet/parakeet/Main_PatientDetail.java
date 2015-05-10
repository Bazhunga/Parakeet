package com.medical.parakeet.parakeet;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.Arrays;
import java.util.List;


public class Main_PatientDetail extends ActionBarActivity{
    public Context context = this;
    View decorView;
    //Makes labels for view
    List<String> labelList = Arrays.asList("Patient Name:", "DOB:", "Age:", "Gender:",
            "Admit Type:", "Room:", "Procedure Date:", "Surgical Staff:", "PreOp Diagnosis:",
            "Patient Profile:", "Anesthesia", "Findings");
    //Match labels with database attributes
    List<String> columnList = Arrays.asList("pName", "dob", "age", "gender",
            "aType", "room", "pDate", "sStaff", "pOpDiagnosis", "pProfile",
            "anesthesia", "findings");

    //Beacon Stuff
    private static final String ESTIMOTE_PROXIMITY_UUID = "B9407F30-F5F8-466E-AFF9-25556B57FE6D";
    private static final Region ALL_ESTIMOTE_BEACONS = new Region("regionId", ESTIMOTE_PROXIMITY_UUID, null, null);
    private BeaconManager beaconManager = new BeaconManager(this);

    private static final String TAG = "BEEEERCAN";
    private static final int NOTIFICATION_ID = 123;

    private NotificationManager notificationManager;
    private Region region;
    private Beacon currentPatient;

    private int turnStile = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main__patient_detail);

//        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        Log.d("start beacon manager", "STARTED");
//        beaconManager.setRangingListener(new BeaconManager.RangingListener() {
//            @Override public void onBeaconsDiscovered(Region region, List<Beacon> beacons) {
//                Log.d(TAG, "Ranged beacons: " + beacons);
////                for (int i = 0; i < beacons.size(); i++){
////                    Log.d(TAG, beacons.get(i).getMacAddress());
////                }
//                if (turnStile == 0){
//                    currentPatient = beacons.get(0);
//                    //Populate the view with background parse call
//                    parseGet(currentPatient.getMacAddress());
//                    turnStile = 1;
//                }
//            }
//        });

        //Puts screen in immersive sticky mode
        decorView = getWindow().getDecorView();
        stickyImmersion(decorView);
    }

    public void museActive(View view)
    {
        try {
            beaconManager.stopRanging(ALL_ESTIMOTE_BEACONS);
        } catch (RemoteException e) {
            Log.e(TAG, "Cannot stop but it does not matter now", e);
        }
        Intent intent = new Intent(context, MuseActivity.class);
        startActivity(intent);
    }

    public void stickyImmersion(View decorView) {
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN
                | android.view.View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(uiOptions);
    }

    //Gets data from Parse database and dynamically adds data with labels into view
    public void parseGet (String sID){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Patient");
        query.whereEqualTo("sID", sID);
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            //Get patient info from parse object.
            public void done(ParseObject patient_object, ParseException e) {
                if (patient_object == null) {
                    Log.d("OH NOO, AN ERROR", String.valueOf(e));
                } else {
                    //Get linearlayout from scrollview to add text dynamically
                    LinearLayout scroll;
                    TextView text;
                    TextView label;
                    View view;
                    LayoutInflater inflater;
                    scroll = (LinearLayout) findViewById(R.id.tiles1);
                    scroll.setBackgroundResource(R.drawable.parakeetbackground);

                    //Iterate through the columns to create labels + text
                    for (int i = 0; i < columnList.size(); i++) {
                        inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        view = inflater.inflate(R.layout.tile, null);
                        text = (TextView) view.findViewById(R.id.text);
                        label = (TextView) view.findViewById(R.id.label);
                        label.setText(labelList.get(i));
                        text.setText(patient_object.getString(columnList.get(i)));
                        //TextView view = new TextView(this.getApplicationContext());
                        scroll.addView(view);
                    }
                    Button button = (Button) findViewById((R.id.muse_button));
                }
            }
        });
    }


}
