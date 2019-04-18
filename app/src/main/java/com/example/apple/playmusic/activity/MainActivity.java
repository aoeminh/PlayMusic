package com.example.apple.playmusic.activity;

import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkRequest;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.apple.playmusic.R;
import com.example.apple.playmusic.adapter.MainViewPagerAdapter;
import com.example.apple.playmusic.connection.Connection;
import com.example.apple.playmusic.fragment.HomeFragment;
import com.example.apple.playmusic.fragment.LocalMp3Fragment;
import com.example.apple.playmusic.fragment.SearchFragment;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager viewPager;
    Connection connection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findView();
        initView();
        registerInternet();
    }

    void findView() {
        tabLayout = findViewById(R.id.tl_main);
        viewPager = findViewById(R.id.vp_main);

    }

    void initView() {
        MainViewPagerAdapter adapter = new MainViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new HomeFragment(),getString(R.string.title_home_fragment));
        adapter.addFragment(new SearchFragment(),getString(R.string.title_search_fragment));
        adapter.addFragment(new LocalMp3Fragment(),"Local");
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        Objects.requireNonNull(tabLayout.getTabAt(0)).setIcon(R.drawable.icon_home_selected);
        Objects.requireNonNull(tabLayout.getTabAt(1)).setIcon(R.drawable.icon_search_unselected);
        Objects.requireNonNull(tabLayout.getTabAt(2)).setIcon(R.drawable.icon_local_unselected);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()){
                    case 0:
                        Objects.requireNonNull(tabLayout.getTabAt(0)).setIcon(R.drawable.icon_home_selected);
                        break;
                    case 1:
                        Objects.requireNonNull(tabLayout.getTabAt(1)).setIcon(R.drawable.icon_search_selected);
                        break;
                    case 2:
                        Objects.requireNonNull(tabLayout.getTabAt(2)).setIcon(R.drawable.icon_local);
                        break;

                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                switch (tab.getPosition()){
                    case 0:
                        Objects.requireNonNull(tabLayout.getTabAt(0)).setIcon(R.drawable.icon_home_unselected);
                        break;
                    case 1:
                        Objects.requireNonNull(tabLayout.getTabAt(1)).setIcon(R.drawable.icon_search_unselected);
                        break;
                    case 2:
                        Objects.requireNonNull(tabLayout.getTabAt(2)).setIcon(R.drawable.icon_local_unselected);


                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    void registerInternet(){
       connection = new Connection();
       IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
       registerReceiver(connection,intentFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(connection);
    }
}
