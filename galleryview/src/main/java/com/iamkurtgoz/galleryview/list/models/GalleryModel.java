package com.iamkurtgoz.galleryview.list.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.File;
import java.util.Date;

public class GalleryModel implements Parcelable{

    boolean isDirectory;
    String size;
    Date date;
    String name;
    String absolutePath;
    int fileItemCount;
    File file;

    public GalleryModel(boolean isDirectory, String size, Date date, String name, String absolutePath, int fileItemCount, File file){
        this.isDirectory = isDirectory;
        this.size = size;
        this.date = date;
        this.name = name;
        this.absolutePath = absolutePath;
        this.fileItemCount = fileItemCount;
        this.file = file;
    }

    protected GalleryModel(Parcel in) {
        isDirectory = in.readByte() != 0;
        size = in.readString();
        name = in.readString();
        absolutePath = in.readString();
        fileItemCount = in.readInt();
    }

    public static final Creator<GalleryModel> CREATOR = new Creator<GalleryModel>() {
        @Override
        public GalleryModel createFromParcel(Parcel in) {
            return new GalleryModel(in);
        }

        @Override
        public GalleryModel[] newArray(int size) {
            return new GalleryModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeByte((byte) (isDirectory ? 1 : 0));
        parcel.writeString(size);
        parcel.writeString(name);
        parcel.writeString(absolutePath);
        parcel.writeInt(fileItemCount);
    }

    public boolean isDirectory() {
        return isDirectory;
    }

    public void setDirectory(boolean directory) {
        isDirectory = directory;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAbsolutePath() {
        return absolutePath;
    }

    public void setAbsolutePath(String absolutePath) {
        this.absolutePath = absolutePath;
    }

    public int getFileItemCount() {
        return fileItemCount;
    }

    public void setFileItemCount(int fileItemCount) {
        this.fileItemCount = fileItemCount;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public static Creator<GalleryModel> getCREATOR() {
        return CREATOR;
    }
}
