package com.iamkurtgoz.galleryview.list.holders;

import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.iamkurtgoz.galleryview.R;

public class MediaHolder extends RecyclerView.ViewHolder {

    public ImageView imageView;
    public ProgressBar progressBar;
    public TextView textMediaType;

    public MediaHolder(@NonNull View itemView) {
        super(itemView);

        imageView = itemView.findViewById(R.id.list_item_media_row_imageView);
        progressBar = itemView.findViewById(R.id.list_item_media_row_progressBar);
        textMediaType = itemView.findViewById(R.id.list_item_media_row_textMediaType);

    }
}
