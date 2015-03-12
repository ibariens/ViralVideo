package com.divimove.ibariens.viral_videos.models;

import android.graphics.drawable.Drawable;

public class GridViewItem {
    public final Drawable icon; // the drawable for the ListView item ImageView
    public final String title; // the text for the GridView item title

    public GridViewItem(Drawable icon, String title) {
        this.icon = icon;
        this.title = title;
    }
}