package com.mssinfotech.iampro.co.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import android.view.View;
import android.widget.TextView;
import com.mssinfotech.iampro.co.events.NewLoginEvent;
import com.mssinfotech.iampro.co.models.CurrentUser;
import com.mssinfotech.iampro.co.utils.ApiEndpoints;
import com.mssinfotech.iampro.co.utils.LoginPreferencesConstants;
import com.mssinfotech.iampro.co.viewmodels.LoginViewModel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.leakcanary.RefWatcher;

import com.mssinfotech.iampro.co.MainApplication;
import com.mssinfotech.iampro.co.R;
import com.mssinfotech.iampro.co.fragments.CartFragment;
import com.mssinfotech.iampro.co.fragments.ChatFragment;
import com.mssinfotech.iampro.co.fragments.HomeFragment;
import com.mssinfotech.iampro.co.fragments.SearchFragment;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Picasso.LoadedFrom;
import com.squareup.picasso.Target;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class MainActivity extends AppCompatActivity
    implements NavigationView.OnNavigationItemSelectedListener {
  private static final String TAG = "MainActivity";

  /* *************************
   * OTHER REFERENCES
   ************************* */
  SharedPreferences loginShardSharedPreferences;

  /* **************************
   * VIEW REFERENCES
   ************************** */
  private BottomNavigationView botNavView;

  private DrawerLayout drawerLayout;
  private NavigationView navigationView;

  /* **************************
   * FRAGMENTS
   ************************ */
  private SearchFragment searchFragment;
  private HomeFragment homeFragment;
  private CartFragment cartFragment;
  private ChatFragment chatFragment;

  /* *************************
   * LISTENERS
   ************************* */
  private BottomNavigationView.OnNavigationItemSelectedListener botNavItemSelectListener =
      new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
          final int id = item.getItemId();
          Log.d(TAG, "onNavigationItemSelected: " + item.getTitle());

          switch (id) {
            case R.id.bot_nav_menu:
              drawerLayout.openDrawer(GravityCompat.START);
              return false;
            case R.id.bot_nav_search:
              showSearchFragment();
              return true;
            case R.id.bot_nav_home:
              showHomeFragment();
              return true;
            case R.id.bot_nav_user:
              showCartFragment();
              return true;
            case R.id.bot_nav_chat:
              showChatFragment();
              return true;
          }

          return true;
        }
      };

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    EventBus.getDefault().register(this);

    getSupportActionBar().setLogo(R.mipmap.ic_launcher);
    getSupportActionBar().setDisplayUseLogoEnabled(true);

    initViews();
    hookViews();
  }

  private void initViews() {
    navigationView = findViewById(R.id.nav_view);
    botNavView = findViewById(R.id.bot_nav_main);
    drawerLayout = findViewById(R.id.drawer_layout);

    loginShardSharedPreferences =
        getSharedPreferences(LoginPreferencesConstants.PREF_NAME, MODE_PRIVATE);
  }

  private void hookViews() {
    navigationView.setNavigationItemSelectedListener(this);

    botNavView.setOnNavigationItemSelectedListener(botNavItemSelectListener);
    botNavView.setSelectedItemId(R.id.bot_nav_home);

    // Change header and menu of navView user already loggedin
    if (loginShardSharedPreferences.contains(LoginPreferencesConstants.KEY_USER_INFO)) {
      changeToLoginHeaderMenus();
    }
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    EventBus.getDefault().unregister(this);
    // Check if MainActivity have any memory leaks
    RefWatcher refWatcher = MainApplication.getRefWatcher(this);
    refWatcher.watch(this);
  }

  @Override
  public void onBackPressed() {
    if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
      drawerLayout.closeDrawer(GravityCompat.START);
      return;
    }
    if (searchFragment != null && searchFragment.isVisible() && searchFragment.isContentShowing) {
      searchFragment.hideContent();
      return;
    }
    super.onBackPressed();
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();

    return super.onOptionsItemSelected(item);
  }

  @SuppressWarnings("StatementWithEmptyBody")
  @Override
  public boolean onNavigationItemSelected(MenuItem item) {
    // Handle navigation view item clicks here.
    int id = item.getItemId();
    Intent intent;
    // Logged in Menus handler
    switch (id) {
      case R.id.nav_login:
        intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        break;
      case R.id.nav_register:
        intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        break;
      case R.id.nav_forget:
        intent = new Intent(this, ForgetActivity.class);
        startActivity(intent);
        break;
      case R.id.nav_dashboard:
        if (!homeFragment.isVisible()) {
          showHomeFragment();
        }
        break;
      case R.id.nav_my_profile:
        showMyProfile();
        break;
      case R.id.nav_update_profile:
        showUpdateProfile();
        break;
      case R.id.nav_change_password:
        showChangePassword();
        break;
      case R.id.nav_join_friend:
        showJoinFriend();
        break;
      case R.id.nav_image_gallery:
        showImageGallery();
        break;
      case R.id.nav_logout:
        new AlertDialog.Builder(this)
            .setTitle("Logout")
            .setMessage("Are you sure")
            .setNegativeButton("No", null)
            .setPositiveButton(
                "Logout",
                (dialog, which) -> {
                  loginShardSharedPreferences
                      .edit()
                      .remove(LoginPreferencesConstants.KEY_USER_INFO)
                      .apply();
                  changeToDefaultHeaderMenus();
                  showHomeFragment();
                })
            .show();
        break;
    }

    drawerLayout.closeDrawer(GravityCompat.START);
    return true;
  }

  @Subscribe
  public void onNewLogin(NewLoginEvent event) {
    if (event.isLoggedIn) {
      changeToLoginHeaderMenus();
    } else {
      changeToDefaultHeaderMenus();
    }
  }

  // <editor-fold desc="Fragment show hide methods">
  private void showSearchFragment() {
    // If fragment isn't initialized then initialize it
    if (searchFragment == null) {
      searchFragment = new SearchFragment();
    }

    // If current Fragment is already visible on screen then no need to do anything
    if (searchFragment.isVisible()) {
      Log.d(TAG, "showSearchFragment: Currently showing this fragment, no need to change");
      return;
    }

    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
    // Check if fragment is already added to fragment manager
    if (!searchFragment.isAdded()) {
      fragmentTransaction
          .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
          .add(R.id.fragmentContainer, searchFragment)
          .show(searchFragment);
      hideAllFragments();
    } else {
      fragmentTransaction
          .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
          .show(searchFragment);
      hideAllFragments();
    }
    fragmentTransaction.commit();
  }

  private void showHomeFragment() {
    // If fragment isn't initialized then initialize it
    if (homeFragment == null) {
      this.homeFragment = new HomeFragment();
    }

    // If current Fragment is already visible on screen then no need to do anything
    if (homeFragment.isVisible()) {
      Log.d(TAG, "showHomeFragment: Currently showing this fragment, no need to change");
      return;
    }

    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
    // Check if fragment is already added to fragment manager
    if (!homeFragment.isAdded()) {
      fragmentTransaction
          .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
          .add(R.id.fragmentContainer, homeFragment)
          .show(homeFragment);
      hideAllFragments();
    } else {
      fragmentTransaction
          .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
          .show(homeFragment);
      hideAllFragments();
    }
    fragmentTransaction.commit();
  }

  private void showCartFragment() {
    // If fragment isn't initialized then initialize it
    if (cartFragment == null) {
      this.cartFragment = new CartFragment();
    }

    // If current Fragment is already visible on screen then no need to do anything
    if (cartFragment.isVisible()) {
      Log.d(TAG, "showCartFragment: Currently showing this fragment, no need to change");
      return;
    }

    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
    // Check if fragment is already added to fragment manager
    if (!cartFragment.isAdded()) {
      fragmentTransaction
          .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
          .add(R.id.fragmentContainer, cartFragment)
          .show(cartFragment);
      hideAllFragments();
    } else {
      fragmentTransaction
          .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
          .show(cartFragment);
      hideAllFragments();
    }
    fragmentTransaction.commit();
  }

  private void showChatFragment() {
    // If fragment isn't initialized then initialize it
    if (chatFragment == null) {
      this.chatFragment = new ChatFragment();
    }

    // If current Fragment is already visible on screen then no need to do anything
    if (chatFragment.isVisible()) {
      Log.d(TAG, "showChatFragment: Currently showing this fragment, no need to change");
      return;
    }

    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
    // Check if fragment is already added to fragment manager
    if (!chatFragment.isAdded()) {
      fragmentTransaction
          .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
          .add(R.id.fragmentContainer, chatFragment)
          .show(chatFragment);
      hideAllFragments();

    } else {
      fragmentTransaction
          .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
          .show(chatFragment);
      hideAllFragments();
    }
    fragmentTransaction.commit();
  }

  private void hideAllFragments() {
    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
    if (searchFragment != null) {
      if (searchFragment.isVisible()) {
        fragmentTransaction.hide(searchFragment);
        Log.d(TAG, "hideAllFragments: searchFragment hided");
      }
    }
    if (homeFragment != null) {
      if (homeFragment.isVisible()) {
        fragmentTransaction.hide(homeFragment);
        Log.d(TAG, "hideAllFragments: homeFragment hided");
      }
    }
    if (cartFragment != null) {
      if (cartFragment.isVisible()) {
        fragmentTransaction.hide(cartFragment);
        Log.d(TAG, "hideAllFragments: cartFragment hided");
      }
    }
    if (chatFragment != null) {
      if (chatFragment.isVisible()) {
        fragmentTransaction.hide(chatFragment);
        Log.d(TAG, "hideAllFragments: chatFragment hided");
      }
    }
    fragmentTransaction.commit();
  }
  // </editor-fold>

  private void changeToLoginHeaderMenus() {
    // Change navView menu
    navigationView.getMenu().clear();
    navigationView.inflateMenu(R.menu.nav_main_menu_loggedin);

    // Change navView Header
    navigationView.removeHeaderView(navigationView.getHeaderView(0));
    navigationView.inflateHeaderView(R.layout.nav_header_main_loggedin);

    final String userJsonStr =
        loginShardSharedPreferences.getString(LoginPreferencesConstants.KEY_USER_INFO, "");
    if (!userJsonStr.isEmpty()) {
      Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
      try {
        CurrentUser currentUser = gson.fromJson(userJsonStr, CurrentUser.class);
        View headerView = navigationView.getHeaderView(0);
        CircularImageView civAvatar = headerView.findViewById(R.id.civAvatar);
        TextView tvUsername = headerView.findViewById(R.id.tvUsername);

        tvUsername.setText(currentUser.getUsername());
        Picasso.get()
            .load(ApiEndpoints.DIR_AVATAR + currentUser.getBannerImage())
            .into(
                new Target() {
                  @Override
                  public void onBitmapLoaded(Bitmap bitmap, LoadedFrom from) {
                    headerView.setBackground(new BitmapDrawable(getResources(), bitmap));
                  }

                  @Override
                  public void onBitmapFailed(Exception e, Drawable errorDrawable) {}

                  @Override
                  public void onPrepareLoad(Drawable placeHolderDrawable) {}
                });
        Picasso.get()
            .load(ApiEndpoints.DIR_AVATAR + currentUser.getAvatar())
            .fit()
            .centerInside()
            .into(civAvatar);
      } catch (JsonSyntaxException e) {
        e.printStackTrace();
      }
    }
  }

  private void changeToDefaultHeaderMenus() {
    // Change navView menu
    navigationView.getMenu().clear();
    navigationView.inflateMenu(R.menu.nav_main_menu);

    // Change navView Header
    navigationView.removeHeaderView(navigationView.getHeaderView(0));
    navigationView.inflateHeaderView(R.layout.nav_header_main);
  }

  private void showMyProfile() {
    Intent intent = new Intent(this, UserDetailsActivity.class);

    intent.setAction(UserDetailsActivity.ACTION_MY_PROFILE);
    startActivity(intent);
  }

  private void showUpdateProfile() {
    Intent intent = new Intent(this, UpdateProfileActivity.class);
    intent.setAction(UpdateProfileActivity.ACTION_UPDATE_PROFILE);
    startActivity(intent);
  }

  private void showChangePassword() {
    Intent intent = new Intent(this, ChangePasswordActivity.class);
    startActivity(intent);
  }

  private void showJoinFriend() {
    Intent intent = new Intent(this, JoinFriendActivity.class);
    startActivity(intent);
  }

  private void showImageGallery() {
    Intent intent = new Intent(this, ImageGalleryActivity.class);
    intent.setAction(ImageGalleryActivity.ACTION_IMAGE_GALLERY);
    startActivity(intent);
  }
}
