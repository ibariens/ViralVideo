package com.divimove.ibariens.viral_videos;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.divimove.ibariens.viral_videos.helpers.DbVideo;
import com.divimove.ibariens.viral_videos.helpers.ViralFetcher;
import com.divimove.ibariens.viral_videos.models.Video;

import java.util.ArrayList;



public class HomeActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        new ViralFetcher(HomeActivity.this).execute();
        DbVideo db = new DbVideo(this);
        ArrayList<Video> videos = db.getAllVideos();
        int new_videos = db.getNewVideos();
        int total_videos = db.getTotalVideos();
        int watched_videos = db.getWatchedVideos().size();

        TextView total_videos_view = (TextView) findViewById(R.id.total_videos);
        total_videos_view.setText(Integer.toString(total_videos));

        TextView total_new_view = (TextView) findViewById(R.id.total_new);
        total_new_view.setText(Integer.toString(new_videos));

        TextView watched_videos_view = (TextView) findViewById(R.id.total_watched);
        watched_videos_view.setText(Integer.toString(total_videos - watched_videos));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void sendMessage(View view) {

        Intent intent = new Intent(this, ViralIndexGridActivity.class);
        startActivity(intent);
    }



}
