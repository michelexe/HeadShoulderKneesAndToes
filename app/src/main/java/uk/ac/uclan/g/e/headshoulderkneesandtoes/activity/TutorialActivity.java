package uk.ac.uclan.g.e.headshoulderkneesandtoes.activity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.microsoft.band.BandClient;
import com.microsoft.band.BandException;
import com.microsoft.band.BandIOException;
import com.microsoft.band.sensors.BandAccelerometerEvent;
import com.microsoft.band.sensors.BandAccelerometerEventListener;
import com.microsoft.band.sensors.SampleRate;

import uk.ac.uclan.g.e.headshoulderkneesandtoes.bandposition.PositionBand;
import uk.ac.uclan.g.e.headshoulderkneesandtoes.R;

public class TutorialActivity extends AppCompatActivity {

    private boolean headTouch = false,
                    shoulderTouch = false,
                    kneeTouch = false;

    // https://www.youtube.com/watch?v=NXT_ULrFLaA
    private BandClient client;
    private Bundle bundle;
    private Button skipButton;
    private MediaPlayer successMedia;
    private ImageView imageView;
    private int step; // depends of tutorial progress
    private int imgTuto[]= {
            R.drawable.bear_shoulder_position,
            R.drawable.bear_knee_position}; // contains all image available for the tuto

    private BandAccelerometerEventListener mAccelerometerEventListener = new BandAccelerometerEventListener() {
        @Override
        public void onBandAccelerometerChanged(final BandAccelerometerEvent event) {// when the band is mouved
            if (event != null) {
                PositionBand positionBand = new PositionBand(event);
                try {
                    processTutorial(positionBand);
                } catch (BandIOException e) {
                    e.printStackTrace();
                }

            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);

        bundle= getIntent().getExtras();
        successMedia = MediaPlayer.create(this,R.raw.success);
        skipButton = (Button)findViewById(R.id.skip_button);
            addClickListener();
        imageView = (ImageView)findViewById(R.id.imageViewTuto);
        step = 0;

        new AccelerometerSubscriptionTask().execute();
                // start the listener
    }

    /**
     * add clickListener
     */
    private void addClickListener (){
        skipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    endOfTutorial();
                } catch (BandIOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * annex function which allows to simplify
     * the BandAccelerometerEventListener code
     * @param positionBand
     * @throws BandIOException
     */
    private void processTutorial(PositionBand positionBand) throws BandIOException {
        if(positionBand.equals(PositionBand.getHeadPosition()) && !headTouch){// if the band is on the head
            headTouch = true;
            musiqueStart();
            nextImageTuto();
        }
        if(headTouch){ // if the head has already been touched by the band
            if(positionBand.equals(PositionBand.getShoulderPosition()) && !shoulderTouch){// same with shoulder
                shoulderTouch = true;
                musiqueStart();
                nextImageTuto();
            }
            if(shoulderTouch){
                if(positionBand.equals(PositionBand.getKneePosition()) && !kneeTouch){// same with knee
                    kneeTouch = true;
                    musiqueStart();
                    nextImageTuto();
                }
                if(kneeTouch){
                    if(positionBand.equals(PositionBand.getToesPosition())){
                        musiqueStart();
                        // end of the tutorial
                        endOfTutorial();
                    }

                }
            }
        }
    }

    /**
     * start the Mediaplayer
     */
    private void musiqueStart(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                successMedia.start();
            }
        }).start();
    }


    /**
     * display next image of the tuto
     */
    private void nextImageTuto(){
        new Thread(){
            @Override
            public void run(){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        imageView.setImageResource(imgTuto[step]);
                        // display the next image
                        step++;
                    }
                });
            }
        }.start();



    }

    /**
     * come back to the Activity LevelChoice
     * @throws BandIOException
     */
    private void endOfTutorial() throws BandIOException {
        Intent intent =new Intent(getApplicationContext(),LevelChoiceActivity.class);
        intent.putExtras(bundle);
        client.getSensorManager().unregisterAccelerometerEventListener(mAccelerometerEventListener);
        startActivity(intent);
    }


    /**
     * Task which will be execute in background
     */
    private class AccelerometerSubscriptionTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            try {

                int i = bundle.getInt(ListBluetoothDeviceActiviy.BAND_POSITION);
                client = ListBluetoothDeviceActiviy.getBand(i); // get the band connected to the device
                if(client != null){
                    client.getSensorManager().registerAccelerometerEventListener(mAccelerometerEventListener, SampleRate.MS128);
                }
            } catch (BandException e) {
                String exceptionMessage="";
                switch (e.getErrorType()) {
                    case UNSUPPORTED_SDK_VERSION_ERROR:
                        exceptionMessage = "Microsoft Health BandService doesn't support your SDK Version. Please update to latest SDK.\n";
                        break;
                    case SERVICE_ERROR:
                        exceptionMessage = "Microsoft Health BandService is not available. Please make sure Microsoft Health is installed and that you have the correct permissions.\n";
                        break;
                    default:
                        exceptionMessage = "Unknown error occured: " + e.getMessage() + "\n";
                        break;
                }


            } catch (Exception e) {

            }
            return null;
        }
    }


}
