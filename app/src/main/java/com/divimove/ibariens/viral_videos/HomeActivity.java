package com.divimove.ibariens.viral_videos;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.divimove.ibariens.viral_videos.helpers.DbVideo;
import com.divimove.ibariens.viral_videos.helpers.ViralFetcher;
import com.divimove.ibariens.viral_videos.models.Video;

import java.util.ArrayList;

import javax.xml.datatype.Duration;


public class HomeActivity extends ActionBarActivity {

    private int total_videos = 0;
    private int new_videos = 0;
    private int unwatched_videos = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        DbVideo db = new DbVideo(this);
        ArrayList<Video> videos = db.getAllVideos();
        total_videos =  db.getAllVideos().size();
        new_videos =  db.getNewVideos().size();
        unwatched_videos =  db.GetUnwatchedVideos().size();

        UpdateLinks();

      db.close();
    }



    @Override
    public void onRestart()
    {
        super.onRestart();
        DbVideo db = new DbVideo(this);
        ArrayList<Video> videos = db.getAllVideos();
        total_videos =  db.getAllVideos().size();
        new_videos =  db.getNewVideos().size();
        unwatched_videos =  db.GetUnwatchedVideos().size();

        UpdateLinks();
        db.close();
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


    public void showVideos(View view, String mode) {
         switch (mode) {
             case "all":
                 if (total_videos != 0 ) {
                     startActivityGrid(mode);
                 }
                 break;
             case "new":
                 if (new_videos != 0 ) {
                     startActivityGrid(mode);
                 }
                 break;
             case "unwatched":
                 if (unwatched_videos != 0 ) {
                     startActivityGrid(mode);
                 }
         }
    }



    private void startActivityGrid(String mode){
        Intent intent = new Intent(this, ViralIndexGridActivity.class);
        intent.putExtra("mode", mode);
        startActivity(intent);
    }


    private void UpdateLinks() {
        TextView total_videos_view = (TextView) findViewById(R.id.total_videos);
        if (total_videos == 0) {
            total_videos_view.setText(Integer.toString(total_videos));
        }
        else {
            total_videos_view.setText(Integer.toString(total_videos) + " (click to show)");
            total_videos_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showVideos(v, "all");
                }
            });
        }

        TextView total_new_view = (TextView) findViewById(R.id.total_new);
        if (new_videos == 0) {
            total_new_view.setText(Integer.toString(new_videos));
        }
        else {
            total_new_view.setText(Integer.toString(new_videos) + " (click to show)");
            total_new_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showVideos(v, "new");
                }
            });
        }



        TextView watched_videos_view = (TextView) findViewById(R.id.total_watched);
        if (unwatched_videos == 0) {
            watched_videos_view.setText(Integer.toString(unwatched_videos));
        }
        else {
            watched_videos_view.setText(Integer.toString(unwatched_videos) + " (click to show)");
            watched_videos_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showVideos(v, "unwatched");
                }
            });
        }
    }

    public void fetchVirals(View view) {
      new ViralFetcher(HomeActivity.this).execute();
    }
}
