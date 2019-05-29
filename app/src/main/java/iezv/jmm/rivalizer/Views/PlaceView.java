package iezv.jmm.rivalizer.Views;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import iezv.jmm.rivalizer.Adapters.GameAdapter;
import iezv.jmm.rivalizer.POJO.Game;
import iezv.jmm.rivalizer.POJO.Generic;
import iezv.jmm.rivalizer.POJO.Place;
import iezv.jmm.rivalizer.R;

public class PlaceView extends AppCompatActivity {

    Place currentPlace;
    ImageView photoPlace;
    TextView placeDistance;
    TextView placeAdscribed;
    TextView placeName;
    TextView placeReview;
    Button adscribeUnadscribe;
    Button goMap;
    private RecyclerView rvAvGames;

    private FirebaseAuth mAuth;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference placesDB = database.getReference("places");
    DatabaseReference gamesDB = database.getReference("games");

    private List<Game> availableGames = new ArrayList<Game>();

    static final int GET_GAMES_REQUEST = 13;

    FirebaseUser user = null;
    DatabaseReference placeRef = null;
    DatabaseReference players = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_view);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        photoPlace = findViewById(R.id.photoPlaceView);
        placeAdscribed = findViewById(R.id.placeAdscribedView);
        placeDistance = findViewById(R.id.placeDistanceView);
        placeName = findViewById(R.id.placeNameView);
        placeReview = findViewById(R.id.place_review);
        adscribeUnadscribe = findViewById(R.id.adscribeUnadscribe);
        goMap = findViewById(R.id.viewPlaceMap);

        Bundle data = getIntent().getExtras();
        currentPlace = data.getParcelable("place");

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        placeRef = placesDB.child(currentPlace.getIdPlace());
        players = placeRef.child("players");

        String photoLink = currentPlace.getUrlPhoto();
        Picasso.with(PlaceView.this).load(Uri.parse(photoLink)).into(photoPlace);
        placeDistance.setText(currentPlace.getCoordinates());
        placeName.setText(currentPlace.getName());
        //placeReview.setText(currentPlace.getReview());

        setNumAdscribed();

        getAvailableGames();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ArrayList<Generic> mGames = new ArrayList<Generic>();

                gamesDB.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot parent) {
                        for(DataSnapshot child : parent.getChildren() ){
                            Generic game = new Generic();
                            game.setIdGeneric(child.child("idGame").getValue(String.class));
                            game.setNameGeneric(child.child("name").getValue(String.class));
                            game.setPhotoGeneric(child.child("urlPhoto").getValue(String.class));
                            mGames.add(game);
                            for(Game trGame : availableGames){
                                if(trGame.getIdGame().equals(game.getIdGeneric())){
                                    mGames.remove(game);
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                Intent intent = new Intent(PlaceView.this, GenericView.class);
                intent.putExtra("genCode", 2);
                intent.putExtra("games", mGames);
                startActivityForResult(intent, GET_GAMES_REQUEST);
            }
        });

        buttonHandler();
    }


    public void setNumAdscribed(){

        players.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot mPlayers) {
                int count = 0;
                for(DataSnapshot i : mPlayers.getChildren()){
                    count++;
                }
                placeAdscribed.setText(count+"");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void buttonHandler(){
        placeRef.child("players").child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    existe();
                }else{
                    noExiste();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void existe() {

        adscribeUnadscribe.setText(R.string.unadscribe_to);

        adscribeUnadscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference placeRef = placesDB.child(currentPlace.getIdPlace());
                placeRef.child("players").child(user.getUid()).removeValue();
                noExiste();
            }
        });
    }

    public void noExiste() {

        adscribeUnadscribe.setText(R.string.adscribe_to);

        adscribeUnadscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference placeRef = placesDB.child(currentPlace.getIdPlace());
                placeRef.child("players").child(user.getUid()).setValue(1);
                existe();
            }
        });
    }

    public void getAvailableGames(){
        final ArrayList<String> authGames = new ArrayList<String>();

        placeRef.child("games").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot parent) {
                for(DataSnapshot child : parent.getChildren()){
                    authGames.add(child.getKey());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        gamesDB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot parent) {
                for(DataSnapshot child : parent.getChildren()){
                    for(String authGame : authGames){
                        if(child.child("idGame").getValue().equals(authGame)){
                            Game game = new Game();
                            game.setIdGame(child.child("game").getValue(String.class));
                            game.setName(child.child("name").getValue(String.class));
                            game.setDescription(child.child("description").getValue(String.class));
                            game.setRules(child.child("rules").getValue(String.class));
                            game.setValidated(child.child("validated").getValue(Integer.class));
                            game.setUrlPhoto(child.child("urlPhoto").getValue(String.class));
                            availableGames.add(game);
                        }
                    }

                }

                initRecycler();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void initRecycler(){

        final GameAdapter adapter = new GameAdapter(PlaceView.this);
        if(rvAvGames!=null) {
            rvAvGames.setAdapter(adapter);
            GridLayoutManager grManager = new GridLayoutManager(this, 2);

            rvAvGames.setLayoutManager(grManager /*new GridLayoutManager(getActivity(), 2)*/);
            adapter.setGames(availableGames);
            rvAvGames.setAdapter(adapter);
        }

        touchHandler();
    }


    public void touchHandler(){

        final GestureDetector mGestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }
        });

        rvAvGames.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {
                View child = recyclerView.findChildViewUnder(motionEvent.getX(), motionEvent.getY());

                if (child != null && mGestureDetector.onTouchEvent(motionEvent)) {

                    int position = recyclerView.getChildAdapterPosition(child);
                    Game item = availableGames.get(position);
                    /*if(filtering){
                        item = filteredBooks.get(position);
                    }else {
                        item = myBooks.get(position);
                    }*/
                    Intent intent = new Intent(PlaceView.this, GameView.class);
                    intent.putExtra("game", item);
                    startActivity(intent);

                    return true;
                }
                return false;
            }

            @Override
            public void onTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean b) {

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        Log.v("ZZT", "Retornando... "+resultCode);
        if (requestCode == GET_GAMES_REQUEST) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                ArrayList<String> checkeds = data.getStringArrayListExtra("checkeds");
                for(String check : checkeds){
                    DatabaseReference gameRef = placesDB.child(check);
                    placeRef.child("games").child(currentPlace.getIdPlace()).setValue(1);
                }
                noExiste();
            }
        }
    }
}
