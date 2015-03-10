package com.divimove.ibariens.viral_videos.helpers;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


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

        for (int i = 0; i < json.length(); i++) {

            try {
                JSONObject c = json.getJSONObject(i);
                HashMap<String, String> map = new HashMap<String, String>();

                map.put("channel_id", c.getString("channel_id"));
                jsonlist.add(map);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
