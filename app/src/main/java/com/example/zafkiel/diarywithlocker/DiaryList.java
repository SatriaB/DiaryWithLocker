package com.example.zafkiel.diarywithlocker;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.internal.NavigationMenuItemView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class DiaryList extends AppCompatActivity {

    String[] daftar;
    String[] daftar2;
    ListView ListView01;
    RelativeLayout rl;
    protected Cursor cursor;
    DataHelper dbcenter;
    public static DiaryList dl;
    List<RecMaker> rec;
    RecyclerView rv;
    recycler ra;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_list);
        final EditText searchText = new EditText(DiaryList.this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        ImageButton menu = (ImageButton) findViewById(R.id.menu);
        ImageButton search = (ImageButton) findViewById(R.id.search);
        rv = (RecyclerView)findViewById(R.id.my_recycler_view);
        rl = (RelativeLayout) findViewById(R.id.rl);
        new AppChanges(this,rl);
        final NavigationMenuItemView nav = (NavigationMenuItemView) findViewById(R.id.nav);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(view.getContext(), WriteDiary.class));
            }
        });
        dl=this;
        dbcenter = new DataHelper(this);
        RefreshList();
        final AlertDialog dialog = new AlertDialog.Builder(DiaryList.this)
                .setTitle("Search")
                .setMessage("Enter your search key")
                .setView(searchText)
                .setPositiveButton("Search", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        SQLiteDatabase db = dbcenter.getReadableDatabase();
//                        cursor = db.rawQuery("SELECT * FROM diary ORDER BY dt DESC", null);
                        String query = "SELECT * FROM diary WHERE dt LIKE ? OR " +
                                "title LIKE ? OR " +
                                "content LIKE ? ORDER by dt DESC";
                        cursor = db.rawQuery(
                                query, new String[] {
                                        "%"+searchText.getText().toString()+"%",
                                        "%"+searchText.getText().toString()+"%",
                                        "%"+searchText.getText().toString()+"%"
                        });
                        RefreshCycle();
//                        fetch();
                    }
                })
                .setNegativeButton("Reset", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        RefreshList();
                    }
                }).create();
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(nav.getVisibility() == View.INVISIBLE) {
                    nav.setVisibility(View.VISIBLE);
                }
                else {
                    nav.setVisibility(View.INVISIBLE);
                }
            }
        });
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
            }
        });
        ListView navList = (ListView) findViewById(R.id.navList);
        final String[] menuText = {"Lock", "Change Color & Theme"};
        navList.setAdapter(new ArrayAdapter(this, android.R.layout.simple_list_item_1, menuText));
        navList.setSelected(true);
        navList.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView arg0, View arg1, int arg2, long arg3) {
                switch (arg2) {
                    case 0:
                        Intent in = new Intent(getApplicationContext(), Password.class);
                        startActivity(in);
                        break;
                    case 1:
                        Intent on = new Intent(getApplicationContext(), Theme.class);
                        startActivity(on);
                        break;
                }
            }
        });

    }

    public void RefreshList() {
        SQLiteDatabase db = dbcenter.getReadableDatabase();
        cursor = db.rawQuery("SELECT * FROM diary ORDER BY dt DESC", null);
        RefreshCycle();
//        fetch();
    }

    public void fetch() {
        daftar = new String[cursor.getCount()];
        daftar2 = new String[cursor.getCount()];
        cursor.moveToFirst();
        for (int cc = 0; cc < cursor.getCount(); cc++) {
            cursor.moveToPosition(cc);
            daftar[cc] = cursor.getString(0).toString();
        }
        ListView01 = (ListView) findViewById(R.id.lv1);
        ListView01.setAdapter(new ArrayAdapter(this, android.R.layout.simple_list_item_1, daftar));
        ListView01.setSelected(true);
        ListView01.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView arg0, View arg1, int arg2, long arg3) {
                final String selection = daftar[arg2];
                Intent in =new Intent(getApplicationContext(), OpenDiary.class);
                in.putExtra("diary", selection);
                startActivity(in);
            }
        });
        ((ArrayAdapter) ListView01.getAdapter()).notifyDataSetInvalidated();
        Toast.makeText(getApplicationContext(), "Data refreshed", Toast.LENGTH_LONG).show();
    }

    public void RefreshCycle() {
        rec = new ArrayList<>();
        StringBuffer sB = new StringBuffer();
        RecMaker rMec = null;
        cursor.moveToFirst();
        cursor.moveToPosition(0);
        for (int cc = 0; cc < cursor.getCount();cc++) {
            cursor.moveToPosition(cc);
            rMec= new RecMaker();
            String title = cursor.getString(1).toString();
            String dt = cursor.getString(0).toString();
            String content = cursor.getString(2).toString();
            rMec.setTitle(title);
            rMec.setDate(dt);
            rMec.setContent(content);
            sB.append(rMec);
            rec.add(rMec);
        }
        ra = new recycler(rec);
        rv.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        rv.setItemAnimator(new DefaultItemAnimator());
        rv.setAdapter(ra);
        Toast.makeText(getApplicationContext(), "Data Refreshed", Toast.LENGTH_LONG).show();
    }
}