package com.example.mytable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.lang.ref.Reference;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CookRegisterScreen extends AppCompatActivity {

    private Cook cook;
    private Address cookAddress;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference dbRef = database.getReference("users");
    private DatabaseReference images = database.getReference("images");
    ImageView voidChequeImage;
    boolean photoTaken;

    private List<String> emailList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.cook_register_screen);
        cook = new Cook();
        cookAddress = new Address();
        voidChequeImage = (ImageView) findViewById(R.id.voidChequeImage);
        photoTaken = false;
    }

    public void onStart() {
        super.onStart();
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //Clearing previously stored list of clients.
                emailList.clear();
                Iterable<DataSnapshot> usersIterable = snapshot.getChildren();
                for (DataSnapshot postSnapShot : usersIterable){
                    //get each email
                    String email = postSnapShot.child("email").getValue(String.class);
                    //add updated client to list
                    emailList.add(email);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                final String TAG = "Error:fetchlistofcooks";
                Log.w(TAG, "loadPost:onCancelled", error.toException());
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && data != null) { // sets image to photo taken
            Bitmap captureImage = (Bitmap) data.getExtras().get("data");
            voidChequeImage.setImageBitmap(captureImage);
            photoTaken = true;
        }

        if (requestCode == 200 && data != null) { // sets image to photo imported
            Uri selectedImage = data.getData();
            voidChequeImage.setImageURI(selectedImage);
            photoTaken = true;
        }
    }

    public void registerComplete(View v) {
        EditText cookFirstName = (EditText)findViewById(R.id.cookFirstName);
        TextView cookFirstNameInvalid = (TextView)findViewById(R.id.cookFirstNameInvalid);
        boolean firstNameValid = cook.setFirstName(cookFirstName.getText().toString()); // validates first name

        if (!firstNameValid) {

            cookFirstNameInvalid.setVisibility(cookFirstNameInvalid.VISIBLE);

        } else {
            cookFirstNameInvalid.setVisibility(cookFirstNameInvalid.GONE);
        }

        EditText cookLastName = (EditText)findViewById(R.id.cookLastName);
        TextView cookLastNameInvalid = (TextView)findViewById(R.id.cookLastNameInvalid);
        boolean lastNameValid = cook.setLastName(cookLastName.getText().toString()); // validates last name

        if (!lastNameValid) {

            cookLastNameInvalid.setVisibility(cookLastNameInvalid.VISIBLE);

        } else {
            cookLastNameInvalid.setVisibility(cookLastNameInvalid.GONE);
        }

        EditText cookEmail = (EditText)findViewById(R.id.cookEmail);
        TextView cookEmailInvalid = (TextView)findViewById(R.id.cookEmailInvalid);
        boolean emailValid = false;
        boolean emailTaken = false;
        String error;
        String email = cookEmail.getText().toString().toLowerCase();

        if (emailList.contains(email)){
            emailTaken = true;
        } else{
            emailValid = cook.setEmail(cookEmail.getText().toString().toLowerCase());
        }

        if (!emailValid) {
            if (emailTaken){
                error = "Email is already associated with an account.";
            } else{
                error = "Please enter a valid email.";
            }
            cookEmailInvalid.setText(error);
            cookEmailInvalid.setVisibility(cookEmailInvalid.VISIBLE);
        } else {
            cookEmailInvalid.setVisibility(cookEmailInvalid.GONE);
        }

        if (!emailValid) {

            cookEmailInvalid.setVisibility(cookEmailInvalid.VISIBLE);

        } else {
            cookEmailInvalid.setVisibility(cookEmailInvalid.GONE);
        }

        EditText cookPassword1 = (EditText)findViewById(R.id.cookPassword1);
        EditText cookPassword2 = (EditText)findViewById(R.id.cookPassword2);
        LinearLayout cookPasswordInvalid = (LinearLayout)findViewById(R.id.cookPasswordInvalid);
        TextView cookPasswordNoMatch = (TextView)findViewById(R.id.cookPasswordNoMatch);

        boolean passwordValid = cook.setPassword(cookPassword1.getText().toString()); // validates password strength
        boolean passwordsMatch = cookPassword1.getText().toString().equals(cookPassword2.getText().toString()); // validates password match

        if (!passwordValid) {

            cookPasswordInvalid.setVisibility(cookPasswordInvalid.VISIBLE);

        } else {
            cookPasswordInvalid.setVisibility(cookPasswordInvalid.GONE);
        }
        if (!passwordsMatch) {

            cookPasswordNoMatch.setVisibility(cookPasswordNoMatch.VISIBLE);

        } else {
            cookPasswordNoMatch.setVisibility(cookPasswordNoMatch.GONE);
        }

        EditText cookCountry = (EditText)findViewById(R.id.cookCountry);
        TextView cookCountryInvalid = (TextView)findViewById(R.id.cookCountryInvalid);

        boolean countryValid = cookAddress.setCountry(cookCountry.getText().toString()); // validates country name

        if (!countryValid) {
            cookCountryInvalid.setVisibility(cookCountryInvalid.VISIBLE);
        } else {
            cookCountryInvalid.setVisibility(cookCountryInvalid.GONE);
        }

        EditText cookProvince = (EditText)findViewById(R.id.cookProvince);
        TextView cookProvinceInvalid = (TextView)findViewById(R.id.cookProvinceInvalid);

        boolean provinceValid = cookAddress.setProvince(cookProvince.getText().toString()); // validates province or state name

        if (!provinceValid) {
            cookProvinceInvalid.setVisibility(cookProvinceInvalid.VISIBLE);
        } else {
            cookProvinceInvalid.setVisibility(cookProvinceInvalid.GONE);
        }

        EditText cookCity = (EditText)findViewById(R.id.cookCity);
        TextView cookCityInvalid = (TextView)findViewById(R.id.cookCityInvalid);

        boolean cityValid = cookAddress.setCity(cookCity.getText().toString()); // validates city name

        if (!cityValid) {
            cookCityInvalid.setVisibility(cookCityInvalid.VISIBLE);
        } else {
            cookCityInvalid.setVisibility(cookCityInvalid.GONE);
        }

        EditText cookStreetName = (EditText)findViewById(R.id.cookStreetName);
        TextView cookStreetNameInvalid = (TextView)findViewById(R.id.cookStreetNameInvalid);

        boolean streetNameValid = cookAddress.setStreet(cookStreetName.getText().toString()); // validates street name

        if (!streetNameValid) {
            cookStreetNameInvalid.setVisibility(cookStreetNameInvalid.VISIBLE);
        } else {
            cookStreetNameInvalid.setVisibility(cookStreetNameInvalid.GONE);
        }

        EditText cookStreetNumber = (EditText)findViewById(R.id.cookStreetNumber);
        TextView cookStreetNumberInvalid = (TextView)findViewById(R.id.cookStreetNumberInvalid);

        boolean streetNumberValid = cookAddress.setNumber(cookStreetNumber.getText().toString()); // validates street number

        if (!streetNumberValid) {
            cookStreetNumberInvalid.setVisibility(cookStreetNumberInvalid.VISIBLE);
        } else {
            cookStreetNumberInvalid.setVisibility(cookStreetNumberInvalid.GONE);
        }

        EditText cookPostalCode = (EditText)findViewById(R.id.cookPostalCode);
        TextView cookPostalCodeInvalid = (TextView)findViewById(R.id.cookPostalCodeInvalid);

        boolean cookPostalCodeValid = cookAddress.setPostalCode(cookPostalCode.getText().toString()); // validates postal code

        if (!cookPostalCodeValid) {
            cookPostalCodeInvalid.setVisibility(cookPostalCodeInvalid.VISIBLE);
        } else {
            System.out.println("postal code valid");
            cookPostalCodeInvalid.setVisibility(cookPostalCodeInvalid.GONE);
        }
        
        EditText cookUnitNumber = (EditText)findViewById(R.id.cookUnitNumber);

        cookAddress.setUnit(cookUnitNumber.getText().toString());

        cook.setCookAddress(cookAddress);

        TextView photoInvalid = findViewById(R.id.photoInvalid);

        if(!photoTaken) {
            photoInvalid.setVisibility(photoInvalid.VISIBLE);
        } else {
            photoInvalid.setVisibility(photoInvalid.GONE);
        }

        if(firstNameValid && lastNameValid && emailValid && (!emailTaken) && passwordValid && passwordsMatch && countryValid && provinceValid && cityValid && streetNameValid && streetNumberValid && photoTaken) {
            System.out.println("All fields valid");
            postNewCook(cook);
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
        }

    }

    public void postNewCook(Cook newCook){ // uploads
        String userId = dbRef.push().getKey();
        dbRef.child(userId).setValue(newCook);
//        voidChequeImage.setDrawingCacheEnabled(true);
//        voidChequeImage.buildDrawingCache();
//        Bitmap bitmap = ((BitmapDrawable) voidChequeImage.getDrawable()).getBitmap();
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
//        byte[] data = baos.toByteArray();
//
//        Task uploadTask = images.;
    }

    public void takePhoto(View v) {
        if (ContextCompat.checkSelfPermission(CookRegisterScreen.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(CookRegisterScreen.this, new String[]{
                    Manifest.permission.CAMERA
            },
                    100);
        }
        
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 100);
    }

    public void importPhoto(View v) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 200);



    }



}

