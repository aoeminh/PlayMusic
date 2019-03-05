package com.example.apple.playmusic.activity;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toolbar;

import com.example.apple.playmusic.R;
import com.example.apple.playmusic.action.IOnItemClick;
import com.example.apple.playmusic.adapter.CategoryThemeAdapter;
import com.example.apple.playmusic.adapter.ListThemeAdapter;
import com.example.apple.playmusic.contract.IListThemePresenter;
import com.example.apple.playmusic.contract.IListThemeView;
import com.example.apple.playmusic.fragment.CategoryThemeFragment;
import com.example.apple.playmusic.model.Theme;
import com.example.apple.playmusic.presenter.ListThemePresenter;

import java.util.ArrayList;

public class ListThemeActivity extends AppCompatActivity implements IOnItemClick,IListThemeView{

    public static final String EXTRA_LIST_THEME = "extra.listheme";
    public static final String ACTION_LISTHEME = "action.listtheme";
    IListThemePresenter presenter;
    private android.support.v7.widget.Toolbar toolbar;
    private RecyclerView rvListheme;
    private ListThemeAdapter adapter;
    private ArrayList<Theme> themeArrayList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_theme);
        rvListheme  = findViewById(R.id.rv_list_theme);
        toolbar = findViewById(R.id.toolbar_list_theme);
        presenter = new ListThemePresenter(this);
        presenter.getAllTheme();
        initToolbar();

    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(view -> finish());
    }

    @Override
    public void onClickItem(int position) {
        Theme theme =themeArrayList.get(position);
        Intent intent = new Intent(this,SongListActivity.class);
        intent.putExtra(CategoryThemeFragment.EXTRA_THEME,theme);
        intent.setAction(ACTION_LISTHEME);
        startActivity(intent);
    }

    @Override
    public void onResponseAllTheme(ArrayList<Theme> themes) {
        themeArrayList = themes;
        adapter = new ListThemeAdapter(this, themeArrayList, this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvListheme.setAdapter(adapter);
        rvListheme.setLayoutManager(layoutManager);
    }
}
