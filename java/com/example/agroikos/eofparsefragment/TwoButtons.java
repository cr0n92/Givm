package com.example.agroikos.eofparsefragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import google.zxing.integration.android.IntentIntegrator;
import google.zxing.integration.android.IntentResult;

public class TwoButtons extends HelperActivity
{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.helperOnCreate(R.layout.mainee, true);

        final Button scanButton = (Button) findViewById(R.id.button1);
        scanButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                IntentIntegrator scanIntegrator = new IntentIntegrator(TwoButtons.this);
                scanIntegrator.initiateScan();
            }
        });

        final Button userButton = (Button) findViewById(R.id.button2);
        userButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(TwoButtons.this, Inputter.class);
                startActivity(myIntent);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_simple, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanningResult.getContents() != null) {
            String scanContent = scanningResult.getContents();
            Intent showItemIntent = new Intent(getApplicationContext(), Outputer.class);
            showItemIntent.putExtra("data", scanContent);
            startActivity(showItemIntent);
        }
        else{
            Toast toast = Toast.makeText(getApplicationContext(),
                    "No scan data received!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }


}