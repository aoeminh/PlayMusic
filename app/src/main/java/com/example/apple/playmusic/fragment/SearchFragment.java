package com.example.apple.playmusic.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.apple.playmusic.R;
import com.example.apple.playmusic.action.IOnItemClick;
import com.example.apple.playmusic.activity.PlayMusicActivity;
import com.example.apple.playmusic.contract.ISearchPresenter;
import com.example.apple.playmusic.contract.ISearchView;
import com.example.apple.playmusic.model.Song;
import com.example.apple.playmusic.presenter.SearchPresenter;

import java.util.ArrayList;

public class SearchFragment extends Fragment implements ISearchView, IOnItemClick,SearchView.OnQueryTextListener, View.OnClickListener {
    View view;
    SearchView searchView;
    ISearchPresenter presenter;
    SearchAdapter adapter;
    RecyclerView rvSearch;
    ArrayList<Song> mSongs;
    TextView tvNotResult;
    ProgressBar mProgressBar;
    ConstraintLayout mLayout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new SearchPresenter(this);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.search_fragment,container,false);
        initView(view);
        setAction();
        return view;
    }

    private void initView(View view){
        searchView=  view.findViewById(R.id.simpleSearchView);
        rvSearch = view.findViewById(R.id.rv_search_fragment);
        tvNotResult= view.findViewById(R.id.tv_not_result);
        mProgressBar = view.findViewById(R.id.progress_bar);
        mLayout = view.findViewById(R.id.layout_search_fragment);
        DividerItemDecoration itemDecorator = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        itemDecorator.setDrawable(ContextCompat.getDrawable(getContext(), R.drawable.divider));

        rvSearch.addItemDecoration(itemDecorator);

    }

    private void setAction(){
        searchView.setOnQueryTextListener(this);
        rvSearch.setOnClickListener(this);
        mLayout.setOnClickListener(this);
        tvNotResult.setOnClickListener(this);
    }

    @Override
    public void onResponseSearch(ArrayList<Song> songs) {
        hideProgressBar();
        if(songs.size()>0){
            mSongs = songs;
            adapter = new SearchAdapter(getActivity(),songs,this::onClickItem);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            rvSearch.setAdapter(adapter);
            rvSearch.setLayoutManager(layoutManager);
            tvNotResult.setVisibility(View.GONE);
        }else {
            tvNotResult.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onClickItem(int position) {
        hideKeyboard(getActivity());
        ArrayList<Song> lovesongs = new ArrayList<>();
        lovesongs.add(mSongs.get(position));
        Intent intent = new Intent(getActivity(), PlayMusicActivity.class);
        intent.putParcelableArrayListExtra("song",lovesongs);
        startActivity(intent);
    }


    private   void hideKeyboard(Activity activity) {
        if (activity != null && activity.getWindow() != null && activity.getWindow().getDecorView() != null) {
            InputMethodManager imm = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(activity.getWindow().getDecorView().getWindowToken(), 0);
        }
    }

    private void showProgressBar(){
        if(mProgressBar !=null){
            mLayout.setBackgroundColor(getResources().getColor(R.color.color_back_ground_for_waitting_search));
            mProgressBar.setVisibility(View.VISIBLE);
        }
    }

    private void hideProgressBar(){
        if(mProgressBar !=null){
            mLayout.setBackgroundColor(getResources().getColor(R.color.color_back_ground_for_search_done));
            mProgressBar.setVisibility(View.GONE);
        }
    }

    private void cleardata(){
        if(mSongs !=null) mSongs.clear();

    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        cleardata();
        tvNotResult.setVisibility(View.GONE);
        presenter.getSongFromSearch(query);
        showProgressBar();

        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.rv_search_fragment|| v.getId() == R.id.layout_search_fragment || v.getId() == R.id.tv_not_result){
            hideKeyboard(getActivity());
        }
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(!getUserVisibleHint()){
            hideProgressBar();
            hideKeyboard(getActivity());
        }
    }
}
