package com.iamkurtgoz.galleryview;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.iamkurtgoz.galleryview.list.adapters.GalleryAdapter;
import com.iamkurtgoz.galleryview.list.models.GalleryModel;
import com.iamkurtgoz.galleryview.tools.GalleryCheckPermission;
import com.iamkurtgoz.galleryview.tools.GalleryMediaType;
import com.iamkurtgoz.galleryview.tools.GalleryTools;
import com.iamkurtgoz.galleryview.view.SpacingItemDecoration;

import java.util.ArrayList;
import java.util.Collections;

public class GalleryActivity extends AppCompatActivity implements GalleryAdapter.ClickListener {

    public static final Integer GALLERY_RESULT_SUCCESS = 99;
    public static final Integer GALLERY_RESULT_PERMISSION_ERROR = 98;
    public static final Integer GALLERY_RESULT_PERMISSION_CANCEL = 98;
    public static final Integer GALLERY_REQUEST_CODE = 9891;
    public static final String FILE_PATH = "com.iamkurtgoz.galleryview.FILE_PATH";
    private String externalPath = "" , defaultPath = "";

    private GalleryMediaType gallleryMediaType;
    public static void start(Activity activity, GalleryMediaType gallleryMediaType){
        Bundle bundle = new Bundle();
        bundle.putInt("GALLERY_MEDIA_TYPE", gallleryMediaType.getValue());
        Intent intent = new Intent(activity, GalleryActivity.class);
        intent.putExtras(bundle);
        activity.startActivityForResult(intent, GALLERY_REQUEST_CODE);
    }

    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private FrameLayout progressContainer;

    private ArrayList<GalleryModel> arrayList;
    private GalleryAdapter galleryAdapter;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        defaultPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        externalPath = defaultPath;
        setContentView(R.layout.activity_gallery);
        initGalleryMediaType();
        init();
        initToolbar(false, defaultPath, 0);

        initRecyclerView();
        initAdapter();

        new GetMedia(defaultPath).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private void initGalleryMediaType(){
        int galleryMediaType = getIntent().getIntExtra("GALLERY_MEDIA_TYPE", GalleryMediaType.ALL_MEDIA.getValue());
        if (galleryMediaType == GalleryMediaType.ALL_MEDIA.getValue()){
            gallleryMediaType = GalleryMediaType.ALL_MEDIA;
        } else if (galleryMediaType == GalleryMediaType.IMAGE_VIDEO.getValue()){
            gallleryMediaType = GalleryMediaType.IMAGE_VIDEO;
        } else if (galleryMediaType == GalleryMediaType.ONLY_IMAGE.getValue()){
            gallleryMediaType = GalleryMediaType.ONLY_IMAGE;
        } else if (galleryMediaType == GalleryMediaType.ONLY_VIDEO.getValue()){
            gallleryMediaType = GalleryMediaType.ONLY_VIDEO;
        }
    }

    private void init(){
        toolbar = findViewById(R.id.activity_gallery_toolbar);
        recyclerView = findViewById(R.id.activity_gallery_recyclerView);
        progressContainer = findViewById(R.id.activity_gallery_progressContainer);
    }

    private void initToolbar(boolean isBack, String title, int totalItems){
        setSupportActionBar(toolbar);
        if (getSupportActionBar() == null) return;
        getSupportActionBar().setDisplayHomeAsUpEnabled(isBack);
        getSupportActionBar().setDisplayShowHomeEnabled(isBack);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.galleryview_ic_arrow_black);
        getSupportActionBar().setTitle(title);
        getSupportActionBar().setSubtitle(totalItems + " " + getString(R.string.file));
    }

    private void initRecyclerView(){
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new SpacingItemDecoration(3, GalleryTools.dpToPx(getActivity(), 2), true));
    }

    private void initAdapter(){
        arrayList = new ArrayList<>();
        galleryAdapter = new GalleryAdapter(getActivity(), arrayList, this);
        recyclerView.setAdapter(galleryAdapter);
    }

    @Override
    public void onClickDirecyory(GalleryModel galleryModel) {
        if (galleryModel.isDirectory()){
            new GetMedia(galleryModel.getAbsolutePath()).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
    }

    private class GetMedia extends AsyncTask<Boolean, Boolean, ArrayList<GalleryModel>>{

        private Handler handler = new Handler();
        private boolean isBackDirectory = false;
        private String path = defaultPath;
        public GetMedia(String path){
            if (path == null) return;
            this.path = path;
        }

        public GetMedia(boolean isBackDirectory, String path){
            if (path == null) return;
            this.isBackDirectory = isBackDirectory;
            this.path = path;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            handler.postDelayed(onProgressRunneable, 500);
        }

        @Override
        protected ArrayList<GalleryModel> doInBackground(Boolean... booleans) {
            if (GalleryCheckPermission.check(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)){
                return GalleryTools.with(getActivity(), GalleryMediaType.IMAGE_VIDEO).getGallery(path);
            } else {
                return null;
            }
        }

        @Override
        protected void onPostExecute(final ArrayList<GalleryModel> getMediaList) {
            super.onPostExecute(arrayList);
            handler.removeCallbacks(onProgressRunneable);
            recyclerView.setEnabled(true);
            progressContainer.setVisibility(View.GONE);
            if (getMediaList == null){
                setPermissionError();
            } else {
                externalPath = path;
                initToolbar(!externalPath.equalsIgnoreCase(defaultPath), externalPath, getMediaList.size());
                arrayList.clear();
                arrayList.addAll(getMediaList);
                galleryAdapter.notifyDataSetChanged();
            }
        }

        Runnable onProgressRunneable = new Runnable() {
            @Override
            public void run() {
                recyclerView.setEnabled(false);
                progressContainer.setVisibility(View.VISIBLE);
            }
        };
    }

    @Override
    public void onClickItem(GalleryModel galleryModel) {
        setSelected(galleryModel.getAbsolutePath());
    }

    private void backPath(){
        String lastCharCheck = externalPath.substring(externalPath.length() - 1);
        if (String.valueOf(lastCharCheck).equalsIgnoreCase("/")){
            externalPath = externalPath.substring(0, externalPath.length() - 1);
        }
        externalPath = externalPath.substring(0,externalPath.lastIndexOf("/"));
        new GetMedia(true, externalPath).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    public void onBackPressed() {
        if (externalPath.equalsIgnoreCase(defaultPath)){
            setCancel();
        } else {
            backPath();
        }
    }

    //RESULT SEND
    private void setCancel(){
        Intent returnIntent = new Intent();
        setResult(GALLERY_RESULT_PERMISSION_CANCEL, returnIntent);
        finish();
    }

    private void setPermissionError(){
        Intent returnIntent = new Intent();
        setResult(GALLERY_RESULT_PERMISSION_ERROR, returnIntent);
        finish();
    }

    private void setSelected(String path){
        Intent returnIntent = new Intent();
        returnIntent.putExtra(FILE_PATH, path);
        setResult(GALLERY_RESULT_SUCCESS, returnIntent);
        finish();
    }

    private Context getActivity(){
        return GalleryActivity.this;
    }
}

