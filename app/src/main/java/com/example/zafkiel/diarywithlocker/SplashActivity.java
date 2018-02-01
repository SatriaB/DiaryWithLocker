package com.example.zafkiel.diarywithlocker;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SplashActivity extends AppCompatActivity {
    protected Cursor cursor;
    DataHelper dbcenter;
    SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        dbcenter = new DataHelper(this);
        pref = this.getSharedPreferences("Lock", Context.MODE_PRIVATE);

        final Intent intentL = new Intent(this, DiaryList.class);
        final EditText pass = (EditText)findViewById(R.id.pass);
        TextView forg = (TextView)findViewById(R.id.forget);
        Button con = (Button)findViewById(R.id.confirm);
        SQLiteDatabase db = dbcenter.getReadableDatabase();
        cursor = db.rawQuery("SELECT * FROM account", null);
        cursor.moveToFirst();
        if (cursor.getCount() > 0 && pref.getString("status","") == "ON") {
            cursor.moveToPosition(0);
            pass.setVisibility(View.VISIBLE);
            con.setVisibility(View.VISIBLE);
            forg.setVisibility(View.VISIBLE);
        }
        else {
            new CountDownTimer(3000,1000) {
                @Override
                public void onTick(long l) {

                }
                public void onFinish() {
                    startActivity(intentL);
                    finish();
                }
            }.start();
        }
        con.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick (View v) {
                if (pass.getText().toString().equals(cursor.getString(1).toString())) {
                    startActivity(intentL);
                    finish();
                }
                else {
                    Toast.makeText(getApplicationContext(),"Wrong Password",Toast.LENGTH_LONG).show();
                }
            }
        });
        forg.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick (View v) {
                cursor.moveToPosition(0);
                sendPass();
            }
        });
    }

    protected void sendPass() {
        GMailSender sender = new GMailSender(this, cursor.getString(2).toString(), "Diary Recovery"," Your Password : " + cursor.getString(1).toString());
        sender.execute();
    }
}
