package com.example.apple.playmusic.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.apple.playmusic.R;
import com.example.apple.playmusic.action.IOnItemClick;
import com.example.apple.playmusic.model.Category;
import com.example.apple.playmusic.model.CategoryTheme;
import com.example.apple.playmusic.model.Theme;

import java.util.ArrayList;

public class CategoryThemeAdapter extends RecyclerView.Adapter<CategoryThemeAdapter.ViewHolder> {
    public final String TAG ="CategoryThemeAdapter";
    private Context context;
    private CategoryTheme categoryThemes = new CategoryTheme();
    private ArrayList<Theme> themes = new ArrayList<>();
    private ArrayList<Category> categoryArrayList = new ArrayList<>();
    private ArrayList<String> listImageUrl = new ArrayList<>();
    private IOnItemClick iOnItemClick;

    public CategoryThemeAdapter(Context context, IOnItemClick i) {
        this.context = context;
        this.iOnItemClick = i;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_item_category_and_theme,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
       Glide.with(context).load(listImageUrl.get(i)).into(viewHolder.imageView);
    }

    @Override
    public int getItemCount() {
        return themes.size() + categoryArrayList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.im_category_theme);
            itemView.setOnClickListener(view -> {
                iOnItemClick.onClickItem(getAdapterPosition());
            });
        }
    }

    public void setListImageUrl(CategoryTheme categoryThemes){
        this.categoryArrayList = categoryThemes.getCategoryArrayList();
        this.themes = categoryThemes.getThemeArrayList();
        if (listImageUrl.size() >0){
            listImageUrl.clear();
        }
        for (Theme t: themes) {
            listImageUrl.add(t.getHinhChude());
        }
        for (Category c: categoryArrayList
             ) {
            listImageUrl.add(c.getCategoryImage());
        }
        Log.d(TAG,"listImageUrl " + listImageUrl.size());
    }
}
