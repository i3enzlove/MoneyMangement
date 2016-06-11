package com.example.i3enz.moneymanagementv1;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.location.Location;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class PayActivity extends AppCompatActivity
        implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {


    EditText rnoteEdit, moneyEdit, rcalendar;
    private MainTable objMainTable;
    String aaa, note2, type2, date2;
    ImageView imgCalendar, imageAdd, map;
    Spinner typeSpiner;
    ListView listview1;
    String date_now;

    private static final String TAG = "LocationDemo";
    private GoogleApiClient mGoogleApiClient;

    private SimpleCursorAdapter dbAdapter;
    private SQLiteDatabase mDb;
    private MySQLiteOpenHelper mHelper;
    private Cursor mCursor;

    private Calendar myCalendar;
    private DatePickerDialog.OnDateSetListener date;

    private TextView mLatitudeTextView;
    private TextView mLongitudeTextView;
    float latitude2, longitude2;

    ArrayList<Contact> imageArry = new ArrayList<Contact>();
    ContactImageAdapter imageAdapter;
    private static final int CAMERA_REQUEST = 1;
    private static final int PICK_FROM_GALLERY = 2;
    ListView dataList;
    byte[] imageName;
    int imageId;
    Bitmap theImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);


        rnoteEdit = (EditText) findViewById(R.id.pnote);
        typeSpiner = (Spinner) findViewById(R.id.ptype);
        moneyEdit = (EditText) findViewById(R.id.pmoney);
        imgCalendar = (ImageView) findViewById(R.id.imgCalendra);
        rcalendar = (EditText) findViewById(R.id.pdate);
        listview1 = (ListView) findViewById(R.id.PayListView);

        imageAdd = (ImageView) findViewById(R.id.imageAdd);

        mLatitudeTextView = (TextView) findViewById(R.id.latitudePay);
        mLongitudeTextView = (TextView) findViewById(R.id.longitudePay);

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

        connectedDatabase();
        getlistview();
        getdataspinner();

////////////////////////////////////////////////////////////////////////////////////////////

        /**
         * open dialog for choose camera/gallery
         */

        final String[] option = new String[]{"Take from Camera",
                "Select from Gallery"};
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

    public void getdataspinner() {
        ArrayList<String> spinArray = new ArrayList<String>();
        mHelper = new MySQLiteOpenHelper(this);
        mDb = mHelper.getWritableDatabase();
        Cursor cursor = mDb.rawQuery("SELECT "
                + MainTable.COLUMN_PAYCATEGORIES
                + " FROM " + MainTable.CATEGORIES_TABLE + " WHERE " + MainTable.COLUMN_PAYCATEGORIES + " IS NOT NULL", null);

        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {

            spinArray.add(cursor.getString(cursor.getColumnIndex(MainTable.COLUMN_PAYCATEGORIES)));
            cursor.moveToNext();
        }

        ArrayAdapter<String> adaptersp = new ArrayAdapter<String>(PayActivity.this, android.R.layout.simple_list_item_1, spinArray);
        typeSpiner.setAdapter(adaptersp);
    }

    public void getlistview() {
        ArrayList<String> dirArray = new ArrayList<String>();
        mHelper = new MySQLiteOpenHelper(this);
        mDb = mHelper.getWritableDatabase();
        mCursor = mDb.rawQuery("SELECT "
                + MainTable.COLUMN_PAY
                + ", " + MainTable.COLUMN_CATEGORIES
                + ", " + MainTable.COLUMN_NOTE
                + ", " + MainTable.COLUMN_DATE
                + " FROM " + MainTable.MAIN_TABLE + " WHERE " + MainTable.COLUMN_DATE + " = " + "'" + date_now + "'" + " AND " + MainTable.COLUMN_RECEIVE + " = 0", null);

        mCursor.moveToFirst();

        while (!mCursor.isAfterLast()) {

            dirArray.add(mCursor.getString(mCursor.getColumnIndex(MainTable.COLUMN_NOTE)) + "\t\t\t Date : "
                    + mCursor.getString(mCursor.getColumnIndex(MainTable.COLUMN_DATE)) + "\n"
                    + "Categories :  " + mCursor.getString(mCursor.getColumnIndex(MainTable.COLUMN_CATEGORIES)) + "\n"
                    + "รายจ่าย  :  "
                    + mCursor.getString(mCursor.getColumnIndex(MainTable.COLUMN_PAY)) + "  บาท ");

            mCursor.moveToNext();
        }

        ArrayAdapter<String> adapterDir = new ArrayAdapter<String>(PayActivity.this, android.R.layout.simple_list_item_1, dirArray);
        listview1.setAdapter(adapterDir);
    }


    //////////////////////////////////////ปฏิฐิน
    private void connectedDatabase() {
        objMainTable = new MainTable(this);
    } //สร้างตาราง

    public void OnClickcalendar(View v) {

        getcalenda();
        new DatePickerDialog(PayActivity.this, date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    public void getcalenda() {
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
/////////////////////////////////////////////////////////////////////////////////

    public void OnClickreceive2(View v) {
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

        objMainTable.addNewItem(0, money2, type, note, date ,latitude2,longitude2 );

        Toast.makeText(getApplicationContext(), " was success", Toast.LENGTH_LONG).show();

        Intent abc = new Intent();
        abc.putExtra("money3", money2);
        abc.putExtra("note3", note2);
        abc.putExtra("type3", type2);
        abc.putExtra("date3", date2);
        setResult(Activity.RESULT_OK, abc);
        PayActivity.this.finish();
    }

//////////////////////////////////////////////////////////////////////////////////////////////
/*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK)
            return;

        switch (requestCode) {
            case CAMERA_REQUEST:

                Bundle extras = data.getExtras();

                if (extras != null) {
                    Bitmap yourImage = extras.getParcelable("data");
                    // convert bitmap to byte
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    yourImage.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byte imageInByte[] = stream.toByteArray();
                    Log.e("output before conversion", imageInByte.toString());
                    // Inserting Contacts
                    Log.d("Insert: ", "Inserting ..");
                    db.addContact(new Contact("Android", imageInByte));
                    Intent i = new Intent(SQLiteDemoActivity.this,
                            SQLiteDemoActivity.class);
                    startActivity(i);
                    finish();

                }
                break;
            case PICK_FROM_GALLERY:
                Bundle extras2 = data.getExtras();

                if (extras2 != null) {
                    Bitmap yourImage = extras2.getParcelable("data");
                    // convert bitmap to byte
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    yourImage.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byte imageInByte[] = stream.toByteArray();
                    Log.e("output before conversion", imageInByte.toString());
                    // Inserting Contacts
                    Log.d("Insert: ", "Inserting ..");
                    db.addContact(new Contact("Android", imageInByte));
                    Intent i = new Intent(SQLiteDemoActivity.this,
                            SQLiteDemoActivity.class);
                    startActivity(i);
                    finish();
                }

                break;
        }
    }
*/

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

