package com.example.imusic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PlaySong extends AppCompatActivity {
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.start();
        mediaPlayer.release();
        updateseek.interrupt();
    }

    TextView textView;
    ImageView play,previous,next;
    ArrayList<File> songs;
    SeekBar seekBar;
    String textcontent;
    int positon;
    Thread updateseek;
MediaPlayer mediaPlayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_song);
         textView=findViewById(R.id.textView);
         play=findViewById(R.id.imageView4);
         previous=findViewById(R.id.imageView2);
         next=findViewById(R.id.imageView5);
         seekBar=findViewById(R.id.seekBar);
         Intent intent=getIntent();
         Bundle bundle=intent.getExtras();
         songs=(ArrayList) bundle.getParcelableArrayList("songlist");
         textcontent=intent.getStringExtra("currentsong");
         textView.setText(textcontent);
         textView.setSelected(true);
         positon=intent.getIntExtra("positon",0);
         Uri uri=Uri.parse(songs.get(positon).toString());
         mediaPlayer=MediaPlayer.create(this,uri);
         mediaPlayer.start();
         seekBar.setMax(mediaPlayer.getDuration());
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(seekBar.getProgress());
            }
        });
        updateseek=new Thread()
        {
            @Override
            public void run()
            {
                int currentposition=0;
                try {
                    while (currentposition<mediaPlayer.getDuration())
                    {
                        currentposition=mediaPlayer.getCurrentPosition();

                        seekBar.setProgress(currentposition);
                        sleep(400);
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        };
        updateseek.start();
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mediaPlayer.isPlaying())
                {
                    mediaPlayer.pause();
                    play.setImageResource(R.drawable.play);
                }
                else
                {
                    mediaPlayer.start();
                    play.setImageResource(R.drawable.pause);
                }
            }
        });
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.start();
                mediaPlayer.release();
                if(positon!=0)
                {
                    positon--;
                }
                else
                {
                    positon=songs.size()-1;
                }
                Uri uri=Uri.parse(songs.get(positon).toString());
                mediaPlayer=MediaPlayer.create(getApplicationContext(),uri);
                mediaPlayer.start();
                seekBar.setMax(mediaPlayer.getDuration());
                textcontent=songs.get(positon).getName();
                textView.setText(textcontent);
                play.setImageResource(R.drawable.pause);
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.start();
                mediaPlayer.release();
                if(positon!=songs.size()-1)
                {
                    positon++;
                }
                else
                {
                    positon=0;
                }Uri uri=Uri.parse(songs.get(positon).toString());
                mediaPlayer=MediaPlayer.create(getApplicationContext(),uri);
                mediaPlayer.start();
                seekBar.setMax(mediaPlayer.getDuration());
                play.setImageResource(R.drawable.pause);
                textcontent=songs.get(positon).getName();
                textView.setText(textcontent);
            }
        });
    }
}