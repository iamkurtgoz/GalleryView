package com.iamkurtgoz.galleryview.list.models;

import android.os.Parcel;
import android.os.Parcelable;

public class PathModel implements Parcelable, Comparable<PathModel> {

    private String path = "";
    private String pathTitle = "";
    private boolean isSelect = false;
    private int itemCount = 0;

    public PathModel(String path, String pathTitle, Boolean isSelect, int itemCount){
        this.path = path;
        this.pathTitle = pathTitle;
        this.isSelect = isSelect;
        this.itemCount = itemCount;
    }

    protected PathModel(Parcel in) {
        path = in.readString();
        pathTitle = in.readString();
        isSelect = in.readByte() != 0;
        itemCount = in.readInt();
    }

    public static final Creator<PathModel> CREATOR = new Creator<PathModel>() {
        @Override
        public PathModel createFromParcel(Parcel in) {
            return new PathModel(in);
        }

        @Override
        public PathModel[] newArray(int size) {
            return new PathModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(path);
        parcel.writeString(pathTitle);
        parcel.writeByte((byte) (isSelect ? 1 : 0));
        parcel.writeInt(itemCount);
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getPathTitle() {
        return pathTitle;
    }

    public void setPathTitle(String pathTitle) {
        this.pathTitle = pathTitle;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public int getItemCount() {
        return itemCount;
    }

    public void setItemCount(int itemCount) {
        this.itemCount = itemCount;
    }

    @Override
    public int compareTo(PathModel model) {
        return model.itemCount - itemCount;
    }
}
