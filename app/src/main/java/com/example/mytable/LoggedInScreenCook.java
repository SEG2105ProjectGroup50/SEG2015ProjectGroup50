package com.example.mytable;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mytable.R;
import com.example.mytable.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

public class LoggedInScreenCook extends AppCompatActivity {

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference dbRef = database.getReference("users");
    TextView text;
    String id, welcomeText = null;
    User user;
    Button btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.logged_in_screen_cook);
        text = (TextView) findViewById(R.id.txtWelcomeCook);
        btnLogout = (Button) findViewById(R.id.btnLogout);
        Intent i = getIntent();
        Bundle bundle = i.getExtras();
        if (bundle != null){
            id = (String) bundle.get("id");
        }
    }

    public void onStart() {
        super.onStart();
        dbRef.child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user = snapshot.getValue(Cook.class);
                if (user.getsuspendedStatus() == true){
                    if (user.getSuspensionDate() == -1){
                        welcomeText = "YOUR ACCOUNT HAS BEEN INDEFINITELY SUSPENDED";
                    } else {
                        long value = user.getSuspensionDate();
                        LocalDate date = Instant.ofEpochMilli(value).atZone(ZoneId.systemDefault()).toLocalDate();
                        welcomeText = "ACCOUNT SUSPENDED! THE SUSPENSION WILL BE LIFTED ON: " + date.getYear() + "/" + date.getMonthValue() + "/" + date.getDayOfMonth();
                    }
                    text.setText(welcomeText);
                } else{
                    welcomeText = "Welcome, " + user.getFirstName() + " " + user.getLastName() + "\n" + "You are a: " + user.getUserType();
                    text.setText(welcomeText);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                final String TAG = "Couldn't fetch users";
                Log.w(TAG, "loadPost:onCancelled", error.toException());
            }
        });
    }

    public void logout(View v){
        dbRef.child(id).child("loginStatus").setValue(false);
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

}