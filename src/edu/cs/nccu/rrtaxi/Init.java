package edu.cs.nccu.rrtaxi;

//import android.app.Activity;
import android.os.Bundle;

import edu.cs.nccu.rrtaxi.tabActivity.map;
import edu.cs.nccu.rrtaxi.tabActivity.record;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.widget.TabHost;

public class Init extends TabActivity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        Resources res = getResources();
        TabHost tabHost = getTabHost();
        TabHost.TabSpec spec;
        Intent intent;
        
        intent = new Intent().setClass(this, map.class);
        spec = tabHost.newTabSpec("map").setIndicator("map", res.getDrawable(R.drawable.tab_icon1)).setContent(intent);
        
        tabHost.addTab(spec);
        
        intent = new Intent().setClass(this, record.class);
        spec = tabHost.newTabSpec("record").setIndicator("record", res.getDrawable(R.drawable.tab_icon2)).setContent(intent);
        
        tabHost.addTab(spec);
        
        tabHost.setCurrentTab(1);
        
        
    }

}