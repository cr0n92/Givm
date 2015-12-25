package com.example.agroikos.eofparsefragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.ViewDragHelper;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class Eleipseis extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static NeedAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.eleipseis);

        Toolbar mToolBar = (Toolbar) findViewById(R.id.tool_bar_eleipseis);
        mToolBar.setTitle("Ελλείψεις");
        setSupportActionBar(mToolBar);

        try {
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

            Field mDragger = drawer.getClass().getDeclaredField("mLeftDragger"); //mRightDragger for right obviously
            mDragger.setAccessible(true);

            ViewDragHelper draggerObj = (ViewDragHelper) mDragger.get(drawer);

            Field mEdgeSize = draggerObj.getClass().getDeclaredField("mEdgeSize");
            mEdgeSize.setAccessible(true);

            int edge = 0;
            edge = mEdgeSize.getInt(draggerObj);
            mEdgeSize.setInt(draggerObj, edge * 5); //optimal value as for me, you may set any constant in dp
            // You can set it even to the value you want like mEdgeSize.setInt(draggerObj, 150); for 150dp

            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, mToolBar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.setDrawerListener(toggle);
            toggle.syncState();

            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);

        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        mAdapter = new NeedAdapter(getApplicationContext());
        ListView list = (ListView)findViewById(R.id.list);
        list.setFooterDividersEnabled(true);
        //registerForContextMenu(getListView());
        list.setAdapter(mAdapter);

        new HttpGetTask().execute();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_simple, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_personal_pharmacy) {
            startActivity(new Intent(getApplicationContext(), Farmakeio.class));
        } else if (id == R.id.nav_register_med) {
            startActivity(new Intent(getApplicationContext(), TwoButtons.class));
        } else if (id == R.id.nav_needs) {

        } else if (id == R.id.nav_donations) {
            //startActivity(new Intent(getApplicationContext(), Donations.class));
        } else if (id == R.id.nav_profile) {
            //startActivity(new Intent(getApplicationContext(), Profile.class));
        } else if (id == R.id.nav_communication) {
            //startActivity(new Intent(getApplicationContext(), Communication.class));
        } else if (id == R.id.nav_share) {
            //startActivity(new Intent(getApplicationContext(), Share.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private class HttpGetTask extends AsyncTask<Void, Void, JSONArray> {

        private static final String TAG = "HttpGetTask";
        private int error = -1;

        @Override
        protected JSONArray doInBackground(Void... arg0) {
            String URL = "http://192.168.1.93:8000/needs/";
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
                data = readStream(in);

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

        private String readStream(InputStream in) {
            BufferedReader reader = null;
            StringBuffer data = new StringBuffer("");
            try {
                reader = new BufferedReader(new InputStreamReader(in));
                String line = "";
                while ((line = reader.readLine()) != null) {
                    data.append(line);
                }
            } catch (IOException e) {
                Log.e(TAG, "IOException");
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return data.toString();
        }
    }
}