package com.example.i3enz.moneymanagementv1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import java.util.ArrayList;
import java.util.List;

public class MainTable {

    private MySQLiteOpenHelper objMySQLiteOpenHelper;
    private SQLiteDatabase writeSqLiteDatabase, readSqLiteDatabase;
    //receive integer ,pay integer ,categories text ,note text
    public static final String MAIN_TABLE = "mainTABLE";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_RECEIVE = "receive";
    public static final String COLUMN_PAY = "pay";
    public static final String COLUMN_CATEGORIES = "categories";
    public static final String COLUMN_NOTE = "note";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_LATITUDE = "latitude";
    public static final String COLUMN_LONGITUDE = "longitude";
   // public static final String KEY_NAME = "name";
   // public static final String KEY_IMAGE = "image";

    public static final String CATEGORIES_TABLE = "categoriesTABLE";
    public static final String COLUMN_IDCATEGORIES = "_id";
    public static final String COLUMN_RECEIVECATEGORIES = "receivetype";
    public static final String COLUMN_PAYCATEGORIES = "paytype";

    public static final String ALERT_TABLE = "alertTABLE";
    public static final String COLUMN_SETPAY = "setpay";

    public static final String CONTACTS_TABLE = "contactsTABLE";
    public static final String KEY_ID = "id";
    public static final String KEY_NAME = "name";
    public static final String KEY_IMAGE = "image";

    public MainTable(Context context){
        objMySQLiteOpenHelper = new MySQLiteOpenHelper(context);
        writeSqLiteDatabase = objMySQLiteOpenHelper.getWritableDatabase();
        readSqLiteDatabase = objMySQLiteOpenHelper.getReadableDatabase();
        //constructor
    }

    public long addNewAlert(int intSetpay){
        ContentValues objContentValues = new ContentValues();
        objContentValues.put(COLUMN_SETPAY, intSetpay);
        return readSqLiteDatabase.insert(ALERT_TABLE, null, objContentValues);
    }

    public long addNewCategories(String strReceive, String strPay){
        ContentValues objContentValues = new ContentValues();
        objContentValues.put(COLUMN_RECEIVECATEGORIES, strReceive);
        objContentValues.put(COLUMN_PAYCATEGORIES, strPay);
        return readSqLiteDatabase.insert(CATEGORIES_TABLE, null, objContentValues);
    }


    public long addNewItem(int intReceive, int intPay, String strCategories, String strNote , String strDate
            ,float flLatitude ,float flLongitude){
        ContentValues objContentValues = new ContentValues();
        objContentValues.put(COLUMN_RECEIVE, intReceive);
        objContentValues.put(COLUMN_PAY, intPay);
        objContentValues.put(COLUMN_CATEGORIES, strCategories);
        objContentValues.put(COLUMN_NOTE, strNote);
        objContentValues.put(COLUMN_DATE, strDate);
        objContentValues.put(COLUMN_LATITUDE, flLatitude);
        objContentValues.put(COLUMN_LONGITUDE, flLongitude);
     //   objContentValues.put(KEY_NAME, strName);
    //    objContentValues.put(KEY_IMAGE, blImage);

        return readSqLiteDatabase.insert(MAIN_TABLE, null, objContentValues);
    }

    public void deleteforreceipt(String retype){
     //   ContentValues objContentValues = new ContentValues(
        // );
    //    objContentValues.remove(COLUMN_NOTE, strNote);
      // String txt_id = String.valueOf(id);
     //   Log.d("Whooooooooo1 : ",txt_id.toString());
        writeSqLiteDatabase.execSQL("DELETE FROM "+ CATEGORIES_TABLE +" WHERE " + COLUMN_RECEIVECATEGORIES + " = '" + retype +"'");
    }

    public void deleteforpayceipt(String retype){

        writeSqLiteDatabase.execSQL("DELETE FROM "+ CATEGORIES_TABLE +" WHERE " + COLUMN_PAYCATEGORIES + " = '" + retype +"'");
    }

    public void deleteforsetpay(int a){

        writeSqLiteDatabase.execSQL("DELETE FROM "+ ALERT_TABLE);
    }
/////////////////////////////////////////////////////////////////////////////////


    public void addContact(Contact contact) {
        SQLiteDatabase db = objMySQLiteOpenHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, contact._name); // Contact Name
        values.put(KEY_IMAGE, contact._image); // Contact Phone

        // Inserting Row
        db.insert(CONTACTS_TABLE, null, values);
        db.close(); // Closing database connection
    }

    // Getting single contact
    Contact getContact(int id) {
        SQLiteDatabase db = objMySQLiteOpenHelper.getWritableDatabase();

        Cursor cursor = db.query(CONTACTS_TABLE, new String[] { KEY_ID,
                        KEY_NAME, KEY_IMAGE }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Contact contact = new Contact(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getBlob(1));

        // return contact
        return contact;

    }

    // Getting All Contacts
    public List<Contact> getAllContacts() {
        List<Contact> contactList = new ArrayList<Contact>();
        // Select All Query
        String selectQuery = "SELECT  * FROM contactsTABLE ORDER BY name";

        SQLiteDatabase db = objMySQLiteOpenHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Contact contact = new Contact();
                contact.setID(Integer.parseInt(cursor.getString(0)));
                contact.setName(cursor.getString(1));
                contact.setImage(cursor.getBlob(2));
                // Adding contact to list
                contactList.add(contact);
            } while (cursor.moveToNext());
        }
        // close inserting data from database
        db.close();
        // return contact list
        return contactList;

    }

    // Updating single contact
    public int updateContact(Contact contact) {
        SQLiteDatabase db = objMySQLiteOpenHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, contact.getName());
        values.put(KEY_IMAGE, contact.getImage());

        // updating row
        return db.update(CONTACTS_TABLE, values, KEY_ID + " = ?",
                new String[] { String.valueOf(contact.getID()) });

    }

    // Deleting single contact
    public void deleteContact(Contact contact) {
        SQLiteDatabase db = objMySQLiteOpenHelper.getWritableDatabase();
        db.delete(CONTACTS_TABLE, KEY_ID + " = ?",
                new String[] { String.valueOf(contact.getID()) });
        db.close();
    }

    // Getting contacts Count
    public int getContactsCount() {
        String countQuery = "SELECT  * FROM " + CONTACTS_TABLE;
        SQLiteDatabase db = objMySQLiteOpenHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }

    public void addEntry( String name, byte[] image) throws SQLiteException {
        SQLiteDatabase database = objMySQLiteOpenHelper.getWritableDatabase();
        ContentValues cv = new  ContentValues();
        cv.put(KEY_NAME,    name);
        cv.put(KEY_IMAGE,   image);
        database.insert( CONTACTS_TABLE, null, cv );
    }
}
