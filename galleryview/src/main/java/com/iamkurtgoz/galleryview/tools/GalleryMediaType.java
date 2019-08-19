package com.iamkurtgoz.galleryview.tools;

public enum GalleryMediaType {
    ALL_MEDIA(0),
    IMAGE_VIDEO(1),
    ONLY_IMAGE(2),
    ONLY_VIDEO(3);

    private final int value;
    private GalleryMediaType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}

