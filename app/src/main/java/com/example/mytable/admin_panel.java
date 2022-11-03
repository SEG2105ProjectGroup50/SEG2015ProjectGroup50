package com.example.mytable;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class admin_panel extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_panel);
    }
    public void tocomplainmenu(View v){
        Intent i = new Intent(this, pendingComplainScreen.class);
        startActivity(i);
    }

    public void adminback(View v){
        Intent i = new Intent(this, LoggedInScreen.class);
        startActivity(i);
    }
}