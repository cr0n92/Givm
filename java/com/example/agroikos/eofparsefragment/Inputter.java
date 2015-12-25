package com.example.agroikos.eofparsefragment;

/**
 * Created by agroikos on 22/11/2015.
 */

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

public class Inputter extends HelperActivity {
    private EditText mEditText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setMenu(R.menu.menu_main);
        super.helperOnCreate(R.layout.input_byhand, "Vale", true);

        mEditText = (EditText) findViewById(R.id.edit1);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_tick) {
            String code=mEditText.getText().toString();

            if (code.length()!=12) {
                Toast.makeText(getApplicationContext(), "Το μήκος του barcode δεν είναι έγκυρο",
                        Toast.LENGTH_LONG).show();
            }
            else {
                mEditText.setError(null);

                Intent showItemIntent = new Intent(getApplicationContext(), Outputer.class);
                showItemIntent.putExtra("data", code);
                startActivity(showItemIntent);
            }
        }

        return super.onOptionsItemSelected(item);
    }

}