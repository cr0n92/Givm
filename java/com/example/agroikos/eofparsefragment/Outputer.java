package com.example.agroikos.eofparsefragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.AlertDialog;
import android.util.Log;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Outputer extends HelperActivity {
    private EditText mEditText1, mEditText2;
    private CheckBox mCheckBox;
    private String sclearName = "", name = "", date = "", barcode = "", eofcode = "", is_it = "", price = "";
    AlertDialog alert;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setMenu(R.menu.menu_main);
        super.helperOnCreate(R.layout.outputs, R.string.outputer, true);

        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        date = intent.getStringExtra("date");
        eofcode = intent.getStringExtra("eofcode");

        // pernoume mono thn prwth le3h tou farmakou
        String sclearName = null;
        if(name.contains(" "))
            sclearName = name.substring(0, name.indexOf(" "));
        else
            sclearName = name;
        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!TEDX!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

        barcode = intent.getStringExtra("barcode");

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Το φάρμακο έχει ήδη καταχωρηθεί!")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent TedxIntent = new Intent(getApplicationContext(), Farmakeio.class);
                        TedxIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        TedxIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(TedxIntent);
                    }
                });
        alert = builder.create();
        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!TEDX!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

        mEditText1 = (EditText) findViewById(R.id.textView1_outputer);
        mEditText1.setText(date);
        mEditText2 = (EditText) findViewById(R.id.textView2_outputer);
        mEditText2.setText(name);
        mCheckBox = (CheckBox) findViewById(R.id.opend);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_tick) {
            boolean checked = mCheckBox.isChecked();
            is_it = (checked) ? "2" : "1";
            //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!TEDX!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
            new HttpGetTask().execute();
            //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!TEDX!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        }

        return super.onOptionsItemSelected(item);
    }
    //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!TEDX!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

    private class HttpGetTask extends AsyncTask<Object, Void, Integer> {

        private static final String TAG = "HttpGetTask_Outputer";
        private int error = -1;
        private int result ;
        @Override
        protected Integer doInBackground(Object... input) {

            String data = "";

            java.net.URL url = null;
            HttpURLConnection conn2 = null;
            String state = is_it.equals("2") ? "O" : "C";

            String URL = "http://" + server + "/med_check/12345/";

            try {
                url = new URL(URL);
                String urlParameters =
                        "state=" + state +
                        "&name=" + name +
                        "&expirationDate=" + date +
                        "&eofcode=" + eofcode +
                        "&barcode=" + barcode;

                byte[] postData = urlParameters.getBytes(Charset.forName("UTF-8"));
                int postDataLength = postData.length;
                conn2 = (HttpURLConnection) url.openConnection();//Obtain a new HttpURLConnection
                conn2.setDoOutput(true);
                DataOutputStream wr = new DataOutputStream(conn2.getOutputStream());//Transmit data by writing to the stream returned by getOutputStream().
                wr.write(postData);
                InputStream in = new BufferedInputStream(conn2.getInputStream());//The response body may be read from the stream returned by getInputStream(). If the response has no body, that method returns an empty stream.
                data = readStream(in);

                Log.e(TAG, "Komple? " + data);

                result = conn2.getResponseCode();

                JSONObject obj;
                if (result != 204) {
                    obj = new JSONObject(data);
                    price = obj.getString("medPrice");
                }
                Log.e(TAG, "Received HTTP response: " + result);

            } catch (ProtocolException e) {
                error = 1;
                Log.e(TAG, "ProtocolException");
                e.printStackTrace();
            } catch (MalformedURLException exception) {
                error = 1;
                Log.e(TAG, "MalformedURLException");
                exception.printStackTrace();
            } catch (IOException exception) {
                error = 2;
                Log.e(TAG, "IOException");
                exception.printStackTrace();
            } catch (JSONException e) {
                error = 2;
                e.printStackTrace();
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
            } else {
                if (result == 204) {
                    alert.show();
                    return;
                }

                Log.i("lege re", name + " " + price + " " + date + " " + barcode);

                Intent showItemIntent = new Intent(getApplicationContext(), Farmakeio.class);
                Medicine.packageIntent(showItemIntent, name, price, date, barcode);
                showItemIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                showItemIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(showItemIntent);
            }
            //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!TEDX!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        }
    }

}