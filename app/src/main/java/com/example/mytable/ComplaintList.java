package com.example.mytable;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class ComplaintList extends ArrayAdapter<Complaint> {

    private Activity context;
    List<Complaint> complaints;

    public ComplaintList(Activity context, List<Complaint> complaints) {
        super(context, R.layout.complaint_layout, complaints);
        this.context = context;
        this.complaints = complaints;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.complaint_layout, null, true);

        TextView textCookID = (TextView) listViewItem.findViewById(R.id.textComplaintCookID);
        TextView textComplaintTitle = (TextView) listViewItem.findViewById(R.id.textComplaintListTitle);


        Complaint complaint = complaints.get(position);

        textCookID.setText(complaint.getCookId());
        textComplaintTitle.setText(complaint.getDescription());
        return listViewItem;
    }


}
