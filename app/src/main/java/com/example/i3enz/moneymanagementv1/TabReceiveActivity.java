package com.example.i3enz.moneymanagementv1;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import java.util.ArrayList;


public class TabReceiveActivity extends Activity {

    private MainTable objMainTable;
    private SimpleCursorAdapter dbAdapter;
    private SQLiteDatabase mDb;
    private MySQLiteOpenHelper mHelper;
    private Cursor mCursor;
    ArrayList<String> TypeArray1 = new ArrayList<String>();
    String temptype,getType;
    EditText TypeReceiveText;
    ArrayAdapter<String> adapterDir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabreceive);

        objMainTable = new MainTable(this);


        final ListView listView1 = (ListView) findViewById(R.id.ListView1);
        mHelper = new MySQLiteOpenHelper(this);
        mDb = mHelper.getWritableDatabase();
        mCursor = mDb.rawQuery("SELECT "
                + MainTable.COLUMN_RECEIVECATEGORIES
                + " FROM " + MainTable.CATEGORIES_TABLE + " WHERE " + MainTable.COLUMN_RECEIVECATEGORIES + " IS NOT NULL"  , null);

        mCursor.moveToFirst();

        while (!mCursor.isAfterLast()) {

            TypeArray1.add(mCursor.getString(mCursor.getColumnIndex(MainTable.COLUMN_RECEIVECATEGORIES)));
            mCursor.moveToNext();
        }

        adapterDir = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,TypeArray1);
        listView1.setAdapter(adapterDir);
/*
        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String  retype = TypeArray1.get(position);

                objMainTable.deleteforreceipt(retype);
                TypeArray1.remove(position);
                adapterDir.notifyDataSetChanged();
            }
        });
        */
        listView1.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(TabReceiveActivity.this);
                alertDialogBuilder.setTitle("Item was select.");
                // set dialog message
                alertDialogBuilder
                        .setMessage("Do you want to delete")
                        .setCancelable(false)
                        .setPositiveButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        })
                        .setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                String  retype = TypeArray1.get(position);

                                objMainTable.deleteforreceipt(retype);
                                TypeArray1.remove(position);
                                adapterDir.notifyDataSetChanged();
                            }
                        });
                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();
                return false;
            }


        });
    }



    public void clickAddType1(View v){
        final Dialog dialog = new Dialog(TabReceiveActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.activity_customreceive);
        dialog.setCancelable(true);
        TypeReceiveText   = (EditText)dialog.findViewById(R.id.TypeReceiveText);

        Button button1 = (Button)dialog.findViewById(R.id.addTypeReceieve);
        button1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(getApplicationContext()
                        , "success", Toast.LENGTH_SHORT).show();
                getType = (TypeReceiveText.getText().toString());
                objMainTable.addNewCategories(getType,null);
                TypeArray1.add(getType);
                adapterDir.notifyDataSetChanged();
                dialog.cancel();
            }
        });

        Button button2 = (Button)dialog.findViewById(R.id.cancelTypeReceive);
        button2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(getApplicationContext()
                        , "Close dialog", Toast.LENGTH_SHORT).show();
                dialog.cancel();
            }
        });
        dialog.show();
    }
}
