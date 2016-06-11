package com.example.i3enz.moneymanagementv1;


import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;

public class CategoriesActivity extends TabActivity {
        private static final String RECEIVE_TAB = "Receive Type";
        private static final String PAY_TAB = "Pay Type";

        @Override
        public void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_categories);
                TabHost tabHost = getTabHost();
               // TabHost tabHost = (TabHost)findViewById(R.id.tabHost);

                // Inbox Tab
                TabHost.TabSpec ReceiveSpec = tabHost.newTabSpec(RECEIVE_TAB);
                // Tab Icon
                ReceiveSpec.setIndicator(RECEIVE_TAB, getResources().getDrawable(R.drawable.ic_menu_report));
                Intent receiveIntent = new Intent(this, TabReceiveActivity.class);
                // Tab Content
                ReceiveSpec.setContent(receiveIntent);

                // Outbox Tab
                TabHost.TabSpec PaySpec = tabHost.newTabSpec(PAY_TAB);
                PaySpec.setIndicator(PAY_TAB, getResources().getDrawable(R.drawable.ic_menu_report));
                Intent payIntent = new Intent(this, TabPayActivity.class);
                PaySpec.setContent(payIntent);


                // Adding all TabSpec to TabHost
                tabHost.addTab(ReceiveSpec); // Adding Inbox tab
                tabHost.addTab(PaySpec); // Adding Outbox tab
        }
        }
