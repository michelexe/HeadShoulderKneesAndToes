package uk.ac.uclan.g.e.headshoulderkneesandtoes;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.microsoft.band.sensors.BandAccelerometerEvent;
import com.microsoft.band.sensors.BandAccelerometerEventListener;

public class UnrestrickedModeActivity extends AppCompatActivity {

    private BandAccelerometerEventListener bandAccelerometerEventListener = new BandAccelerometerEventListener() {
        @Override
        public void onBandAccelerometerChanged(BandAccelerometerEvent event) {
            PositionBand positionBand = new PositionBand(event);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unrestricked_mode);
    }
}
