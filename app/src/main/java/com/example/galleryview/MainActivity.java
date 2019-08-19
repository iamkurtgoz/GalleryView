package com.example.galleryview;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.iamkurtgoz.galleryview.GalleryActivity;
import com.iamkurtgoz.galleryview.tools.GalleryMediaType;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Dexter.withActivity(this).withPermission(Manifest.permission.READ_EXTERNAL_STORAGE).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse response) {
                GalleryActivity.start(MainActivity.this, GalleryMediaType.IMAGE_VIDEO);
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse response) {

            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

            }
        }).check();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GalleryActivity.GALLERY_REQUEST_CODE) {

            if (resultCode == GalleryActivity.GALLERY_RESULT_PERMISSION_CANCEL){
                Toast.makeText(this, "Cancelled.", Toast.LENGTH_SHORT).show();
            } else if (resultCode == GalleryActivity.GALLERY_RESULT_PERMISSION_ERROR){
                Toast.makeText(this, "Permission Error.", Toast.LENGTH_SHORT).show();
            } else if (resultCode == GalleryActivity.GALLERY_RESULT_SUCCESS){
                String path = data.getStringExtra(GalleryActivity.FILE_PATH);
                Toast.makeText(this, path, Toast.LENGTH_SHORT).show();
            }

        }
    }
}
