package com.medical.parakeet.parakeet;

import com.parse.Parse;

import android.app.Application;

public class ParseDatabase extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Parse.initialize(this, "JIPThRPo44v22ZiPuVYdoqqJOJMWenXZcisfa8ut",
                "5xApFui0egw9P8M5bxkVxiXtpImOBd2ZRBb1hlxe");
    }
}
