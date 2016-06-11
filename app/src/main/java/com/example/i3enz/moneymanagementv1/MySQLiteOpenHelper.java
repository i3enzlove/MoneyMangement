package com.example.i3enz.moneymanagementv1;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MySQLiteOpenHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "MoneyManagement.db";
    private static final int DATABASE_VERSION = 1;
    private static final String CREATE_MAIN_TABLE ="create table mainTABLE (_id integer primary key ," +
            " receive integer ,pay integer ,categories text ,note text ,date text ,latitude float ,longitude float);";

    private static final String CREATE_CATEGORIES_TABLE ="create table categoriesTABLE (_id integer primary key ," +
            " receivetype text ,paytype text);";

    private static final String CREAT_ALERT_TABLE ="create table alertTABLE (setpay integer);";

    private static final String CREAT_CONTACTS_TABLE ="create table contactsTABLE (id integer primary key ," +
            " name text ,image blob);";

    public MySQLiteOpenHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_MAIN_TABLE);
        db.execSQL(CREATE_CATEGORIES_TABLE);
        db.execSQL(CREAT_ALERT_TABLE);
        db.execSQL(CREAT_CONTACTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + CREAT_CONTACTS_TABLE);

        // Create tables again
        onCreate(db);
    }
}
