package com.divimove.ibariens.viral_videos;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.divimove.ibariens.viral_videos.adapters.GridViewAdapter;
import com.divimove.ibariens.viral_videos.fragments.Video;
import com.divimove.ibariens.viral_videos.helpers.DbVideo;
import com.divimove.ibariens.viral_videos.models.VideoEntry;
import com.google.android.youtube.player.YouTubeApiServiceUtil;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;

import java.util.ArrayList;
import java.util.List;


public class ViralIndexGridActivity extends Activity implements YouTubePlayer.OnFullscreenListener {

    private static final DbVideo db = new DbVideo(ViralVideoApp.getContext());
    private static final ArrayList<com.divimove.ibariens.viral_videos.models.Video> video_list = db.getAllVideos();
    /** The request code when calling startActivityForResult to recover from an API service error. */
    private static final int RECOVERY_DIALOG_REQUEST = 1;
    private List<VideoEntry> video_entry_list = new ArrayList<VideoEntry>();

    private GridViewAdapter adapter;
    private View videoBox;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viral_index_grid);


        for (int i = 0; i < video_list.size(); i++) {
          video_entry_list.add(new VideoEntry(video_list.get(i).getVideo_title(), video_list.get(i).getVideo_id()));
        }

        adapter = new GridViewAdapter(this, video_entry_list);
        GridView gridView = (GridView) findViewById(R.id.view_grid);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView parent, View v, int position, long id) {
                Toast.makeText(getApplicationContext(),  "" + position, Toast.LENGTH_SHORT).show();

                Log.d("click", "click");
                String videoId = video_entry_list.get(position).videoId;

                Video.VideoFragment videoFragment = (Video.VideoFragment) getFragmentManager().findFragmentById(R.id.video_fragment_container);
                videoFragment.setVideoId(videoId);


                videoBox = findViewById(R.id.video_box);
                // The videoBox is INVISIBLE if no video was previously selected, so we need to show it now.
                if (videoBox.getVisibility() != View.VISIBLE) {
                    videoBox.setVisibility(View.VISIBLE);
                }
            }
        });


        checkYouTubeApi();
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

    private void checkYouTubeApi() {
        YouTubeInitializationResult errorReason =
                YouTubeApiServiceUtil.isYouTubeApiServiceAvailable(this);
        if (errorReason.isUserRecoverableError()) {
            errorReason.getErrorDialog(this, RECOVERY_DIALOG_REQUEST).show();
        } else if (errorReason != YouTubeInitializationResult.SUCCESS) {
            String errorMessage =
                    String.format(getString(R.string.error_player), errorReason.toString());
            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onFullscreen(boolean fullscreen) {
        Toast.makeText(getApplicationContext(),  "fullscreen", Toast.LENGTH_SHORT).show();
    }

    public void onClickClose(View view) {
        videoBox = findViewById(R.id.video_box);
        videoBox.setVisibility(LinearLayout.GONE);
        Toast.makeText(getApplicationContext(),  "close", Toast.LENGTH_SHORT).show();
    }
}
