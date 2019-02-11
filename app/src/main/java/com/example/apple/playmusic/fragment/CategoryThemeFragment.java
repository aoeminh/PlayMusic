package com.example.apple.playmusic.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.apple.playmusic.R;
import com.example.apple.playmusic.adapter.CategoryThemeAdapter;
import com.example.apple.playmusic.contract.IPresenterCallback;
import com.example.apple.playmusic.contract.IViewCallback;
import com.example.apple.playmusic.model.Advertise;
import com.example.apple.playmusic.model.CategoryTheme;
import com.example.apple.playmusic.model.Playlist;
import com.example.apple.playmusic.presenter.HomePresenter;

import java.util.ArrayList;

public class CategoryThemeFragment extends Fragment implements IViewCallback
{
    private IPresenterCallback presenter;
    View view;
    private RecyclerView rvCategoryTheme;
    private TextView tvViewMore;
    private CategoryThemeAdapter  adapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_category_theme,container,false);
        initView(view);
        getData();
        return view;
    }

    private void initView(View view){
        rvCategoryTheme = view.findViewById(R.id.rv_category_theme_day);
        tvViewMore = view.findViewById(R.id.tv_view_more_category_theme_fragment);
        presenter = new HomePresenter(this);
    }

    void getData(){
        presenter.requestCategoryTheme();
    }
    @Override
    public void responseBanner(ArrayList<Advertise> advertises) {

    }

    @Override
    public void responsePlaylist(ArrayList<Playlist> playlists) {

    }

    @Override
    public void responseCategoryTheme(CategoryTheme categoryThemes) {
        adapter = new CategoryThemeAdapter(getActivity());
        adapter.setListImageUrl(categoryThemes);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvCategoryTheme.setLayoutManager(layoutManager);
        rvCategoryTheme.setAdapter(adapter);
    }
}
