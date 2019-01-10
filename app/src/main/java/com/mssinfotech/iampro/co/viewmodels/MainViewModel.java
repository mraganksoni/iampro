package com.mssinfotech.iampro.co.viewmodels;

import android.annotation.SuppressLint;
import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.mssinfotech.iampro.co.models.ImageDetails;
import com.mssinfotech.iampro.co.models.ProductDetails;
import com.mssinfotech.iampro.co.models.ProvideAndDemandItemModel;
import com.mssinfotech.iampro.co.models.TopSliderImageModel;
import com.mssinfotech.iampro.co.models.UserDetails;
import com.mssinfotech.iampro.co.models.VideoDetails;
import com.mssinfotech.iampro.co.utils.ApiEndpoints;
import com.mssinfotech.iampro.co.utils.VolleyUtil;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MainViewModel extends AndroidViewModel {
  private static final String TAG = "MainViewModel";

  /** **************************** VARIABLES *************************** */

  /** **************************** LIVE DATA REFERENCES *************************** */
  private final MutableLiveData<List<TopSliderImageModel>> topSliderImages =
      new MutableLiveData<>();

  private final MutableLiveData<List<ImageDetails>> latestPhotosImages = new MutableLiveData<>();
  private final MutableLiveData<List<VideoDetails>> latestVideosImages = new MutableLiveData<>();
  private final MutableLiveData<List<UserDetails>> smartMembers = new MutableLiveData<>();
  private final MutableLiveData<List<ProductDetails>> latestProducts = new MutableLiveData<>();
  private final MutableLiveData<List<ProductDetails>> provideAndDemands = new MutableLiveData<>();
  private final MutableLiveData<Map<String, List<ImageDetails>>> allImageList =
      new MutableLiveData<>();
  private final MutableLiveData<Map<String, List<ImageDetails>>> allVideosList =
      new MutableLiveData<>();
  private final MutableLiveData<List<UserDetails>> allUsersList = new MutableLiveData<>();
  private final MutableLiveData<Map<String, List<ProductDetails>>> allProductsList =
      new MutableLiveData<>();
  private final MutableLiveData<Map<String, List<ProductDetails>>> allProvidesList =
      new MutableLiveData<>();
  private final MutableLiveData<Map<String, List<ProductDetails>>> allDemandsList =
      new MutableLiveData<>();

  /** **************************** VOLLEY NETWORK REQUESTS *************************** */
  private JsonObjectRequest topSliderImageRequest =
      new JsonObjectRequest(
          Request.Method.GET,
          ApiEndpoints.TOP_SLIDER_IMAGES,
          null,
          new Response.Listener<JSONObject>() {
            @SuppressLint("CheckResult")
            @Override
            public void onResponse(JSONObject response) {
              // Wrapping up all things in new thread so they heavy Lifting wont block main thread
              Single.fromCallable(
                      () -> {
                        Log.d(
                            TAG, "topSliderImageRequest: onResponse: Response succefully retrived");
                        Gson gson = new Gson();
                        ArrayList<TopSliderImageModel> topSliderImageList = new ArrayList<>();
                        for (Iterator<String> rootIterator = response.keys();
                            rootIterator.hasNext(); ) {
                          final String rootChildKey = rootIterator.next();
                          final JSONObject rootChildObject = response.optJSONObject(rootChildKey);
                          if (rootChildObject != null) {
                            TopSliderImageModel model =
                                gson.fromJson(
                                    rootChildObject.toString(), TopSliderImageModel.class);
                            topSliderImageList.add(model);
                          }
                        }
                        return topSliderImageList;
                      })
                  .subscribeOn(Schedulers.computation())
                  .observeOn(AndroidSchedulers.mainThread())
                  .subscribe(
                      topSliderImageModels -> {
                        topSliderImages.setValue(topSliderImageModels);
                      });
            }
          },
          new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
              Log.e(TAG, "topSliderImageRequest: onErrorResponse: ", error.getCause());
            }
          });

  private JsonObjectRequest latestPhotosRequest =
      new JsonObjectRequest(
          Request.Method.GET,
          ApiEndpoints.LATEST_PHOTOS,
          null,
          new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
              Log.d(TAG, "latestPhotosRequest: onResponse: Response succefully retrived");
              Single.fromCallable(
                      () -> {
                        Gson gson = new Gson();
                        ArrayList<ImageDetails> imageDetailsList = new ArrayList<>();
                        for (Iterator<String> rootIterator = response.keys();
                            rootIterator.hasNext(); ) {
                          final String keyRootIterator = rootIterator.next();
                          final JSONObject rootChildObject =
                              response.optJSONObject(keyRootIterator);
                          if (rootChildObject != null) {
                            try {
                              ImageDetails imageDetails =
                                  gson.fromJson(rootChildObject.toString(), ImageDetails.class);
                              imageDetailsList.add(imageDetails);
                            } catch (JsonSyntaxException e) {
                              e.printStackTrace();
                            }
                          }
                        }

                        return imageDetailsList;
                      })
                  .subscribeOn(Schedulers.computation())
                  .observeOn(AndroidSchedulers.mainThread())
                  .subscribe(
                      imageDetailsList -> {
                        latestPhotosImages.setValue(imageDetailsList);
                      });
            }
          },
          new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
              Log.e(TAG, "latestPhotosRequest: onErrorResponse: ", error.getCause());
            }
          });

  private JsonObjectRequest latestVideosRequest =
      new JsonObjectRequest(
          Request.Method.GET,
          ApiEndpoints.LATEST_VIDEOS,
          null,
          new Response.Listener<JSONObject>() {
            @SuppressLint("CheckResult")
            @Override
            public void onResponse(JSONObject response) {
              Log.d(TAG, "latestVideosRequest: onResponse: Response succefully retrived");

              Gson gson = new Gson();
              ArrayList<VideoDetails> videoDetailsList = new ArrayList<>();

              Single.fromCallable(
                      () -> {
                        for (Iterator<String> rootIterator = response.keys();
                            rootIterator.hasNext(); ) {
                          final String rootChildKey = rootIterator.next();
                          final JSONObject rootChildObject = response.optJSONObject(rootChildKey);
                          if (rootChildObject != null) {
                            try {
                              VideoDetails videoDetails =
                                  gson.fromJson(rootChildObject.toString(), VideoDetails.class);
                              videoDetailsList.add(videoDetails);
                            } catch (JsonSyntaxException e) {
                              e.printStackTrace();
                            }
                          }
                        }
                        return videoDetailsList;
                      })
                  .subscribeOn(Schedulers.computation())
                  .observeOn(AndroidSchedulers.mainThread())
                  .subscribe(
                      videoDetails -> {
                        latestVideosImages.setValue(videoDetails);
                      });
            }
          },
          new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
              Log.e(TAG, "latestVideosRequest: onErrorResponse: ", error.getCause());
            }
          });

  private JsonArrayRequest smartMembersRequest =
      new JsonArrayRequest(
          Request.Method.GET,
          ApiEndpoints.SMART_MEMBERS,
          null,
          new Response.Listener<JSONArray>() {
            @SuppressLint("CheckResult")
            @Override
            public void onResponse(JSONArray response) {
              Log.d(TAG, "smartMembersRequest: onResponse: ");

              Gson gson = new Gson();
              ArrayList<UserDetails> userDetailsList = new ArrayList<>();
              Single.fromCallable(
                      () -> {
                        final int size = response.length();
                        for (int row = 0; row < size; ++row) {
                          JSONObject rootChildObject = response.optJSONObject(row);
                          try {
                            UserDetails userDetails =
                                gson.fromJson(rootChildObject.toString(), UserDetails.class);
                            userDetailsList.add(userDetails);
                          } catch (JsonSyntaxException e) {
                            e.printStackTrace();
                          }
                        }
                        return userDetailsList;
                      })
                  .subscribeOn(Schedulers.computation())
                  .observeOn(AndroidSchedulers.mainThread())
                  .subscribe(userDetails -> smartMembers.setValue(userDetails));
            }
          },
          new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
              Log.e(TAG, "smartMembersRequest: onErrorResponse: ", error.getCause());
            }
          });

  private JsonObjectRequest latestProductsRequest =
      new JsonObjectRequest(
          Request.Method.GET,
          ApiEndpoints.LATEST_PRODUCTS,
          null,
          response -> {
            Log.d(TAG, "latestProductsRequest: onResponse: Response succefully retrived");

            Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
            ArrayList<ProductDetails> productDetailsList = new ArrayList<>();
            Single.fromCallable(
                    () -> {
                      for (Iterator<String> rootIterator = response.keys();
                          rootIterator.hasNext(); ) {
                        final String rootChildKey = rootIterator.next();
                        final JSONObject rootChildObject = response.optJSONObject(rootChildKey);
                        try {
                          ProductDetails productDetails =
                              gson.fromJson(rootChildObject.toString(), ProductDetails.class);
                          // Check if json object has userdetails
                          try {
                            JSONObject userDetailsJsonObj =
                                rootChildObject.getJSONObject("user_detail");
                            UserDetails userDetails =
                                gson.fromJson(userDetailsJsonObj.toString(), UserDetails.class);
                            productDetails.setUserDetails(userDetails);
                          } catch (JSONException e) {
                            e.printStackTrace();
                          }
                          productDetailsList.add(productDetails);
                        } catch (JsonSyntaxException e) {
                          e.printStackTrace();
                        }
                      }
                      return productDetailsList;
                    })
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(productDetails -> latestProducts.setValue(productDetails));
          },
          new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
              Log.e(TAG, "latestProductsRequest: onErrorResponse: ", error.getCause());
            }
          });

  private JsonObjectRequest provideAndDemandRequest =
      new JsonObjectRequest(
          Request.Method.GET,
          ApiEndpoints.PROVIDE_AND_DEMAND,
          null,
          new Response.Listener<JSONObject>() {
            @SuppressLint("CheckResult")
            @Override
            public void onResponse(JSONObject response) {
              Log.d(TAG, "provideAndDemandRequest: onResponse: Response successfully retrived");

              Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
              ArrayList<ProductDetails> itemsList = new ArrayList<>();

              Single.fromCallable(
                      () -> {
                        for (Iterator<String> rootIterator = response.keys();
                            rootIterator.hasNext(); ) {
                          final String rootChildKey = rootIterator.next();
                          final JSONObject rootChildObject = response.optJSONObject(rootChildKey);
                          if (rootChildObject != null) {
                            ProductDetails itemModel =
                                gson.fromJson(rootChildObject.toString(), ProductDetails.class);
                            // Check if json object has userdetails
                            try {
                              JSONObject userDetailsJsonObj =
                                  rootChildObject.getJSONObject("user_detail");
                              UserDetails userDetails =
                                  gson.fromJson(userDetailsJsonObj.toString(), UserDetails.class);
                              itemModel.setUserDetails(userDetails);
                            } catch (JSONException e) {
                              e.printStackTrace();
                            }
                            itemsList.add(itemModel);
                          }
                        }
                        return itemsList;
                      })
                  .subscribeOn(Schedulers.computation())
                  .observeOn(AndroidSchedulers.mainThread())
                  .subscribe(provideAndDemands::setValue);
            }
          },
          new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
              Log.e(TAG, "provideAndDemandRequest: onErrorResponse: ", error.getCause());
            }
          });

  private JsonObjectRequest allImagesRequest =
      new JsonObjectRequest(
          Request.Method.GET,
          ApiEndpoints.ALL_IMAGES,
          null,
          response -> {
            Log.d(TAG, "allImagesRequest: onResponse: Response successfully retrived");

            Map<String, List<ImageDetails>> mainMap = new HashMap<>();
            Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

            Single.fromCallable(
                    () -> {
                      for (Iterator<String> categoryIterator = response.keys();
                          categoryIterator.hasNext(); ) {
                        final String categoryKey = categoryIterator.next();
                        final JSONObject categoryJsonObj = response.optJSONObject(categoryKey);
                        final List<ImageDetails> imagesList = new ArrayList<>();
                        String categoryName = null;
                        if (categoryJsonObj.has("name")) {
                          categoryName = categoryJsonObj.optString("name");
                        }

                        // If categoryJsonObj is null then no need to perfrom any other task just
                        // return
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
                        for (Iterator<String> imageIterator = imagesListJsonObj.keys();
                            imageIterator.hasNext(); ) {
                          final String imageKey = imageIterator.next();
                          final JSONObject imageJsonObj = imagesListJsonObj.optJSONObject(imageKey);
                          if (imageJsonObj == null) {
                            continue;
                          }
                          final ImageDetails imageDetails =
                              gson.fromJson(imageJsonObj.toString(), ImageDetails.class);
                          imageDetails.setCategory(categoryName);
                          if (imageJsonObj.has("user_detail")) {
                            JSONObject userDetailsJsonObj =
                                imageJsonObj.optJSONObject("user_detail");
                            if (userDetailsJsonObj != null) {
                              UserDetails userDetails =
                                  gson.fromJson(userDetailsJsonObj.toString(), UserDetails.class);
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
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(stringListMap -> allImageList.setValue(stringListMap));
          },
          new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
              Log.e(TAG, "allImagesRequest: onErrorResponse: ", error.getCause());
            }
          });

  private JsonObjectRequest allVideosRequest =
      new JsonObjectRequest(
          Request.Method.GET,
          ApiEndpoints.ALL_VIDEOS,
          null,
          new Response.Listener<JSONObject>() {
            @SuppressLint("CheckResult")
            @Override
            public void onResponse(JSONObject response) {
              Log.d(TAG, "allVideosRequest: onResponse: data successfuly retrived");
              Map<String, List<ImageDetails>> mainMap = new HashMap<>();
              Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

              Single.fromCallable(
                      () -> {
                        for (Iterator<String> categoryIterator = response.keys();
                            categoryIterator.hasNext(); ) {
                          final String categoryKey = categoryIterator.next();
                          final JSONObject categoryJsonObj = response.optJSONObject(categoryKey);
                          final List<ImageDetails> imagesList = new ArrayList<>();
                          String categoryName = null;
                          if (categoryJsonObj.has("name")) {
                            categoryName = categoryJsonObj.optString("name");
                          }

                          // If categoryJsonObj is null then no need to perfrom any other task just
                          // return
                          if (categoryJsonObj == null) {
                            if (categoryName != null && !categoryName.isEmpty()) {
                              mainMap.put(categoryName, imagesList);
                            } else {
                              mainMap.put(categoryKey, imagesList);
                            }
                            continue;
                          }

                          // if response don't have img_detail then no need to do anything just
                          // return
                          JSONObject imagesListJsonObj =
                              categoryJsonObj.optJSONObject("img_detail");
                          if (imagesListJsonObj == null) {
                            if (categoryName != null && !categoryName.isEmpty()) {
                              mainMap.put(categoryName, imagesList);
                            } else {
                              mainMap.put(categoryKey, imagesList);
                            }
                            continue;
                          }

                          // Getting image list from categoryJsonObj
                          for (Iterator<String> imageIterator = imagesListJsonObj.keys();
                              imageIterator.hasNext(); ) {
                            final String imageKey = imageIterator.next();
                            final JSONObject imageJsonObj =
                                imagesListJsonObj.optJSONObject(imageKey);
                            if (imageJsonObj == null) {
                              continue;
                            }
                            final ImageDetails imageDetails =
                                gson.fromJson(imageJsonObj.toString(), ImageDetails.class);
                            if (imageJsonObj.has("user_detail")) {
                              JSONObject userDetailsJsonObj =
                                  imageJsonObj.optJSONObject("user_detail");
                              if (userDetailsJsonObj != null) {
                                UserDetails userDetails =
                                    gson.fromJson(userDetailsJsonObj.toString(), UserDetails.class);
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
                  .observeOn(AndroidSchedulers.mainThread())
                  .subscribe(stringListMap -> allVideosList.setValue(stringListMap));
            }
          },
          new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
              Log.e(TAG, "allVideosRequest: onErrorResponse: ", error.getCause());
            }
          });

  private JsonObjectRequest allUserRequest =
      new JsonObjectRequest(
          Request.Method.GET,
          ApiEndpoints.ALL_USERS,
          null,
          new Response.Listener<JSONObject>() {
            @SuppressLint("CheckResult")
            @Override
            public void onResponse(JSONObject response) {
              Log.d(TAG, "allUserRequest: onResponse: data successfully retrieved");
              Single.fromCallable(
                      () -> {
                        List<UserDetails> userDetailsList = new ArrayList<>();
                        Gson gson =
                            new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
                        for (Iterator<String> rootIterator = response.keys();
                            rootIterator.hasNext(); ) {
                          final String rootChildKey = rootIterator.next();
                          final JSONObject rootChildJsonObj = response.optJSONObject(rootChildKey);
                          if (rootChildJsonObj == null) continue;

                          try {
                            UserDetails userDetails =
                                gson.fromJson(rootChildJsonObj.toString(), UserDetails.class);
                            userDetailsList.add(userDetails);
                          } catch (JsonSyntaxException e) {
                            e.printStackTrace();
                          }
                        }
                        return userDetailsList;
                      })
                  .subscribeOn(Schedulers.computation())
                  .observeOn(AndroidSchedulers.mainThread())
                  .subscribe(userDetails -> allUsersList.setValue(userDetails));
            }
          },
          new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
              Log.e(TAG, "allUserRequest: onErrorResponse: ", error.getCause());
            }
          });

  private JsonObjectRequest allProductsRequest =
      new JsonObjectRequest(
          Request.Method.GET,
          ApiEndpoints.ALL_PRODUCTS,
          null,
          response -> {
            Log.d(TAG, "allProductsRequest: onResponse: data successfully retrieved");
            Single.fromCallable(
                    () -> {
                      Map<String, List<ProductDetails>> mainMap = new HashMap<>();
                      Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

                      for (Iterator<String> categoryIterator = response.keys();
                          categoryIterator.hasNext(); ) {
                        final String categoryKey = categoryIterator.next();
                        final JSONObject categoryJsonObj = response.optJSONObject(categoryKey);
                        final List<ProductDetails> productsList = new ArrayList<>();
                        String categoryName = null;

                        if (categoryJsonObj.has("name")) {
                          categoryName = categoryJsonObj.optString("name");
                        }

                        // If categoryJsonObj is null then no need to perform any other task just
                        // return
                        if (categoryJsonObj == null) {
                          if (categoryName != null && !categoryName.isEmpty()) {
                            mainMap.put(categoryName, productsList);
                          } else {
                            mainMap.put(categoryKey, productsList);
                          }
                          continue;
                        }

                        // if response don't have pro_detail then no need to do anything just return
                        JSONObject productsListJsonObj =
                            categoryJsonObj.optJSONObject("pro_detail");
                        if (productsListJsonObj == null) {
                          if (categoryName != null && !categoryName.isEmpty()) {
                            mainMap.put(categoryName, productsList);
                          } else {
                            mainMap.put(categoryKey, productsList);
                          }
                          continue;
                        }

                        // Getting image list from categoryJsonObj
                        for (Iterator<String> productIterator = productsListJsonObj.keys();
                            productIterator.hasNext(); ) {
                          final String productKey = productIterator.next();
                          final JSONObject productJsonObj =
                              productsListJsonObj.optJSONObject(productKey);
                          if (productJsonObj == null) {
                            continue;
                          }
                          final ProductDetails productDetails =
                              gson.fromJson(productJsonObj.toString(), ProductDetails.class);
                          if (productJsonObj.has("user_detail")) {
                            JSONObject userDetailsJsonObj =
                                productJsonObj.optJSONObject("user_detail");
                            if (userDetailsJsonObj != null) {
                              UserDetails userDetails =
                                  gson.fromJson(userDetailsJsonObj.toString(), UserDetails.class);
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
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    stringListMap -> {
                      allProductsList.setValue(stringListMap);
                    });
          },
          error -> {
            Log.e(TAG, "allUserRequest: onErrorResponse: ", error.getCause());
          });

  private JsonObjectRequest allProvidesRequest =
      new JsonObjectRequest(
          Request.Method.GET,
          ApiEndpoints.ALL_PROVIDERS,
          null,
          response -> {
            Log.d(TAG, "allProductsRequest: onResponse: data successfully retrieved");
            Single.fromCallable(
                    () -> {
                      Map<String, List<ProductDetails>> mainMap = new HashMap<>();
                      Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

                      for (Iterator<String> categoryIterator = response.keys();
                          categoryIterator.hasNext(); ) {
                        final String categoryKey = categoryIterator.next();
                        final JSONObject categoryJsonObj = response.optJSONObject(categoryKey);
                        final List<ProductDetails> productsList = new ArrayList<>();
                        String categoryName = null;

                        if (categoryJsonObj.has("name")) {
                          categoryName = categoryJsonObj.optString("name");
                        }

                        // If categoryJsonObj is null then no need to perform any other task just
                        // return
                        if (categoryJsonObj == null) {
                          if (categoryName != null && !categoryName.isEmpty()) {
                            mainMap.put(categoryName, productsList);
                          } else {
                            mainMap.put(categoryKey, productsList);
                          }
                          continue;
                        }

                        // if response don't have pro_detail then no need to do anything just return
                        JSONObject productsListJsonObj =
                            categoryJsonObj.optJSONObject("pro_detail");
                        if (productsListJsonObj == null) {
                          if (categoryName != null && !categoryName.isEmpty()) {
                            mainMap.put(categoryName, productsList);
                          } else {
                            mainMap.put(categoryKey, productsList);
                          }
                          continue;
                        }

                        // Getting image list from categoryJsonObj
                        for (Iterator<String> productIterator = productsListJsonObj.keys();
                            productIterator.hasNext(); ) {
                          final String productKey = productIterator.next();
                          final JSONObject productJsonObj =
                              productsListJsonObj.optJSONObject(productKey);
                          if (productJsonObj == null) {
                            continue;
                          }
                          final ProductDetails productDetails =
                              gson.fromJson(productJsonObj.toString(), ProductDetails.class);
                          if (productJsonObj.has("user_detail")) {
                            JSONObject userDetailsJsonObj =
                                productJsonObj.optJSONObject("user_detail");
                            if (userDetailsJsonObj != null) {
                              UserDetails userDetails =
                                  gson.fromJson(userDetailsJsonObj.toString(), UserDetails.class);
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
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    stringListMap -> {
                      allProvidesList.setValue(stringListMap);
                    });
          },
          error -> {
            Log.e(TAG, "allProvidesRequest: onErrorResponse: ", error.getCause());
          });

  private JsonObjectRequest allDemandsRequest =
      new JsonObjectRequest(
          Request.Method.GET,
          ApiEndpoints.ALL_PROVIDERS,
          null,
          response -> {
            Log.d(TAG, "allDemandsRequest: onResponse: data successfully retrieved");
            Single.fromCallable(
                    () -> {
                      Map<String, List<ProductDetails>> mainMap = new HashMap<>();
                      Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

                      for (Iterator<String> categoryIterator = response.keys();
                          categoryIterator.hasNext(); ) {
                        final String categoryKey = categoryIterator.next();
                        final JSONObject categoryJsonObj = response.optJSONObject(categoryKey);
                        final List<ProductDetails> productsList = new ArrayList<>();
                        String categoryName = null;

                        if (categoryJsonObj.has("name")) {
                          categoryName = categoryJsonObj.optString("name");
                        }

                        // If categoryJsonObj is null then no need to perform any other task just
                        // return
                        if (categoryJsonObj == null) {
                          if (categoryName != null && !categoryName.isEmpty()) {
                            mainMap.put(categoryName, productsList);
                          } else {
                            mainMap.put(categoryKey, productsList);
                          }
                          continue;
                        }

                        // if response don't have pro_detail then no need to do anything just return
                        JSONObject productsListJsonObj =
                            categoryJsonObj.optJSONObject("pro_detail");
                        if (productsListJsonObj == null) {
                          if (categoryName != null && !categoryName.isEmpty()) {
                            mainMap.put(categoryName, productsList);
                          } else {
                            mainMap.put(categoryKey, productsList);
                          }
                          continue;
                        }

                        // Getting image list from categoryJsonObj
                        for (Iterator<String> productIterator = productsListJsonObj.keys();
                            productIterator.hasNext(); ) {
                          final String productKey = productIterator.next();
                          final JSONObject productJsonObj =
                              productsListJsonObj.optJSONObject(productKey);
                          if (productJsonObj == null) {
                            continue;
                          }
                          final ProductDetails productDetails =
                              gson.fromJson(productJsonObj.toString(), ProductDetails.class);
                          if (productJsonObj.has("user_detail")) {
                            JSONObject userDetailsJsonObj =
                                productJsonObj.optJSONObject("user_detail");
                            if (userDetailsJsonObj != null) {
                              UserDetails userDetails =
                                  gson.fromJson(userDetailsJsonObj.toString(), UserDetails.class);
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
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(allDemandsList::setValue);
          },
          error -> {
            Log.e(TAG, "allDemandsRequest: onErrorResponse: ", error.getCause());
          });

  public MainViewModel(@NonNull Application application) {
    super(application);

    VolleyUtil.getInstance(getApplication()).addRequest(topSliderImageRequest);
    VolleyUtil.getInstance(getApplication()).addRequest(latestPhotosRequest);
    VolleyUtil.getInstance(getApplication()).addRequest(latestVideosRequest);
    VolleyUtil.getInstance(getApplication()).addRequest(smartMembersRequest);
    VolleyUtil.getInstance(getApplication()).addRequest(latestProductsRequest);
    VolleyUtil.getInstance(getApplication()).addRequest(provideAndDemandRequest);
    Log.d(TAG, "MainViewModel: " + "request added");
    Log.d(TAG, "MainViewModel: Thread Name" + Thread.currentThread().getName());
  }

  /** **************************** LIVE DATA GET METHODS *************************** */
  public LiveData<List<TopSliderImageModel>> getTopSliderImages() {
    return topSliderImages;
  }

  public LiveData<List<ImageDetails>> getLatesPhotosImages() {
    return latestPhotosImages;
  }

  public LiveData<List<VideoDetails>> getLatestVideos() {
    return latestVideosImages;
  }

  public LiveData<List<UserDetails>> getSmartMembers() {
    return smartMembers;
  }

  public LiveData<List<ProductDetails>> getLatestProducts() {
    return latestProducts;
  }

  public LiveData<List<ProductDetails>> getProvideAndDemands() {
    return provideAndDemands;
  }

  public LiveData<Map<String, List<ImageDetails>>> getAllImageList() {
    return allImageList;
  }

  public LiveData<Map<String, List<ImageDetails>>> getAllVideoList() {
    return allVideosList;
  }

  public LiveData<List<UserDetails>> getAllUsersList() {
    return allUsersList;
  }

  public LiveData<Map<String, List<ProductDetails>>> getAllProducts() {
    return allProductsList;
  }

  public LiveData<Map<String, List<ProductDetails>>> getAllProviders() {
    return allProvidesList;
  }

  public LiveData<Map<String, List<ProductDetails>>> getAllDemands() {
    return allDemandsList;
  }

  /** **************************** REFRESH DATA METHODS *************************** */
  public void loadAllImagesData() {
    VolleyUtil.getInstance(getApplication()).addRequest(allImagesRequest);
  }

  public void loadAllVideosData() {
    VolleyUtil.getInstance(getApplication()).addRequest(allVideosRequest);
  }

  public void loadAllUsersData() {
    VolleyUtil.getInstance(getApplication()).addRequest(allUserRequest);
  }

  public void loadAllProductsData() {
    VolleyUtil.getInstance(getApplication()).addRequest(allProductsRequest);
  }

  public void loadAllProvidesData() {
    VolleyUtil.getInstance(getApplication()).addRequest(allProvidesRequest);
  }

  public void loadAllDemandsData() {
    VolleyUtil.getInstance(getApplication()).addRequest(allDemandsRequest);
  }
}
