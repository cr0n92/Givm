package com.example.agroikos.eofparsefragment;

import android.content.Intent;
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
import android.widget.EditText;
import android.widget.Toast;

import java.lang.reflect.Field;

public class Giver extends AppCompatActivity {
    private EditText mEditText1;
    private EditText mEditText2;
    private Integer pos;
    private Integer posothta;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.giver);

        Toolbar mToolBar = (Toolbar) findViewById(R.id.tool_bar);
        mToolBar.setTitle("Εισαγωγή");
        mToolBar.setNavigationIcon(R.drawable.ic_arrows);
        setSupportActionBar(mToolBar);


        mEditText1 = (EditText) findViewById(R.id.edit2);
        mEditText2 = (EditText) findViewById(R.id.edit3);
        Intent i = getIntent();
        Bundle extras = i.getExtras();

        pos = (int) (long) extras.getLong("Pos",-1);
        //Integer pos1 = (int) (long) pos;
        Log.i("Xeri :", "" + pos);
        Medicine newMed = (Medicine) Farmakeio.mAdapter.getItem(pos);
        posothta = newMed.getQuantity();
        Log.i("POsothta :", "" + newMed.getQuantity());



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
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
            Integer number = Integer.parseInt(mEditText1.getText().toString());
            if (number > posothta) {
                Toast.makeText(getApplicationContext(), "Δεν έχεις τόσα φάρμακα τρελέ",
                        Toast.LENGTH_LONG).show();
            }
            else{
                String comment=mEditText2.getText().toString();
                Intent data = new Intent();
                data.putExtra("posothta",number);
                data.putExtra("thesh",pos);
                setResult(RESULT_OK,data);
                finish();
            }

           /* if (code.length()!=12) {
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
            }*/
        }

        return super.onOptionsItemSelected(item);
    }

}