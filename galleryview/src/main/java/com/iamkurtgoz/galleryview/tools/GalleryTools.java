package com.iamkurtgoz.galleryview.tools;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.webkit.MimeTypeMap;

import androidx.core.content.FileProvider;

import com.iamkurtgoz.galleryview.list.models.GalleryModel;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;

public class GalleryTools extends ContextWrapper {

    private GalleryMediaType galleryMediaType;
    private String[] okFileExtensions = null;
    public static GalleryTools with(Context context){
        return new GalleryTools(context, GalleryMediaType.ALL_MEDIA);
    }

    public static GalleryTools with(Context context, GalleryMediaType galleryMediaType){
        return new GalleryTools(context, galleryMediaType);
    }

    private GalleryTools(Context context, GalleryMediaType galleryMediaType){
        super(context);
        this.galleryMediaType = galleryMediaType;
        createFileExtensions();
    }

    public void createFileExtensions(){
        if (galleryMediaType.getValue() == GalleryMediaType.ALL_MEDIA.getValue()){
            okFileExtensions = new String[] {"all"}; // aslında = */*
        } else if (galleryMediaType.getValue() == GalleryMediaType.IMAGE_VIDEO.getValue()){
            okFileExtensions = new String[] {"image", "video"}; //aslında image/*, video/*
        } else if (galleryMediaType.getValue() == GalleryMediaType.ONLY_IMAGE.getValue()){
            okFileExtensions = new String[] {"image"};
        } else if (galleryMediaType.getValue() == GalleryMediaType.ONLY_VIDEO.getValue()){
            okFileExtensions = new String[] {"video"};
        }
    }

    public String getMimeType(String path){
        String type = null;
        final String extension = MimeTypeMap.getFileExtensionFromUrl(path);
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension.toLowerCase());
        }
        if (type == null) {
            type = "image/*"; // fallback type. You might set it to */*
        }
        return type;
    }

    public String[] getMimeTypeSplit(File file) {
        return getMimeType(file.getAbsolutePath()).split("/");
    }

    private boolean isAcceptFile(File file){
        String mimeTypeMediaType = getMimeTypeSplit(file)[0];
        for (String extension : okFileExtensions) {
            //Log.d("PRETTY_LOGGER", "DOSYA : " + file.getName() + " - UZANTI : " + extension + " - UYUMLULUK : " + extension.equalsIgnoreCase(mimeTypeMediaType));
            if (extension.equalsIgnoreCase("all") || extension.equalsIgnoreCase(mimeTypeMediaType)){
                return true;
            } else {

            }
        }
        return false;
    }

    public ArrayList<GalleryModel> getGallery(String path){
        ArrayList<GalleryModel> arrayList = new ArrayList<>();
        if (path == null) return arrayList;
        File pathFile = new File(path);

        for (File file:pathFile.listFiles()){
            boolean isDirectory = file.isDirectory();
            String size = getFileSize(file.length());
            Date date = new Date(file.lastModified());
            String name = file.getName();
            String absolutePath = file.getAbsolutePath();
            int fileItemCount = 0;
            if (name.startsWith(".")) continue;
            if (name.endsWith(".tmp")) continue;
            if (!isDirectory){
                if (isAcceptFile(file)){
                    arrayList.add(new GalleryModel(isDirectory, size, date, name, absolutePath, fileItemCount, file));
                }
            } else {
                fileItemCount = file.listFiles().length;
                arrayList.add(new GalleryModel(isDirectory, size, date, name, absolutePath, fileItemCount, file));
            }
        }

        return arrayList;
    }


    public static String getFileSize(long size) {
        if (size <= 0)
            return "0";
        final String[] units = new String[] { "B", "KB", "MB", "GB", "TB" };
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }

    public static int dpToPx(Context c, int dp) {
        Resources r = c.getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    public static int pxToDp(int px, Context c) {
        DisplayMetrics displayMetrics = c.getResources().getDisplayMetrics();
        return Math.round(px * (displayMetrics.ydpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    public Uri getUriForNougat(File file){
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(this, getPackageName() + ".provider", file);
        } else {
            uri = Uri.fromFile(file);
        }
        return uri;
    }
}
