package com.mssinfotech.iampro.co.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.util.Log;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.OkHttpResponseAndJSONObjectRequestListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import okhttp3.Response;
import org.json.JSONObject;

public class ImageGalleryViewModel extends AndroidViewModel {
  private static final String TAG = "ImageGalleryViewModel";

  /* *************************
   * Muteable LiveData
   ************************* */
  private MutableLiveData<List<JSONObject>> mImageGalleryDetails = new MutableLiveData<>();

  public ImageGalleryViewModel(
      @NonNull Application application) {
    super(application);
  }

  public void loadImageGalleryDetails(int uid) {
    AndroidNetworking.get("http://www.iampro.co/api/gallery.php")
        .addQueryParameter("type", "getMyAlbemsListt")
        .addQueryParameter("gallery_type", "image")
        .addQueryParameter("uid", String.valueOf(uid))
        .build()
        .getAsOkHttpResponseAndJSONObject(new OkHttpResponseAndJSONObjectRequestListener() {
          @Override
          public void onResponse(Response okHttpResponse, JSONObject response) {
            List<JSONObject> list = new ArrayList<>();

            for (Iterator<String> rootIterator = response.keys(); rootIterator.hasNext();){
              list.add(response.optJSONObject(rootIterator.next()));
            }

            mImageGalleryDetails.setValue(list);
            Log.d(TAG, "loadImageGalleryDetails loaded successfully");
          }

          @Override
          public void onError(ANError anError) {
            Log.e(TAG, "loadImageGalleryDetails failed", anError);
          }
        });
  }

  public LiveData<List<JSONObject>> getImageGalleryDetails() {
    return mImageGalleryDetails;
  }
}
