package com.medical.parakeet.parakeet;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.Arrays;
import java.util.List;


public class Main_PatientDetail extends ActionBarActivity{

    View decorView;
    //Makes labels for view
    List<String> labelList = Arrays.asList("Date of Birth:", "Age:", "Gender:",
            "Admit Type:", "Room:", "Procedure Date:", "Surgical Staff:", "PreOp Diagnosis:",
            "Patient Profile:", "Anesthesia", "Findings");
    //Match labels with database attributes
    List<String> columnList = Arrays.asList("dob", "age", "gender",
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

    Typeface latoreg;
    Typeface latoNar;
    Typeface latoblack;

    LinearLayout scroll;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main__patient_detail);

        latoreg = Typeface.createFromAsset(this.getAssets(), "fonts/Lato-Bold.ttf");
        latoNar = Typeface.createFromAsset(this.getAssets(), "fonts/Lato-Light.ttf");
        latoblack = Typeface.createFromAsset(this.getAssets(), "fonts/Lato-Black.ttf");

        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Log.d("start beacon manager", "STARTED");
        beaconManager.setRangingListener(new BeaconManager.RangingListener() {
            @Override public void onBeaconsDiscovered(Region region, List<Beacon> beacons) {
                Log.d(TAG, "Ranged beacons: " + beacons);
//                for (int i = 0; i < beacons.size(); i++){
//                    Log.d(TAG, beacons.get(i).getMacAddress());
//                }
                if (turnStile == 0){
                    currentPatient = beacons.get(0);
                    if (currentPatient != null){
                        Log.d("Current patient MAC Address", currentPatient.getMacAddress());
                        //Populate the view with background parse call
                        parseGet(currentPatient.getMacAddress());
                        turnStile = 1;
                        try {
                            beaconManager.stopRanging(ALL_ESTIMOTE_BEACONS);
                        } catch (RemoteException e) {
                            Log.e(TAG, "Cannot stop but it does not matter now", e);
                        }
                    }
                }
            }
        });

        //Puts screen in immersive sticky mode
        decorView = getWindow().getDecorView();
        stickyImmersion(decorView);
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

                    TextView text;
                    TextView label;
                    View underline;
                    View view;
                    LayoutInflater inflater;
                    scroll = (LinearLayout) findViewById(R.id.tiles1);
                    inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    view = inflater.inflate(R.layout.phototile, null);

                    //Get the photo
                    ParseFile image = patient_object.getParseFile("Photo");
                    ParseImageView parseImage = (ParseImageView) view.findViewById(R.id.parseImage);
                    parseImage.setParseFile(image);
                    parseImage.loadInBackground(new GetDataCallback() {
                        @Override
                        public void done(byte[] data, ParseException e) {

                        }
                    });
                    text = (TextView) view.findViewById(R.id.name);
                    text.setText(patient_object.getString("pName"));
                    text.setTypeface(latoblack);
                    text.setTextSize(35);
                    scroll.addView(view);

                    //Iterate through the columns to create labels + text
                    for (int i = 0; i < columnList.size(); i++) {
                        view = inflater.inflate(R.layout.tile, null);
                        text = (TextView) view.findViewById(R.id.text);
                        label = (TextView) view.findViewById(R.id.label);
                        underline = (View) view.findViewById(R.id.line);
                        label.setText(labelList.get(i));
                        label.setTextSize(28);
                        label.setTypeface(latoreg);
                        label.measure(0, 0);
                        Log.d("Label width", String.valueOf(label.getMeasuredWidth()));
                        underline.getLayoutParams().width = label.getMeasuredWidth() + 10;
                        text.setTextSize(22);
                        text.setTypeface(latoNar);
                        text.setText(patient_object.getString(columnList.get(i)));
                        scroll.addView(view);
                    }
                    updateFigure(patient_object.getString("bodyParts"));
                }
            }
        });
    }

    public void refreshPatient(View view){
        turnStile = 0;
        scroll.removeAllViews();
        //Set the man to green;
        resetFigure();

        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Log.d("start beacon manager", "STARTED");
        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override public void onServiceReady() {
                try {
                    beaconManager.startRanging(ALL_ESTIMOTE_BEACONS);
                } catch (RemoteException e) {
                    Log.e(TAG, "Cannot start ranging", e);
                }
            }
        });

        beaconManager.setRangingListener(new BeaconManager.RangingListener() {
            @Override public void onBeaconsDiscovered(Region region, List<Beacon> beacons) {
                Log.d(TAG, "Ranged beacons: " + beacons);
//                for (int i = 0; i < beacons.size(); i++){
//                    Log.d(TAG, beacons.get(i).getMacAddress());
//                }
                if (turnStile == 0){
                    currentPatient = beacons.get(0);
                    if (currentPatient != null){
                        Log.d("Current patient MAC Address", currentPatient.getMacAddress());
                        //Populate the view with background parse call
                        parseGet(currentPatient.getMacAddress());
                        turnStile = 1;
                    }
                }
            }
        });

    }

    public void resetFigure(){
        ImageView head = (ImageView) findViewById(R.id.head);
        ImageView larm = (ImageView) findViewById(R.id.larm);
        ImageView rarm = (ImageView) findViewById(R.id.rarm);
        ImageView upbod = (ImageView) findViewById(R.id.upbod);
        ImageView lowbod = (ImageView) findViewById(R.id.lowbod);
        ImageView leftleg = (ImageView) findViewById(R.id.leftleg);
        ImageView rightleg = (ImageView) findViewById(R.id.rightleg);

        head.setImageResource(R.drawable.status_good_head);
        larm.setImageResource(R.drawable.status_good_larm);
        rarm.setImageResource(R.drawable.status_good_rarm);
        upbod.setImageResource(R.drawable.status_good_upbod);
        lowbod.setImageResource(R.drawable.status_good_lowbod);
        leftleg.setImageResource(R.drawable.status_good_leftleg);
        rightleg.setImageResource(R.drawable.status_good_rightleg);

    }

    public void updateFigure(String bodyparts){
        String [] parts = bodyparts.split(", ");
        ImageView head = (ImageView) findViewById(R.id.head);
        ImageView larm = (ImageView) findViewById(R.id.larm);
        ImageView rarm = (ImageView) findViewById(R.id.rarm);
        ImageView upbod = (ImageView) findViewById(R.id.upbod);
        ImageView lowbod = (ImageView) findViewById(R.id.lowbod);
        ImageView leftleg = (ImageView) findViewById(R.id.leftleg);
        ImageView rightleg = (ImageView) findViewById(R.id.rightleg);

        for (int i = 0; i < parts.length; i++ ){
            switch(parts[i]){
                case "head": head.setImageResource(R.drawable.status_bad_head);
                    break;
                case "larm": larm.setImageResource(R.drawable.status_bad_larm);
                    break;
                case "rarm": rarm.setImageResource(R.drawable.status_bad_rarm);
                    break;
                case "upbod": upbod.setImageResource(R.drawable.status_bad_upbod);
                    break;
                case "lowbod": lowbod.setImageResource(R.drawable.status_bad_lowbod);
                    break;
                case "leftleg": leftleg.setImageResource(R.drawable.status_bad_leftleg);
                    break;
                case "rightleg": rightleg.setImageResource(R.drawable.status_bad_rightleg);
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void onStart(){
        super.onStart();
        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override public void onServiceReady() {
                try {
                    beaconManager.startRanging(ALL_ESTIMOTE_BEACONS);
                } catch (RemoteException e) {
                    Log.e(TAG, "Cannot start ranging", e);
                }
            }
        });
    }

    @Override
    public void onStop(){
        // Should be invoked in #onStop.
        super.onStop();
        try {
            beaconManager.stopRanging(ALL_ESTIMOTE_BEACONS);
        } catch (RemoteException e) {
            Log.e(TAG, "Cannot stop but it does not matter now", e);
        }
    }

    @Override
    protected void onDestroy() {
        notificationManager.cancel(NOTIFICATION_ID);
        beaconManager.disconnect();
        super.onDestroy();
    }

    private void postNotification(String msg) {
        Intent notifyIntent = new Intent(Main_PatientDetail.this, Main_PatientDetail.class);
        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivities(
                Main_PatientDetail.this,
                0,
                new Intent[]{notifyIntent},
                PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification = new Notification.Builder(Main_PatientDetail.this)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle("Patient Found")
                .setContentText(msg)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .build();
        notification.defaults |= Notification.DEFAULT_SOUND;
        notification.defaults |= Notification.DEFAULT_LIGHTS;
        notificationManager.notify(NOTIFICATION_ID, notification);
    }

}
