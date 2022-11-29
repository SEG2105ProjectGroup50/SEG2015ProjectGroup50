package com.example.mytable;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class OrderList extends ArrayAdapter<Order>{
    private Activity context;
    List<Order> orders;

    public OrderList(Activity context, List<Order> orders) {
        super(context, R.layout.order, orders);
        this.context = context;
        this.orders = orders;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.order, null, true);

        TextView orderCookId = (TextView) listViewItem.findViewById(R.id.orderCookId);
        TextView orderClientId = (TextView) listViewItem.findViewById(R.id.orderClientId);
        TextView orderItemName = (TextView) listViewItem.findViewById(R.id.orderItemName);

        Order order = orders.get(position);
        orderCookId.setText(order.getCookId());
        orderClientId.setText(order.getClientId());
        orderItemName.setText(order.getItemName());
        return listViewItem;
    }
}
