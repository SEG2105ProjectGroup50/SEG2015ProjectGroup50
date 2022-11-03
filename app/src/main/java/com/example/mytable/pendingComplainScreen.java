package com.example.mytable;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class pendingComplainScreen extends AppCompatActivity {

    ListView listView;
    List list = new ArrayList();
    ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_complain_screen);

        listView = (ListView)findViewById(R.id.complainList);

        list.add("bruh");
        list.add("bruh");
        list.add("bruh");
        list.add("bruh");
        list.add("bruh");

        adapter = new ArrayAdapter<>(pendingComplainScreen.this, android.R.layout.simple_list_item_1,list);
        listView.setAdapter(adapter);


    }




}