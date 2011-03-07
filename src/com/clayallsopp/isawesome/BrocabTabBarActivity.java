package com.clayallsopp.isawesome;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

public class BrocabTabBarActivity extends TabActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab);

        TabHost tabHost = (TabHost)findViewById(android.R.id.tabhost);
        
        TabSpec firstTabSpec = tabHost.newTabSpec("tid1");
        firstTabSpec.setIndicator("Brocabs");
        firstTabSpec.setContent(new Intent(this,OfflineListActivity.class));

        TabSpec secondTabSpec = tabHost.newTabSpec("tid2");
        secondTabSpec.setIndicator("Online");
        secondTabSpec.setContent(new Intent(this,OnlineListActivity.class));

        TabSpec thirdTabSpec = tabHost.newTabSpec("tid3");
        thirdTabSpec.setIndicator("Favorites");
        thirdTabSpec.setContent(new Intent(this,FavoritesListActivity.class));
        
        tabHost.addTab(firstTabSpec);
        tabHost.addTab(secondTabSpec);
        tabHost.addTab(thirdTabSpec);
    }
}