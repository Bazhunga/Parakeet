package com.medical.parakeet.parakeet;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;

import com.parse.Parse;

/**
 * Created by Jesse on 2015-05-09.
 */
public class ParseDatabase extends Application {

    public void onCreate(Bundle savedInstanceState) {
        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "JIPThRPo44v22ZiPuVYdoqqJOJMWenXZcisfa8ut",
                "5xApFui0egw9P8M5bxkVxiXtpImOBd2ZRBb1hlxe");
    }

}
