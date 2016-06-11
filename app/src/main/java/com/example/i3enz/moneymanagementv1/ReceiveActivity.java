package com.example.i3enz.moneymanagementv1;


import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class ReceiveActivity extends AppCompatActivity
        implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "LocationDemo";
    private GoogleApiClient mGoogleApiClient;

    private TextView mLatitudeTextView;
    private TextView mLongitudeTextView;

    EditText rnoteEdit, moneyEdit, rcalendar, location1;
    private MainTable objMainTable;
    float latitude2,longitude2;
    String  note2, type2, date2;
    ImageView imgCalendar, imgmap ,imageAdd;
    ListView listview1;
    String date_now;
    Spinner typeSpiner;
    Bitmap yourImage;

    private SimpleCursorAdapter dbAdapter;
    private SQLiteDatabase mDb;
    private MySQLiteOpenHelper mHelper;
    private Cursor mCursor;

    private Calendar myCalendar;
    private DatePickerDialog.OnDateSetListener date;

    ArrayList<Contact> imageArry = new ArrayList<Contact>();
    ContactImageAdapter imageAdapter;
    private static final int CAMERA_REQUEST = 1;
    private static final int PICK_FROM_GALLERY = 2;
    ListView dataList;
    byte[] imageName;
    int imageId;
    Bitmap theImage;

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receive);

        rnoteEdit = (EditText) findViewById(R.id.rNote);
        typeSpiner = (Spinner) findViewById(R.id.rtype);
        moneyEdit = (EditText) findViewById(R.id.rmoney);
        imgCalendar = (ImageView) findViewById(R.id.imgCalendar);
        rcalendar = (EditText) findViewById(R.id.rdate);
        listview1 = (ListView) findViewById(R.id.ReceiveListView);

        imgmap = (ImageView) findViewById(R.id.imgmap);

        imageAdd = (ImageView) findViewById(R.id.imageAdd);

        // อ้างอิงไปยังเท็กซ์วิวทั้งสอง
        mLatitudeTextView = (TextView) findViewById(R.id.latitude);
        mLongitudeTextView = (TextView) findViewById(R.id.longitude);

        // สร้างออบเจ็ค GoogleApiClient
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        Calendar c = Calendar.getInstance();
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        date_now = sdf.format(c.getTime());
        rcalendar.setText(date_now);

        connectedDatabase(); //เรียกใช้ดาต้าเบส

        getlistview(); //เรียกแสดงลิสวิว

        getdataspinner(); //เลือกหมวดหมู่

////////////////////////////////////////////////////////////////////////////////////////////
        /**
         * open dialog for choose camera/gallery
         */
        final String[] option = new String[] { "Take from Camera",
                "Select from Gallery" };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.select_dialog_item, option);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Select Option");
        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                Log.e("Selected Item", String.valueOf(which));
                if (which == 0) {
                    callCamera();
                }
                if (which == 1) {
                    callGallery();
                }
            }
        });
        final AlertDialog dialog = builder.create();

        imageAdd.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog.show();
            }
        });

//////////////////////////////////////////////////////////////////////////////////////////////
    }

    public void  getdataspinner()
    {
        ArrayList<String> spinArray = new ArrayList<String>();
        mHelper = new MySQLiteOpenHelper(this);
        mDb = mHelper.getWritableDatabase();
        Cursor cursor = mDb.rawQuery("SELECT "
                + MainTable.COLUMN_RECEIVECATEGORIES
                + " FROM " + MainTable.CATEGORIES_TABLE + " WHERE " + MainTable.COLUMN_RECEIVECATEGORIES + " IS NOT NULL"  , null);

        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {

            spinArray.add(cursor.getString(cursor.getColumnIndex(MainTable.COLUMN_RECEIVECATEGORIES)));

            cursor.moveToNext();
        }

        ArrayAdapter<String> adaptersp = new ArrayAdapter<String>(ReceiveActivity.this, android.R.layout.simple_list_item_1,spinArray);

       typeSpiner.setAdapter(adaptersp);
    }

    public  void  getlistview()
    {
        ArrayList<String> dirArray = new ArrayList<String>();
        mHelper = new MySQLiteOpenHelper(this);
        mDb = mHelper.getWritableDatabase();
        mCursor = mDb.rawQuery("SELECT "
                + MainTable.COLUMN_RECEIVE
                + ", " + MainTable.COLUMN_CATEGORIES
                + ", " + MainTable.COLUMN_NOTE
                + ", " + MainTable.COLUMN_DATE
                + " FROM " + MainTable.MAIN_TABLE + " WHERE " + MainTable.COLUMN_DATE  + " = " + "'" + date_now + "'" + " AND " + MainTable.COLUMN_PAY + " = 0" , null);

        mCursor.moveToFirst();

        while (!mCursor.isAfterLast()) {

            dirArray.add(mCursor.getString(mCursor.getColumnIndex(MainTable.COLUMN_NOTE)) +"\t\t\t Date : "
                    + mCursor.getString(mCursor.getColumnIndex(MainTable.COLUMN_DATE)) + "\n"
                    + "Categories :  " + mCursor.getString(mCursor.getColumnIndex(MainTable.COLUMN_CATEGORIES))+ "\n"
                    + "รายรับ  :  "
                    + mCursor.getString(mCursor.getColumnIndex(MainTable.COLUMN_RECEIVE)) + "  บาท ");

            mCursor.moveToNext();
        }

        ArrayAdapter<String> adapterDir = new ArrayAdapter<String>(ReceiveActivity.this, android.R.layout.simple_list_item_1,dirArray);
        listview1.setAdapter(adapterDir);
    }
    //////////////////////////////////////
    private void connectedDatabase() {
        objMainTable = new MainTable(this);
    } //สร้างตาราง

    public void OnClickcalendar(View v){

        getcalenda();
        new DatePickerDialog(ReceiveActivity.this, date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    public void getcalenda()
    {
       myCalendar = Calendar.getInstance();

       date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };
    }

    private void updateLabel() {

        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        rcalendar.setText(sdf.format(myCalendar.getTime()));
    }



    public void OnClickreceive(View v){

        String note = rnoteEdit.getText().toString();
        String type = typeSpiner.getSelectedItem().toString();
        String date = rcalendar.getText().toString();
        int money2 = 0;
        money2 = Integer.parseInt(moneyEdit.getText().toString());
        note2 = (rnoteEdit.getText().toString());
        type2 = typeSpiner.getSelectedItem().toString();
        date2 = (rcalendar.getText().toString());
        latitude2 = Float.parseFloat(mLatitudeTextView.getText().toString());
        longitude2 = Float.parseFloat(mLongitudeTextView.getText().toString());

        objMainTable.addNewItem(money2,0,type,note,date,latitude2,longitude2);

        Toast.makeText(getApplicationContext()," was success", Toast.LENGTH_SHORT).show();
        Intent abc = new Intent();
        abc.putExtra("money2",money2);
        abc.putExtra("note2",note2);
        abc.putExtra("type2",type2);
        abc.putExtra("date2",date2);
        setResult(Activity.RESULT_OK,abc);
        ReceiveActivity.this.finish();
    }
//////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK)
            return;

        switch (requestCode) {
            case CAMERA_REQUEST:

                Bundle extras = data.getExtras();

                if (extras != null) {
                    yourImage = extras.getParcelable("data");
                //    Toast.makeText(this, "สวัสดีจ้า", Toast.LENGTH_LONG).show();
                    // convert bitmap to byte
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    yourImage.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byte imageInByte[] = stream.toByteArray();
                    byte image1[] = DbBitmapUtility.getBytes(yourImage);
                  //  Log.e("output before1", imageInByte.toString());
                 //   Log.e("output before2", image1.toString());
                    // Inserting Contacts
                  //  Log.d("Insert: ", "Inserting ..");
                  //  objMainTable.addContact(new Contact("Android", imageInByte));

                 //   objMainTable.addEntry("Android",image1); *********

                 //   Intent i = new Intent(ReceiveActivity.this,
                  //          ReceiveActivity.class);
                 //   startActivity(i);
                    imageAdd.setImageBitmap(yourImage);
                 //   finish();
                }
                break;
            case PICK_FROM_GALLERY:
                Bundle extras2 = data.getExtras();

                if (extras2 != null) {
                    yourImage = extras2.getParcelable("data");
                    // convert bitmap to byte
                  //  Toast.makeText(this, "กากสวัสดีจ้า", Toast.LENGTH_LONG).show();
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    yourImage.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byte image1[] = DbBitmapUtility.getBytes(yourImage);
                  //  objMainTable.addEntry("Android",image1);

                    imageAdd.setImageBitmap(yourImage);
                }
                break;
        }
    }
    /**
     * open camera method
     */
    public void callCamera() {
        Intent cameraIntent = new Intent(
                android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra("crop", "true");
        cameraIntent.putExtra("aspectX", 0);
        cameraIntent.putExtra("aspectY", 0);
        cameraIntent.putExtra("outputX", 200);
        cameraIntent.putExtra("outputY", 150);
        startActivityForResult(cameraIntent, CAMERA_REQUEST);
    }

    /**
     * open gallery method
     */

    public void callGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 0);
        intent.putExtra("aspectY", 0);
        intent.putExtra("outputX", 200);
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        startActivityForResult(
                Intent.createChooser(intent, "Complete action using"),
                PICK_FROM_GALLERY);
    }

//////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void onConnected(Bundle bundle) {
        Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (lastLocation != null) {
            mLatitudeTextView.setText(String.valueOf(lastLocation.getLatitude()));
            mLongitudeTextView.setText(String.valueOf(lastLocation.getLongitude()));
         //   Toast.makeText(this, "สวัสดีจ้า", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "ไม่สามารถระบุตำแหน่งได้", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Connection suspended");
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i(TAG, "Connection failed: error code = " + connectionResult.getErrorCode());
    }
    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
        super.onStop();
    }

}





