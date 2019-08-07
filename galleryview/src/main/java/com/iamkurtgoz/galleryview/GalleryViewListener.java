package com.iamkurtgoz.galleryview;

import com.iamkurtgoz.galleryview.list.models.MediaModel;

public interface GalleryViewListener {
    void onReadyMedia(MediaModel mediaModel, String mediaType, String mediaTypeDetail);
}
