package com.example.agroikos.eofparsefragment;

import android.content.Intent;

/**
 * Created by agroikos on 29/11/2015.
 */
public class Need {
    public static final String ITEM_SEP = System.getProperty("line.separator");



    public final static String NEED_NAME = "name";
    public final static String NEED_ID = "id";
    public final static String NEED_PHONE = "phone";
    public final static String NEED_ADDRESS = "address";
    public final static String FILENAME = "filename";


    int mID;
    private String mNeedMedName = new String();
    private String mNeedPhone = new String();
    private String mNeedAddress = new String();


    public Need() {

    }

    public Need(int id, String needMedName, String needAddress,String needPhone){
        this.mID = id;
        this.mNeedMedName = needMedName;
        this.mNeedAddress = needAddress;
        this.mNeedPhone = needPhone;
    }

    // constructor
    public Need(String needMedName, String needAddress,String needPhone){
        this.mNeedMedName = needMedName;
        this.mNeedAddress = needAddress;
        this.mNeedPhone = needPhone;
    }

    Need(Intent intent) {

        mNeedMedName = intent.getStringExtra(Need.NEED_NAME);
        mNeedPhone = intent.getStringExtra(Need.NEED_PHONE);
        mNeedAddress = intent.getStringExtra(Need.NEED_ADDRESS);


    }

    // getting ID
    public int getID(){
        return this.mID;
    }

    // setting id
    public void setID(int id){
        this.mID = id;
    }

    public String getName() {
        return mNeedMedName;
    }

    public void setName(String name) {
        mNeedMedName = name;
    }

    public String getPhone() {
        return mNeedPhone;
    }

    public void setPhone(String phone) {
        mNeedPhone = phone;
    }

    public String getAddress() {
        return mNeedAddress;
    }

    public void setAddress(String address) {
        mNeedAddress = address;
    }



    // Take a set of String data values and
    // package them for transport in an Intent

    public static void packageIntent(Intent intent, String name,
                                     String phone, String address) {

        intent.putExtra(Need.NEED_NAME, name);
        intent.putExtra(Need.NEED_PHONE, phone);
        intent.putExtra(Need.NEED_ADDRESS, address);

    }

    public String toString() {
        return mNeedMedName + ITEM_SEP + mNeedPhone + ITEM_SEP + mNeedAddress;
    }

   /* public String toLog() {
        return "Title:" + mTitle + ITEM_SEP + "Priority:" + mPriority
                + ITEM_SEP + "Status:" + mStatus + ITEM_SEP + "Date:"
                + FORMAT.format(mDate) + "\n";
    }
    */
}