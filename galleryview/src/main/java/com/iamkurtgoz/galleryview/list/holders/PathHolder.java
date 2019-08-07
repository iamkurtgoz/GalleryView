package com.iamkurtgoz.galleryview.list.holders;

import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.iamkurtgoz.galleryview.R;

public class PathHolder extends RecyclerView.ViewHolder {

    public Button btnPathName;

    public PathHolder(@NonNull View itemView) {
        super(itemView);

        btnPathName = itemView.findViewById(R.id.list_item_path_row_btnPathName);

    }
}
