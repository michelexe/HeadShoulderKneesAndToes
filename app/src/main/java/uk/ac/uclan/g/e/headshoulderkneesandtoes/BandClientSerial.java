package uk.ac.uclan.g.e.headshoulderkneesandtoes;

import com.microsoft.band.BandClient;

import java.io.Serializable;

/**
 * Created by ENZO on 03/03/2016.
 * BandClient isn't serializable so
 * the idea is to create a class which is serializable
 * and which is going to contain our bandClient object
 */
public class BandClientSerial implements Serializable {
    BandClient band;
    public BandClientSerial(BandClient b){
        this.band= b;
    }

    public BandClient getBand(){
        return this.band;
    }
}
