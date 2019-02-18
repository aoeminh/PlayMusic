package com.example.apple.playmusic.activity;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.apple.playmusic.R;
import com.example.apple.playmusic.adapter.MainViewPagerAdapter;
import com.example.apple.playmusic.fragment.HomeFragment;
import com.example.apple.playmusic.fragment.SearchFragment;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findView();
        initView();
    }

    void findView() {
        tabLayout = findViewById(R.id.tl_main);
        viewPager = findViewById(R.id.vp_main);

    }

    void initView() {
        MainViewPagerAdapter adapter = new MainViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new HomeFragment(),getString(R.string.title_home_fragment));
        adapter.addFragment(new SearchFragment(),getString(R.string.title_search_fragment));
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        Objects.requireNonNull(tabLayout.getTabAt(0)).setIcon(R.drawable.icontrangchu);
        Objects.requireNonNull(tabLayout.getTabAt(1)).setIcon(R.drawable.iconsearch);

    }




}
