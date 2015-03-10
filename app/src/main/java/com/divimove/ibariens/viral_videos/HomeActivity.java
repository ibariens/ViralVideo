package com.divimove.ibariens.viral_videos;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


import com.divimove.ibariens.viral_videos.helpers.DbVideo;
import com.divimove.ibariens.viral_videos.helpers.ViralFetcher;
import com.divimove.ibariens.viral_videos.models.Video;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class HomeActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        new ViralFetcher(HomeActivity.this).execute();
        DbVideo db = new DbVideo(this);

        Video video = new Video();
        video.setChannel_id("sdfsdf");
        video.setVideo_title("Sdfsdf");
        video.setWatched(false);
        video.setIs_new(true);

        DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        try {
            Date date = format.parse("1984-12-06");
            video.setPublished_at(date);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        video.setView_count(23423424243L);

        db.addVideo(video);
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

        Intent intent = new Intent(this, ViralIndexActivity.class);
        startActivity(intent);
    }
}
