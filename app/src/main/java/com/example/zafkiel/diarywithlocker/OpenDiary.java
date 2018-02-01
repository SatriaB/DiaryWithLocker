package com.example.zafkiel.diarywithlocker;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class OpenDiary extends AppCompatActivity {

    int year,month,dayOfMonth,dayOfWeek,hour,minute,second;
    String dayName, monthName;
    String[] strDays, strMonths;
    Calendar GCalendar;
    DataHelper dbHelper;
    protected Cursor cursor;
    RelativeLayout rl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_diary);
        dbHelper = new DataHelper(this);
        GCalendar = (GregorianCalendar) GregorianCalendar.getInstance(TimeZone.getDefault());
        final EditText dt = (EditText) findViewById(R.id.DateTime);
        final EditText title = (EditText) findViewById(R.id.title);
        final EditText content = (EditText) findViewById(R.id.content);
        final SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        rl = (RelativeLayout) findViewById(R.id.rl);
        new AppChanges(this,rl);

        SQLiteDatabase db = dbHelper.getReadableDatabase();
//        cursor = db.rawQuery("SELECT * FROM diary WHERE dt = DATETIME('" +
//                getIntent().getStringExtra("diary") + "')" , null);
        cursor = db.rawQuery("SELECT * FROM diary WHERE title = '" +
                getIntent().getStringExtra("diary") + "' AND content ='" +
                        getIntent().getStringExtra("con") + "' AND dt = DATETIME('" +
                getIntent().getStringExtra("date") + "')", null);
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            cursor.moveToPosition(0);
            String saved = cursor.getString(0).toString();
            try {
                GCalendar.setTime(dateFormatter.parse(saved));
                CalendarNow();
                dt.setText(dayName + ", " + dayOfMonth + " " + monthName + " " + year);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            title.setText(cursor.getString(1).toString());
            content.setText(cursor.getString(2).toString());
        }
        dt.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick (View v) {
                DatePickerDialog datepicker = new DatePickerDialog(OpenDiary.this, new DatePickerDialog.OnDateSetListener() {
                    public  void onDateSet(DatePicker view, int SetYear, int SetMonth, int SetDay ) {
                        GCalendar.set(SetYear,SetMonth,SetDay,hour,minute,second);
                        CalendarNow();
                        dt.setText(dayName + ", " + dayOfMonth + " " + monthName + " " + year);
                    }
                }, year, month-1, dayOfMonth);

                datepicker.show();
            }
        });
        Button saveButton = (Button) findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dateFormatter.setCalendar(GCalendar);
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                db.execSQL("UPDATE diary SET dt = '" + dateFormatter.format(GCalendar.getTime()) +
                        "', title = '" + title.getText().toString() +
                        "', content = '"+ content.getText().toString() +
                        "' WHERE dt = DATETIME('"+ getIntent().getStringExtra("date") + "')");
                Toast.makeText(getApplicationContext(), "Berhasil", Toast.LENGTH_LONG).show();
                DiaryList.dl.RefreshList();
                finish();
            }
        });
        Button delButton = (Button) findViewById(R.id.delButton);
        delButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dateFormatter.setCalendar(GCalendar);
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                db.execSQL("DELETE FROM diary WHERE dt = DATETIME('"+ getIntent().getStringExtra("date") + "')");
                Toast.makeText(getApplicationContext(), "Berhasil", Toast.LENGTH_LONG).show();
                DiaryList.dl.RefreshList();
                finish();
            }
        });
    }

    public void CalendarNow() {
        year = GCalendar.get(Calendar.YEAR);
        month = GCalendar.get(Calendar.MONTH)+1;
        strMonths = new String[] { "December", "January", "February", "March", "April", "Mei", "June",
                "July", "August", "September", "October", "November", "December" };
        monthName = strMonths[month];
        dayOfMonth = GCalendar.get(Calendar.DAY_OF_MONTH);
        dayOfWeek = GCalendar.get(Calendar.DAY_OF_WEEK);
        strDays = new String[] { "Sunday", "Monday", "Tuesday", "Wednesday", "Thusday",
                "Friday", "Saturday", "Sunday" };
        dayName = strDays[dayOfWeek];
        hour = GCalendar.get(Calendar.HOUR_OF_DAY);
        minute = GCalendar.get(Calendar.MINUTE);
        second = GCalendar.get(Calendar.SECOND);
    }
}
