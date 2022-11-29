package com.example.mytable;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

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

public class LoggedInScreenClient extends AppCompatActivity {

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference dbRefUsers = database.getReference("users");
    private DatabaseReference dbRefMenus = database.getReference("menus");
    private DatabaseReference dbRefOrders = database.getReference("orders");
    TextView text;
    String id, welcomeText = null;
    User user;
    Button buttonManageComplaints;
    Button btnLogout;
    List<MenuItem> menuItemList;
    MenuItemList menuItemListAdapter;
    ListView menuItemListView, pendingOrdersListView, completedOrdersListView, rejectedOrdersListView;
    DataSnapshot usersDbSnapshot;
    List<Order> pendingOrders, completedOrders, rejectedOrders;
    OrderList pendingOrdersListAdapter, completedOrdersListAdapter, rejectedOrdersListAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.logged_in_screen_client);
        btnLogout = (Button) findViewById(R.id.btnLogout);
        Intent i = getIntent();
        Bundle bundle = i.getExtras();
        text = (TextView) findViewById(R.id.clientWelcomeText);


        if (bundle != null){
            id = (String) bundle.get("id");
        }
        menuItemListView = (ListView) findViewById(R.id.clientMealsList);
        pendingOrdersListView = (ListView) findViewById(R.id.clientPendingOrdersList);
        completedOrdersListView = (ListView) findViewById(R.id.clientCompletedOrdersList);
        rejectedOrdersListView = (ListView) findViewById(R.id.clientRejectedOrdersList);
        onItemLongClick();
    }

    public void onStart() {
        super.onStart();
        dbRefUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                usersDbSnapshot = snapshot;
                user = snapshot.child(id).getValue(Client.class);
                if (user.getsuspendedStatus() == true){
                    if (user.getSuspensionDate() == -1){
                        welcomeText = "YOUR ACCOUNT HAS BEEN INDEFINITELY SUSPENDED";
                    } else {
                        long value = user.getSuspensionDate();
                        LocalDate date = Instant.ofEpochMilli(value).atZone(ZoneId.systemDefault()).toLocalDate();
                        welcomeText = "ACCOUNT SUSPENDED! THE SUSPENSION WILL BE LIFTED ON: " + date.getYear() + "/" + date.getMonthValue() + "/" + date.getDayOfMonth();
                    }
                    text.setText(welcomeText);
                    buttonManageComplaints.setVisibility(View.GONE);
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

        dbRefMenus.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                menuItemList = new ArrayList<MenuItem>();
                DataSnapshot menuSnapshot = snapshot;
                Iterable<DataSnapshot> menusIterable = menuSnapshot.getChildren();

                for (DataSnapshot menu : menusIterable) {
                    Iterable<DataSnapshot> menuItemsIterable = menu.getChildren();
                    for (DataSnapshot menuItem : menuItemsIterable) {
                        MenuItem menuItemTemp = menuItem.getValue(MenuItem.class);
                        menuItemTemp.setCookId(menu.getKey());
                        if (menuItemTemp.getIsOffered()) {
                            menuItemList.add(menuItemTemp);
                        }
                        System.out.println(menuItemTemp.getId());
                    }
                }
                menuItemListAdapter = new MenuItemList(LoggedInScreenClient.this, menuItemList);
                menuItemListView.setAdapter(menuItemListAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                final String TAG = "Couldn't fetch menus";
                Log.w(TAG, "loadPost:onCancelled", error.toException());
            }
        });

        dbRefOrders.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                pendingOrders = new ArrayList<Order>();
                completedOrders = new ArrayList<Order>();
                rejectedOrders = new ArrayList<Order>();
                DataSnapshot orderSnapshot = snapshot;
                Iterable<DataSnapshot> ordersIterable = orderSnapshot.getChildren();

                for (DataSnapshot order : ordersIterable) {
                    Order tempOrder = order.getValue(Order.class);
                    if (tempOrder.getClientId().equals(id) && tempOrder.getStatus().equals("PENDING")) {
                        pendingOrders.add(tempOrder);
                    } else if (tempOrder.getClientId().equals(id) && tempOrder.getStatus().equals("ACCEPTED")) {
                        completedOrders.add(tempOrder);
                    } else if (tempOrder.getClientId().equals(id)) {
                        rejectedOrders.add(tempOrder);
                    }
                }
                pendingOrdersListAdapter = new OrderList(LoggedInScreenClient.this, pendingOrders);
                completedOrdersListAdapter = new OrderList(LoggedInScreenClient.this, completedOrders);
                rejectedOrdersListAdapter = new OrderList(LoggedInScreenClient.this, rejectedOrders);
                pendingOrdersListView.setAdapter(pendingOrdersListAdapter);
                completedOrdersListView.setAdapter(completedOrdersListAdapter);
                rejectedOrdersListView.setAdapter(rejectedOrdersListAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                final String TAG = "Couldn't fetch orders";
                Log.w(TAG, "loadPost:onCancelled", error.toException());
            }
        });
    }

    private void showOrderDialog(MenuItem menuItem) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.order_meal_popup, null);
        dialogBuilder.setView(dialogView);


        final TextView cookName = (TextView) dialogView.findViewById(R.id.orderMealCookName);
        final TextView cookRating = (TextView) dialogView.findViewById(R.id.orderMealCookRating);
        final TextView menuItemName = (TextView) dialogView.findViewById(R.id.orderMealName);
        final TextView menuItemDescription = (TextView) dialogView.findViewById(R.id.orderMealDescription);
        final TextView menuItemPrice = (TextView) dialogView.findViewById(R.id.orderMealPrice);
        final Button placeOrder = (Button) dialogView.findViewById(R.id.orderMealPlaceOrder);



        Cook cook = usersDbSnapshot.child(menuItem.getCookId()).getValue(Cook.class);
        cookName.setText("Cook Name: " + cook.getFirstName());
        cookRating.setText("Cook Rating: " + String.valueOf(cook.getCookRating()));
        menuItemName.setText("Item Name: " + menuItem.getName());
        menuItemDescription.setText("Item Description: " + menuItem.getDescription());
        menuItemPrice.setText("Item Price: " + menuItem.getPrice());

        TextView txtTitle = new TextView(this);
        txtTitle.setText("Place an Order");
        txtTitle.setGravity(Gravity.CENTER);

        dialogBuilder.setCustomTitle(txtTitle);
        final AlertDialog b = dialogBuilder.create();
        b.show();

        placeOrder.setOnClickListener(new View.OnClickListener() {
            View dialogView = inflater.inflate(R.layout.update_menu_item_popup, null);

            @Override
            public void onClick(View view) {
                Order order = new Order();
                order.setClientId(id);
                order.setCookId(menuItem.getCookId());
                order.setItemId(menuItem.getId());
                order.setItemName(menuItem.getName());
                order.setStatus("PENDING");
                String orderId = dbRefOrders.push().getKey();
                order.setId(orderId);
                dbRefOrders.child(orderId).setValue(order);
            }

        });

    }

    public void onItemLongClick() {

        menuItemListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                MenuItem menuItem = menuItemList.get(i);

                showOrderDialog(menuItem);
                return true;
            }
        });
    }

    public void logout(View v){
        dbRefUsers.child(id).child("loginStatus").setValue(false);
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

}