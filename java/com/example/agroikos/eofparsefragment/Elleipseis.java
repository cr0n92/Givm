package com.example.agroikos.eofparsefragment;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.List;

public class Elleipseis extends HelperActivity
{

    public static NeedAdapter mAdapter;
    NeedHandler db = new NeedHandler(this);


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setMenu(R.menu.menu_main_simple);
        super.helperOnCreate(R.layout.eleipseis, R.string.elleipseis, false);

        mAdapter = new NeedAdapter(getApplicationContext());
        ListView list = (ListView)findViewById(R.id.list);
        list.setFooterDividersEnabled(true);
        //registerForContextMenu(getListView());
        list.setAdapter(mAdapter);

        new HttpGetTask().execute();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        Log.i("KAI ETSI 2", "" + id);

        return super.onOptionsItemSelected(item);
    }

    //Gia na tsekaroume pio grhgora an prepei na paroume ta dedomena apo thn SQLite
    //Alliws argei upervolika
    //Allh enallaktikh:InetAddress.getByName(host).isReachable(timeOut)->den douleuei panta kala
    //Isws timeout sto http connection
    //http://stackoverflow.com/questions/1443166/android-how-to-check-if-the-server-is-available

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    private class HttpGetTask extends AsyncTask<Void, Void, JSONArray> {

        private static final String TAG = "HttpGetTask";
        private int error = -1;

        @Override
        protected JSONArray doInBackground(Void... arg0) {

            String URL = "http://192.168.1.2:8000/needs/";

            String data = "";
            JSONArray out = null;

            if (!isOnline()){
                try {
                    throw new IOException();
                } catch (IOException e) {
                    error = 1;
                    e.printStackTrace();
                    return out;
                }
            }

            String request = URL;
            java.net.URL url = null;
            HttpURLConnection conn = null;

            try {
                url = new URL(request);
                conn = (HttpURLConnection) url.openConnection();//Obtain a new HttpURLConnection

                //conn.setConnectTimeout(10* 1000);          // 10 s.
                //conn.connect();

                conn.setDoInput(true);

                InputStream in = new BufferedInputStream(conn.getInputStream());//The response body may be read from the stream returned by getInputStream(). If the response has no body, that method returns an empty stream.
                data = HelperActivity.readStream(in);

                out = new JSONArray(data);

            } catch (JSONException e) {
                Log.e(TAG, "JsonException");
            } catch (ProtocolException e) {
                error = 1;
                Log.e(TAG, "ProtocolException");
            } catch (MalformedURLException exception) {
                error = 1;
                Log.e(TAG, "MalformedURLException");
            } catch (IOException exception) {
                error = 1;
                Log.e(TAG, "IOException");
            } finally {
                if (null != conn)
                    conn.disconnect();
            }

            return out;
        }

        @Override
        protected void onPostExecute(JSONArray result) {
            if (error > 0) {
                Toast.makeText(getApplicationContext(), (error == 1)? "No internet connection" : "Nothing to show",
                        Toast.LENGTH_LONG).show();
                //TODO na to ftiaksoume kalutera,kanei diplh douleia

                List<Need> needs = db.getAllNeeds();
                for (Need element : needs) {
                    mAdapter.add(element);

                }
            }
            else {
                try {
                    // create apps list
                    //List<Application> apps = new ArrayList<Application>();
                    db.deleteAll();

                    for(int i=0; i<result.length(); i++) {
                        JSONObject json = result.getJSONObject(i);
                        Need needo = new Need();
                        needo.setID(json.getInt("id"));
                        needo.setName(json.getString("needMedName"));
                        needo.setPhone(json.getString("needPhone"));
                        needo.setAddress(json.getString("needAddress"));
                        db.addNeed(new Need(json.getInt("id"),json.getString("needMedName"), json.getString("needPhone"), json.getString("needAddress")));

                        List<Need> needs = db.getAllNeeds();

                        for (Need cn : needs) {
                            String log = "Id: "+cn.getID()+" ,Name: " + cn.getName()+",Address: " + cn.getAddress()+ " ,Phone: " + cn.getPhone();
                            // Writing Contacts to log
                            Log.d("Name: ", log);
                        }

                        // add the app to apps list
                        mAdapter.add(needo);
                    }

                    //notify the activity that fetch data has been complete
                } catch (JSONException e) {
                    Log.e(TAG, ""+e);
                }
            }
        }
    }
}