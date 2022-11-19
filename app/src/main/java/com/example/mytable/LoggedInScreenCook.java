package com.example.mytable;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
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
import java.util.ArrayList;
import java.util.List;

public class LoggedInScreenCook extends AppCompatActivity {

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference dbRef = database.getReference("users");
    private DatabaseReference dbRefMenus = database.getReference("menus");
    TextView text;
    String id, welcomeText = null;
    User user;
    Button btnLogout, buttonOpenAddMenuItemPopup;
    DataSnapshot dbSnapshot;
    List<MenuItem> menuItemList;
    ListView menuListView;
    MenuItemList adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.logged_in_screen_cook);
        text = (TextView) findViewById(R.id.txtWelcomeCook);
        btnLogout = (Button) findViewById(R.id.btnLogout);
        Intent i = getIntent();
        Bundle bundle = i.getExtras();
        menuListView = (ListView) findViewById(R.id.menuList);
        buttonOpenAddMenuItemPopup = (Button) findViewById(R.id.buttonOpenAddMenuItemPopup);
        if (bundle != null){
            id = (String) bundle.get("id");
        }
        onItemLongClick();
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
                    text.setVisibility(text.VISIBLE);
                    menuListView.setVisibility(menuListView.GONE);
                    buttonOpenAddMenuItemPopup.setVisibility(buttonOpenAddMenuItemPopup.GONE);
                    user.setLoginStatus(false);
                } else{
                    welcomeText = "Welcome, " + user.getFirstName() + " " + user.getLastName() + "\n" + "You are a: " + user.getUserType();
                    text.setText(welcomeText);
                    text.setVisibility(text.VISIBLE);
                    menuListView.setVisibility(menuListView.VISIBLE);
                    buttonOpenAddMenuItemPopup.setVisibility(buttonOpenAddMenuItemPopup.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                final String TAG = "Couldn't fetch users";
                Log.w(TAG, "loadPost:onCancelled", error.toException());
            }
        });
        dbRefMenus.child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dbSnapshot = snapshot;
                menuItemList = new ArrayList<>();
                Iterable<DataSnapshot> menuItemsIterable = dbSnapshot.getChildren();
                for(DataSnapshot PostSnapshot : menuItemsIterable) {
                    MenuItem x = PostSnapshot.getValue(MenuItem.class);
                    menuItemList.add(x);
                }
                adapter = new MenuItemList(LoggedInScreenCook.this, menuItemList);
                menuListView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                final String TAG = "Couldn't fetch menus";
                Log.w(TAG, "loadPost:onCancelled", error.toException());
            }
        });
    }

    public void logout(View v){
        dbRef.child(id).child("loginStatus").setValue(false);
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

    public void onItemLongClick() {

        menuListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                MenuItem menuItem = menuItemList.get(i);

                showMenuItemUpdateDialog(menuItem);
                return true;
            }
        });
    }

    private void showMenuItemUpdateDialog(MenuItem menuItem) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.update_menu_item_popup, null);
        dialogBuilder.setView(dialogView);


        final EditText editMenuItemName = (EditText) dialogView.findViewById(R.id.editMenuItemName);
        final EditText editMenuItemDescription = (EditText) dialogView.findViewById(R.id.editMenuItemDescription);
        final EditText editMenuItemPrice = (EditText) dialogView.findViewById(R.id.editMenuItemPrice);
        final Button buttonUpdateMenuItem = (Button) dialogView.findViewById(R.id.buttonUpdateMenuItem);
        final Button buttonDeleteMenuItem = (Button) dialogView.findViewById(R.id.buttonDeleteMenuItem);
        final Switch switchMenuItemIsOfferedUpdate = (Switch) dialogView.findViewById(R.id.switchMenuItemIsOfferedUpdate);


        editMenuItemName.setText(menuItem.getName());
        editMenuItemDescription.setText(menuItem.getDescription());
        editMenuItemPrice.setText(menuItem.getPrice());
        switchMenuItemIsOfferedUpdate.setChecked(menuItem.getIsOffered());

        TextView txtTitle = new TextView(this);
        txtTitle.setText("Update or Delete Menu Item");
        txtTitle.setGravity(Gravity.CENTER);

        dialogBuilder.setCustomTitle(txtTitle);
        final AlertDialog b = dialogBuilder.create();
        b.show();



        buttonUpdateMenuItem.setOnClickListener(new View.OnClickListener() {
            View dialogView = inflater.inflate(R.layout.update_menu_item_popup, null);

            @Override
            public void onClick(View view) {
                TextView invalidMenuItemFields2 =  dialogView.findViewById(R.id.invalidMenuItemFields2);
                if (!editMenuItemName.getText().toString().equals("") && !editMenuItemDescription.getText().toString().equals("") && editMenuItemPrice.getText().toString().matches("-?\\d+(\\.\\d+)?")){
                    dbRefMenus.child(id).child(menuItem.getId()).child("name").setValue(editMenuItemName.getText().toString());
                    dbRefMenus.child(id).child(menuItem.getId()).child("description").setValue(editMenuItemDescription.getText().toString());
                    dbRefMenus.child(id).child(menuItem.getId()).child("price").setValue(editMenuItemPrice.getText().toString());
                    dbRefMenus.child(id).child(menuItem.getId()).child("isOffered").setValue(switchMenuItemIsOfferedUpdate.isChecked());
                    b.hide();
                } else{
                    invalidMenuItemFields2.setVisibility(invalidMenuItemFields2.VISIBLE);
                }


            }
        });

        buttonDeleteMenuItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView offeredError =  dialogView.findViewById(R.id.offeredError);
                Boolean isOffered = dbSnapshot.child(menuItem.getId()).child("isOffered").getValue(Boolean.class);
                if (!isOffered){
                    dbRefMenus.child(id).child(menuItem.getId()).removeValue();
                    b.hide();
                } else{
                    offeredError.setVisibility(offeredError.VISIBLE);
                }
            }
        });
    }

    public void showMenuItemAddDialog(View v) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.add_menu_item_popup, null);
        dialogBuilder.setView(dialogView);


        final EditText addMenuItemName = (EditText) dialogView.findViewById(R.id.addMenuItemName);
        TextView invalidMenuItemFields = dialogView.findViewById(R.id.invalidMenuItemFields);
        final EditText addMenuItemDescription = (EditText) dialogView.findViewById(R.id.addMenuItemDescription);
        final EditText addMenuItemPrice = (EditText) dialogView.findViewById(R.id.addMenuItemPrice);
        final Button buttonAddMenuItem = (Button) dialogView.findViewById(R.id.buttonAddMenuItem);
        final Switch switchMenuItemIsOfferedAdd = (Switch) dialogView.findViewById(R.id.switchMenuItemIsOfferedAdd);

        switchMenuItemIsOfferedAdd.setChecked(false);

        TextView txtTitle = new TextView(this);
        txtTitle.setText("Add Menu Item");
        txtTitle.setGravity(Gravity.CENTER);

        dialogBuilder.setCustomTitle(txtTitle);
        final AlertDialog b = dialogBuilder.create();
        b.show();



        buttonAddMenuItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MenuItem menuItem = new MenuItem(addMenuItemName.getText().toString(), addMenuItemDescription.getText().toString(), addMenuItemPrice.getText().toString(), switchMenuItemIsOfferedAdd.isChecked());
                String itemId = dbRefMenus.child(id).push().getKey();
                menuItem.setId(itemId);
                if (!addMenuItemName.getText().toString().equals("") && !addMenuItemDescription.getText().toString().equals("") && addMenuItemPrice.getText().toString().matches("-?\\d+(\\.\\d+)?")){
                    dbRefMenus.child(id).child(itemId).setValue(menuItem);
                    b.hide();
                } else{
                    invalidMenuItemFields.setVisibility(View.VISIBLE);
                }

            }
        });


    }

}