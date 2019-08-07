package com.iamkurtgoz.galleryview;

import android.content.Context;
import android.os.Environment;
import android.webkit.MimeTypeMap;

import com.iamkurtgoz.galleryview.list.models.MediaModel;
import com.iamkurtgoz.galleryview.list.models.PathModel;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

public class Tools {

    public String ROOT_DIR = Environment.getExternalStorageDirectory().getPath();

    public ArrayList<PathModel> getPathList(Context context){
        ArrayList<PathModel> arrayList = new ArrayList<>();
        File[] files = new File(ROOT_DIR).listFiles();
        if (files == null) return arrayList;
        if (files.length > 0){
            if (isValueExistsPath(ROOT_DIR)){
                arrayList.add(0, new PathModel(
                        ROOT_DIR,
                        context.getString(R.string.root_dir),
                        false,
                        files.length
                ));
                for (File file : files){
                    if (file.isDirectory()){
                        arrayList.add(new PathModel(
                                file.getPath(),
                                file.getName(),
                                false,
                                (file.listFiles() == null) ? 0:file.listFiles().length
                        ));
                    }
                }
            } else {
                int i = 0;
                for (File file : files){
                    if (file.isDirectory()){
                        arrayList.add(new PathModel(
                                file.getPath(),
                                file.getName(),
                                false,
                                (file.listFiles() == null) ? 0:file.listFiles().length
                        ));
                    }
                    i++;
                }
            }
        }
        return arrayList;
    }

    private boolean isValueExistsPath(String path){
        File[] files = new File(ROOT_DIR).listFiles();
        if (files == null) return false;
        for (File file : files){
            if (!file.isDirectory()){
                return true;
            }
        }
        return false;
    }

    private String[] okFileExtensions = null;
    public ArrayList<MediaModel> getMediaList(String MEDIA_TYPE, String path){
        if (MEDIA_TYPE.equalsIgnoreCase(GalleryViewFragment.ALL_MEDIA)){
            okFileExtensions =  new String[] {"3gp", "mp4", "jpg", "png", "gif","jpeg"};
        } else if (MEDIA_TYPE.equalsIgnoreCase(GalleryViewFragment.ONLY_IMAGE)){
            okFileExtensions =  new String[] {"jpg", "png","jpeg"};
        } else if (MEDIA_TYPE.equalsIgnoreCase(GalleryViewFragment.ONLY_IMAGE_AND_GIF)){
            okFileExtensions =  new String[] {"jpg", "png", "gif","jpeg"};
        } else if (MEDIA_TYPE.equalsIgnoreCase(GalleryViewFragment.ONLY_GIF)){
            okFileExtensions =  new String[] {"gif"};
        } else if (MEDIA_TYPE.equalsIgnoreCase(GalleryViewFragment.ONLY_VIDEO)){
            okFileExtensions =  new String[] {"3gp", "mp4"};
        }
        ArrayList<MediaModel> arrayList = new ArrayList<>();
        File[] files = new File(path).listFiles();
        if (files == null) return arrayList;
        if (files.length > 0){
            for (File file : files){
                if (file.isDirectory()){
                    addMediaForMediaModel(arrayList, file.getAbsolutePath());
                } else {
                    if (isAcceptFile(file)){
                        arrayList.add(new MediaModel(file));
                    }
                }
            }
        }
        return arrayList;
    }

    private void addMediaForMediaModel(ArrayList<MediaModel> arrayList, String path){
        File[] files = new File(path).listFiles();
        if (files == null) return;
        if (files.length > 0){
            for (File file : files){
                if (file.isDirectory()){
                    addMediaForMediaModel(arrayList, file.getAbsolutePath());
                } else {
                    if (isAcceptFile(file)){
                        arrayList.add(new MediaModel(file));
                    }
                }
            }
        }
    }

    private boolean isAcceptFile(File file){
        for (String extension : okFileExtensions) {
            if (file.getName().toLowerCase().endsWith(extension)) {
                return true;
            }
        }
        return false;
    }

    public String[] getMimeType(File file) {
        String type = null;
        final String url = file.toString();
        final String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension.toLowerCase());
        }
        if (type == null) {
            type = "image/*"; // fallback type. You might set it to */*
        }
        return type.split("/");
    }
}
