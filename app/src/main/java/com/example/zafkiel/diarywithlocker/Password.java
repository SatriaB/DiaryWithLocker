package com.example.zafkiel.diarywithlocker;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class Password extends AppCompatActivity {
    protected Cursor cursor;
    DataHelper dbcenter;
    RelativeLayout rl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);
        dbcenter = new DataHelper(this);
        final Intent intent = new Intent(this, LockerSetting.class);
        final EditText email = (EditText)findViewById(R.id.email);
        final EditText pass = (EditText)findViewById(R.id.password);
        final EditText user = (EditText)findViewById(R.id.username);
        rl = (RelativeLayout) findViewById(R.id.rl);
        new AppChanges(this,rl);
        Button create = (Button)findViewById(R.id.create);
        SQLiteDatabase db = dbcenter.getReadableDatabase();
        cursor = db.rawQuery("SELECT * FROM account", null);
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            cursor.moveToPosition(0);
            startActivity(intent);
            finish();
        }
        create.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick (View v) {
                SQLiteDatabase db = dbcenter.getWritableDatabase();
                db.execSQL("INSERT INTO account(username,password,email) VALUES('" +
                        email.getText().toString() + "','" +
                        user.getText().toString() + "','" +
                        pass.getText().toString() + "')");
                Toast.makeText(getApplicationContext(), "Berhasil", Toast.LENGTH_LONG).show();
                startActivity(intent);
                finish();
            }
        });
    }
}
