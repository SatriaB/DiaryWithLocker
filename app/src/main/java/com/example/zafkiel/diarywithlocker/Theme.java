package com.example.zafkiel.diarywithlocker;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;


public class Theme extends AppCompatActivity {

    SeekBar sbr,sbg,sbb;
    SharedPreferences pref;
    RelativeLayout rl;
    String hexR,hexG,hexB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theme);
        rl = (RelativeLayout)findViewById(R.id.theme);
        final TextView rc = (TextView) findViewById(R.id.textR);
        final TextView gc = (TextView) findViewById(R.id.textG);
        final TextView bc = (TextView) findViewById(R.id.textB);
        sbr = (SeekBar) findViewById(R.id.Rcolor);
        sbg = (SeekBar) findViewById(R.id.Gcolor);
        sbb = (SeekBar) findViewById(R.id.Bcolor);
        pref = this.getSharedPreferences("Lock", Context.MODE_PRIVATE);
        if (pref.getString("Red",null) == null &&
                pref.getString("Green",null) == null &&
                pref.getString("Blue",null) == null) {
            hexR = "ff";
            hexG = "ff";
            hexB = "ff";
            sbr.setProgress(Integer.parseInt(hexR,16));
            sbg.setProgress(Integer.parseInt(hexG,16));
            sbb.setProgress(Integer.parseInt(hexB,16));
            rl.setBackgroundColor(Color.parseColor("#ffffff"));
        }
        else {
            hexR = pref.getString("Red",null);
            hexG = pref.getString("Green",null);
            hexB = pref.getString("Blue",null);
            rl.setBackgroundColor(Color.parseColor("#"+hexR+hexG+hexB));
            sbr.setProgress(Integer.parseInt(hexR,16));
            sbg.setProgress(Integer.parseInt(hexG,16));
            sbb.setProgress(Integer.parseInt(hexB,16));
        }
        sbr.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (i>15) {
                    hexR = Integer.toHexString(i);
                }
                else {
                    hexR = "0"+Integer.toHexString(i);
                }
                rc.setText("Red : " + hexR);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                pref.edit().putString("Red",hexR).commit();
                Log.d("notice","#"+hexR+hexG+hexB);
                rl.setBackgroundColor(Color.parseColor("#"+hexR+hexG+hexB));
                DiaryList.dl.rl.setBackgroundColor(Color.parseColor("#"+hexR+hexG+hexB));
            }
        });
        sbg.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (i>15) {
                    hexG = Integer.toHexString(i);
                }
                else {
                    hexG = "0"+Integer.toHexString(i);
                }
                gc.setText("Green : " + hexG);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                pref.edit().putString("Green",hexG).commit();
                Log.d("notice","#"+hexR+hexG+hexB);
                rl.setBackgroundColor(Color.parseColor("#"+hexR+hexG+hexB));
                DiaryList.dl.rl.setBackgroundColor(Color.parseColor("#"+hexR+hexG+hexB));
            }
        });
        sbb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (i>15) {
                    hexB = Integer.toHexString(i);
                }
                else {
                    hexB = "0"+Integer.toHexString(i);
                }
                bc.setText("Blue : " + hexB);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                pref.edit().putString("Blue",hexB).commit();
                Log.d("notice","#"+hexR+hexG+hexB);
                rl.setBackgroundColor(Color.parseColor("#"+hexR+hexG+hexB));
                DiaryList.dl.rl.setBackgroundColor(Color.parseColor("#"+hexR+hexG+hexB));
            }
        });
    }
}
