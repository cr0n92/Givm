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


}