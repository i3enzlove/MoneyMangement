package com.example.i3enz.moneymanagementv1;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
   implements NavigationView.OnNavigationItemSelectedListener {

    ////////////////////////////////////////

    private MainTable objMainTable;
    private SimpleCursorAdapter dbAdapter;
    private SQLiteDatabase mDb;
    private MySQLiteOpenHelper mHelper;
    private Cursor mCursor;

    int tempPay =0,tempReceive = 0,sumPay = 0,sumReceive,sumBalance;
    String tp,tr,tb,aaa;
    int money2,money3;
    String note2,type2,date2,note3,type3,date3;
    TextView textreceive,textpay,textbalance;
    ArrayList<String> dirArray = new ArrayList<String>();

    //////////////////////////////////////
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ////////////เชื่อมดาต้าเบส///////
        connectedDatabase();

//        money2 = getIntent().getExtras().getInt("money2");


        /////////////////////////////

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

////////////////////////////////โชวว์ข้อมูลนlistview/////////////////////////////////////////////


        ListView listView1 = (ListView) findViewById(R.id.ListView1);
        mHelper = new MySQLiteOpenHelper(this);
        mDb = mHelper.getWritableDatabase();
        mCursor = mDb.rawQuery("SELECT "
                + MainTable.COLUMN_RECEIVE
                + ", " + MainTable.COLUMN_PAY
                + ", " + MainTable.COLUMN_CATEGORIES
                + ", " + MainTable.COLUMN_NOTE
                + ", " + MainTable.COLUMN_DATE
                + " FROM " + MainTable.MAIN_TABLE, null);


        mCursor.moveToFirst();

        while (!mCursor.isAfterLast()) {

            dirArray.add(mCursor.getString(mCursor.getColumnIndex(MainTable.COLUMN_NOTE)) +"\t\t\t Date : "
                    + mCursor.getString(mCursor.getColumnIndex(MainTable.COLUMN_DATE)) + "\n"
                    + "Categories :  " + mCursor.getString(mCursor.getColumnIndex(MainTable.COLUMN_CATEGORIES))+ "\n"
                    + "รายรับ  :  "
                    + mCursor.getString(mCursor.getColumnIndex(MainTable.COLUMN_RECEIVE)) + "  บาท " + "\t\t"
                    + "รายจ่าย  :  " +mCursor.getString(mCursor.getColumnIndex(MainTable.COLUMN_PAY))+ "\t" + "บาท ");

            tempPay = Integer.parseInt(mCursor.getString(mCursor.getColumnIndex(MainTable.COLUMN_PAY)));
            sumPay = sumPay + tempPay;

            tempReceive = Integer.parseInt(mCursor.getString(mCursor.getColumnIndex(MainTable.COLUMN_RECEIVE)));
            sumReceive = sumReceive + tempReceive;
            tp = String.valueOf(sumPay);
         //   Log.d("มีอยู่ไหม",tp.toString() );
            mCursor.moveToNext();
        }
  //      for (int i=0; i< dirArray.size(); i++)
  //          Log.v("MyTag", Integer.toString(i) + ":" + dirArray.get(i));


        sumBalance = sumReceive - sumPay;
        textreceive = (TextView)findViewById(R.id.textReceive);
        textpay = (TextView)findViewById(R.id.textPay);
        textbalance = (TextView)findViewById(R.id.textBalance);
        tr = String.valueOf(sumReceive);
        tp = String.valueOf(sumPay);
        tb = String.valueOf(sumBalance);
        textreceive.setText(tr);
        textpay.setText(tp);
        textbalance.setText(tb);


        //  Toast.makeText(getApplicationContext(),g, Toast.LENGTH_LONG).show();


        ///////////////////////////////////////////////////////////////////////////////////

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == 1){

            if(resultCode == Activity.RESULT_OK){

                money2 = data.getExtras().getInt("money2");
                note2 = data.getStringExtra("note2");
                type2 = data.getStringExtra("type2");
                date2 = data.getStringExtra("date2");


               // aaa = String.valueOf(money2);
                sumReceive = sumReceive + money2;
                sumBalance = sumReceive - sumPay;

                tr = String.valueOf(sumReceive);
                tb = String.valueOf(sumBalance);

                textreceive.setText(tr);
                textbalance.setText(tb);

                dirArray.add(note2 +"\t\t\t Date : "
                        + date2 + "\n"
                        + "Categories : "  + type2 + "\n"
                        + "รายรับ  :  "
                        + money2 + "  บาท " + "\t\t"
                        + "รายจ่าย  :  " + "0" + "\t" + "บาท ");
            }
        }
        if(requestCode == 2){

            if(resultCode == Activity.RESULT_OK){

                money3 = data.getExtras().getInt("money3");
                note3 = data.getStringExtra("note3");
                type3 = data.getStringExtra("type3");
                date3 = data.getStringExtra("date3");
                sumPay = sumPay + money3;
                sumBalance = sumReceive - sumPay;
                tp = String.valueOf(sumPay);
                tb = String.valueOf(sumBalance);

                textpay.setText(tp);
                textbalance.setText(tb);

                dirArray.add(note3 +"\t\t\t Date : "
                        + date3 + "\n"
                        + "Categories : "  + type3 + "\n"
                        + "รายรับ  :  "
                        + "0" + "  บาท " + "\t\t"
                        + "รายจ่าย  :  " + money3 + "\t" + "บาท ");
            }
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Toast.makeText(getApplicationContext()
                , "Restart", Toast.LENGTH_SHORT);
    }


    //////////////////////////////////////
    private void connectedDatabase() {
        objMainTable = new MainTable(this);
    } //สร้างตาราง
    //////////////////////////////////////

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_receive) {

                Intent intent = new Intent(MainActivity.this,ReceiveActivity.class);
            startActivityForResult(intent,1);
           //     startActivity(intent);
          //  this.finish();
        } else if (id == R.id.nav_pay) {
            Intent intent = new Intent(MainActivity.this,PayActivity.class);
            startActivityForResult(intent,2);
        } else if (id == R.id.nav_group) {
            Intent intent = new Intent(MainActivity.this,CategoriesActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_report) {
            Intent intent = new Intent(MainActivity.this,ReportMainActivity.class);
            intent.putExtra("sumArray",dirArray);
            intent.putExtra("tr",tr);
            intent.putExtra("tp",tp);
            intent.putExtra("tb",tb);
       //     Log.d("Whooooooooo",tr.toString() );
       //    Log.d("Areeeeeeeee",tp.toString() );
       //     Log.d("Youuuuuuuuu",tb.toString() );
            startActivity(intent);
        } else if (id == R.id.nav_setting) {
            Intent intent = new Intent(MainActivity.this,SettingActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_backup) {
            Intent intent = new Intent(MainActivity.this,BackupActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_exit) {
            Intent intent = new Intent(MainActivity.this,Screenlist.class);
            startActivity(intent);
          //  android.os.Process.killProcess(android.os.Process.myPid());
          //  System.exit(1);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void onPause() {
        super.onPause();
        mHelper.close();
        mDb.close();
    }



}
