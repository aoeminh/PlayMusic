package com.example.apple.playmusic.fragment;

import android.content.Intent;
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
import com.example.apple.playmusic.action.IOnItemClick;
import com.example.apple.playmusic.activity.SongListActivity;
import com.example.apple.playmusic.adapter.CategoryThemeAdapter;
import com.example.apple.playmusic.contract.IPresenterCallback;
import com.example.apple.playmusic.contract.IViewCallback;
import com.example.apple.playmusic.model.Advertise;
import com.example.apple.playmusic.model.Album;
import com.example.apple.playmusic.model.Category;
import com.example.apple.playmusic.model.CategoryTheme;
import com.example.apple.playmusic.model.Playlist;
import com.example.apple.playmusic.model.Song;
import com.example.apple.playmusic.model.Theme;
import com.example.apple.playmusic.presenter.HomePresenter;

import java.util.ArrayList;

public class CategoryThemeFragment extends Fragment implements IViewCallback,IOnItemClick
{

    public static final String EXTRA_THEME="theme.extra";
    public static final String ACTION_THEME ="theme.action";
    public static final String EXTRA_CATEGORY ="category.extra";
    private IPresenterCallback presenter;
    private CategoryTheme categoryTheme;
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
        categoryTheme =categoryThemes;
        adapter = new CategoryThemeAdapter(getActivity(),this);
        adapter.setListImageUrl(categoryThemes);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvCategoryTheme.setLayoutManager(layoutManager);
        rvCategoryTheme.setAdapter(adapter);
    }

    @Override
    public void responseAlbumHot(ArrayList<Album> albums) {

    }

    @Override
    public void responseLoveSong(ArrayList<Song> songs) {

    }

    @Override
    public void onClickItem(int position) {
        ArrayList<Theme> themeArrayList  =categoryTheme.getThemeArrayList();
        ArrayList<Category> categoryArrayList = categoryTheme.getCategoryArrayList();
        Intent intent = new Intent(getActivity(),SongListActivity.class);
        if(position< categoryTheme.getThemeArrayList().size()){
            intent.putExtra(EXTRA_THEME,themeArrayList.get(position));
            intent.setAction(ACTION_THEME);
            startActivity(intent);
        }else{
            intent.putExtra(EXTRA_CATEGORY,categoryArrayList.get(position- themeArrayList.size() ));
            intent.setAction(ACTION_THEME);
            startActivity(intent);
        }

    }
}
