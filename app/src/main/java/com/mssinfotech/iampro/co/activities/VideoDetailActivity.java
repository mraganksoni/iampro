package com.mssinfotech.iampro.co.activities;

import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.mssinfotech.iampro.co.R;

public class VideoDetailActivity extends AppCompatActivity {
  public static final String EXTRA_VIDEO_URI = "extra_video_uri";
  public static final String EXTRA_TRANSITION_NAME = "extra_transition_name";
  public static final String EXTRA_WINDOW_TITLE = "extra_window_title";

  private Toolbar toolbar;

  private VideoView vvMain;
  private MediaController mediaController;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_video_detail);

    toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setDisplayShowHomeEnabled(true);

    initViews();

    final Intent intent = getIntent();

    // Check if intent have window title
    if (intent.hasExtra(EXTRA_WINDOW_TITLE)) {
      final String windowTitle = intent.getStringExtra(EXTRA_WINDOW_TITLE);
      if (windowTitle != null && !windowTitle.isEmpty()) {
        getSupportActionBar().setTitle(windowTitle);
      }
    }


    if (intent.hasExtra(EXTRA_VIDEO_URI)) {
      if (intent.hasExtra(EXTRA_TRANSITION_NAME)) {
        vvMain.setTransitionName(intent.getStringExtra(EXTRA_TRANSITION_NAME));
      }
      String videoUrl = intent.getStringExtra(EXTRA_VIDEO_URI);
      initMediaPlayer(Uri.parse(videoUrl));
    }
  }

  private void initViews() {
    vvMain = findViewById(R.id.vvMain);
    mediaController = new MediaController(this);
    vvMain.setMediaController(mediaController);
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    releasePlayer();
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.video_detail_act_main, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    final int id = item.getItemId();

    switch (id) {
      case R.id.action_download:
        Toast.makeText(this, "download clicked", Toast.LENGTH_SHORT).show();
        return true;
      default:
        return false;
    }
  }

  private void initMediaPlayer(Uri uri) {
    vvMain.setVideoURI(uri);
    mediaController.show(5000);
    vvMain.start();
  }

  private void releasePlayer() {
    vvMain.stopPlayback();
  }
}
