package com.example.mytable;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class pendingComplaintScreen extends AppCompatActivity {

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference dbRef = database.getReference("complaints");
    private DatabaseReference dbRef2 = database.getReference("users");
    ListView listView;
    List<Complaint> complaintList;
    ComplaintList adapter;
    DataSnapshot dbSnapshot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_complaint_screen);
        listView = (ListView)findViewById(R.id.complaintList);
        onItemLongClick();

    }

    public void onStart() {
        super.onStart();
        dbRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dbSnapshot = snapshot;
                complaintList = new ArrayList<>();
                Iterable<DataSnapshot> complaintsIterable = dbSnapshot.getChildren();
                for(DataSnapshot PostSnapshot : complaintsIterable) {
                    if (!PostSnapshot.getValue(Complaint.class).getStatus().equals("RESOLVED")) {
                        Complaint x = PostSnapshot.getValue(Complaint.class);
                        x.setId(PostSnapshot.getKey());
                        complaintList.add(x);
                    }
                }
                adapter = new ComplaintList(pendingComplaintScreen.this, complaintList);
                listView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                final String TAG = "Couldn't fetch users";
                Log.w(TAG, "loadPost:onCancelled", error.toException());
            }
        });
    }

    public void onItemLongClick() {

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Complaint complaint = complaintList.get(i);

                showComplaintDecisionDialog(complaint);
                return true;
            }
        });
    }

    private void showComplaintDecisionDialog(Complaint complaint) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.complaint_decision_screen, null);
        dialogBuilder.setView(dialogView);


        final TextView textClientID = (TextView) dialogView.findViewById(R.id.textClientID);
        final TextView textComplaintTitle = (TextView) dialogView.findViewById(R.id.textComplaintTitle);
        final TextView textComplaintDescription = (TextView) dialogView.findViewById(R.id.textComplaintDescription);
        final Button buttonSuspendTemporarily = (Button) dialogView.findViewById(R.id.buttonSuspendTemporarily);
        final Button buttonSuspendIndefinitely = (Button) dialogView.findViewById(R.id.buttonSuspendIndefinitely);
        final Button discardComplaint = (Button) dialogView.findViewById(R.id.discardComplaint);


        textClientID.setText(complaint.getClientId());
        textComplaintTitle.setText(complaint.getTitle());
        textComplaintDescription.setText(complaint.getDescription());


        TextView txtTitle = new TextView(this);
        txtTitle.setText("Complaint on CookID "+ complaint.getCookId());
        txtTitle.setGravity(Gravity.CENTER);

        dialogBuilder.setCustomTitle(txtTitle);
        final AlertDialog b = dialogBuilder.create();
        b.show();



        buttonSuspendTemporarily.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long date = 1670286893l;
                dbRef2.child(complaint.getCookId()).child("suspendedStatus").setValue(true);
                dbRef2.child(complaint.getCookId()).child("suspensionDate").setValue(date);
                dbRef.child(complaint.getId()).child("status").setValue("RESOLVED");
                b.hide();
            }
        });

        buttonSuspendIndefinitely.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long date = -1;
                dbRef2.child(complaint.getCookId()).child("suspendedStatus").setValue(true);
                dbRef2.child(complaint.getCookId()).child("suspensionDate").setValue(date);
                dbRef.child(complaint.getId()).child("status").setValue("RESOLVED");
                b.hide();
            }
        });

        discardComplaint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbRef.child(complaint.getId()).child("status").setValue("RESOLVED");
                b.hide();
            }
        });
    }









}