package com.divimove.ibariens.viral_videos.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.divimove.ibariens.viral_videos.models.Video;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


public class DbVideo extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 4;
    private static final String DATABASE_NAME = "ViralVideos";
    private static final SimpleDateFormat date_formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
    private int watchedVideos;

    public DbVideo(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_BOOK_TABLE = "CREATE TABLE videos ( " +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "channel_id TEXT, " +
                "video_id TEXT, " +
                "video_title TEXT, " +
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
    public Video getVideo(String video_id) {

        String table = "videos";
        String[] columns = {"id", "channel_id", "video_id", "video_title", "watched", "is_new", "published_at", "view_count"};
        String selection = "video_id = ?";
        String[] selection_args = {video_id};

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

        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();

            Video video = new Video();
            video.setId(Integer.parseInt(cursor.getString(0)));
            video.setChannel_id(cursor.getString(1));
            video.setVideo_id(cursor.getString(2));
            video.setVideo_title(cursor.getString(3));
            video.setWatched((cursor.getInt(4) == 1));
            video.setIs_new((cursor.getInt(5) == 1));

            try {
                Date date = date_formatter.parse(cursor.getString(6));
                video.setPublished_at(date);
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            video.setView_count(cursor.getLong(7));
            return video;
        } else {
            return null;
        }
    }

    public void addVideo(Video video) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("channel_id", video.getChannel_id());
        values.put("video_id", video.getVideo_id());
        values.put("video_title", video.getVideo_title());
        values.put("watched", video.getWatched());
        values.put("is_new", video.getIs_new());
        values.put("published_at", date_formatter.format(video.getPublished_at()));
        values.put("view_count", video.getView_count());

        db.insert("videos",
                null, //nullColumnHack
                values);

        db.close();
    }


    public int updateVideo(Video video) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("channel_id", video.getChannel_id());
        values.put("video_id", video.getVideo_id());
        values.put("video_title", video.getVideo_title());
        values.put("watched", video.getWatched());
        values.put("is_new", video.getIs_new());
        values.put("published_at", date_formatter.format(video.getPublished_at()));
        values.put("view_count", video.getView_count());

        int i = db.update("videos",
                values,
                "id = ?",
                new String[]{String.valueOf(video.getId())});

        db.close();
        return i;
    }


    public ArrayList<Video> getAllVideos() {
        ArrayList<Video> videos = new ArrayList<Video>();

        String query = "SELECT  * FROM videos";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        videos = extractVideoFromDb(cursor);

        return videos;
    }


    public ArrayList<Video> getNewVideos() {
        ArrayList<Video> videos = new ArrayList<Video>();

        String query = "SELECT * FROM videos WHERE is_new = 1";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        videos = extractVideoFromDb(cursor);

        return videos;
    }

    public ArrayList<Video> GetUnwatchedVideos() {
        ArrayList<Video> videos = new ArrayList<Video>();

        String query = "SELECT * FROM videos WHERE watched = 0";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        videos = extractVideoFromDb(cursor);

        return videos;
    }


    public void setVideoAsWatched(String videoId) {
        // Bit Slow, but lazy way to do it.
        Video video = getVideo(videoId);
        video.setWatched(true);
        updateVideo(video);
    }


    private ArrayList<Video> extractVideoFromDb(Cursor cursor) {
        ArrayList<Video> videos = new ArrayList<Video>();
        if (cursor != null && cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {
                    Video video = new Video();
                    video.setId(Integer.parseInt(cursor.getString(0)));
                    video.setChannel_id(cursor.getString(1));
                    video.setVideo_id(cursor.getString(2));
                    video.setVideo_title(cursor.getString(3));
                    video.setWatched((cursor.getInt(4) == 1));
                    video.setIs_new((cursor.getInt(5) == 1));

                    try {
                        Date date = date_formatter.parse(cursor.getString(6));
                        video.setPublished_at(date);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    video.setView_count(cursor.getLong(7));
                    videos.add(video);
                } while (cursor.moveToNext());
            }

        }
        return videos;
    }
}
