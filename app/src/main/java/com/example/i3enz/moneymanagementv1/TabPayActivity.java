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


public class TabPayActivity extends Activity{

    private MainTable objMainTable;
    private SimpleCursorAdapter dbAdapter;
    private SQLiteDatabase mDb;
    private MySQLiteOpenHelper mHelper;
    private Cursor mCursor;
    ArrayList<String> TypeArray2 = new ArrayList<String>();
    String temptype,getType;
    EditText TypePayText;
    ArrayAdapter<String> adapterDir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabpay);
        objMainTable = new MainTable(this);


        final ListView listView1 = (ListView) findViewById(R.id.ListView1);
        mHelper = new MySQLiteOpenHelper(this);
        mDb = mHelper.getWritableDatabase();
        mCursor = mDb.rawQuery("SELECT "
                + MainTable.COLUMN_PAYCATEGORIES
                + " FROM " + MainTable.CATEGORIES_TABLE + " WHERE " + MainTable.COLUMN_PAYCATEGORIES + " IS NOT NULL"  , null);

        mCursor.moveToFirst();

        while (!mCursor.isAfterLast()) {

            TypeArray2.add(mCursor.getString(mCursor.getColumnIndex(MainTable.COLUMN_PAYCATEGORIES)));
            mCursor.moveToNext();
        }

        adapterDir = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,TypeArray2);
        listView1.setAdapter(adapterDir);

        listView1.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(TabPayActivity.this);
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

                                String  retype = TypeArray2.get(position);
                      //         Log.d("มีอยู่ไหม",retype.toString() );
                                objMainTable.deleteforpayceipt(retype);
                                TypeArray2.remove(position);
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

    public void clickAddType2(View v){
        final Dialog dialog = new Dialog(TabPayActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.activity_custompay);
        dialog.setCancelable(true);
        TypePayText   = (EditText)dialog.findViewById(R.id.TypePayText);

        Button button1 = (Button)dialog.findViewById(R.id.addTypePay);
        button1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(getApplicationContext()
                        , "success", Toast.LENGTH_SHORT).show();
                getType = (TypePayText.getText().toString());
                objMainTable.addNewCategories(null,getType);
                TypeArray2.add(getType);
                adapterDir.notifyDataSetChanged();
                dialog.cancel();
            }
        });

        Button button2 = (Button)dialog.findViewById(R.id.cancelTypePay);
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
