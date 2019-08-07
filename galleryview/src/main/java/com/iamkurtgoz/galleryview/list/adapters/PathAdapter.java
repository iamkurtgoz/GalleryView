package com.iamkurtgoz.galleryview.list.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.iamkurtgoz.galleryview.R;
import com.iamkurtgoz.galleryview.list.holders.PathHolder;
import com.iamkurtgoz.galleryview.list.models.PathModel;

import java.util.ArrayList;
import java.util.Collections;

public class PathAdapter extends RecyclerView.Adapter<PathHolder> {

    private Context context;
    private RecyclerView recyclerView;
    private ArrayList<PathModel> arrayList;
    private PathClickListener pathClickListener;

    public PathAdapter(Context context, RecyclerView recyclerView, ArrayList<PathModel> arrayList, PathClickListener pathClickListener){
        this.context = context;
        this.recyclerView = recyclerView;
        this.arrayList = arrayList;
        this.pathClickListener = pathClickListener;
        Collections.sort(arrayList);
        if (pathClickListener != null || arrayList.size()>0){
            arrayList.get(0).setSelect(true);
            pathClickListener.onClickPath(arrayList.get(0));
        }
    }

    public interface PathClickListener{
        void onClickPath(PathModel model);
    }

    @NonNull
    @Override
    public PathHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PathHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_path_row, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final PathHolder holder, final int position) {
        final PathModel model = (PathModel) arrayList.get(position);
        holder.btnPathName.setText(model.getPathTitle());
        Drawable btnBackground = context.getResources().getDrawable(R.drawable.custom_button_transparent);
        int btnTextColor = ContextCompat.getColor(context, R.color.black);

        if (model.isSelect()){
            btnBackground = context.getResources().getDrawable(R.drawable.custom_button_dark);
            btnTextColor = ContextCompat.getColor(context, R.color.white);
        }
        holder.btnPathName.setBackground(btnBackground);
        holder.btnPathName.setTextColor(btnTextColor);

        holder.btnPathName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeAllSelect();
                removeSelectAllButton();
                holder.btnPathName.setBackground(context.getResources().getDrawable(R.drawable.custom_button_dark));
                holder.btnPathName.setTextColor(ContextCompat.getColor(context, R.color.white));
                model.setSelect(true);
                pathClickListener.onClickPath(arrayList.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    private void removeAllSelect(){
        for (PathModel model : arrayList){
            model.setSelect(false);
        }
    }

    public void removeSelectAllButton(){
        for (int position = 0; position < arrayList.size(); position++){
            PathModel model = (PathModel) arrayList.get(position);
            RecyclerView.ViewHolder viewHolder = recyclerView.findViewHolderForAdapterPosition(position);
            if (viewHolder instanceof PathHolder){
                PathHolder holder = (PathHolder) viewHolder;
                Drawable btnBackground = context.getResources().getDrawable(R.drawable.custom_button_transparent);
                int btnTextColor = ContextCompat.getColor(context, R.color.black);
                holder.btnPathName.setBackground(btnBackground);
                holder.btnPathName.setTextColor(btnTextColor);
            }
        }
    }
}
