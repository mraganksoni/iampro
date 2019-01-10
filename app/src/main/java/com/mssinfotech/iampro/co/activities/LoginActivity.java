package com.mssinfotech.iampro.co.activities;

import android.support.transition.Transition;
import android.support.transition.TransitionInflater;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.mssinfotech.iampro.co.R;
import com.mssinfotech.iampro.co.fragments.LoginFragment;
import com.mssinfotech.iampro.co.fragments.LoginHomeFragment;
import com.mssinfotech.iampro.co.fragments.LoginHomeFragment.LoginHomeFragmentCallback;
import com.mssinfotech.iampro.co.fragments.SignupFragment;

public class LoginActivity extends AppCompatActivity implements LoginHomeFragmentCallback{

  /* ***********************************
   * View References
   *********************************** */
  private Button btnLogin;
  private Button btnSignup;

  /* ***********************************
   * Fragment Instances
   *********************************** */
  LoginHomeFragment homeFragment;
  LoginFragment loginFragment;
  SignupFragment signupFragment;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    showLoginHomeFragment();
    initViews();
    hookViews();
  }

  private void initViews() {

  }

  private void hookViews() {

  }

  @Override
  public void onBackPressed() {
    FragmentManager fragmentManager = getSupportFragmentManager();
    if (fragmentManager.getBackStackEntryCount() != 0) {
      fragmentManager.popBackStack();
      return;
    }
    super.onBackPressed();
  }

  private void showLoginHomeFragment() {
    if (homeFragment == null) {
      homeFragment = new LoginHomeFragment();
      homeFragment.setCallback(this);
    }

    FragmentManager fragmentManager = getSupportFragmentManager();
    fragmentManager.beginTransaction()
        .replace(android.R.id.content, homeFragment, "HOME")
        .commit();
  }

  private void showLoginFragment() {
    if (loginFragment == null) {
      loginFragment = new LoginFragment();
      Transition modeTransition = TransitionInflater.from(this).inflateTransition(android.R.transition.move);
      Transition fadeTransition = TransitionInflater.from(this).inflateTransition(android.R.transition.fade);
      loginFragment.setSharedElementEnterTransition(modeTransition);
      loginFragment.setSharedElementReturnTransition(modeTransition);
      loginFragment.setEnterTransition(fadeTransition);
      loginFragment.setExitTransition(fadeTransition);
    }
    final View sharedImageView = homeFragment.getView().findViewById(R.id.imageView);
    final View sharedLoginButton = homeFragment.getView().findViewById(R.id.btnLogin);
    FragmentManager fragmentManager = getSupportFragmentManager();
    fragmentManager.beginTransaction()
        .replace(android.R.id.content, loginFragment, "LOGIN")
        .addSharedElement(sharedImageView, "image")
        .addSharedElement(sharedLoginButton, "buttonLogin")
        .setTransition(android.R.transition.move)
        .addToBackStack("LOGIN")
        .commit();
  }

  private void showSignupFragment() {
    if (signupFragment == null) {
      signupFragment = new SignupFragment();
      Transition modeTransition = TransitionInflater.from(this).inflateTransition(android.R.transition.move);
      Transition fadeTransition = TransitionInflater.from(this).inflateTransition(android.R.transition.fade);
      signupFragment.setSharedElementEnterTransition(modeTransition);
      signupFragment.setSharedElementReturnTransition(modeTransition);
      signupFragment.setEnterTransition(fadeTransition);
      signupFragment.setExitTransition(fadeTransition);

    }
    FragmentManager fragmentManager = getSupportFragmentManager();
    fragmentManager.beginTransaction()
        .replace(android.R.id.content, signupFragment, "SIGNUP")
        .addToBackStack("SIGNUP")
        .commit();
  }

  @Override
  public void onLoginButtonClicked() {
    showLoginFragment();
  }

  @Override
  public void onSignupButtonClicked() {
    showSignupFragment();
  }
}
