package com.iamkurtgoz.galleryview.list.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.File;

public class MediaModel implements Parcelable {

    private String filePath;
    private boolean isCheck = false;

    public MediaModel(File file){
        this.filePath = file.getAbsolutePath();
    }


    protected MediaModel(Parcel in) {
        filePath = in.readString();
        isCheck = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(filePath);
        dest.writeByte((byte) (isCheck ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MediaModel> CREATOR = new Creator<MediaModel>() {
        @Override
        public MediaModel createFromParcel(Parcel in) {
            return new MediaModel(in);
        }

        @Override
        public MediaModel[] newArray(int size) {
            return new MediaModel[size];
        }
    };

    public File getFile() {
        return new File(filePath);
    }

    public void setFile(File file) {
        this.filePath = file.getAbsolutePath();
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    public boolean isCheck() {
        return isCheck;
    }
}
