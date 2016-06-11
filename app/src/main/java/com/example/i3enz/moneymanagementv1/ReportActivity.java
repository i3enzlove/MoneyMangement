package com.example.i3enz.moneymanagementv1;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ReportActivity extends Activity {

    String tp,tr,tb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        ArrayList<String> myArray = getIntent().getExtras().getStringArrayList("sumArray1");
        String tr1 = getIntent().getExtras().getString("tr1");
        String tp2 = getIntent().getExtras().getString("tp1");
        String tb3 = getIntent().getExtras().getString("tb1");

        tr = tr1.toString();
        tp = tp2.toString();
        tb = tb3.toString();

        for (int i=0; i< myArray.size();i++)
        {
            Log.d("Whooooooooo1",myArray.get(i) );
        }

        ListView listView1 = (ListView) findViewById(R.id.ListView1);
        ArrayAdapter<String> adapterDir = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,myArray);
        listView1.setAdapter(adapterDir);
    }

    public void clickShowSum(View v){
        final Dialog dialog = new Dialog(ReportActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.activity_customdialog);
        dialog.setCancelable(true);

        Button button1 = (Button)dialog.findViewById(R.id.button1);
        button1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(getApplicationContext()
                        , "Close dialog", Toast.LENGTH_SHORT);
                dialog.cancel();
            }
        });
        TextView textView1 = (TextView)dialog.findViewById(R.id.textView1);
        textView1.setText("รายรับ    :  " + tr + "  บาท" + "\n"
                + "รายจ่าย  :  " + tp + "  บาท" + "\n"
                + "ยอดคงเหลือ  :  " + tb + "  บาท");
        dialog.show();
    }
}

