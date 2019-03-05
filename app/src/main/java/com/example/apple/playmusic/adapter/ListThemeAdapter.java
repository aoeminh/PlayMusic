package com.example.apple.playmusic.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.apple.playmusic.R;
import com.example.apple.playmusic.action.IOnItemClick;
import com.example.apple.playmusic.model.Theme;

import java.util.ArrayList;

public class ListThemeAdapter extends RecyclerView.Adapter<ListThemeAdapter.ViewHolder> {

private Context context;
    private ArrayList<Theme> themeArrayList;
    private IOnItemClick onItemClick;

    public ListThemeAdapter(Context context, ArrayList<Theme> themes, IOnItemClick i){
        this.context = context;
        this.themeArrayList = themes;
        this.onItemClick = i;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).
                inflate(R.layout.row_item_category_and_theme,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Glide.with(context).load(themeArrayList.get(i).getHinhChude()).into(viewHolder.imageView);
    }

    @Override
    public int getItemCount() {
        return themeArrayList.size();
    }

    public class ViewHolder  extends RecyclerView.ViewHolder{

        ImageView imageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.im_category_theme);
            imageView.setOnClickListener(view -> onItemClick.onClickItem(getAdapterPosition()));
        }
    }
}
