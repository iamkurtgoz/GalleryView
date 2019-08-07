package com.iamkurtgoz.galleryview.list.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.iamkurtgoz.galleryview.R;
import com.iamkurtgoz.galleryview.Tools;
import com.iamkurtgoz.galleryview.list.holders.MediaHolder;
import com.iamkurtgoz.galleryview.list.models.MediaModel;

import java.io.File;
import java.util.ArrayList;

public class MediaAdapter extends RecyclerView.Adapter<MediaHolder> {

    private Context context;
    private ArrayList<MediaModel> arrayList;
    private MediaClickListener mediaClickListener;

    public MediaAdapter(Context context, ArrayList<MediaModel> arrayList, MediaClickListener mediaClickListener){
        this.context = context;
        this.arrayList = arrayList;
        this.mediaClickListener = mediaClickListener;
    }

    public void setMediaList(ArrayList<MediaModel> arrayList){
        this.arrayList = arrayList;
    }

    public interface MediaClickListener{
        void onMediaClick(MediaModel model);
    }

    @NonNull
    @Override
    public MediaHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MediaHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_media_row, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final MediaHolder holder, int position) {
        final MediaModel model = (MediaModel) arrayList.get(position);
        String mediaDetail = "IMAGE";
        String mediaType = new Tools().getMimeType(model.getFile())[0];
        String mediaExtend = new Tools().getMimeType(model.getFile())[1];
        if (mediaType.equalsIgnoreCase("image")){
            if (mediaExtend.equalsIgnoreCase("gif")){
                mediaDetail = "GIF";
                Glide.with(context)
                        .asGif()
                        .load(model.getFile())
                        .apply(new RequestOptions().centerCrop().skipMemoryCache(true))
                        .listener(new RequestListener<GifDrawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<GifDrawable> target, boolean isFirstResource) {
                                holder.progressBar.setVisibility(View.GONE);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(GifDrawable resource, Object model, Target<GifDrawable> target, DataSource dataSource, boolean isFirstResource) {
                                holder.progressBar.setVisibility(View.GONE);
                                return false;
                            }
                        })
                        .into(holder.imageView);
            } else {
                mediaDetail = "IMAGE";
                Glide.with(context)
                        .load(model.getFile())
                        .apply(new RequestOptions().centerCrop().diskCacheStrategy(DiskCacheStrategy.AUTOMATIC))
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                holder.progressBar.setVisibility(View.GONE);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                holder.progressBar.setVisibility(View.GONE);
                                return false;
                            }
                        })
                        .into(holder.imageView);
            }
        } else if (mediaType.equalsIgnoreCase("video")){
            mediaDetail = "VIDEO";
            long interval = 5000 * 1000;
            RequestOptions options = new RequestOptions().frame(interval).centerCrop().skipMemoryCache(true);
            Glide.with(context)
                    .asBitmap()
                    .load(model.getFile())
                    .apply(options)
                    .listener(new RequestListener<Bitmap>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                            holder.progressBar.setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                            holder.progressBar.setVisibility(View.GONE);
                            return false;
                        }
                    })
                    .into(holder.imageView);
        }
        holder.textMediaType.setText(mediaDetail);
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaClickListener.onMediaClick(model);
                //context.startActivity(new Intent(context, FullScreenMediaView.class));
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    /*
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
    }*/
}
