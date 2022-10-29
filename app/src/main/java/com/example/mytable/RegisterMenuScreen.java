package com.example.mytable;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class RegisterMenuScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_menu);
    }

    public void registerCook(View v) {
        Intent i = new Intent(this, CookRegisterScreen.class);

        startActivity(i);
    }
    public void registerClient(View v) {
        Intent i = new Intent(this, ClientRegisterScreen.class);

        startActivity(i);
    }
}