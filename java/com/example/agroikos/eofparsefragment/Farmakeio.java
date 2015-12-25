package com.example.agroikos.eofparsefragment;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ParseException;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class Farmakeio extends ListActivity {

    private static final int GIVER_REQUEST = 0;
    public static medAdapter mAdapter;
    public static Medicine newMed;
    private static final String FILE_NAME = "GivMed6.txt";

    // IDs for menu items
    private static final int MENU_DELETE = Menu.FIRST;
    private static final int MENU_GIVE = Menu.FIRST + 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create a new TodoListAdapter for this ListActivity's ListView
        mAdapter = new medAdapter(getApplicationContext());
        getListView().setFooterDividersEnabled(true);

        TextView footerView = (TextView) getLayoutInflater().inflate(R.layout.footer_view, null);
        getListView().addFooterView(footerView);
        registerForContextMenu(getListView());

        footerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Farmakeio.this, TwoButtons.class));
            }
        });

        // TODO - Attach the adapter to this ListActivity's ListView
        getListView().setAdapter(mAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();

        // Load saved ToDoItems, if necessary
        Log.i("Activity :", "" + hashCode());

        //Log.i("Count :", "" + mAdapter.getCount());

        if (mAdapter.getCount() == 0)
            loadItems();
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Save ToDoItems
        saveItems();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        // TODO - Check result code and request code
        if (resultCode == RESULT_OK && requestCode == GIVER_REQUEST) {
            Integer posa = data.getIntExtra("posothta",-1);
            Integer pos = data.getIntExtra("thesh",-1);

            Log.i("posa :", "" + posa);
            Medicine change = (Medicine) mAdapter.getItem(pos);
            Integer previous = change.getQuantity();
            if (posa == previous) {
                mAdapter.remove(mAdapter.getItem(pos));
                saveItems();
            }
            else {
                change.setQuantity(previous - posa);
                mAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu,View view, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, view, menuInfo);

        menu.add(Menu.NONE, MENU_DELETE, Menu.NONE, "Delete medicine");
        menu.add(Menu.NONE, MENU_GIVE, Menu.NONE, "Give Medicine");

        AdapterView.AdapterContextMenuInfo info;
        try {
            info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        } catch (ClassCastException e) {
            Log.e("kaka", "bad menuInfo", e);
            return;
        }
        long id = info.position;
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        final AdapterView.AdapterContextMenuInfo info;
        try {
            info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        } catch (ClassCastException e) {
            Log.e("kaka", "bad menuInfo", e);
            return false;

        }
         final long id = info.position;
            View medView=info.targetView;

        switch (item.getItemId()) {
            case MENU_DELETE:
                AlertDialog alertDialog = new AlertDialog.Builder(Farmakeio.this).create();
                alertDialog.setTitle("Delete entry");
                alertDialog.setMessage("Are you sure you want to delete this entry?");
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Log.i("Count before:", "" + mAdapter.getCount());
                                mAdapter.remove(mAdapter.getItem(info.position));
                                //mAdapter.mItems.remove(id);
                                //mAdapter.notifyDataSetChanged();
                                Log.i("Count after:", "" + mAdapter.getCount());

                                Log.i("Thesh :", "" + id);


                            }
                        });
                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                alertDialog.show();

                return true;
            case MENU_GIVE:
                Intent GiverIntent = new Intent(getApplicationContext(), Giver.class);
                //showItemIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                //showItemIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

                // Start the QuoteListActivity using Activity.startActivity()
                GiverIntent.putExtra("Pos",id);;
                startActivityForResult(GiverIntent,GIVER_REQUEST);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    // Load stored ToDoItems
    private void loadItems() {
        BufferedReader reader = null;
        try {
            FileInputStream fis = openFileInput(FILE_NAME);
            reader = new BufferedReader(new InputStreamReader(fis));

            String title = null;
            String date = null;
            Integer quantity = null;

            while (null != (title = reader.readLine())) {
                quantity = Integer.parseInt(reader.readLine());
                date = reader.readLine();

                mAdapter.add(new Medicine(title,date,
                        quantity));
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } finally {
            if (null != reader) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // Save ToDoItems to file
    private void saveItems() {
        PrintWriter writer = null;
        try {
            FileOutputStream fos = openFileOutput(FILE_NAME, MODE_PRIVATE);
            writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
                    fos)));

            for (int idx = 0; idx < mAdapter.getCount(); idx++) {

                writer.println(mAdapter.getItem(idx));

            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != writer) {
                writer.close();
            }
        }
    }
}
