package com.mssinfotech.iampro.co.viewmodels;

import android.annotation.SuppressLint;
import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.util.Log;
import com.mssinfotech.iampro.co.models.ImageDetails;
import com.mssinfotech.iampro.co.models.ProductDetails;
import com.mssinfotech.iampro.co.models.UserDetails;
import com.mssinfotech.iampro.co.utils.ApiEndpoints;
import com.mssinfotech.iampro.co.utils.VolleyUtil;
import com.android.volley.AuthFailureError;
import com.android.volley.Request.Method;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

public class SearchViewModel extends AndroidViewModel {
  private final String TAG = "SearchViewModel";

  /* ****************************
   * LIVE DATA REFERENCES
   **************************** */
  private final MutableLiveData<ArrayList<String>> imageSubCategories = new MutableLiveData<>();
  private final MutableLiveData<ArrayList<String>> videoSubCategories = new MutableLiveData<>();
  private final MutableLiveData<ArrayList<String>> friendSubCategories = new MutableLiveData<>();
  private final MutableLiveData<ArrayList<String>> productSubCategories = new MutableLiveData<>();
  private final MutableLiveData<ArrayList<String>> provideSubCategories = new MutableLiveData<>();
  private final MutableLiveData<ArrayList<String>> demandSubCategories = new MutableLiveData<>();

  /* ****************** Image Sliders Live Data ************************** */
  private final MutableLiveData<ArrayList<String>> imageSlidersList = new MutableLiveData<>();
  private final MutableLiveData<ArrayList<String>> videoSlidersList = new MutableLiveData<>();
  private final MutableLiveData<ArrayList<String>> friendSlidersList = new MutableLiveData<>();
  private final MutableLiveData<ArrayList<String>> productSlidersList = new MutableLiveData<>();
  private final MutableLiveData<ArrayList<String>> provideSlidersList = new MutableLiveData<>();
  private final MutableLiveData<ArrayList<String>> demandSlidersList = new MutableLiveData<>();

  /* ****************** Image Search Live Data ************************** */
  private final MutableLiveData<Map<String, List<ImageDetails>>> imageSearchData = new MutableLiveData<>();
  private final MutableLiveData<Map<String, List<ImageDetails>>> videoSearchData = new MutableLiveData<>();
  private final MutableLiveData<List<UserDetails>> friendSearchData = new MutableLiveData<>();
  private final MutableLiveData<Map<String, List<ProductDetails>>> productSearchData = new MutableLiveData<>();
  private final MutableLiveData<Map<String, List<ProductDetails>>> provideSearchData = new MutableLiveData<>();
  private final MutableLiveData<Map<String, List<ProductDetails>>> demandSearchData = new MutableLiveData<>();

  /* ****************************
   * VOLLEY NETWORK REQUESTS
   **************************** */
  private final JsonObjectRequest imageCategoryListRequest =
      new JsonObjectRequest(
          Method.GET,
          ApiEndpoints.SUB_CAT_IMAGES,
          null,
          response ->
              Single.fromCallable(
                      () -> {
                        ArrayList<String> catList = new ArrayList<>();
                        catList.add("");
                        for (Iterator<String> rootIterator = response.keys();
                            rootIterator.hasNext(); ) {
                          try {
                            final String rootKey = rootIterator.next();
                            final JSONObject rootChildObj = response.getJSONObject(rootKey);
                            String subCat = rootChildObj.getString("name");
                            catList.add(subCat);
                          } catch (JSONException e) {
                            e.printStackTrace();
                          }
                        }

                        return catList;
                      })
                  .subscribeOn(Schedulers.computation())
                  .subscribe(imageSubCategories::postValue),
          error -> Log.e(TAG, "imageCategoryListRequest: OnError", error.getCause()));

  private final JsonObjectRequest videoCategoryListRequest =
      new JsonObjectRequest(
          Method.GET,
          ApiEndpoints.SUB_CAT_VIDEO,
          null,
          response ->
              Single.fromCallable(
                      () -> {
                        ArrayList<String> catList = new ArrayList<>();
                        catList.add("");
                        for (Iterator<String> rootIterator = response.keys();
                            rootIterator.hasNext(); ) {
                          try {
                            final String rootKey = rootIterator.next();
                            final JSONObject rootChildObj = response.getJSONObject(rootKey);
                            String subCat = rootChildObj.getString("name");
                            catList.add(subCat);
                          } catch (JSONException e) {
                            e.printStackTrace();
                          }
                        }

                        return catList;
                      })
                  .subscribeOn(Schedulers.computation())
                  .subscribe(videoSubCategories::postValue),
          error -> Log.e(TAG, "videoCategoryListRequest: OnError", error.getCause()));

  private final JsonObjectRequest friendCategoryListRequest =
      new JsonObjectRequest(
          Method.GET,
          ApiEndpoints.SUB_CAT_FRIEND,
          null,
          response ->
              Single.fromCallable(
                      () -> {
                        ArrayList<String> catList = new ArrayList<>();
                        catList.add("");
                        for (Iterator<String> rootIterator = response.keys();
                            rootIterator.hasNext(); ) {
                          try {
                            final String rootKey = rootIterator.next();
                            final JSONObject rootChildObj = response.getJSONObject(rootKey);
                            String subCat = rootChildObj.getString("name");
                            catList.add(subCat);
                          } catch (JSONException e) {
                            e.printStackTrace();
                          }
                        }

                        return catList;
                      })
                  .subscribeOn(Schedulers.computation())
                  .subscribe(friendSubCategories::postValue),
          error -> Log.e(TAG, "friendCategoryListRequest: OnError", error.getCause()));

  private final JsonObjectRequest productCategoryListRequest =
      new JsonObjectRequest(
          Method.GET,
          ApiEndpoints.SUB_CAT_PRODUCT,
          null,
          response ->
              Single.fromCallable(
                      () -> {
                        ArrayList<String> catList = new ArrayList<>();
                        catList.add("");
                        for (Iterator<String> rootIterator = response.keys();
                            rootIterator.hasNext(); ) {
                          try {
                            final String rootKey = rootIterator.next();
                            final JSONObject rootChildObj = response.getJSONObject(rootKey);
                            String subCat = rootChildObj.getString("name");
                            catList.add(subCat);
                          } catch (JSONException e) {
                            e.printStackTrace();
                          }
                        }

                        return catList;
                      })
                  .subscribeOn(Schedulers.computation())
                  .subscribe(productSubCategories::postValue),
          error -> Log.e(TAG, "productCategoryListRequest: OnError", error.getCause()));

  private final JsonObjectRequest provideCategoryListRequest =
      new JsonObjectRequest(
          Method.GET,
          ApiEndpoints.SUB_CAT_PROVIDE,
          null,
          response ->
              Single.fromCallable(
                      () -> {
                        ArrayList<String> catList = new ArrayList<>();
                        catList.add("");
                        for (Iterator<String> rootIterator = response.keys();
                            rootIterator.hasNext(); ) {
                          try {
                            final String rootKey = rootIterator.next();
                            final JSONObject rootChildObj = response.getJSONObject(rootKey);
                            String subCat = rootChildObj.getString("name");
                            catList.add(subCat);
                          } catch (JSONException e) {
                            e.printStackTrace();
                          }
                        }

                        return catList;
                      })
                  .subscribeOn(Schedulers.computation())
                  .subscribe(provideSubCategories::postValue),
          error -> Log.e(TAG, "provideCategoryListRequest: OnError", error.getCause()));

  private final JsonObjectRequest demandCategoryListRequest =
      new JsonObjectRequest(
          Method.GET,
          ApiEndpoints.SUB_CAT_DEMAND,
          null,
          response ->
              Single.fromCallable(
                      () -> {
                        ArrayList<String> catList = new ArrayList<>();
                        catList.add("");
                        for (Iterator<String> rootIterator = response.keys();
                            rootIterator.hasNext(); ) {
                          try {
                            final String rootKey = rootIterator.next();
                            final JSONObject rootChildObj = response.getJSONObject(rootKey);
                            String subCat = rootChildObj.getString("name");
                            catList.add(subCat);
                          } catch (JSONException e) {
                            e.printStackTrace();
                          }
                        }

                        return catList;
                      })
                  .subscribeOn(Schedulers.computation())
                  .subscribe(demandSubCategories::postValue),
          error -> Log.e(TAG, "demandCategoryListRequest: OnError", error.getCause()));

  /* ****************** Slider Requests ************************** */
  private final JsonObjectRequest imageSliderRequest =
      new JsonObjectRequest(
          Method.GET,
          ApiEndpoints.SLIDER_IMAGE,
          null,
          response ->
              Single.fromCallable(
                      () -> {
                        ArrayList<String> imageList = new ArrayList<>();
                        for (Iterator<String> rootIterator = response.keys();
                            rootIterator.hasNext(); ) {
                          try {
                            final String rootKey = rootIterator.next();
                            final JSONObject rootChildObj = response.getJSONObject(rootKey);
                            String subCat = rootChildObj.getString("image");
                            imageList.add(subCat);
                          } catch (JSONException e) {
                            e.printStackTrace();
                          }
                        }

                        return imageList;
                      })
                  .subscribeOn(Schedulers.computation())
                  .subscribe(imageSlidersList::postValue),
          error -> Log.e(TAG, "imageSliderRequest: OnError", error.getCause()));

  private final JsonObjectRequest videoSliderRequest =
      new JsonObjectRequest(
          Method.GET,
          ApiEndpoints.SLIDER_VIDEO,
          null,
          response ->
              Single.fromCallable(
                      () -> {
                        ArrayList<String> imageList = new ArrayList<>();

                        for (Iterator<String> rootIterator = response.keys();
                            rootIterator.hasNext(); ) {
                          try {
                            final String rootKey = rootIterator.next();
                            final JSONObject rootChildObj = response.getJSONObject(rootKey);
                            String subCat = rootChildObj.getString("image");
                            imageList.add(subCat);
                          } catch (JSONException e) {
                            e.printStackTrace();
                          }
                        }

                        return imageList;
                      })
                  .subscribeOn(Schedulers.computation())
                  .subscribe(videoSlidersList::postValue),
          error -> Log.e(TAG, "videoSliderRequest: OnError", error.getCause()));

  private final JsonObjectRequest friendSliderRequest =
      new JsonObjectRequest(
          Method.GET,
          ApiEndpoints.SLIDER_FRIEND,
          null,
          response ->
              Single.fromCallable(
                      () -> {
                        ArrayList<String> imageList = new ArrayList<>();

                        for (Iterator<String> rootIterator = response.keys();
                            rootIterator.hasNext(); ) {
                          try {
                            final String rootKey = rootIterator.next();
                            final JSONObject rootChildObj = response.getJSONObject(rootKey);
                            String subCat = rootChildObj.getString("image");
                            imageList.add(subCat);
                          } catch (JSONException e) {
                            e.printStackTrace();
                          }
                        }

                        return imageList;
                      })
                  .subscribeOn(Schedulers.computation())
                  .subscribe(friendSlidersList::postValue),
          error -> Log.e(TAG, "friendSliderRequest: OnError", error.getCause()));

  private final JsonObjectRequest productSliderRequest =
      new JsonObjectRequest(
          Method.GET,
          ApiEndpoints.SLIDER_PRODUCT,
          null,
          response ->
              Single.fromCallable(
                      () -> {
                        ArrayList<String> imageList = new ArrayList<>();

                        for (Iterator<String> rootIterator = response.keys();
                            rootIterator.hasNext(); ) {
                          try {
                            final String rootKey = rootIterator.next();
                            final JSONObject rootChildObj = response.getJSONObject(rootKey);
                            String subCat = rootChildObj.getString("image");
                            imageList.add(subCat);
                          } catch (JSONException e) {
                            e.printStackTrace();
                          }
                        }

                        return imageList;
                      })
                  .subscribeOn(Schedulers.computation())
                  .subscribe(productSlidersList::postValue),
          error -> Log.e(TAG, "productSliderRequest: OnError", error.getCause()));

  private final JsonObjectRequest provideSliderRequest =
      new JsonObjectRequest(
          Method.GET,
          ApiEndpoints.SLIDER_PROVIDE,
          null,
          response ->
              Single.fromCallable(
                      () -> {
                        ArrayList<String> imageList = new ArrayList<>();

                        for (Iterator<String> rootIterator = response.keys();
                            rootIterator.hasNext(); ) {
                          try {
                            final String rootKey = rootIterator.next();
                            final JSONObject rootChildObj = response.getJSONObject(rootKey);
                            String subCat = rootChildObj.getString("image");
                            imageList.add(subCat);
                          } catch (JSONException e) {
                            e.printStackTrace();
                          }
                        }

                        return imageList;
                      })
                  .subscribeOn(Schedulers.computation())
                  .subscribe(provideSlidersList::postValue),
          error -> Log.e(TAG, "provideSliderRequest: OnError", error.getCause()));

  private final JsonObjectRequest demandSliderRequest =
      new JsonObjectRequest(
          Method.GET,
          ApiEndpoints.SLIDER_DEMAND,
          null,
          response ->
              Single.fromCallable(
                      () -> {
                        ArrayList<String> imageList = new ArrayList<>();

                        for (Iterator<String> rootIterator = response.keys();
                            rootIterator.hasNext(); ) {
                          try {
                            final String rootKey = rootIterator.next();
                            final JSONObject rootChildObj = response.getJSONObject(rootKey);
                            String subCat = rootChildObj.getString("image");
                            imageList.add(subCat);
                          } catch (JSONException e) {
                            e.printStackTrace();
                          }
                        }

                        return imageList;
                      })
                  .subscribeOn(Schedulers.computation())
                  .subscribe(demandSlidersList::postValue),
          error -> Log.e(TAG, "demandSliderRequest: OnError", error.getCause()));


  public SearchViewModel(@NonNull Application application) {
    super(application);

    loadImageCategoriesData();
    loadVideoCategoriesData();
    loadFriendCategoriesData();
    loadProductCategoriesData();
    loadProvideCategoriesData();
    loadDemandCategoriesData();

    // Fetching all sliders images
    VolleyUtil.getInstance(getApplication()).addRequest(imageSliderRequest);
    VolleyUtil.getInstance(getApplication()).addRequest(videoSliderRequest);
    VolleyUtil.getInstance(getApplication()).addRequest(friendSliderRequest);
    VolleyUtil.getInstance(getApplication()).addRequest(productSliderRequest);
    VolleyUtil.getInstance(getApplication()).addRequest(provideSliderRequest);
    VolleyUtil.getInstance(getApplication()).addRequest(demandSliderRequest);
  }

  /* ****************************
   * Load Triggering Methods
   ************************** */
  public void loadImageCategoriesData() {
    VolleyUtil.getInstance(getApplication()).addRequest(imageCategoryListRequest);
  }

  public void loadVideoCategoriesData() {
    VolleyUtil.getInstance(getApplication()).addRequest(videoCategoryListRequest);
  }

  public void loadFriendCategoriesData() {
    VolleyUtil.getInstance(getApplication()).addRequest(friendCategoryListRequest);
  }

  public void loadProductCategoriesData() {
    VolleyUtil.getInstance(getApplication()).addRequest(productCategoryListRequest);
  }

  public void loadProvideCategoriesData() {
    VolleyUtil.getInstance(getApplication()).addRequest(provideCategoryListRequest);
  }

  public void loadDemandCategoriesData() {
    VolleyUtil.getInstance(getApplication()).addRequest(demandCategoryListRequest);
  }

  /* ****************** Load Search Data ************************** */
  public void loadImageSearch(String searchData) {
    String url = "http://www.iampro.co/api/search.php?type=search_all_items&search_type=IMAGE&search_data=";
    if (searchData != null && !searchData.isEmpty()) {
      try {
        url += URLEncoder.encode(searchData, "UTF-8");
      } catch (UnsupportedEncodingException e) {
        e.printStackTrace();
      }
    }
    final JsonObjectRequest searchRequest =
        new JsonObjectRequest(
            Method.GET,
            url,
            null,
            response -> {
              Log.d(TAG, "allImagesRequest: onResponse: Response successfully retrived");
              Single.fromCallable(() -> {
                Map<String, List<ImageDetails>> mainMap = new HashMap<>();
                Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

                for (Iterator<String> categoryIterator = response.keys(); categoryIterator.hasNext(); ) {
                  final String categoryKey = categoryIterator.next();
                  final JSONObject categoryJsonObj = response.optJSONObject(categoryKey);
                  final List<ImageDetails> imagesList = new ArrayList<>();
                  String categoryName = null;
                  if (categoryJsonObj.has("name")) {
                    categoryName = categoryJsonObj.optString("name");
                  }

                  // If categoryJsonObj is null then no need to perfrom any other task just return
                  if (categoryJsonObj == null) {
                    if (categoryName != null && !categoryName.isEmpty()) {
                      mainMap.put(categoryName, imagesList);
                    } else {
                      mainMap.put(categoryKey, imagesList);
                    }
                    continue;
                  }

                  // if response don't have img_detail then no need to do anything just return
                  JSONObject imagesListJsonObj = categoryJsonObj.optJSONObject("img_detail");
                  if (imagesListJsonObj == null) {
                    if (categoryName != null && !categoryName.isEmpty()) {
                      mainMap.put(categoryName, imagesList);
                    } else {
                      mainMap.put(categoryKey, imagesList);
                    }
                    continue;
                  }

                  // Getting image list from categoryJsonObj
                  for (Iterator<String> imageIterator = imagesListJsonObj.keys(); imageIterator.hasNext(); ) {
                    final String imageKey = imageIterator.next();
                    final JSONObject imageJsonObj = imagesListJsonObj.optJSONObject(imageKey);
                    if (imageJsonObj == null) {
                      continue;
                    }
                    final ImageDetails imageDetails = gson.fromJson(imageJsonObj.toString(), ImageDetails.class);
                    imageDetails.setCategory(categoryName);
                    if (imageJsonObj.has("user_detail")) {
                      JSONObject userDetailsJsonObj = imageJsonObj.optJSONObject("user_detail");
                      if (userDetailsJsonObj != null) {
                        UserDetails userDetails = gson.fromJson(userDetailsJsonObj.toString(), UserDetails.class);
                        imageDetails.setUserDetail(userDetails);
                      }
                    }
                    imagesList.add(imageDetails);
                  }

                  // adding data in mainMap
                  if (categoryName != null && !categoryName.isEmpty()) {
                    mainMap.put(categoryName, imagesList);
                  } else {
                    mainMap.put(categoryKey, imagesList);
                  }
                }
                return mainMap;
              })
                  .subscribeOn(Schedulers.computation())
                  .subscribe(imageSearchData::postValue);
            },
            error -> Log.e(TAG, "demandSliderRequest: OnError", error.getCause()));
    VolleyUtil.getInstance(getApplication()).addRequest(searchRequest);
  }

  public void loadVideoSearch(String searchData) {
    String url = "http://www.iampro.co/api/search.php?type=search_all_items&search_type=VIDEO&search_data=";
    if (searchData != null && !searchData.isEmpty()) {
      try {
        url += URLEncoder.encode(searchData, "UTF-8");
      } catch (UnsupportedEncodingException e) {
        e.printStackTrace();
      }
    }
    final JsonObjectRequest searchRequest =
        new JsonObjectRequest(
            Method.GET,
            url,
            null,
            response -> {
              Log.d(TAG, "videoSearchRequest: onResponse: Response successfully retrived");
              Single.fromCallable(() -> {
                Map<String, List<ImageDetails>> mainMap = new HashMap<>();
                Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

                for (Iterator<String> categoryIterator = response.keys(); categoryIterator.hasNext(); ) {
                  final String categoryKey = categoryIterator.next();
                  final JSONObject categoryJsonObj = response.optJSONObject(categoryKey);
                  final List<ImageDetails> imagesList = new ArrayList<>();
                  String categoryName = null;
                  if (categoryJsonObj.has("name")) {
                    categoryName = categoryJsonObj.optString("name");
                  }

                  // If categoryJsonObj is null then no need to perfrom any other task just return
                  if (categoryJsonObj == null) {
                    if (categoryName != null && !categoryName.isEmpty()) {
                      mainMap.put(categoryName, imagesList);
                    } else {
                      mainMap.put(categoryKey, imagesList);
                    }
                    continue;
                  }

                  // if response don't have img_detail then no need to do anything just return
                  JSONObject imagesListJsonObj = categoryJsonObj.optJSONObject("img_detail");
                  if (imagesListJsonObj == null) {
                    if (categoryName != null && !categoryName.isEmpty()) {
                      mainMap.put(categoryName, imagesList);
                    } else {
                      mainMap.put(categoryKey, imagesList);
                    }
                    continue;
                  }

                  // Getting image list from categoryJsonObj
                  for (Iterator<String> imageIterator = imagesListJsonObj.keys(); imageIterator.hasNext(); ) {
                    final String imageKey = imageIterator.next();
                    final JSONObject imageJsonObj = imagesListJsonObj.optJSONObject(imageKey);
                    if (imageJsonObj == null) {
                      continue;
                    }
                    final ImageDetails imageDetails = gson.fromJson(imageJsonObj.toString(), ImageDetails.class);
                    if (imageJsonObj.has("user_detail")) {
                      JSONObject userDetailsJsonObj = imageJsonObj.optJSONObject("user_detail");
                      if (userDetailsJsonObj != null) {
                        UserDetails userDetails = gson.fromJson(userDetailsJsonObj.toString(), UserDetails.class);
                        imageDetails.setUserDetail(userDetails);
                      }
                    }
                    imagesList.add(imageDetails);
                  }

                  // adding data in mainMap
                  if (categoryName != null && !categoryName.isEmpty()) {
                    mainMap.put(categoryName, imagesList);
                  } else {
                    mainMap.put(categoryKey, imagesList);
                  }
                }
                return mainMap;
              })
                  .subscribeOn(Schedulers.computation())
                  .subscribe(videoSearchData::postValue);
            },
            error -> Log.e(TAG, "videoSearchRequest: OnError", error.getCause()));
    VolleyUtil.getInstance(getApplication()).addRequest(searchRequest);
  }

  public void loadFriendSearch(String searchData) {
    String url = "http://www.iampro.co/api/search.php?type=search_all_friends&search_type=FRIEND&search_data=";
    if (searchData != null && !searchData.isEmpty()) {
      try {
        url += URLEncoder.encode(searchData, "UTF-8");
      } catch (UnsupportedEncodingException e) {
        e.printStackTrace();
      }
    }
    final JsonObjectRequest searchRequest =
        new JsonObjectRequest(
            Method.GET,
            url,
            null,
            response -> {
              Log.d(TAG, "loadFriendSearch: onResponse: data successfully retrieved");
              Single.fromCallable(() -> {
                List<UserDetails> userDetailsList = new ArrayList<>();
                Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
                for (Iterator<String> rootIterator = response.keys(); rootIterator.hasNext();){
                  final String rootChildKey = rootIterator.next();
                  final JSONObject rootChildJsonObj = response.optJSONObject(rootChildKey);
                  if (rootChildJsonObj == null) continue;

                  try {
                    UserDetails userDetails = gson.fromJson(rootChildJsonObj.toString(), UserDetails.class);
                    userDetailsList.add(userDetails);
                  } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                  }
                }
                return userDetailsList;
              })
                  .subscribeOn(Schedulers.computation())
                  .subscribe(friendSearchData::postValue);
            },
            error -> Log.e(TAG, "loadFriendSearch: OnError", error.getCause()));
    VolleyUtil.getInstance(getApplication()).addRequest(searchRequest);
  }

  public void loadProductSearch(String searchData) {
    String url = "http://www.iampro.co/api/search.php?type=search_all_items&search_type=PRODUCT&search_data=";
    if (searchData != null && !searchData.isEmpty()) {
      try {
        url += URLEncoder.encode(searchData, "UTF-8");
      } catch (UnsupportedEncodingException e) {
        e.printStackTrace();
      }
    }
    final JsonObjectRequest searchRequest =
        new JsonObjectRequest(
            Method.GET,
            url,
            null,
            response -> {
              Log.d(TAG, "loadProductSearch: onResponse: data successfully retrieved");
              Single.fromCallable(() -> {
                Map<String, List<ProductDetails>> mainMap = new HashMap<>();
                Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

                for (Iterator<String> categoryIterator = response.keys(); categoryIterator.hasNext(); ) {
                  final String categoryKey = categoryIterator.next();
                  final JSONObject categoryJsonObj = response.optJSONObject(categoryKey);
                  final List<ProductDetails> productsList = new ArrayList<>();
                  String categoryName = null;

                  if (categoryJsonObj.has("name")) {
                    categoryName = categoryJsonObj.optString("name");
                  }

                  // If categoryJsonObj is null then no need to perform any other task just return
                  if (categoryJsonObj == null) {
                    if (categoryName != null && !categoryName.isEmpty()) {
                      mainMap.put(categoryName, productsList);
                    } else {
                      mainMap.put(categoryKey, productsList);
                    }
                    continue;
                  }

                  // if response don't have pro_detail then no need to do anything just return
                  JSONObject productsListJsonObj = categoryJsonObj.optJSONObject("pro_detail");
                  if (productsListJsonObj == null) {
                    if (categoryName != null && !categoryName.isEmpty()) {
                      mainMap.put(categoryName, productsList);
                    } else {
                      mainMap.put(categoryKey, productsList);
                    }
                    continue;
                  }

                  // Getting image list from categoryJsonObj
                  for (Iterator<String> productIterator = productsListJsonObj.keys(); productIterator.hasNext(); ) {
                    final String productKey = productIterator.next();
                    final JSONObject productJsonObj = productsListJsonObj.optJSONObject(productKey);
                    if (productJsonObj == null) {
                      continue;
                    }
                    final ProductDetails productDetails = gson.fromJson(productJsonObj.toString(), ProductDetails.class);
                    if (productJsonObj.has("user_detail")) {
                      JSONObject userDetailsJsonObj = productJsonObj.optJSONObject("user_detail");
                      if (userDetailsJsonObj != null) {
                        UserDetails userDetails = gson.fromJson(userDetailsJsonObj.toString(), UserDetails.class);
                        productDetails.setUserDetails(userDetails);
                      }
                    }
                    productsList.add(productDetails);
                  }

                  // adding data in mainMap
                  if (categoryName != null && !categoryName.isEmpty()) {
                    mainMap.put(categoryName, productsList);
                  } else {
                    mainMap.put(categoryKey, productsList);
                  }
                }
                return mainMap;
              })
                  .subscribeOn(Schedulers.computation())
                  .subscribe(productSearchData::postValue);
            },
            error -> Log.e(TAG, "loadProductSearch: OnError", error.getCause()));
    VolleyUtil.getInstance(getApplication()).addRequest(searchRequest);
  }

  public void loadProvideSearch(String searchData) {
    String url = "http://www.iampro.co/api/search.php?type=search_all_items&search_type=PROVIDE&search_data=";
    if (searchData != null && !searchData.isEmpty()) {
      try {
        url += URLEncoder.encode(searchData, "UTF-8");
      } catch (UnsupportedEncodingException e) {
        e.printStackTrace();
      }
    }
    final JsonObjectRequest searchRequest =
        new JsonObjectRequest(
            Method.GET,
            url,
            null,
            response -> {
              Log.d(TAG, "loadProvideSearch: onResponse: data successfully retrieved");
              Single.fromCallable(() -> {
                Map<String, List<ProductDetails>> mainMap = new HashMap<>();
                Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

                for (Iterator<String> categoryIterator = response.keys(); categoryIterator.hasNext(); ) {
                  final String categoryKey = categoryIterator.next();
                  final JSONObject categoryJsonObj = response.optJSONObject(categoryKey);
                  final List<ProductDetails> productsList = new ArrayList<>();
                  String categoryName = null;

                  if (categoryJsonObj.has("name")) {
                    categoryName = categoryJsonObj.optString("name");
                  }

                  // If categoryJsonObj is null then no need to perform any other task just return
                  if (categoryJsonObj == null) {
                    if (categoryName != null && !categoryName.isEmpty()) {
                      mainMap.put(categoryName, productsList);
                    } else {
                      mainMap.put(categoryKey, productsList);
                    }
                    continue;
                  }

                  // if response don't have pro_detail then no need to do anything just return
                  JSONObject productsListJsonObj = categoryJsonObj.optJSONObject("pro_detail");
                  if (productsListJsonObj == null) {
                    if (categoryName != null && !categoryName.isEmpty()) {
                      mainMap.put(categoryName, productsList);
                    } else {
                      mainMap.put(categoryKey, productsList);
                    }
                    continue;
                  }

                  // Getting image list from categoryJsonObj
                  for (Iterator<String> productIterator = productsListJsonObj.keys(); productIterator.hasNext(); ) {
                    final String productKey = productIterator.next();
                    final JSONObject productJsonObj = productsListJsonObj.optJSONObject(productKey);
                    if (productJsonObj == null) {
                      continue;
                    }
                    final ProductDetails productDetails = gson.fromJson(productJsonObj.toString(), ProductDetails.class);
                    if (productJsonObj.has("user_detail")) {
                      JSONObject userDetailsJsonObj = productJsonObj.optJSONObject("user_detail");
                      if (userDetailsJsonObj != null) {
                        UserDetails userDetails = gson.fromJson(userDetailsJsonObj.toString(), UserDetails.class);
                        productDetails.setUserDetails(userDetails);
                      }
                    }
                    productsList.add(productDetails);
                  }

                  // adding data in mainMap
                  if (categoryName != null && !categoryName.isEmpty()) {
                    mainMap.put(categoryName, productsList);
                  } else {
                    mainMap.put(categoryKey, productsList);
                  }
                }
                return mainMap;
              })
                  .subscribeOn(Schedulers.computation())
                  .subscribe(provideSearchData::postValue);
            },
            error -> Log.e(TAG, "loadProvideSearch: OnError", error.getCause()));
    VolleyUtil.getInstance(getApplication()).addRequest(searchRequest);
  }

  public void loadDemandSearch(String searchData) {
    String url = "http://www.iampro.co/api/search.php?type=search_all_items&search_type=DEMAND&search_data=";
    if (searchData != null && !searchData.isEmpty()) {
      try {
        url += URLEncoder.encode(searchData, "UTF-8");
      } catch (UnsupportedEncodingException e) {
        e.printStackTrace();
      }
    }
    final JsonObjectRequest searchRequest =
        new JsonObjectRequest(
            Method.GET,
            url,
            null,
            response -> {
              Log.d(TAG, "loadDemandSearch: onResponse: data successfully retrieved");
              Single.fromCallable(() -> {
                Map<String, List<ProductDetails>> mainMap = new HashMap<>();
                Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

                for (Iterator<String> categoryIterator = response.keys(); categoryIterator.hasNext(); ) {
                  final String categoryKey = categoryIterator.next();
                  final JSONObject categoryJsonObj = response.optJSONObject(categoryKey);
                  final List<ProductDetails> productsList = new ArrayList<>();
                  String categoryName = null;

                  if (categoryJsonObj.has("name")) {
                    categoryName = categoryJsonObj.optString("name");
                  }

                  // If categoryJsonObj is null then no need to perform any other task just return
                  if (categoryJsonObj == null) {
                    if (categoryName != null && !categoryName.isEmpty()) {
                      mainMap.put(categoryName, productsList);
                    } else {
                      mainMap.put(categoryKey, productsList);
                    }
                    continue;
                  }

                  // if response don't have pro_detail then no need to do anything just return
                  JSONObject productsListJsonObj = categoryJsonObj.optJSONObject("pro_detail");
                  if (productsListJsonObj == null) {
                    if (categoryName != null && !categoryName.isEmpty()) {
                      mainMap.put(categoryName, productsList);
                    } else {
                      mainMap.put(categoryKey, productsList);
                    }
                    continue;
                  }

                  // Getting image list from categoryJsonObj
                  for (Iterator<String> productIterator = productsListJsonObj.keys(); productIterator.hasNext(); ) {
                    final String productKey = productIterator.next();
                    final JSONObject productJsonObj = productsListJsonObj.optJSONObject(productKey);
                    if (productJsonObj == null) {
                      continue;
                    }
                    final ProductDetails productDetails = gson.fromJson(productJsonObj.toString(), ProductDetails.class);
                    if (productJsonObj.has("user_detail")) {
                      JSONObject userDetailsJsonObj = productJsonObj.optJSONObject("user_detail");
                      if (userDetailsJsonObj != null) {
                        UserDetails userDetails = gson.fromJson(userDetailsJsonObj.toString(), UserDetails.class);
                        productDetails.setUserDetails(userDetails);
                      }
                    }
                    productsList.add(productDetails);
                  }

                  // adding data in mainMap
                  if (categoryName != null && !categoryName.isEmpty()) {
                    mainMap.put(categoryName, productsList);
                  } else {
                    mainMap.put(categoryKey, productsList);
                  }
                }
                return mainMap;
              })
                  .subscribeOn(Schedulers.computation())
                  .subscribe(demandSearchData::postValue);
            },
            error -> Log.e(TAG, "loadDemandSearch: OnError", error.getCause()));
    VolleyUtil.getInstance(getApplication()).addRequest(searchRequest);
  }

  /* ****************************
   *LIVE DATA GET METHODS
   ************************** */
  public LiveData<ArrayList<String>> getImageSubCategories() {
    return imageSubCategories;
  }

  public LiveData<ArrayList<String>> getVideoSubCategories() {
    return videoSubCategories;
  }

  public LiveData<ArrayList<String>> getFriendSubCategories() {
    return friendSubCategories;
  }

  public LiveData<ArrayList<String>> getProdutSubCategories() {
    return productSubCategories;
  }

  public LiveData<ArrayList<String>> getProvideSubCategories() {
    return provideSubCategories;
  }

  public LiveData<ArrayList<String>> getDemandSubCategories() {
    return demandSubCategories;
  }

  /* ****************** Image Sliders Live Data ************************** */
  public LiveData<ArrayList<String>> getImageSlidersList() {
    return imageSlidersList;
  }

  public LiveData<ArrayList<String>> getVideoSlidersList() {
    return videoSlidersList;
  }

  public LiveData<ArrayList<String>> getFriendSlidersList() {
    return friendSlidersList;
  }

  public LiveData<ArrayList<String>> getProductSlidersList() {
    return productSlidersList;
  }

  public LiveData<ArrayList<String>> getProvideSlidersList() {
    return provideSlidersList;
  }

  public LiveData<ArrayList<String>> getDemandSlidersList() {
    return demandSlidersList;
  }

  /* ****************** Image Search Live Data ************************** */
  public LiveData<Map<String, List<ImageDetails>>> getImageSearchData() {
    return imageSearchData;
  }

  public LiveData<Map<String, List<ImageDetails>>> getVideoSearchData() {
    return videoSearchData;
  }

  public LiveData<List<UserDetails>> getFriendSearchData() {
    return friendSearchData;
  }

  public LiveData<Map<String, List<ProductDetails>>> getProductSearchData() {
    return productSearchData;
  }

  public LiveData<Map<String, List<ProductDetails>>> getProvideSearchData() {
    return provideSearchData;
  }

  public LiveData<Map<String, List<ProductDetails>>> getDemandSearchData() {
    return demandSearchData;
  }
}
