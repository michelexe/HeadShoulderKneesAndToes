package uk.ac.uclan.g.e.headshoulderkneesandtoes;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.microsoft.band.BandClient;
import com.microsoft.band.BandException;
import com.microsoft.band.BandIOException;
import com.microsoft.band.sensors.BandAccelerometerEvent;
import com.microsoft.band.sensors.BandAccelerometerEventListener;
import com.microsoft.band.sensors.SampleRate;

public class Tutorial extends AppCompatActivity {

    private boolean headTouch = false,
                    shoulderTouch = false,
                    kneeTouch = false;

    // https://www.youtube.com/watch?v=NXT_ULrFLaA
    private BandClient client;
    private Bundle bundle;
    private Button skipButton;
    private MediaPlayer successMedia;

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

        new AccelerometerSubscriptionTask().execute();
                // start the listener




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


        }
        if(headTouch){ // if the head has already been touched by the band

            if(positionBand.equals(PositionBand.getShoulderPosition())){// same with shoulder
                shoulderTouch = true;
                successMedia.start();
            }
            if(shoulderTouch){
                successMedia.stop();
                successMedia.start();
                if(positionBand.equals(PositionBand.getKneePosition())){// same with knee
                    kneeTouch = true;
                    successMedia.start();
                }
                if(kneeTouch){
                    successMedia.stop();
                    successMedia.start();
                    if(positionBand.equals(PositionBand.getToesPosition())){
                        successMedia.start();
                        // end of the tutorial
                        endOfTutorial();
                    }

                }
            }
        }
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

    private void musiqueStart(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                successMedia.start();
            }
        }).start();
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
