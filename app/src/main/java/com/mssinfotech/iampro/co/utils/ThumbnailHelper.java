package com.mssinfotech.iampro.co.utils;

import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.concurrent.Callable;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ThumbnailHelper {
    private static ThumbnailHelper instance = null;

    MediaMetadataRetriever metadataRetriever = new MediaMetadataRetriever();

    private ThumbnailHelper() {  }

    public static synchronized ThumbnailHelper getInstance() {
        if (instance == null) {
            instance = new ThumbnailHelper();
        }
        return instance;
    }

    public synchronized Single<Bitmap> getThumbnailsFromUrl(String url, long time) {
        return Single.fromCallable(() -> {
            MediaMetadataRetriever metadataRetriever = new MediaMetadataRetriever();
            Map <String, String> headerMap = new HashMap<>();
            metadataRetriever.setDataSource(url, headerMap);
            Bitmap bitmap = metadataRetriever.getFrameAtTime(time, MediaMetadataRetriever.OPTION_CLOSEST_SYNC);
            return Bitmap.createScaledBitmap(bitmap, 300, 200, false);
        }).subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread());
    }

}
