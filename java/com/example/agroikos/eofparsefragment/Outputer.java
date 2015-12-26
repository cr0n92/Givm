package com.example.agroikos.eofparsefragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Outputer extends HelperActivity {
    private EditText mEditText1, mEditText2, mEditText3;
    private String sclearName = "", date = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setMenu(R.menu.menu_main);
        super.helperOnCreate(R.layout.outputs, R.string.outputer, true);

        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        date = intent.getStringExtra("date");

        Pattern clearNamePattern = Pattern.compile("(.+[MG]).*");
        Matcher clearName;
        clearName=clearNamePattern.matcher(name);
        clearName.find();
        sclearName = clearName.group(1);

        mEditText1 = (EditText) findViewById(R.id.textView1_outputer);
        mEditText1.setText(date);
        mEditText2 = (EditText) findViewById(R.id.textView2_outputer);
        mEditText2.setText(name);
        mEditText3 = (EditText) findViewById(R.id.textView3_outputer);
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

                String quantity = (String) (mEditText3.getText().toString());
                //Farmakeio.newMed = new Medicine(sclearName, date, quantity);



                //Farmakeio.mAdapter.add(Farmakeio.newMed);

                Intent showItemIntent = new Intent(getApplicationContext(), Farmakeio.class);
                Medicine.packageIntent(showItemIntent ,sclearName,quantity,date);

                //If set, and the activity being launched is already running in the current task, then instead of
                // launching a new instance of that activity, all of the other activities on top of it will be closed and
                // this Intent will be delivered to the (now on top) old activity as a new Intent.
                showItemIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                //If set, the activity will not be launched if it is already running at the top of the history stack.
                showItemIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

                startActivity(showItemIntent);
            }
        }

        return super.onOptionsItemSelected(item);
    }


}