package com.medical.parakeet.parakeet;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;


public class Main_PatientDetail extends ActionBarActivity{

    View decorView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Puts screen in immersive sticky mode

        setContentView(R.layout.activity_main__patient_detail);

        LinearLayout scroll = (LinearLayout) findViewById(R.id.tiles1);

        List<String> labelList = Arrays.asList("Patient Name:", "DOB:", "Age:", "Gender:",
                "Admit Type:", "Room:", "Procedure Date:", "Surgical Staff:", "PreOp Diagnosis:",
                "Patient Profile:", "Anesthesia", "Findings");

        for(int i = 0; i < 10; i++){

            //TextView view = new TextView(this.getApplicationContext());
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.tile, null);
            TextView internal = (TextView )view.findViewById(R.id.label);
            internal.setText(labelList.get(i));
            scroll.addView(view);

        }
        decorView = getWindow().getDecorView();
        stickyImmersion(decorView);
    }

    public void stickyImmersion(View decorView) {
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN
                | android.view.View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(uiOptions);
    }

}
