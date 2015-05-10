package com.medical.parakeet.parakeet;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Main_PatientDetail extends ActionBarActivity{

    View decorView;
    //Makes labels for view
    List<String> labelList = Arrays.asList("Patient Name:", "DOB:", "Age:", "Gender:",
            "Admit Type:", "Room:", "Procedure Date:", "Surgical Staff:", "PreOp Diagnosis:",
            "Patient Profile:", "Anesthesia", "Findings");
    //Match labels with database attributes
    List<String> columnList = Arrays.asList("pName", "dob", "age", "gender",
            "aType", "room", "pDate", "sStaff", "pOpDiagnosis", "pProfile",
            "anesthesia", "findings");

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main__patient_detail);

        //Populate the view with background parse call
        parseGet("22");

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
                }
            }
        });
    }

}
