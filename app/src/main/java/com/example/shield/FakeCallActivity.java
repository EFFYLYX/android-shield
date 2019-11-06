package com.example.shield;

import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import java.io.IOException;

public class FakeCallActivity extends AppCompatActivity {
    ImageView acceptCall;
    ImageView endCall;

    ImageView endCalling;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

            testLoopPlayer();

        setContentView(R.layout.activity_fake_call);
        acceptCall = findViewById(R.id.accept_call);
        endCall = findViewById(R.id.end_call);

        acceptCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPlayer.stop();
                setContentView(R.layout.layout_accept_fake_call);
                endCalling = findViewById(R.id.end_calling);

                endCalling.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });
            }
        });

        endCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPlayer.stop();
                finish();
            }
        });










    }




    private MediaPlayer mPlayer, mNextPlayer;
    private int mPlayResId = R.raw.phone;


    public void testLoopPlayer()  {
//        mPlayer.setDataSource(getApplicationContext(), RingtoneManager
//                .getDefaultUri(RingtoneManager.TYPE_RINGTONE));
        mPlayer = MediaPlayer.create(this, mPlayResId);
        mPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mPlayer.start();
            }
        });
        createNextMediaPlayer();
    }

    private void createNextMediaPlayer() {



        mPlayer.setVolume(1.0f, 1.0f);

//        mPlayer.stop();audioMgr
        mNextPlayer = MediaPlayer.create(this, mPlayResId);
        mPlayer.setNextMediaPlayer(mNextPlayer);
        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.release();

                mPlayer = mNextPlayer;

                createNextMediaPlayer();
            }
        });
    }


}
