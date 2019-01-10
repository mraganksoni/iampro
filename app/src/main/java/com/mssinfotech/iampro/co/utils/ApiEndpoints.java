package com.mssinfotech.iampro.co.utils;

public interface ApiEndpoints {
    String ROOT = "http://www.iampro.co/";


    String TOP_SLIDER_IMAGES = ROOT + "api/index.php?type=get_slider&name=TOP_SLIDER";
    String LATEST_PHOTOS = ROOT + "api/dashboard.php?type=all_item&name=image";
    String LATEST_VIDEOS = ROOT + "api/dashboard.php?type=all_item&name=video";
    String SMART_MEMBERS = ROOT + "api/dashboard.php?type=getAllUser&name=6";
    String LATEST_PRODUCTS = ROOT + "api/dashboard.php?type=all_product&name=name";
    String PROVIDE_AND_DEMAND = ROOT + "api/dashboard.php?type=all_product_classified";
    String ALL_IMAGES = ROOT + "api/search.php?type=search_all_items&search_type=IMAGE&search_data=";
    String ALL_VIDEOS = ROOT + "api/search.php?type=search_all_items&search_type=VIDEO&search_data=";
    String ALL_USERS = ROOT + "api/search.php?type=search_all_friends&search_type=FRIEND&search_data=";
    String ALL_PRODUCTS = ROOT + "api/search.php?type=search_all_items&search_type=PRODUCT&search_data=";
    String ALL_PROVIDERS = ROOT + "api/search.php?type=search_all_items&search_type=PROVIDE&search_data=";

    String SUB_CAT_IMAGES = ROOT + "api/search.php?type=get_category&name=IMAGE";
    String SUB_CAT_VIDEO = ROOT + "api/search.php?type=get_category&name=VIDEO";
    String SUB_CAT_FRIEND = ROOT + "api/search.php?type=get_category&name=FRIEND";
    String SUB_CAT_PRODUCT = ROOT + "api/search.php?type=get_category&name=PRODUCT";
    String SUB_CAT_PROVIDE = ROOT + "api/search.php?type=get_category&name=PROVIDE";
    String SUB_CAT_DEMAND = ROOT + "api/search.php?type=get_category&name=DEMAND";

    String SLIDER_IMAGE = ROOT + "api/search.php?type=search_slider&search_type=IMAGE_SLIDER";
    String SLIDER_VIDEO = ROOT + "api/search.php?type=search_slider&search_type=VIDEO_SIDE_SLIDER";
    String SLIDER_FRIEND= ROOT + "api/search.php?type=search_slider&search_type=FRIEND_SLIDER";
    String SLIDER_PRODUCT = ROOT + "api/search.php?type=search_slider&search_type=PRODUCT_SLIDER";
    String SLIDER_PROVIDE = ROOT + "api/search.php?type=search_slider&search_type=PROVIDE_SLIDER";
    String SLIDER_DEMAND= ROOT + "api/search.php?type=search_slider&search_type=DEMAND_SLIDER";

    String DIR_SLIDER = ROOT + "uploads/slider/";
    String DIR_PHOTOS = ROOT + "uploads/album/";
    String DIR_VIDEOS = ROOT + "uploads/video/";
    String DIR_PRODUCTS = ROOT + "uploads/product/";
    String DIR_AVATAR = ROOT + "uploads/avatar/";
    String DIR_VIDEOS_THUMBS = ROOT + "uploads/v_image/";
}
