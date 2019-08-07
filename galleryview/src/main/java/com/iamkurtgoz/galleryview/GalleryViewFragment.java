package com.iamkurtgoz.galleryview;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.iamkurtgoz.galleryview.list.adapters.MediaAdapter;
import com.iamkurtgoz.galleryview.list.adapters.PathAdapter;
import com.iamkurtgoz.galleryview.list.models.MediaModel;
import com.iamkurtgoz.galleryview.list.models.PathModel;
import com.iamkurtgoz.galleryview.view.AnimationUtils;
import com.iamkurtgoz.galleryview.view.GridSpacingItemDecoration;

import java.util.ArrayList;



public class GalleryViewFragment extends Fragment implements PathAdapter.PathClickListener, MediaAdapter.MediaClickListener {

    private View view;
    private ProgressBar progressBarMedia;
    private LinearLayout layoutSelectMediaContainer;
    private static GalleryViewListener galleryViewListener;

    public static GalleryViewFragment with(GalleryViewListener listener){
        galleryViewListener = listener;
        return new GalleryViewFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_gallery_view, container,false);
        progressBarMedia = view.findViewById(R.id.fragment_gallery_view_progressBarMedia);
        layoutSelectMediaContainer = view.findViewById(R.id.list_item_path_row_layoutSelectMediaContainer);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupDestinationRecyclerView();
    }

    private void setupDestinationRecyclerView(){
        RecyclerView recyclerView = view.findViewById(R.id.fragment_gallery_view_recyclerPath);
        PathAdapter pathAdapter = new PathAdapter(getActivity(), recyclerView, new Tools().getPathList(getActivity()), this);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity() , LinearLayoutManager.HORIZONTAL , false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(pathAdapter);
    }

    @Override
    public void onClickPath(PathModel model) {
        setMediaList(model.getPath());
    }

    private RecyclerView recyclerView;
    private MediaAdapter mediaAdapter;
    private void setUpMediaRecyclerView(){
        recyclerView = view.findViewById(R.id.fragment_gallery_view_recyclerMedia);
        mediaAdapter = new MediaAdapter(getActivity(), new ArrayList<MediaModel>(), this);
        GridSpacingItemDecoration spacingDecoration = new GridSpacingItemDecoration(3, pxToDp(3, getActivity()), true);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(spacingDecoration);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        recyclerView.setAdapter(mediaAdapter);
    }

    private void setMediaList(String path){
        if (mediaAdapter == null){
            setUpMediaRecyclerView();
        }
        class GetMedia extends AsyncTask<ArrayList<MediaModel>, ArrayList<MediaModel>, ArrayList<MediaModel>>{

            private String path;
            public GetMedia(String path){this.path = path;}

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressBarMedia.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            }

            @Override
            protected ArrayList<MediaModel> doInBackground(ArrayList<MediaModel>... arrayLists) {
                return new Tools().getMediaList(path);
            }

            @Override
            protected void onPostExecute(ArrayList<MediaModel> mediaModels) {
                super.onPostExecute(mediaModels);
                progressBarMedia.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                mediaAdapter.setMediaList(mediaModels);
                mediaAdapter.notifyDataSetChanged();
            }
        }
        new GetMedia(path).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private MediaModel oldModel;
    @Override
    public void onMediaClick(MediaModel model) {
        if (galleryViewListener == null) return;
        galleryViewListener.onReadyMedia(model, new Tools().getMimeType(model.getFile())[0], new Tools().getMimeType(model.getFile())[1]);
    }

    public static int pxToDp(int px, Context c) {
        DisplayMetrics displayMetrics = c.getResources().getDisplayMetrics();
        return Math.round(px * (displayMetrics.ydpi / DisplayMetrics.DENSITY_DEFAULT));
    }


}
