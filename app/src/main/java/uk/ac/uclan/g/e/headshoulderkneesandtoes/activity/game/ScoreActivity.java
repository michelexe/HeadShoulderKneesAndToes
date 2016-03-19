package uk.ac.uclan.g.e.headshoulderkneesandtoes.activity.game;

import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import uk.ac.uclan.g.e.headshoulderkneesandtoes.R;
import uk.ac.uclan.g.e.headshoulderkneesandtoes.activity.LevelChoiceActivity;
import uk.ac.uclan.g.e.headshoulderkneesandtoes.activity.MainActivity;

public class ScoreActivity extends AppCompatActivity {

    private TextView score;
    private Button replay,menu,exit;
    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        init();
        addClickListener();
    }


    private void init(){
        bundle = getIntent().getExtras();
        int final_score= bundle.getInt("FINAL_SCORE");
        score =(TextView)findViewById(R.id.textScore);
        score.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);

        score.setText("Score: "+final_score);


        replay = (Button)findViewById(R.id.replayButton);
        menu = (Button)findViewById(R.id.menuButon);
        exit =(Button)findViewById(R.id.exitButton);

    }

    private void addClickListener(){
        replay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(getApplicationContext(),RandomGameActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(getApplicationContext(), LevelChoiceActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });


    }
}
