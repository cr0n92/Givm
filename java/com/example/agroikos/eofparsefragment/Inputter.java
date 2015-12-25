package com.example.agroikos.eofparsefragment;

/**
 * Created by agroikos on 22/11/2015.
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.ViewDragHelper;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import java.lang.reflect.Field;

public class Inputter extends AppCompatActivity {
    private EditText mEditText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.input_byhand);

        Toolbar mToolBar = (Toolbar) findViewById(R.id.tool_bar);
        mToolBar.setTitle("Εισαγωγή");
        mToolBar.setNavigationIcon(R.drawable.ic_arrows);
        setSupportActionBar(mToolBar);


        mEditText = (EditText) findViewById(R.id.edit1);
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
            String code=mEditText.getText().toString();

            if (code.length()!=12) {
                Toast.makeText(getApplicationContext(), "Το μήκος του barcode δεν είναι έγκυρο",
                        Toast.LENGTH_LONG).show();
                //mEditText.setError("Το μήκος του barcode δεν είναι έγκυρο");
            }
            else {
                mEditText.setError(null);

                Intent showItemIntent = new Intent(getApplicationContext(), Outputer.class);

                // Add an Extra representing the currently selected position
                // The name of the Extra is stored in INDEX
                showItemIntent.putExtra("data", code);

                // Start the QuoteListActivity using Activity.startActivity()
                startActivity(showItemIntent);
            }
        }

        return super.onOptionsItemSelected(item);
    }

}