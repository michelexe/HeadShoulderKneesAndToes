package uk.ac.uclan.g.e.headshoulderkneesandtoes.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import uk.ac.uclan.g.e.headshoulderkneesandtoes.R;
import uk.ac.uclan.g.e.headshoulderkneesandtoes.activity.game.RandomGameActivity;

public class LevelChoiceActivity extends AppCompatActivity {

    private Button button_easy, button_normal, button_hard;
    private ImageButton button_tuto;
    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level_choice);

        init();

    }

    private void init(){
        this.button_easy = (Button)findViewById(R.id.button_easy_mode);

        this.button_normal = (Button) findViewById(R.id.button_normal_mode);

        this.button_hard = (Button)findViewById(R.id.button_hard_mode);

        this.button_tuto =(ImageButton) findViewById(R.id.button_tutorial);

        this.bundle = getIntent().getExtras();
            // get back the informations sended by the last activity

        this.addClickListener();



    }


    private void addClickListener(){
        this.button_easy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Under construction",
                        Toast.LENGTH_LONG).show();
            }
        });


        this.button_normal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(getApplicationContext(),RandomGameActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        this.button_hard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Wait i'm not finished yet",
                        Toast.LENGTH_LONG).show();
            }
        });

        this.button_tuto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),TutorialActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }
}
