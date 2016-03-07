package uk.ac.uclan.g.e.headshoulderkneesandtoes;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.microsoft.band.BandClient;
import com.microsoft.band.sensors.BandAccelerometerEventListener;
import com.microsoft.band.sensors.BandSensorManager;

import java.util.Random;

/**
 * a loading Screen
 */
public class SplashScreen extends AppCompatActivity {
    private ProgressBar progressBar;
    private int progressStatus = 1;
    private final int SPLASH_SCREEN_DELAY[]={1000,1500,2000,
                                            2500,3000,3500,4000,
                                            4500,5000,5500,6000,
                                            6500,7000,7500,8000,
                                            8500,9000,9500,10000};
    private ImageView bearImage,loadingImage;
    private AnimationDrawable animationBear, animationLoading;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        this.displayAnimation();
        



        Random rand = new Random();
        final int randomNbr = rand.nextInt(SPLASH_SCREEN_DELAY.length);


        // a progress bar
        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        progressBar.setProgress(progressStatus);
        final int TICK = 100;
        CountDownTimer countDownTimer = new
                CountDownTimer(SPLASH_SCREEN_DELAY[randomNbr]+1,TICK) {
            @Override
            public void onTick(long millisUntilFinished) {
                progressStatus+=(TICK*100)/SPLASH_SCREEN_DELAY[randomNbr];
                progressBar.setProgress(progressStatus);
                        // the bar is updated
            }

            @Override
            public void onFinish() {
                if(progressStatus < 100-(TICK*100)/SPLASH_SCREEN_DELAY[randomNbr]){
                    progressStatus=100;
                }else {
                    progressStatus+=(TICK*100)/SPLASH_SCREEN_DELAY[randomNbr];
                }
                progressBar.setProgress(progressStatus);
                stopAnimation();
                new Thread(new Runnable() {
                   @Override
                   public void run() {
                       try {
                           Thread.sleep(1900);
                           Intent intent = new Intent(getApplicationContext(),LevelChoice.class);
                           intent.putExtras(getIntent().getExtras());
                           // the microsoft band actually connected
                           startActivity(intent);
                       } catch (InterruptedException e) {
                           e.printStackTrace();
                       }

                   }
               }).start();

            }
        }.start();


        /*
         * random delay which correspond at the loading screen duration

          new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getApplicationContext(),LevelChoice.class);
                intent.putExtras(getIntent().getExtras());
                        // the microsoft band actually connected
                startActivity(intent);
                SplashScreen.this.finish();
            }
        }, SPLASH_SCREEN_DELAY[randomNbr]);

        */

    }

    /**
     * dispplay animation
     * bear and loading message
     */
    private void displayAnimation(){
        // animation which dance HSK&T
        bearImage = (ImageView)findViewById(R.id.imageView);
        animationBear = (AnimationDrawable)bearImage.getBackground();
        bearImage.post(new Runnable() {
            @Override
            public void run() {
                animationBear.start();
                // the animation start
            }
        });

        loadingImage= (ImageView)findViewById(R.id.imageView2);
        animationLoading =(AnimationDrawable)loadingImage.getBackground();
        loadingImage.post(new Runnable() {
            @Override
            public void run() {
                animationLoading.start();
            }
        });
    }

    /**
     * stop all animations
     */
    private void stopAnimation(){
        animationLoading.stop();
        animationBear.stop();
    }
}
