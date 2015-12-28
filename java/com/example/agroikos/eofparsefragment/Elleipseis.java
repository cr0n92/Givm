package com.example.agroikos.eofparsefragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
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

public class Elleipseis extends HelperActivity
{

    public static NeedAdapter mAdapter;

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

    private class HttpGetTask extends AsyncTask<Void, Void, JSONArray> {

        private static final String TAG = "HttpGetTask";
        private int error = -1;

        @Override
        protected JSONArray doInBackground(Void... arg0) {
            String URL = "http://192.168.1.2:8000/needs/";
            String data="";
            JSONArray out=null;

            String request = URL;
            java.net.URL url = null;
            HttpURLConnection conn = null;

            try {
                url = new URL(request);
                conn = (HttpURLConnection) url.openConnection();//Obtain a new HttpURLConnection
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
            }
            else {
                try {
                    // create apps list
                    //List<Application> apps = new ArrayList<Application>();

                    for(int i=0; i<result.length(); i++) {
                        JSONObject json = result.getJSONObject(i);
                        Need needo = new Need();

                        needo.setName(json.getString("needMedName"));
                        needo.setPhone(json.getString("needPhone"));
                        needo.setAddress(json.getString("needAddress"));

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