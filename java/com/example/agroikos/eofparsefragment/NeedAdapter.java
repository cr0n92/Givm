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


public class NeedAdapter extends BaseAdapter {

    public final List<Need> mItems = new ArrayList<Need>();
    private final Context mContext;

    public NeedAdapter(Context context) {

        mContext = context;

    }

    public void add(Need item) {

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
        final Need toDoItem = (Need) getItem(position);


        // TODO - Inflate the View for this ToDoItem
        // from todo_item.xml
        LinearLayout itemLayout = (LinearLayout) LayoutInflater.from(mContext).inflate(
                R.layout.need, parent, false);

        // Fill in specific ToDoItem data
        // Remember that the data that goes in this View
        // corresponds to the user interface elements defined
        // in the layout file

        // TODO - Display Title in TextView
        final TextView titleView = (TextView) itemLayout.findViewById(R.id.nameView);
        titleView.setText(toDoItem.getName());

        // TODO - Display Priority in a TextView
        final TextView quantity = (TextView) itemLayout.findViewById(R.id.phoneView);
        quantity.setText(toDoItem.getPhone());

        // TODO - Display Time and Date.

        final TextView dateView = (TextView) itemLayout.findViewById(R.id.addressView);
        dateView.setText(toDoItem.getAddress());

        // Return the View you just created
        return itemLayout;

    }
}