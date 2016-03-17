package uk.ac.uclan.g.e.headshoulderkneesandtoes;

import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.microsoft.band.BandClient;
import com.microsoft.band.BandException;
import com.microsoft.band.BandIOException;
import com.microsoft.band.sensors.BandAccelerometerEvent;
import com.microsoft.band.sensors.BandAccelerometerEventListener;
import com.microsoft.band.sensors.SampleRate;

/**
 * @see PositionBand
 * @see RandomPosition
 */
public class RandomGameActivity extends AppCompatActivity {
    private Bundle bundle;
    private BandClient client;
    private RandomPosition randomPosition;
    private TextView textView, score;
    private Button skipButton;
    private CountDownTimer generalTimer, // duration of the game
                            iterationTimer; // timer between each position

    private ProgressBar generalProgressBar,
                        // informs about time remaining before the end of game
                        iterationProgressBar;
                        // informs about time remaining before end of iteration
    private int point,// point scored
            progressGeneralBar=0, progressIteration=0;

    private final int MAX_ITERATION_BAR = 100;

    private final long gameDuration=60000;
            private long currentTimePlayed, iterationDuration; // time played

    private PositionBand positionBand;

    private BandAccelerometerEventListener bandAccelerometerEventListener =
            new BandAccelerometerEventListener() {
                @Override
                public void onBandAccelerometerChanged(BandAccelerometerEvent event) {
                    PositionBand pos = new PositionBand(event);
                    if (pos.equals(positionBand)) { // if we got the right position
                        Toast.makeText(getApplicationContext(),"toucher",
                                Toast.LENGTH_SHORT).show();
                        point++;
                        newIteration();
                    }
                }
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_random_game);
        init();
        generalTimer = new CountDownTimer(gameDuration,1) {
            @Override
            public void onTick(long millisUntilFinished) {
                currentTimePlayed=+millisUntilFinished;
                generalProgressBar.setProgress(progressGeneralBar);
                try {
                    client.getSensorManager()
                            .unregisterAccelerometerEventListener(bandAccelerometerEventListener);
                } catch (BandIOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFinish() {
                iterationTimer.cancel();
                Toast.makeText(getApplicationContext(),"fin",Toast.LENGTH_LONG).show();
            }
        }.start();
        newIteration();
        new AccelerometerSubscriptionTask().execute();
        // start the listener
    }

    /**
     * initialize the activity
     */
    private void init(){
        randomPosition = new RandomPosition();
        textView = (TextView) findViewById(R.id.randText);
        point = 0;
        //iterationDuration = gameDuration/10;
        generalProgressBar = (ProgressBar)findViewById(R.id.generalProgressBar);
        iterationProgressBar = (ProgressBar)findViewById(R.id.iterationProgressbar);
        iterationProgressBar.setMax(MAX_ITERATION_BAR);
    }


    private void addClickListener(){

    }

    /**
     * if the game is not oover yet
     * starts a new iteration,
     * with a new Band position
     * iterationTimer restarted
     */
    private void newIteration(){
       if(currentTimePlayed < gameDuration){
           progressIteration=0;
           positionBand = randomPosition.getRandPosition();
           // new BandPosition

           textView.setText(positionBand.getFlag().toString());
           // set Text which matchs positionband type name
           textView.setTextColor(getResources().getColor(randomPosition.getColorDisplayed()));

           if(iterationTimer != null){ // if the iterationTimer is playing
               iterationTimer.cancel();

           }
           iterationTimer = new CountDownTimer(5000,1) {
               // new iteration
               @Override
               public void onTick(long millisUntilFinished) {
                   iterationProgressBar.setProgress(progressIteration);
                   progressIteration++;
               }

               @Override
               public void onFinish() {
                   newIteration();
                   //
               }
           }.start();
       }
    }


    /**
     * @see TutorialActivity
     */
    private class AccelerometerSubscriptionTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            try {

                int i = bundle.getInt(ListBluetoothDeviceActiviy.BAND_POSITION);
                client = ListBluetoothDeviceActiviy.getBand(i); // get the band connected to the device
                if(client != null){
                    client.getSensorManager().registerAccelerometerEventListener(bandAccelerometerEventListener, SampleRate.MS128);
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
