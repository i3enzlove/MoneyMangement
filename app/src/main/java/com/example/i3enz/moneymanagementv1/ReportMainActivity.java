package com.example.i3enz.moneymanagementv1;


import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TabHost;

import java.util.ArrayList;

public class ReportMainActivity extends TabActivity {
    private static final String REPORT_TAB = "Report";
    private static final String REPORTPIE_TAB = "Reportpie";
    private static final String REPORTLINE_TAB = "Reportline";

    String tp,tr,tb;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_main);

        ArrayList<String> myArray = getIntent().getExtras().getStringArrayList("sumArray");
        String tr1 = getIntent().getExtras().getString("tr");
        String tp2 = getIntent().getExtras().getString("tp");
        String tb3 = getIntent().getExtras().getString("tb");

        tr = tr1.toString();
        tp = tp2.toString();
        tb = tb3.toString();

        Log.d("traw",tr.toString());
        Log.d("tpaw",tp.toString());
        Log.d("tbaw",tb.toString());


        TabHost tabHost = getTabHost();
        // TabHost tabHost = (TabHost)findViewById(R.id.tabHost);

        // Inbox Tab
        TabHost.TabSpec ReportSpec = tabHost.newTabSpec(REPORT_TAB);
        // Tab Icon
        ReportSpec.setIndicator(REPORT_TAB, getResources().getDrawable(R.drawable.ic_menu_report));
        Intent reportIntent = new Intent(this, ReportActivity.class);
        reportIntent.putExtra("sumArray1",myArray);
        reportIntent.putExtra("tr1",tr);
        reportIntent.putExtra("tp1",tp);
        reportIntent.putExtra("tb1",tb);
        // Tab Content
        ReportSpec.setContent(reportIntent);

        // Outbox Tab
        TabHost.TabSpec ReportpieSpec = tabHost.newTabSpec(REPORTPIE_TAB);
        ReportpieSpec.setIndicator(REPORTPIE_TAB, getResources().getDrawable(R.drawable.ic_menu_report));
        Intent reportpieIntent = new Intent(this, ReportPieActivity.class);
        ReportpieSpec.setContent(reportpieIntent);

        TabHost.TabSpec ReportlineSpec = tabHost.newTabSpec(REPORTLINE_TAB);
        ReportlineSpec.setIndicator(REPORTLINE_TAB, getResources().getDrawable(R.drawable.ic_menu_report));
        Intent reportlineIntent = new Intent(this, ReportLineActivity.class);
        ReportlineSpec.setContent(reportlineIntent);


        // Adding all TabSpec to TabHost
        tabHost.addTab(ReportSpec);
        tabHost.addTab(ReportpieSpec);
        tabHost.addTab(ReportlineSpec);
    }
}
