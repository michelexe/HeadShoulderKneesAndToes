package uk.ac.uclan.g.e.headshoulderkneesandtoes.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.microsoft.band.BandClient;
import com.microsoft.band.BandException;
import com.microsoft.band.BandIOException;
import com.microsoft.band.sensors.BandAccelerometerEvent;
import com.microsoft.band.sensors.BandAccelerometerEventListener;
import com.microsoft.band.sensors.SampleRate;

import uk.ac.uclan.g.e.headshoulderkneesandtoes.R;
import uk.ac.uclan.g.e.headshoulderkneesandtoes.bandposition.CalibPosition;
import uk.ac.uclan.g.e.headshoulderkneesandtoes.bandposition.PositionBand;
import uk.ac.uclan.g.e.headshoulderkneesandtoes.bandposition.PositionFlag;

public class CalibrateActivity extends AppCompatActivity {

    private int index;
    private Bundle bundle;

    private CalibPosition calibPosition;
    private CountDownTimer cdCalibrate;
    private long CALIB_POSITION_DURATION = 2000; // 2s

    private ProgressBar bar;
    private Button startButton;
    private TextView namePosition;

    private final PositionFlag flags []={
            PositionFlag.HEAD,
            PositionFlag.SHOULDER,
            PositionFlag.KNEE,
            PositionFlag.TOES
    };
    /* give an order of execution
    first head, then shoulder and knee, finally toes
     */


    private BandClient client;


    private BandAccelerometerEventListener bandAccelerometerEventListener= new BandAccelerometerEventListener() {
        @Override
        public void onBandAccelerometerChanged(BandAccelerometerEvent event) {
            calibPosition.addEvent(event); // while time isn't over add events to the Arraylist
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calibrate);
        init();
        setCalibrate();
    }

    private void init(){
        bundle = getIntent().getExtras();
        int i = bundle.getInt(ListBluetoothDeviceActiviy.BAND_POSITION);
        client = ListBluetoothDeviceActiviy.getBand(i); // get the band connected to the device


        index = 0;

        calibPosition = new CalibPosition();

        startButton = (Button)findViewById(R.id.calibButton);
            addClickListener();

        namePosition = (TextView) findViewById(R.id.calibText);
        bar = (ProgressBar) findViewById(R.id.barCalibrate);
        bar.setMax((int) CALIB_POSITION_DURATION);

        new AccelerometerSubscriptionTask().execute();
            // set the listener


    }


    /**
     * add click listener on buttons
     */
    private void addClickListener(){
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runCountDown();
            }
        });
    }

    /**
     * hides the button, resets the progressBar and the cdCalibrate
     * at the end of the timer (cdCalibrate) unregistres the Listener
     * and shows again the button, then modifies default values in the
     * class PositionBand
     * @see PositionBand
     * @see CalibPosition
     *
     */
    private void setCalibrate(){
        if(index < flags.length ){
            setTextView();
            startButton.setVisibility(View.VISIBLE); // the button is invisible
            bar.setProgress(0);
            calibPosition.emptyEventList(); // empty all even from the arraylist
        }

    }


    /**
     * main function
     * activates de bandAccelerometerEventListener
     * resets and runs the CountDown timer
     * and the end of the timer if all positions aren't calibrate yet
     * uses setCalibrate function
     *
     * this function is called by startButton
     */
    private void runCountDown(){
        startButton.setVisibility(View.INVISIBLE);
        new AccelerometerSubscriptionTask().execute();
        cdCalibrate = new CountDownTimer(CALIB_POSITION_DURATION,1) {
            @Override
            public void onTick(long millisUntilFinished) {
                bar.setProgress(bar.getMax() - (int)millisUntilFinished);
            }

            @Override
            public void onFinish() {
                calibPosition.setCalibCoordinate(flags[index]);
                // modify default coordinate

                    index++;
                    if(index >= flags.length){
                        try {
                            client.getSensorManager()
                                    .unregisterAccelerometerEventListener
                                            (bandAccelerometerEventListener);
                            // stop the listener
                        } catch (BandIOException e) {
                            e.printStackTrace();
                        }
                        Intent intent = new Intent(getApplicationContext(),LevelChoiceActivity.class);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }else{
                        setCalibrate(); // run the main function
                        startButton.setVisibility(View.VISIBLE);
                    }
            }
        }.start();

    }

    /**
     * update the text in the TextView namePostion
     */
    private void setTextView(){
        new Thread(){
            @Override
            public void run(){
                // allows to change a component which belong to the main Thread
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        namePosition.setText(flags[index].toString());
                        // set Text which matchs positionband type nam

                    }
                });
            }
        }.start();
    }

    /**
     * @see TutorialActivity
     */
    private class AccelerometerSubscriptionTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            try {


                if(client != null){
                    client.getSensorManager().registerAccelerometerEventListener(bandAccelerometerEventListener, SampleRate.MS128);
                }
            } catch (BandException e) {
            } catch (Exception e) {

            }
            return null;
        }
    }
}
