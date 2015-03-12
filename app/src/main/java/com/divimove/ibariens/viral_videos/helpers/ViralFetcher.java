package com.divimove.ibariens.viral_videos.helpers;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.divimove.ibariens.viral_videos.models.Video;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;


public class ViralFetcher extends AsyncTask<String, Void, Boolean> {

    private ProgressDialog dialog;
    private Context context;
    private Activity activity;

    ArrayList<HashMap<String, String>> jsonlist = new ArrayList<HashMap<String, String>>();

    public ViralFetcher(Context context) {
        this.context = context;
        dialog = new ProgressDialog(context);
    }


    protected void onPreExecute() {
        this.dialog.setMessage("Looking for new virals!");
        this.dialog.setCancelable(true);
        this.dialog.show();
    }

    @Override
    protected void onPostExecute(final Boolean success) {
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    protected Boolean doInBackground(final String... args) {

        JSONParser jParser = new JSONParser();

        // get JSON data from URL
        JSONArray json = jParser.getJSONFromUrl("https://datacruncher.divimove.com/viral_videos");
        if (json != null) {
            for (int i = 0; i < json.length(); i++) {

                try {
                    JSONObject j = json.getJSONObject(i);
                    createOrUpdateVideoInDb(j);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    private void createOrUpdateVideoInDb(JSONObject j) throws JSONException {
        String video_id = j.getString("video_id");
        DbVideo db = new DbVideo(this.context);

        Video video = db.getVideo(video_id);
        if (video == null) {
            // New video found!
            video = new Video();
            video.setChannel_id(j.getString("channel_id"));
            video.setVideo_id(j.getString("video_id"));
            video.setVideo_title(j.getString("video_title"));
            video.setWatched(false);
            video.setIs_new(true);
            video.setView_count(j.getLong("view_count"));

            DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            try {
                Date date = format.parse(j.getString("published_at"));
                video.setPublished_at(date);
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            db.addVideo(video);
        }
        else {
            // Update video for latest view count and title
            video.setChannel_id(j.getString("channel_id"));
            video.setVideo_id(j.getString("video_id"));
            video.setVideo_title(j.getString("video_title"));
            video.setIs_new(false);
            video.setView_count(j.getLong("view_count"));

            db.updateVideo(video);
        }
    }
}
