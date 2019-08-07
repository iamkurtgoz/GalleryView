package com.example.galleryview;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.iamkurtgoz.galleryview.GalleryViewFragment;
import com.iamkurtgoz.galleryview.GalleryViewListener;
import com.iamkurtgoz.galleryview.list.models.MediaModel;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

public class MainActivity extends AppCompatActivity implements GalleryViewListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Dexter.withActivity(this).withPermission(Manifest.permission.READ_EXTERNAL_STORAGE).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse response) {
                replaceFragment(GalleryViewFragment.with(MainActivity.this));
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse response) {

            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

            }
        }).check();

    }

    private void replaceFragment(@NonNull Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction tr = fm.beginTransaction();
        tr.replace(R.id.activity_main_container, fragment);
        tr.commitAllowingStateLoss();
    }

    @Override
    public void onReadyMedia(MediaModel mediaModel, String mediaType, String mediaTypeDetail) {
        Toast.makeText(this, "Dosya seçildi.\n" + mediaModel.getFile().getAbsolutePath() + "\n" + mediaType + "\n" + mediaTypeDetail, Toast.LENGTH_SHORT).show();
    }
}
