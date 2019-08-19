package com.iamkurtgoz.galleryview.tools;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;

public class GalleryCheckPermission {

    public static boolean check(Context context, String permission) {
        int res = context.checkCallingOrSelfPermission(permission);
        return res == PackageManager.PERMISSION_GRANTED;
    }

    public static boolean check(Context context, String... permissions) {
        boolean hasAllPermissions = true;
        for(String permission : permissions) {
            if (!check(context, permission)) {hasAllPermissions = false; }
        }
        return hasAllPermissions;
    }

    public static boolean checkStoragePermission(Context context){
        return GalleryCheckPermission.check(context, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }
}