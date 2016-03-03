package uk.ac.uclan.g.e.headshoulderkneesandtoes;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.microsoft.band.BandClient;
import com.microsoft.band.BandClientManager;
import com.microsoft.band.BandException;
import com.microsoft.band.BandInfo;
import com.microsoft.band.BandPendingResult;
import com.microsoft.band.ConnectionState;
import com.microsoft.band.notifications.BandNotificationManager;

import java.io.InterruptedIOException;
import java.io.Serializable;
import java.util.ArrayList;

public class ListBluetoothDevice extends AppCompatActivity {
    private static final int REQUEST_ENABLE_BT = 0;
    private ListView listView;
    private BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    private CustomAdapter mAdapter;
    private final String BAND_CHOICE = "Chose your device";
    private final String EMPTY="currently no devices paired";
    private BandInfo[] pairedBands;
    private static BandClient[] pairedClientBand;
    public static final String BAND_POSITION ="BandClient_position";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_bluetooth_device);

        init();
        if(bluetoothAdapter ==null){
            raisedNotification();
        }else {
            if(!bluetoothAdapter.isEnabled()){
                bluetoothActivation();
            }else{
                displayListView();
            }
        }
    }

    /**
     * ask for bluetooth activation
     */
    private void bluetoothActivation() {
        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
    }



    private void raisedNotification(){
        final Notification notificationBluetooth =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.warning_icone)
                        .setContentTitle("Error")
                        .setContentText("Bluetooth isn't available on this device").build();
        // the workseet method didn't work
        //

        final NotificationManager notifManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        notifManager.notify(1, notificationBluetooth);
    }

    private void init(){
        mAdapter = new CustomAdapter(this);
        listView = (ListView)findViewById(R.id.liste_devices);
    }


    private void displayListView(){
        pairedBands = BandClientManager.getInstance().getPairedBands();
        pairedClientBand = new BandClient[pairedBands.length];
        // Define a new Adapter
        mAdapter.addSectionHeaderItem(BAND_CHOICE);
        if(pairedBands.length==0){
            mAdapter.addItem(EMPTY);
        }
        for(int i=0; i<pairedBands.length;i++){
            mAdapter.addItem(pairedBands[i].getName());
            pairedClientBand[i]= BandClientManager
                    .getInstance()
                    .create(getApplicationContext(),pairedBands[i]);
        }
        listView.setAdapter(mAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                String itemValue = (String) listView.getItemAtPosition(position);
                if(itemValue != BAND_CHOICE){
                        if(bluetoothAdapter.isEnabled()){
                            ConnectionBand connectionBand =
                                    new ConnectionBand(position-1);
                            connectionBand.start();
                        }else{
                            bluetoothActivation();
                        }


                }
            }
        });
    }

    /**
     * treat the answer of user after bluetooth activation request
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode != REQUEST_ENABLE_BT)
            return; // end of method
        if (resultCode == RESULT_OK) {
            Toast.makeText(getApplicationContext(), "Bluetooth is activated",
                    Toast.LENGTH_LONG).show();
            displayListView();

            // L'utilisation a activé le bluetooth
        } else {
            Toast.makeText(getApplicationContext(),"Bluetooth is required to play",
                    Toast.LENGTH_LONG).show();
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            // L'utilisation n'a pas activé le bluetooth
        }
    }

    // https://developer.microsoftband.com/Content/docs/Microsoft%20Band%20SDK.pdf

    /**
     * private class which will allows connection
     * to a band
     */
    private class ConnectionBand extends Thread {
        private BandPendingResult<ConnectionState> pendingResult;
        private BandClient bandClient;
        private int indice;

        /**
         * take an int that allows to transfer
         * the band to other activities
         * @param indice
         */
        public ConnectionBand(int indice){
            this.indice = indice;
            this.bandClient = pairedClientBand[indice];
            this.bandClient.disconnect();
            this.pendingResult = this.bandClient.connect();

        }


        @Override
        public void run(){
            try {
                ConnectionState state =pendingResult.await();
                if(state == ConnectionState.CONNECTED){ // if connected
                    displaySuccessNotification();
                    MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(),
                            R.raw.connection_successful);
                    mediaPlayer.start(); // play a sound
                    Thread.sleep(3000); //
                    Intent intent = new Intent(getApplicationContext(), SplashScreen.class);
                            // loading screen
                    Bundle bundle = new Bundle();
                    bundle.putInt(BAND_POSITION,this.indice);
                    intent.putExtras(bundle);
                    startActivity(intent);

                }else {

                }
            }catch (InterruptedException ex){
                ex.printStackTrace();
            }catch (BandException ex){
                ex.printStackTrace();
            }
        }

        /**
         * display a new Notification
         */
        private final void displaySuccessNotification(){
            NotificationCompat.InboxStyle inboxStyle =
                    new NotificationCompat.InboxStyle();
            String[] informations = {"your are now connected to",
                    "your Microsoft Band"};
            // Sets a title for the Inbox in expanded layout
            inboxStyle.setBigContentTitle("do you wanna play ?");
            // Moves informations into the expanded layout
            for (int i=0; i < informations.length; i++) {
                inboxStyle.addLine(informations[i]);
            }
            // Moves the expanded layout object into the notification object.

            Notification notification =
                    new NotificationCompat.Builder(getApplicationContext())
                            .setSmallIcon(R.drawable.smiley_happy)
                            .setContentTitle("Connection succesful!")
                            .setAutoCancel(true)
                            .setStyle(inboxStyle)
                            .build();


            final NotificationManager notifManager =
                    (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notification.vibrate = new long[]{0,100,200,300};
            // option - auto cancel after select

            notifManager.notify(1, notification);

        }


    }

    /**
     *
     * @param indice
     * @return BandClient selected
     */
    public static BandClient getBand(int indice){
        return pairedClientBand[indice];
    }
}
