package com.iamkurtgoz.galleryview.list.holders;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.iamkurtgoz.galleryview.R;

public class GalleryHolder extends RecyclerView.ViewHolder {

    public RelativeLayout container;
    public ImageView imgHeader;
    public TextView textExt, textTitle, textDate, textDetail;

    public GalleryHolder(@NonNull View itemView) {
        super(itemView);

        container = itemView.findViewById(R.id.list_item_gallery_container);
        imgHeader = itemView.findViewById(R.id.list_item_gallery_imgHeader);
        textExt = itemView.findViewById(R.id.list_item_gallery_textExt);
        textTitle = itemView.findViewById(R.id.list_item_gallery_textTitle);
        textDate = itemView.findViewById(R.id.list_item_gallery_textDate);
        textDetail = itemView.findViewById(R.id.list_item_gallery_textDetail);
    }
}
