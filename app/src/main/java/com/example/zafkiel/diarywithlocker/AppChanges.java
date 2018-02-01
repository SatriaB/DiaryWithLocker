package com.example.zafkiel.diarywithlocker;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.widget.RelativeLayout;

/**
 * Created by Zafkiel on 1/16/2018.
 */

public class AppChanges {

    public AppChanges (Context con, RelativeLayout v) {
        SharedPreferences prefered = con.getSharedPreferences("Lock", Context.MODE_PRIVATE);
        String R,G,B;
        if (prefered.getString("Red",null) == null &&
                prefered.getString("Green",null) == null &&
                prefered.getString("Blue",null) == null) {
            v.setBackgroundColor(Color.parseColor("#ffffff"));
        }
        else {
            R = prefered.getString("Red",null);
            G = prefered.getString("Green",null);
            B = prefered.getString("Blue",null);
            if (Integer.parseInt(R,16) <= 15 &&
                    Integer.parseInt(G,16) <= 15 &&
                    Integer.parseInt(B,16) <= 15) {
                R = "0" + R;
                G = "0" + G;
                B = "0" + B;
            }
            v.setBackgroundColor(Color.parseColor("#"+R+G+B));
        }
    }
}