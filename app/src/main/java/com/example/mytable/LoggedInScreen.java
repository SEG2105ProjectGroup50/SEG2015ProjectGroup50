package com.example.mytable;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoggedInScreen extends AppCompatActivity {

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference dbRef = database.getReference("users");
    TextView text;
    String id, welcomeText = null;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.logged_in_screen);
        text = (TextView) findViewById(R.id.txtWelcome);
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
                user = snapshot.getValue(User.class);
                welcomeText = "Welcome, " + user.getFirstName() + " " + user.getLastName() + "\n" + "You are a: " + user.getUserType();
                text.setText(welcomeText);


                Button adminpanel = (Button)findViewById(R.id.adminpanel);



                if (user.getUserType().equals("Administrator") ){
                    adminpanel.setVisibility(View.VISIBLE);
                }
                else {
                    adminpanel.setVisibility(View.GONE);
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
    public void openAdminPanel(View v){
        Intent i = new Intent(this, admin_panel.class);
        startActivity(i);
    }
}