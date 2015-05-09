package com.medical.parakeet.parakeet;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;


public class Main_PatientDetail extends ActionBarActivity{

    View decorView;

    @Override
    public void onCreate(Bundle savedInstanceState){
        //Puts screen in immersive sticky mode
        decorView = getWindow().getDecorView();
        stickyImmersion(decorView);

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main__patient_detail);
/*
        TextView pName = (TextView) findViewById(R.id.pName);
        pName.setTextColor(Color.rgb(200, 0, 0));
        pName.setText(pName.getText().toString() + "Mary Jones");

        TextView dob = (TextView) findViewById(R.id.dob);
        dob.setText(dob.getText().toString() + "10/10/10");

        TextView age = (TextView) findViewById(R.id.age);
        age.setText(age.getText().toString() + "32");

        TextView gender = (TextView) findViewById(R.id.gender);
        gender.setText(gender.getText().toString() + "Female");

        TextView aType = (TextView) findViewById(R.id.aType);
        aType.setText(aType.getText().toString() + "Inpatient"); */

        TextView pProfileText = (TextView) findViewById(R.id.pProfileText);
        pProfileText.setText("The patient is a 32 year old female. " +
                "The patient has symptoms of abdominal pain. Refer to note in patient chart for" +
                "documentation of history and physical. previously obtained ct showed: stones found in the" +
                "gallbladder. the patient has failed previous conservative treatment. laparoscopy is " +
                "recommended due to the patient's progressive symptoms. the altnernatives, risks and" +
                "benefits of surgery were discussed with the patient. THe patient verbalized" +
                "understanding of the risks as well as the alternatives to surgery. The patient wished" +
                "to proceed with operative intervention");
    }

    public void stickyImmersion(View decorView) {
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN
                | android.view.View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(uiOptions);
    }

}
