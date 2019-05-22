package iezv.jmm.rivalizer.Views;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import iezv.jmm.rivalizer.POJO.Game;
import iezv.jmm.rivalizer.R;

public class GameView extends AppCompatActivity {

    Game currentGame;
    ImageView gamePhoto;
    ImageView gamePhotoFull;
    TextView gameName;
    TextView gameDesc;
    TextView gameRules;
    RecyclerView rvAvPlaces;
    Button addUserToGame;

    private FirebaseAuth mAuth;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference gamesDB = database.getReference("games");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_view);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAuth = FirebaseAuth.getInstance();

        gamePhoto = findViewById(R.id.gamePhoto);
        gamePhotoFull = findViewById(R.id.gamePhotoFull);
        gameName = findViewById(R.id.gameName);
        gameDesc = findViewById(R.id.gameDesc);
        gameRules = findViewById(R.id.gameRules);
        rvAvPlaces = findViewById(R.id.rvAvPlaces);
        addUserToGame = findViewById(R.id.addUserToGame);

        Bundle data = getIntent().getExtras();
        currentGame = data.getParcelable("game");

        String photoLink = currentGame.getUrlPhoto();
        Picasso.with(GameView.this).load(Uri.parse(photoLink)).into(gamePhotoFull);
        Picasso.with(GameView.this).load(Uri.parse(photoLink)).into(gamePhoto);
        gameName.setText(currentGame.getName());
        gameDesc.setText(currentGame.getDescription());
        gameRules.setText(currentGame.getRules());




        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        buttonHandler();
    }

    public void buttonHandler(){
        addUserToGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser user = mAuth.getCurrentUser();
                Log.v("GAMEID", currentGame.getIdGame());
                DatabaseReference gameRef = gamesDB.child(currentGame.getIdGame());
                gameRef.child("players").child(user.getUid()).setValue(1);
            }
        });
    }

}
