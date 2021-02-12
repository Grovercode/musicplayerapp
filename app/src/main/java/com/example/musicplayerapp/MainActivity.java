package com.example.musicplayerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private MediaPlayer mediaPlayer;
    private ImageView artistImage;
    private TextView lefttime, righttime;
    private SeekBar seekBar;
    private Button prev, pauseplay, next;
    private Thread thread;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupUI();

        seekBar.setMax(mediaPlayer.getDuration());
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                if(fromUser){
                    mediaPlayer.seekTo(progress);
                }

                SimpleDateFormat dateFormat = new SimpleDateFormat("mm:ss");
                int currrntpos = mediaPlayer.getCurrentPosition();
                int duration = mediaPlayer.getDuration();

                lefttime.setText(dateFormat.format(new Date(currrntpos)));
                righttime.setText(dateFormat.format(new Date(duration-currrntpos)));

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });



    }

    public void setupUI()
    {
        artistImage=(ImageView) findViewById(R.id.imageView);
        lefttime = (TextView) findViewById(R.id.lefttextID);
        righttime = (TextView) findViewById(R.id.righttextID);
        prev = (Button) findViewById(R.id.prevButton);
        pauseplay = (Button) findViewById(R.id.playButton);
        next = (Button) findViewById(R.id.nextButton);
        seekBar = (SeekBar) findViewById(R.id.seekBar);

        mediaPlayer= new MediaPlayer();
        mediaPlayer = MediaPlayer.create(getApplicationContext(),R.raw.music );

        prev.setOnClickListener(this);
        pauseplay.setOnClickListener(this);
        next.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.prevButton :
                backmusic();
                break;

            case R.id.playButton:
                if(mediaPlayer.isPlaying())
                pause();
                else
                    startmusic();
                break;

            case R.id.nextButton:
                nextmusci();
                break;


        }
    }

    public void pause(){

        if(mediaPlayer!= null)
        {
            mediaPlayer.pause();
            pauseplay.setBackgroundResource(android.R.drawable.ic_media_play);
        }

    }

    public void startmusic(){
        if(mediaPlayer!=null){
            mediaPlayer.start();
            updateThread();
            pauseplay.setBackgroundResource(android.R.drawable.ic_media_pause);

        }
    }

    public void backmusic(){

            mediaPlayer.seekTo(0);
            if(!mediaPlayer.isPlaying())
            pauseplay.setBackgroundResource(android.R.drawable.ic_media_play);

    }

    public void nextmusci(){

            mediaPlayer.seekTo(mediaPlayer.getDuration()-1000);
            pauseplay.setBackgroundResource(android.R.drawable.ic_media_play);

    }

    public void updateThread(){

        thread = new Thread(){
            @Override
            public void run() {
                try {
                    while (mediaPlayer != null && mediaPlayer.isPlaying()){

                    Thread.sleep(50);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            int newpostion = mediaPlayer.getCurrentPosition();
                            int newMax = mediaPlayer.getDuration();
                            seekBar.setMax(newMax);
                            seekBar.setProgress(newpostion);

                            //update the text
                            lefttime.setText(String.valueOf(new SimpleDateFormat("mm:ss")
                            .format(new Date(mediaPlayer.getCurrentPosition()))));

                            righttime.setText(String.valueOf(new SimpleDateFormat("mm:ss")
                                    .format(new Date(mediaPlayer.getDuration() - mediaPlayer.getCurrentPosition()))));
                        }
                    });

                    }
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
        };
        thread.start();
    }

    //destroying ram stuff
    @Override
    protected void onDestroy() {
        if(mediaPlayer != null && mediaPlayer.isPlaying())
        {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        //destroy thread also
        thread.interrupt();
        thread = null;

        super.onDestroy();
    }
}