package com.divimove.ibariens.viral_videos.fragments;

import android.os.Bundle;

import com.divimove.ibariens.viral_videos.ViralIndexGridActivity;
import com.divimove.ibariens.viral_videos.ViralVideoApp;
import com.divimove.ibariens.viral_videos.helpers.DbVideo;
import com.divimove.ibariens.viral_videos.secrets.DeveloperKey;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerFragment;


public class Video {

    public static final class VideoFragment extends YouTubePlayerFragment implements YouTubePlayer.OnInitializedListener {

        private YouTubePlayer player;
        private String videoId;
        private final DbVideo db = new DbVideo(ViralVideoApp.getContext());

        public static VideoFragment newInstance() {
            return new VideoFragment();
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            initialize(DeveloperKey.DEVELOPER_KEY, this);
        }

        @Override
        public void onDestroy() {
            if (player != null) {
                player.release();
            }
            super.onDestroy();
        }

        public void setVideoId(String videoId) {
            if (videoId != null && !videoId.equals(this.videoId)) {
                db.setVideoAsWatched(videoId);
                db.close();

                this.videoId = videoId;
                if (player != null) {
                    player.cueVideo(videoId);
                }
            }
        }

        public void pause() {
            if (player != null) {
                player.pause();
            }
        }

        @Override
        public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player, boolean restored) {
            this.player = player;
            player.addFullscreenControlFlag(YouTubePlayer.FULLSCREEN_FLAG_CUSTOM_LAYOUT);
            player.setOnFullscreenListener((ViralIndexGridActivity) getActivity());
            if (!restored && videoId != null) {
                player.cueVideo(videoId);
            }
        }

        @Override
        public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult result) {
            this.player = null;
        }

    }


}
