package com.example.agroikos.eofparsefragment;

import android.content.Intent;

/**
 * Created by agroikos on 29/11/2015.
 */
public class Medicine {
    public static final String ITEM_SEP = System.getProperty("line.separator");

    public final static String TITLE = "title";
    public final static String DATE = "date";
    public final static String PRICE = "price";
    public final static String BARCODE = "barcode";

    //prepei na valoume kai to state kai na kovoume to onoma kai anti gia arxeio
    //na exoume mia vash me ta local farmaka giati mpales me autes tis mlkies

    public final static String FILENAME = "filename";

    private String mTitle = new String();
    private String mDate = new String();
    private String mPrice = new String();
    private String mBarcode = new String();

    Medicine(String title, String price, String date, String barcode) {
        this.mTitle = title;
        this.mPrice = price;
        this.mDate = date;
        this.mBarcode = barcode;
    }

    // Create a new ToDoItem from data packaged in an Intent
    Medicine(Intent intent) {
        mTitle = intent.getStringExtra(Medicine.TITLE);
        mPrice = intent.getStringExtra(Medicine.PRICE);
        mDate = intent.getStringExtra(Medicine.DATE);
        mBarcode = intent.getStringExtra(Medicine.BARCODE);
    }

    public String getTitle() { return mTitle; }

    public void setTitle(String title) { mTitle = title; }

    public String getQuantity() {
        return mPrice;
    }

    public void setQuantity(String price) {
        mPrice = price;
    }

    public String getDate() {
        return mDate;
    }

    public void setDate(String date) {
        mDate = date;
    }

    public String getBarcode() {
        return mBarcode;
    }

    public void setBarcode(String barcode) {
        mBarcode = barcode;
    }

    // Take a set of String data values and
    // package them for transport in an Intent
    public static void packageIntent(Intent intent, String title, String price, String date, String barcode) {
        intent.putExtra("outputter", "new_med");
        intent.putExtra(Medicine.TITLE, title);
        intent.putExtra(Medicine.PRICE, price);
        intent.putExtra(Medicine.DATE, date);
        intent.putExtra(Medicine.BARCODE, barcode);
    }

    public String toString() {
        return mTitle + ITEM_SEP + mPrice + ITEM_SEP + mDate + ITEM_SEP + mBarcode;
    }
}
