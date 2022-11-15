package com.example.mytable;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class MenuItemList extends ArrayAdapter<MenuItem>{
    private Activity context;
    List<MenuItem> menuItems;

    public MenuItemList(Activity context, List<MenuItem> menuItems) {
        super(context, R.layout.menu_item, menuItems);
        this.context = context;
        this.menuItems = menuItems;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.menu_item, null, true);

        TextView menuItemName = (TextView) listViewItem.findViewById(R.id.menuItemName);
        TextView menuItemDescription = (TextView) listViewItem.findViewById(R.id.menuItemDescription);
        TextView menuItemPrice = (TextView) listViewItem.findViewById(R.id.menuItemPrice);
        TextView menuItemIsOffered = (TextView) listViewItem.findViewById(R.id.menuItemIsOffered);

        MenuItem menuItem = menuItems.get(position);

        menuItemName.setText(menuItem.getName());
        menuItemDescription.setText(menuItem.getDescription());
        menuItemPrice.setText(menuItem.getPrice());
        if (menuItem.getIsOffered()) {
            menuItemIsOffered.setText("Yes");
        } else {
            menuItemIsOffered.setText("No");
        }
        return listViewItem;
    }
}
