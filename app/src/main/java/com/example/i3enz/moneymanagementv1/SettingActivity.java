package com.example.i3enz.moneymanagementv1;


import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class SettingActivity extends AppCompatActivity {
    EditText setpayEdit;
    TextView limitpay;
    String temp;
    private MainTable objMainTable;
    private SQLiteDatabase mDb;
    private MySQLiteOpenHelper mHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        //limitpay.setText(temp);

        objMainTable = new MainTable(this);
        setpayEdit = (EditText)findViewById(R.id.setmoney);
        limitpay = (TextView)findViewById(R.id.textshow) ;

        mHelper = new MySQLiteOpenHelper(this);
        mDb = mHelper.getWritableDatabase();

    }


    public void onClicksetPay(View v){
        String setPay = setpayEdit.getText().toString();
        int a = Integer.parseInt(setPay);
        objMainTable.deleteforsetpay(a);
        objMainTable.addNewAlert(a);
        Toast.makeText(getApplicationContext(),setPay, Toast.LENGTH_LONG).show();
        limitpay.setText(setPay);
    }
    public void onClickCancelSet(View v){
        this.finish();
    }
}