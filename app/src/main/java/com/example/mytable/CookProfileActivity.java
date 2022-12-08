package com.example.mytable;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class CookProfileActivity extends AppCompatActivity {
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference dbRef = database.getReference("users");
    String id;
    Cook cook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cook_profile_screen);
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
                cook = snapshot.getValue(Cook.class);
                displayProfile(cook);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                final String TAG = "Couldn't fetch users";
                Log.w(TAG, "loadPost:onCancelled", error.toException());
            }
        });
    }

    public void displayProfile(Cook cook){
        EditText cookName = findViewById(R.id.txtName);
        EditText mealsSold = findViewById(R.id.txtMealsSold);
        EditText cookRating = findViewById(R.id.txtCookRating);
        EditText cookEmail2 = findViewById(R.id.cookEmail2);
        EditText cookCountry2 = (EditText)findViewById(R.id.cookCountry2);
        EditText cookProvince2 = (EditText)findViewById(R.id.cookProvince2);
        EditText cookCity2 = (EditText)findViewById(R.id.cookCity2);
        EditText cookStreetName2 = (EditText)findViewById(R.id.cookStreetName2);
        EditText cookStreetNumber2 = (EditText)findViewById(R.id.cookStreetNumber2);
        EditText cookPostalCode2 = (EditText)findViewById(R.id.cookPostalCode2);
        EditText cookUnitNumber2 = (EditText)findViewById(R.id.cookUnitNumber2);

        cookName.setText(cook.getFirstName() + cook.getLastName());
        mealsSold.setText(String.valueOf(cook.getMealsSold()));
        cookRating.setText(String.valueOf(cook.getCookRating()));
        cookEmail2.setText(cook.getEmail());
        cookCountry2.setText(cook.getCookAddress().getCountry());
        cookProvince2.setText(cook.getCookAddress().getProvince());
        cookCity2.setText(cook.getCookAddress().getCity());
        cookStreetName2.setText(cook.getCookAddress().getStreet());
        cookStreetNumber2.setText(String.valueOf(cook.getCookAddress().getNumber()));
        cookPostalCode2.setText(cook.getCookAddress().getPostalCode());
        cookUnitNumber2.setText(String.valueOf(cook.getCookAddress().getUnit()));

    }

    public void finishActivity(View v){
        finish();
    }
}
