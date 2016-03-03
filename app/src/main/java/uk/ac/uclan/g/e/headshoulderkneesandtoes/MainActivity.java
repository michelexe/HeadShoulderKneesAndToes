package uk.ac.uclan.g.e.headshoulderkneesandtoes;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

public class MainActivity extends AppCompatActivity {

    private VideoView video;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    private void init(){
        addClickListener();
        video = (VideoView) findViewById(R.id.videoView);
        videoImplement();
        video.requestFocus();
        video.start(); // start de video
    }

    private void addClickListener(){
        final Button playButton = (Button) findViewById(R.id.button_play);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ListBluetoothDevice.class);
                startActivity(intent);
            }
        });
    }

    private void videoImplement(){
        video.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.display_video));
        // find de video path
        video.setMediaController(new MediaController(this));

        video.setOnCompletionListener(new MediaPlayer.OnCompletionListener() { // at the end of video
            @Override
            public void onCompletion(MediaPlayer mp) {
                Toast.makeText(getApplicationContext(), "c'est fini...", Toast.LENGTH_LONG).show();
            }
        });
        video.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);// loop on video
            }
        });


        video.setOnTouchListener(new View.OnTouchListener() {
            @Override // whenever the screen is touched while video is displayed
            public boolean onTouch(View v, MotionEvent event) {
                if (video.isPlaying()) { // video is playing
                    video.pause();
                } else {
                    video.start(); //  if it's already on pause state
                }

                return false;
            }
        });
    }
}
