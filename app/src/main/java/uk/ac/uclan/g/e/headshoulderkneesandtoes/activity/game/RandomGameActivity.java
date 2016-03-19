package uk.ac.uclan.g.e.headshoulderkneesandtoes.activity.game;

import android.content.Intent;
import android.media.MediaPlayer;
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

import uk.ac.uclan.g.e.headshoulderkneesandtoes.activity.ListBluetoothDeviceActiviy;
import uk.ac.uclan.g.e.headshoulderkneesandtoes.activity.TutorialActivity;
import uk.ac.uclan.g.e.headshoulderkneesandtoes.bandposition.PositionBand;
import uk.ac.uclan.g.e.headshoulderkneesandtoes.R;
import uk.ac.uclan.g.e.headshoulderkneesandtoes.bandposition.RandomPosition;

/**
 * @see PositionBand
 * @see RandomPosition
 */
public class RandomGameActivity extends AppCompatActivity {
    private Bundle bundle;
        // data from last activity
    private MediaPlayer backgroundSong;
    private RandomPosition randomPosition;
        //
    private TextView textView, score;
        // postion and score
    private Button skipButton;
    private CountDownTimer generalTimer, // duration of the game
                            iterationTimer; // timer between each position

    private ProgressBar generalProgressBar,
                        // informs about time remaining before the end of game
                        iterationProgressBar;
                        // informs about time remaining before end of iteration
    private int point;// point scored


    private final int MAX_ITERATION_BAR = 100;
    private final long GAMEDURATION =60000;

    private long currentTimePlayed, iterationDuration; // time played



    private BandClient client;// band
    private PositionBand positionBand;
    private BandAccelerometerEventListener bandAccelerometerEventListener =
            new BandAccelerometerEventListener() {
                @Override
                public void onBandAccelerometerChanged(BandAccelerometerEvent event) {
                    PositionBand pos = new PositionBand(event);
                    String  congratText="";
                    if (pos.equals(positionBand)) { // if we got the right position
                        point++;
                        if(point==1)
                            congratText="Good!";
                        else if(point==2)
                            congratText="Nice You get it keep going!";
                        else if(point==3)
                            congratText ="Well Played!";
                        else if(point==4)
                            congratText= "Good Job!";
                        else if(point==5)
                            congratText= "Amazing!";
                        else if(point ==6)
                            congratText="Wonderful!";
                        else if(point>=7)
                            congratText="BreathTaking";

                        Toast.makeText(getApplicationContext(),congratText,
                                Toast.LENGTH_SHORT).show();

                        setScore();

                        newIteration();
                    }
                }
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_random_game);
        init();
        generalTimer = new CountDownTimer(GAMEDURATION,1) {
            @Override
            public void onTick(long millisUntilFinished) {
                generalProgressBar.setProgress((int)
                        (generalProgressBar.getMax()-millisUntilFinished)
                );
                currentTimePlayed++;
            }

            @Override
            public void onFinish() {
                iterationTimer.cancel();
                backgroundSong.stop();
                try {
                    client.getSensorManager()
                            .unregisterAccelerometerEventListener(bandAccelerometerEventListener);
                } catch (BandIOException e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent(getApplicationContext(),ScoreActivity.class);
                bundle.putInt("FINAL_SCORE",point);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        }.start();
       newIteration();


    }

    /**
     * initialize the activity
     */
    private void init(){
        randomPosition = new RandomPosition();
        textView = (TextView) findViewById(R.id.randText);

        backgroundSong = MediaPlayer.create(this,R.raw.background_sound);
        point = 0;

        score =(TextView) findViewById(R.id.textViewPoint);
        score.setText("Points:"+point);
        //iterationDuration = GAMEDURATION/10;

        iterationProgressBar = (ProgressBar)findViewById(R.id.iterationProgressbar);
        iterationProgressBar.setMax(MAX_ITERATION_BAR);

        generalProgressBar = (ProgressBar)findViewById(R.id.generalProgressBar);
        generalProgressBar.setMax((int) GAMEDURATION);
        generalProgressBar.setProgress(1);

        bundle = getIntent().getExtras();


        new AccelerometerSubscriptionTask().execute();
        backgroundSong.start();
                // set the listener on
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
       if(currentTimePlayed < GAMEDURATION){
           iterationProgressBar.setProgress(0);
           positionBand = randomPosition.getRandPosition();
           // new BandPosition

           setTextView();

           if(iterationTimer != null){ // if the iterationTimer is playing
               iterationTimer.cancel();

           }
           iterationTimer = new CountDownTimer(10000,1) {
               // new iteration
               @Override
               public void onTick(long millisUntilFinished) {
                   int progress = (int) (millisUntilFinished/100);
                   iterationProgressBar.setProgress(iterationProgressBar.getMax() -progress);
                 //  progressStatus+=(TICK*100)/SPLASH_SCREEN_DELAY[randomNbr];
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
     * set the color and the text of the text view
     */
    private void setTextView(){
        new Thread(){
            @Override
            public void run(){
                // allows to change a component which belong to the main Thread
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        textView.setText(positionBand.getFlag().toString());
                        // set Text which matchs positionband type name
                        textView.setTextColor(getResources().getColor(randomPosition.getColorDisplayed()));

                    }
                });
            }
        }.start();
    }

    /**
     * set the score
     */
    private void setScore(){
        new Thread(){
            @Override
            public void run(){
               runOnUiThread(new Runnable() {
                   @Override
                   public void run() {
                       score.setText("Points:" + point);
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
