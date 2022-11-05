package com.example.mytable;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class LoginScreen extends AppCompatActivity {

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference dbRef = database.getReference("users");
    private HashMap<String,String> users;
    private DataSnapshot dbSnapshot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);
    }

    public void onStart() {
        super.onStart();
        dbRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dbSnapshot = snapshot;
                users = new HashMap<String,String>();
                Iterable<DataSnapshot> usersIterable = snapshot.getChildren();
                for(DataSnapshot PostSnapshot : usersIterable) {
                    String id = PostSnapshot.getKey();
                    String email = PostSnapshot.child("email").getValue(String.class);
                    users.put(email, id);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                final String TAG = "Couldn't fetch users";
                Log.w(TAG, "loadPost:onCancelled", error.toException());
            }
        });

    }

    public void attemptLogin(View v){
        EditText userEmail = (EditText)findViewById(R.id.userEmail);
        EditText userPassword = (EditText)findViewById(R.id.userPassword);
        TextView successfulLogin = (TextView)findViewById(R.id.successLogin);
        TextView failedLogin = (TextView)findViewById(R.id.failedLogin);

        String email = userEmail.getText().toString();
        String password = userPassword.getText().toString();

        if(users.containsKey(email)) {
            String id = users.get(email);
            email.toLowerCase();
            String dbPassword = dbSnapshot.child(id).child("password").getValue(String.class);
            if(dbPassword.equals(password)) {
                failedLogin.setVisibility(failedLogin.GONE);
                successfulLogin.setVisibility(successfulLogin.VISIBLE);
                dbRef.child(id).child("loginStatus").setValue(true);
                String userType = dbSnapshot.child(id).child("userType").getValue(String.class);
                Intent i;
                if (userType.equals("Administrator")){
                    i = new Intent(this, LoggedInScreenAdmin.class);
                } else if (userType.equals("Cook")){
                    i = new Intent(this, LoggedInScreenCook.class);
                } else {
                    i = new Intent(this, LoggedInScreenClient.class);
                }
                i.putExtra("id", id);
                startActivity(i);
            }
        } else{
            failedLogin.setVisibility(failedLogin.VISIBLE);
        }

    }
}