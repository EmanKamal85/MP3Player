package com.example.mp3player;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;

public class MusicActivity extends AppCompatActivity {

    ImageView volumeDown, volumeUp;
    SeekBar volumeSeekbar, progressSeekbar;
    TextView fileName, progressTextView, fileLength;
    Button previous, pause, next;

    ArrayList<String> audioList;
    int position;
    String filePath, title;
    MediaPlayer mediaPlayer;

    Handler handler;
    Runnable runnable;
    int totalTime;

    Animation animation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);

        volumeDown = findViewById(R.id.volume_down);
        volumeUp = findViewById(R.id.volume_up);
        volumeSeekbar = findViewById(R.id.volume_seekbar);
        progressSeekbar = findViewById(R.id.seekbar_progress);
        fileName = findViewById(R.id.musicactivity_textview_file_name);
        progressTextView = findViewById(R.id.textview_progress);
        fileLength = findViewById(R.id.textview_total);
        previous = findViewById(R.id.button_previous);
        pause = findViewById(R.id.button_pause);
        next = findViewById(R.id.button_next);



        Intent intent = getIntent();
        audioList = intent.getStringArrayListExtra("audioList");
        filePath = intent.getStringExtra("filePath");
        title = intent.getStringExtra("title");
        position = intent.getIntExtra("position", 0);

        animation = AnimationUtils.loadAnimation(this, R.anim.translate_animation);

        fileName.setText(title);
        fileName.setAnimation(animation);

        mediaPlayer = new MediaPlayer();
        try {
            //mediaPlayer.release();
            mediaPlayer.setDataSource(filePath);
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("MediaPlayer ", e.toString() );
        }

        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer.isPlaying()){
                    mediaPlayer.pause();
                    pause.setBackgroundResource(R.drawable.ic_baseline_play_arrow_24);
                }else{
                    mediaPlayer.start();
                    pause.setBackgroundResource(R.drawable.ic_baseline_pause_24);
                }

            }
        });

        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.reset();
                if (position == 0){
                  position = audioList.size()-1;
                }else {
                    position--;
                }
                    String newPath = audioList.get(position);
                    try {
                        //mediaPlayer.release();
                        mediaPlayer.setDataSource(newPath);
                        mediaPlayer.prepare();
                        mediaPlayer.start();
                        pause.setBackgroundResource(R.drawable.ic_baseline_pause_24);

                        String newTitle = newPath.substring(newPath.lastIndexOf("/")+1);
                        fileName.setText(newTitle);

                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.e("MediaPlayer ", e.toString() );
                    }

                    fileName.clearAnimation();
                    fileName.setAnimation(animation);
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.reset();
                if (position == audioList.size() -1){
                    position = 0;
                }else {
                    position++;
                }

                    String newPath = audioList.get(position);
                    try {
                        //mediaPlayer.release();
                        mediaPlayer.setDataSource(newPath);
                        mediaPlayer.prepare();
                        mediaPlayer.start();
                        pause.setBackgroundResource(R.drawable.ic_baseline_pause_24);

                        String newTitle = newPath.substring(newPath.lastIndexOf("/")+1);
                        fileName.setText(newTitle);

                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.e("MediaPlayer ", e.toString() );
                    }

                fileName.clearAnimation();
                fileName.setAnimation(animation);

            }
        });

        volumeSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser){
                    volumeSeekbar.setProgress(progress);
                    float volumeLevel = progress/ 100f;
                    mediaPlayer.setVolume(volumeLevel, volumeLevel);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        progressSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser){
                    progressSeekbar.setProgress(progress);
                    mediaPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                totalTime = mediaPlayer.getDuration();
                progressSeekbar.setMax(totalTime);

                int currentPosition = mediaPlayer.getCurrentPosition();
                progressSeekbar.setProgress(currentPosition);
                handler.postDelayed(runnable, 1000);

                fileLength.setText(createTimeLabel(totalTime));
                progressTextView.setText(createTimeLabel(currentPosition));

                if (createTimeLabel(currentPosition).equals(createTimeLabel(totalTime))){
                    mediaPlayer.reset();
                    if (position == audioList.size() -1){
                        position = 0;
                    }else {
                        position++;
                    }

                    String newPath = audioList.get(position);
                    try {
                        //mediaPlayer.release();
                        mediaPlayer.setDataSource(newPath);
                        mediaPlayer.prepare();
                        mediaPlayer.start();
                        pause.setBackgroundResource(R.drawable.ic_baseline_pause_24);

                        String newTitle = newPath.substring(newPath.lastIndexOf("/")+1);
                        fileName.setText(newTitle);
                        fileName.clearAnimation();
                        fileName.setAnimation(animation);

                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.e("MediaPlayer ", e.toString() );
                    }
                }
            }
        };

        handler.post(runnable);
    }

    public String createTimeLabel(int currentPosition){
        String timeLabel;
        int minute, second;

        minute = currentPosition/ 1000/60;
        second = currentPosition/ 1000%60;

        if (second < 10){
            timeLabel = minute + ":0" + second;
        }else {
            timeLabel = minute + ":" + second;
        }

        return  timeLabel;
    }
}