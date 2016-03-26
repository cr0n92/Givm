package com.example.agroikos.eofparsefragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by agroikos on 29/11/2015.
 */
public class medAdapter extends BaseAdapter {

    public final List<Medicine> mItems = new ArrayList<Medicine>();
    private final Context mContext;

    public medAdapter(Context context) {
        mContext = context;
    }

    public void add(Medicine item) {
        mItems.add(item);
        notifyDataSetChanged();
    }

    public void remove(Object item) {
        mItems.remove(item);
        notifyDataSetChanged();
    }

    // Clears the list adapter of all items.
    public void clear() {
        mItems.clear();
        notifyDataSetChanged();
    }

    // Returns the number of ToDoItems
    @Override
    public int getCount() {
        return mItems.size();
    }

    // Retrieve the number of the ToDoItem
    @Override
    public Object getItem(int pos) {
        return mItems.get(pos);
    }

    // Get the ID for the ToDoItem
    // In this case it's just the position
    @Override
    public long getItemId(int pos) {
        return pos;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) { //called after setAdapter,notifyDataSetChanged
        // TODO - Get the current ToDoItem
        final Medicine toDoItem = (Medicine) getItem(position);

        // TODO - Inflate the View for this ToDoItem
        // from todo_item.xml
        LinearLayout itemLayout = (LinearLayout) LayoutInflater.from(mContext).inflate(
                R.layout.medicine, parent, false);

        // Fill in specific ToDoItem data
        // Remember that the data that goes in this View
        // corresponds to the user interface elements defined
        // in the layout file

        // TODO - Display Title in TextView
        final TextView titleView = (TextView) itemLayout.findViewById(R.id.titleView);
        titleView.setText(toDoItem.getTitle());

        // TODO - Display Priority in a TextView
        final TextView quantity = (TextView) itemLayout.findViewById(R.id.quantityView);
        quantity.setText(toDoItem.getQuantity());

        // TODO - Display Time and Date.
        final TextView dateView = (TextView) itemLayout.findViewById(R.id.dateView);
        dateView.setText(toDoItem.getDate());

        final TextView barcodeView = (TextView) itemLayout.findViewById(R.id.barcodeView);
        barcodeView.setText(toDoItem.getBarcode());

        // Return the View you just created
        return itemLayout;
    }
}
