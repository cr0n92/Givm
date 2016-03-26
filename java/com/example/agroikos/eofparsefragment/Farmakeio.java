package com.example.agroikos.eofparsefragment;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.ParseException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.List;

public class Farmakeio extends HelperActivity {

    private static final int GIVER_REQUEST = 0;
    public static medAdapter mAdapter;
    public static Medicine newMed;
    private static final String FILE_NAME = "GivMed6.txt";
    private static TextView sumPriceView;
    public static double sumPrice;

    // IDs for menu items
    private static final int MENU_DELETE = Menu.FIRST;
    private static final int MENU_SHARE = Menu.FIRST + 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setMenu(R.menu.menu_main_simple);
        super.helperOnCreate(R.layout.pharmacy, R.string.farmakeio, true);

        sumPriceView = (TextView) findViewById(R.id.sumPriceView);
        sumPriceView.setText("0.0");

        final FloatingActionButton floatingBut = (FloatingActionButton) findViewById(R.id.fab);
        floatingBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Farmakeio.this, TwoButtons.class));
            }
        });

        mAdapter = new medAdapter(getApplicationContext());

        ListView list = (ListView)findViewById(R.id.list);
        list.setFooterDividersEnabled(true);
        registerForContextMenu(list);
        list.setAdapter(mAdapter);
    }

    @Override
    protected void onNewIntent (Intent intent){
        if (intent.hasExtra("outputter")) {
            setIntent(intent);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        Log.e("Activity :", "" + hashCode());

        //edw pairnoume to intent ka8e fora, epeidh kaleite panta h onResume
        //alla an einai hdh anoixto to Farmakeio.class tote prepei na valoume
        //thn onNewIntent giati den evlepe to intent mesa sthn onResume
        Intent intent = getIntent();
        if (intent.hasExtra("outputter") && !intent.hasExtra("consumed")) {
            newMed = new Medicine(intent);
            mAdapter.add(newMed);

            sumPrice += Double.parseDouble(newMed.getQuantity());
            sumPriceView.setText(Double.toString(Math.round(sumPrice * 100d) / 100d));

            //otan paroume kai xrhsimopoihsoume to intent tote vazoume ena epipleon pedio
            //gia na 3eroume oti den to xreiazomaste
            intent.putExtra("consumed", true);
        }

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

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu,View view, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, view, menuInfo);

        menu.add(Menu.NONE, MENU_DELETE, Menu.NONE, "Delete medicine");
        menu.add(Menu.NONE, MENU_SHARE, Menu.NONE, "Share Medicine");

        AdapterView.AdapterContextMenuInfo info;
        try {
            info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        } catch (ClassCastException e) {
            Log.e("farmakeio", "bad menuInfo", e);
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
            Log.e("farmakeio", "bad menuInfo", e);
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
                                Medicine delMed = (Medicine) mAdapter.getItem(info.position);
                                sumPrice -= Double.parseDouble(delMed.getQuantity());
                                sumPriceView.setText(Double.toString(Math.round(sumPrice * 100d) / 100d));
                                //new HttpGetTask().execute(delMed.getBarcode());
                                mAdapter.remove(delMed);

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
            case MENU_SHARE:
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, "http://www.google.fr/");
                startActivity(Intent.createChooser(intent, "Share with"));
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

            sumPrice = 0;
            String title = null, date = null, price = null, barcode = null;

            while (null != (title = reader.readLine())) {
                price = reader.readLine();
                date = reader.readLine();
                barcode = reader.readLine();

                mAdapter.add(new Medicine(title, price, date, barcode));

                sumPrice += Double.parseDouble(price);
            }

            sumPriceView.setText(Double.toString(Math.round(sumPrice * 100d) / 100d));

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

    private class HttpGetTask extends AsyncTask<String, Void, Integer> {

        private static final String TAG = "HttpGetTask_Pharmacy";
        private int error = -1;
        private int result;

        @Override
        protected Integer doInBackground(String... input) {
            String data = "";
            java.net.URL url = null;
            HttpURLConnection conn2 = null;

            String URL = "http://" + server + "/med_delete/" + input[0] + "/";

            try {
                url = new URL(URL);

                conn2 = (HttpURLConnection) url.openConnection();
                conn2.setRequestMethod("DELETE");

                result = conn2.getResponseCode();
                Log.e(TAG, "Received HTTP response: " + result);

            } catch (ProtocolException e) {
                error = 1;
                Log.e(TAG, "ProtocolException");
                e.printStackTrace();
            } catch (MalformedURLException exception) {
                error = 1;
                Log.e(TAG, "MalformedURLException");
            } catch (IOException exception) {
                error = 2;
                Log.e(TAG, "IOException");
            } finally {
                if (null != conn2)
                    conn2.disconnect();

            }

            return result;
        }

        @Override
        protected void onPostExecute(Integer result) {
            if (error > 0) {
                Toast.makeText(getApplicationContext(), (error == 1) ? "No internet connection" : "Server error",
                        Toast.LENGTH_LONG).show();
            }
        }
    }
}
