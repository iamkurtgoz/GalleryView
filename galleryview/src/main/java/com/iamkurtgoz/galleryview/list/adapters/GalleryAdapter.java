package com.iamkurtgoz.galleryview.list.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.iamkurtgoz.galleryview.tools.GalleryTools;
import com.iamkurtgoz.galleryview.R;
import com.iamkurtgoz.galleryview.list.holders.GalleryHolder;
import com.iamkurtgoz.galleryview.list.models.GalleryModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryHolder> {

    private Context context;
    private ArrayList<GalleryModel> arrayList;
    private ClickListener clickListener;

    public GalleryAdapter(Context context, ArrayList<GalleryModel> arrayList, ClickListener clickListener){
        this.context = context;
        this.arrayList = arrayList;
        this.clickListener = clickListener;
    }

    public interface ClickListener{
        void onClickDirecyory(GalleryModel galleryModel);
        void onClickItem(GalleryModel galleryModel);
    }

    @NonNull
    @Override
    public GalleryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new GalleryHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.lib_galleryview_list_item_gallery_row, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull GalleryHolder holder, int position) {
        final GalleryModel model = arrayList.get(position);

        holder.textTitle.setText(model.getName());
        holder.textExt.setText("");
        holder.textDate.setText(new SimpleDateFormat("yyyy-M-dd HH:mm:ss" , Locale.getDefault()).format(model.getDate()));

        if (model.isDirectory()){
            holder.imgHeader.setImageResource(R.drawable.galleryview_ic_folder_white);
            holder.textDetail.setText(model.getFileItemCount() + " " + context.getString(R.string.file));
        } else {
            holder.textExt.setText(getExt(model.getAbsolutePath()));
            holder.textDetail.setText(model.getSize());
            RequestOptions options = new RequestOptions();
            options.centerCrop();
            String fileMediaType = GalleryTools.with(context).getMimeTypeSplit(model.getFile())[0];
            if (fileMediaType.equalsIgnoreCase("image")){
                holder.textExt.setText("");
                Glide.with(context).load(model.getFile()).apply(options).into(holder.imgHeader);
            } else if (fileMediaType.equalsIgnoreCase("video")){
                holder.textExt.setText("");
                options.frame(0);
                Glide.with(context).load(model.getFile()).apply(options).into(holder.imgHeader);
            }
        }

        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (clickListener == null) return;
                if (model.isDirectory()){
                    clickListener.onClickDirecyory(model);
                } else {
                    clickListener.onClickItem(model);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public String getExt(String filePath){
        int strLength = filePath.lastIndexOf(".");
        if(strLength > 0)
            return filePath.substring(strLength + 1).toLowerCase();
        return null;
    }
}
