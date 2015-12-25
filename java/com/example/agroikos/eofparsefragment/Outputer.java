package com.example.agroikos.eofparsefragment;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.ViewDragHelper;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Outputer extends AppCompatActivity {
    private EditText mEditText1, mEditText2, mEditText3;
    private String date;
    private String name;
    private String sclearName;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.outputs);

        Toolbar mToolBar = (Toolbar) findViewById(R.id.tool_bar_outputer);
        mToolBar.setTitle("Kαταχώρηση");
        mToolBar.setNavigationIcon(R.drawable.ic_arrows);
        setSupportActionBar(mToolBar);



        mEditText1 = (EditText) findViewById(R.id.textView1_outputer);
        mEditText1.setVisibility(View.GONE);
        mEditText2 = (EditText) findViewById(R.id.textView2_outputer);
        mEditText2.setVisibility(View.GONE);
        mEditText3 = (EditText) findViewById(R.id.textView3_outputer);
        mEditText3.setVisibility(View.GONE);

        Intent intent = getIntent();
        String data = intent.getStringExtra("data");

        if (null != data) {
            new HttpGetTask().execute(data);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_tick) {
            String code = "";
            code = mEditText3.getText().toString();

            if (code.trim().equals("")) {
                Toast.makeText(getApplicationContext(), "Βάλε κανα ψιλό",
                        Toast.LENGTH_LONG).show();
            }
            else {
                mEditText3.setError(null);

                Integer quantity = Integer.parseInt(mEditText3.getText().toString());
                Farmakeio.newMed = new Medicine(sclearName, date, quantity);

                Farmakeio.mAdapter.add(Farmakeio.newMed);

                Intent showItemIntent = new Intent(getApplicationContext(), Farmakeio.class);
                showItemIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                showItemIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

                startActivity(showItemIntent);
            }
        }

        return super.onOptionsItemSelected(item);
    }

    private class HttpGetTask extends AsyncTask<String, Void, String[]> {

        private static final String TAG = "HttpGetTask";
        private int error = -1;

        @Override
        protected String[] doInBackground(String... input) {
            String URL = "http://services.eof.gr/labelsearch/";
            String[] results = new String[2];

            Pattern jsess_pattern = Pattern.compile("jsessionid=([0-9a-z]*)/?");
            Pattern state_pattern = Pattern.compile("javax.faces.ViewState\" value=\"([0-9-:]*)/?");
            Matcher jsessid, viewState;

            String request = URL;
            java.net.URL url = null;
            HttpURLConnection conn = null, conn1 = null;
            String value = input[0], data = "", data2 = "", state = "", cookie = "";

            try {
                url = new URL(request);

                conn = (HttpURLConnection) url.openConnection();//Obtain a new HttpURLConnection
                conn.setDoInput(true);
                conn.setInstanceFollowRedirects(true);
                conn.setRequestProperty("Host", "services.eof.gr");
                conn.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
                conn.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
                conn.setRequestProperty("DNT", "1");
                conn.setRequestProperty("Connection", "keep-alive");
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestProperty("charset", "utf-8");
                conn.setUseCaches(false);

                InputStream in = new BufferedInputStream(conn.getInputStream());//The response body may be read from the stream returned by getInputStream(). If the response has no body, that method returns an empty stream.
                data = readStream(in);

                jsessid = jsess_pattern.matcher(data);
                if (!jsessid.find()) throw new Exception("wrong barcode");
                cookie = jsessid.group(1);

                viewState = state_pattern.matcher(data);
                if (!viewState.find()) throw new Exception("wrong barcode");
                state = viewState.group(1).replaceAll(":", "%3A");

                String urlParameters =
                        "javax.faces.partial.ajax=true&javax.faces.source=dlSearch%3Aj_idt10%3Aj_idt14&" +
                        "javax.faces.partial.execute=%40all&javax.faces.partial.render=dlSearch&" +
                        "dlSearch%3Aj_idt10%3Aj_idt14=dlSearch%3Aj_idt10%3Aj_idt14&dlSearch%3Aj_idt10=dlSearch%3Aj_idt10&" +
                        "dlSearch%3Aj_idt10%3AtxtLbarcode_input=" + value + "&javax.faces.ViewState=" + state +
                        "&javax.faces.RenderKitId=PRIMEFACES_MOBILE";
                byte[] postData = urlParameters.getBytes(Charset.forName("UTF-8"));
                int postDataLength = postData.length;

                conn1 = (HttpURLConnection) url.openConnection();
                conn1.setDoOutput(true);
                conn1.setRequestProperty("Faces-Request", "partial/ajax");
                conn1.setRequestProperty("charset", "utf-8");
                conn1.setRequestProperty("X-Requested-With", "XMLHttpRequest");
                conn1.setRequestProperty("Connection", "keep-alive");
                conn1.setRequestProperty("Content-Length", Integer.toString(postDataLength));
                conn1.setRequestProperty("Cookie", "JSESSIONID=" + cookie);
                conn1.setUseCaches(false);

                DataOutputStream wr = new DataOutputStream(conn1.getOutputStream());//Transmit data by writing to the stream returned by getOutputStream().
                wr.write(postData);
                InputStream in2 = new BufferedInputStream(conn1.getInputStream());
                data2 = readStream(in2);

                Pattern brand_pattern = Pattern.compile("<span title=\"Περιγραφή συσκ.\">Περιγραφή συσκ.</span></td><td role=\"gridcell\"><span title=\"Περιγραφή συσκ.\">([^<]*)");
                Pattern date_pattern = Pattern.compile("<span title=\"Ημ/νία λήξης\">Ημ/νία λήξης</span></td><td role=\"gridcell\"><span title=\"Ημ/νία λήξης\">([^<]*)");
                Matcher brand, date;

                date = date_pattern.matcher(data2);
                if (!date.find()) throw new Exception("wrong barcode");
                results[0] = date.group(1);

                brand = brand_pattern.matcher(data2);
                if (!brand.find()) throw new Exception("wrong barcode");
                results[1] = brand.group(1);

            } catch (ProtocolException e) {
                error = 1;
                Log.e(TAG, "ProtocolException");
            } catch (MalformedURLException exception) {
                error = 1;
                Log.e(TAG, "MalformedURLException");
            } catch (IOException exception) {
                error = 1;
                Log.e(TAG, "IOException");
            } catch (Exception exp) {
                error = 2;
                Log.e(TAG, "Wrong barcode");
            } finally {
                if (null != conn)
                    conn.disconnect();

                if (null != conn1)
                    conn1.disconnect();
            }

            return results;
        }

        @Override
        protected void onPostExecute(String[] result) {
            if (error > 0) {
                Toast.makeText(getApplicationContext(), (error == 1)? "No internet connection" : "Wrong barcode number",
                        Toast.LENGTH_LONG).show();
                Intent showItemIntent = new Intent(getApplicationContext(), Inputter.class);
                showItemIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                showItemIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(showItemIntent);
            }
            else {
                mEditText3.setVisibility(View.VISIBLE);//MPAKALIKO
                mEditText1.setText(result[0]);
                mEditText1.setEnabled(false);
                mEditText1.setVisibility(View.VISIBLE);//MPAKALIKO
                mEditText2.setText(result[1]);
                mEditText2.setVisibility(View.VISIBLE);//MPAKALIKO
                mEditText2.setEnabled(false);

                date = result[0];
                name = result[1];
                Pattern clearNamePattern = Pattern.compile("(.+[MG]).*");
                Matcher clearName;
                clearName=clearNamePattern.matcher(name);
                clearName.find();
                sclearName = clearName.group(1);
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