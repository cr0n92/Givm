package com.example.agroikos.eofparsefragment;

import android.content.Intent;

/**
 * Created by agroikos on 29/11/2015.
 */
public class Medicine {
    public static final String ITEM_SEP = System.getProperty("line.separator");



    public final static String TITLE = "title";
    public final static String DATE = "date";
    public final static String QUANTITY = "quantity";
    public final static String FILENAME = "filename";



    private String mTitle = new String();
    private String mDate = new String();
    private Integer mQuantity = new Integer(0);


    Medicine(String title, String date, Integer quanity) {
        this.mTitle = title;
        this.mQuantity = quanity;
        this.mDate = date;
    }

    // Create a new ToDoItem from data packaged in an Intent

    Medicine(Intent intent) {

        mTitle = intent.getStringExtra(Medicine.TITLE);
        mQuantity = Integer.parseInt(intent.getStringExtra(Medicine.QUANTITY));
        mDate = intent.getStringExtra(Medicine.DATE);


    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public Integer getQuantity() {
        return mQuantity;
    }

    public void setQuantity(Integer quantity) {
        mQuantity = quantity;
    }

    public String getDate() {
        return mDate;
    }

    public void setDate(String date) {
        mDate = date;
    }



    // Take a set of String data values and
    // package them for transport in an Intent

    public static void packageIntent(Intent intent, String title,
                                     String quantity, String date) {

        intent.putExtra("outputter", "new_med");
        intent.putExtra(Medicine.TITLE, title);
        intent.putExtra(Medicine.QUANTITY, quantity);
        intent.putExtra(Medicine.DATE, date);

    }

    public String toString() {
        return mTitle + ITEM_SEP + mQuantity + ITEM_SEP + mDate;
    }

   /* public String toLog() {
        return "Title:" + mTitle + ITEM_SEP + "Priority:" + mPriority
                + ITEM_SEP + "Status:" + mStatus + ITEM_SEP + "Date:"
                + FORMAT.format(mDate) + "\n";
    }
    */
}
