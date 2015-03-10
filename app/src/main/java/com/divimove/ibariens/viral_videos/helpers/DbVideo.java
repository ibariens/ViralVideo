package com.divimove.ibariens.viral_videos.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.divimove.ibariens.viral_videos.models.Video;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;


public class DbVideo extends SQLiteOpenHelper  {

    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "ViralVideos";

    public DbVideo(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_BOOK_TABLE = "CREATE TABLE videos ( " +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "channel_id TEXT, "+
                "video_title TEXT, "+
                "watched BOOLEAN," +
                "is_new BOOLEAN," +
                "published_at TIMESTAMP," +
                "view_count LONG" +
                " )";
        db.execSQL(CREATE_BOOK_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS videos");
        this.onCreate(db);
    }





    // Why not in model?
    public Video getVideo(int id) {

        String table = "videos";
        String[] columns = {"id", "channel_id", "video_title", "watched", "is_new", "published_at", "view_count"};
        String selection = "id = ?";
        String[] selection_args = {String.valueOf(id)};

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor =
                db.query(table, // a. table
                        columns, // b. column names
                        selection, // c. selections
                        selection_args, // d. selections args
                        null, // e. group by
                        null, // f. having
                        null, // g. order by
                        null); // h. limit

        if (cursor != null)
            cursor.moveToFirst();

        Video video = new Video();
        video.setId(Integer.parseInt(cursor.getString(0)));
        video.setChannel_id(cursor.getString(1));
        video.setVideo_title(cursor.getString(2));
        video.setWatched((cursor.getInt(3) == 1));
        video.setIs_new((cursor.getInt(4) == 1));

        DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        try {
            Date date = format.parse(cursor.getString(5));
            video.setPublished_at(date);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        video.setView_count(cursor.getLong(6));
        return video;
    }

    public void addVideo(Video video){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("channel_id",  video.getChannel_id());
        values.put("video_title", video.getVideo_title());
        values.put("watched", video.getWatched());
        values.put("is_new", video.getIs_new());
        values.put("published_at", video.getPublished_at().toString());
        values.put("view_count", video.getView_count());

        db.insert("videos",
                    null, //nullColumnHack
                    values);

        db.close();
    }


    public List<Video> getAllVideos() {

        List<Video> videos = new LinkedList<Video>();
        String query = "SELECT  * FROM " + "videos";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        Video video = null;
        if (cursor.moveToFirst()) {
            do {
                video.setId(Integer.parseInt(cursor.getString(0)));
                video.setChannel_id(cursor.getString(1));
                video.setVideo_title(cursor.getString(2));
                video.setWatched((cursor.getInt(3) == 1));
                video.setIs_new((cursor.getInt(4) == 1));

                DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                try {
                    Date date = format.parse(cursor.getString(5));
                    video.setPublished_at(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                video.setView_count(cursor.getLong(6));
                videos.add(video);
            } while (cursor.moveToNext());
        }
        return videos;
    }


    public int updateVideo(Video video) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("channel_id",  video.getChannel_id());
        values.put("video_title", video.getVideo_title());
        values.put("watched", video.getWatched());
        values.put("is_new", video.getIs_new());
        values.put("published_at", video.getPublished_at().toString());
        values.put("view_count", video.getView_count());

        int i = db.update("videos",
                values,
                "id = ?",
                new String[] { String.valueOf(video.getId()) });

        db.close();
        return i;
    }
}


