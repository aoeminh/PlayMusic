package com.example.apple.playmusic.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.SearchView;

import com.example.apple.playmusic.R;
import com.example.apple.playmusic.contract.ISearchPresenter;
import com.example.apple.playmusic.contract.ISearchView;
import com.example.apple.playmusic.model.Song;
import com.example.apple.playmusic.presenter.SearchPresenter;

import java.util.ArrayList;

public class SearchFragment extends Fragment implements View.OnTouchListener, ISearchView {
    View view;
    SearchView searchView;
    ISearchPresenter presenter;
    SearchAdapter adapter;
    RecyclerView rvSearch;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new SearchPresenter(this);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.search_fragment,container,false);
         searchView=  view.findViewById(R.id.simpleSearchView);
         rvSearch = view.findViewById(R.id.rv_search_fragment);
         searchView.setOnTouchListener(this);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {

                presenter.getSongFromSearch(s);
                return false;
            }
        });

        return view;
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if(view.getId() == R.id.simpleSearchView){
            showKeyboard();
        }else {
            InputMethodManager imm = (InputMethodManager) getActivity()
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(searchView.getWindowToken(), 0);
        }
        return false;
    }

    private void showKeyboard(){
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

    }

    @Override
    public void onResponseSearch(ArrayList<Song> songs) {
        adapter = new SearchAdapter(getActivity(),songs);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvSearch.setAdapter(adapter);
        rvSearch.setLayoutManager(layoutManager);

    }
}
