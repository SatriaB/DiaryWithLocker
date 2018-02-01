package com.example.zafkiel.diarywithlocker;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.Toast;

public class LockerSetting extends AppCompatActivity {
    DataHelper dbcenter;
    protected Cursor cursor;
    RelativeLayout rl;
    SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locker_setting);
        ListView navList = (ListView) findViewById(R.id.listMenu);
        pref = this.getSharedPreferences("Lock",Context.MODE_PRIVATE);
        rl = (RelativeLayout) findViewById(R.id.rl);
        new AppChanges(this,rl);
        dbcenter = new DataHelper(this);
        final String[] menu = {"Change Password"};
        final EditText oldPass = new EditText(this);
        oldPass.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
        final EditText newPass = new EditText(this);
        newPass.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
        final Switch lock = (Switch) findViewById(R.id.switch1);
        if (pref.getString("status","") == "ON") {
            lock.setChecked(true);
        }
        else {
            lock.setChecked(false);
        }
        lock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (lock.isChecked()) {
                    pref.edit().putString("status","ON").commit();
                }
                else {
                    pref.edit().putString("status","OFF").commit();
                }
            }
        });

        final AlertDialog dialog2 = new AlertDialog.Builder(this)
                .setTitle("New Passwod")
                .setMessage("Enter your new password")
                .setView(newPass)
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        SQLiteDatabase db = dbcenter.getWritableDatabase();
                        db.execSQL("UPDATE account SET password = '" + newPass.getText().toString() +
                                "' WHERE password = '"+ cursor.getString(1).toString() + "'");
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).create();

        final AlertDialog dialog1 = new AlertDialog.Builder(this)
                .setTitle("Old Password")
                .setMessage("Enter your old Password")
                .setView(oldPass)
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        SQLiteDatabase db = dbcenter.getReadableDatabase();
//                        cursor = db.rawQuery("SELECT * FROM diary ORDER BY dt DESC", null);
                        cursor = db.rawQuery("SELECT * FROM account", null);
                        cursor.moveToFirst();
                        if (cursor.getCount() > 0) {
                            cursor.moveToPosition(0);
                            if (oldPass.getText().toString().equals(cursor.getString(1).toString())) {
                                Toast.makeText(getApplicationContext(), "Enter New Password", Toast.LENGTH_LONG).show();
                                dialog2.show();
                            }
                            else {
                                Toast.makeText(getApplicationContext(),"Wrong Password",Toast.LENGTH_LONG).show();
                            }
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "Error Query", Toast.LENGTH_LONG).show();
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).create();
        navList.setAdapter(new ArrayAdapter(this, android.R.layout.simple_list_item_1, menu));
        navList.setSelected(true);
        navList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView arg0, View arg1, int arg2, long arg3) {
                switch (arg2) {
                    case 0:
                        dialog1.show();
                        break;
                }
            }
        });

    }
}
