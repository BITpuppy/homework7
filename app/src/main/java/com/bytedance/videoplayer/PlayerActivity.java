package com.bytedance.videoplayer;

import android.content.Intent;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import static android.view.View.GONE;

public class PlayerActivity extends AppCompatActivity {
    private static final String TAG = "PlayerActivity";
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            LinearLayout linearLayout = findViewById(R.id.linearLayout);
            linearLayout.setVisibility(View.VISIBLE);
        }
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            LinearLayout linearLayout = findViewById(R.id.linearLayout);
            linearLayout.setVisibility(GONE);
        }
    }

    private int flag = 0;
    private int videoLength;
    private VideoView videoView;
    private Button mBtn;
    private TextView textView1;
    private TextView textView2;
    private SeekBar seekBar;

    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if(videoView.isPlaying()){
                int currentProgress = videoView.getCurrentPosition();
                String minute = String.valueOf(currentProgress / 60000);
                String second = String.valueOf(currentProgress % 60000 / 1000);
                if(Integer.valueOf(second) < 10) {
                    textView1.setText("0" + minute + ":0" + second);
                } else {
                    textView1.setText("0" + minute + ":" + second);
                }
                seekBar.setProgress(currentProgress);
            }
            handler.postDelayed(runnable,500);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        getSupportActionBar().hide();
        mBtn = findViewById(R.id.playButton);
        textView1 = findViewById(R.id.textView1);
        textView2 = findViewById(R.id.textView2);
        seekBar = findViewById(R.id.seekBar);
        videoView = findViewById(R.id.videoView);
        videoView.setVideoPath("android.resource://" + this.getPackageName() + "/" + R.raw.bytedance);
        MediaController mediaController = new MediaController(PlayerActivity.this);
        videoView.setMediaController(mediaController);

        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                videoLength = videoView.getDuration();
                String minute = String.valueOf(videoLength / 60000);
                String second = String.valueOf(videoLength % 60000 / 1000);
                if(Integer.valueOf(second) < 10) {
                    textView2.setText("0" + minute + ":0" + second);
                } else{
                    textView2.setText("0" + minute + ":" + second);
                }
            }
        });

        mBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flag == 0){
                    videoView.start();
                    handler.postDelayed(runnable,0);
                    seekBar.setMax(videoLength);
                    flag = 1;
                } else{
                    videoView.pause();
                    flag = 0;
                }
            }
        });


        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser){
                    videoView.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int progress = seekBar.getProgress();
                if(videoView.isPlaying()){
                    videoView.seekTo(progress);
                }
            }
        });
    }
}
