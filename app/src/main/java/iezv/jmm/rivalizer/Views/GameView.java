package iezv.jmm.rivalizer.Views;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
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

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import iezv.jmm.rivalizer.Adapters.PlaceAdapter;
import iezv.jmm.rivalizer.POJO.Game;
import iezv.jmm.rivalizer.POJO.Generic;
import iezv.jmm.rivalizer.POJO.Place;
import iezv.jmm.rivalizer.R;

import static java.lang.Double.parseDouble;

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
    DatabaseReference placesDB = database.getReference("places");

    private List<Place> playablePlaces = new ArrayList<Place>();
    Location myLocation = null;
    private static DecimalFormat df2 = new DecimalFormat("#.##");

    static final int GET_PLACES_REQUEST = 12;

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
        Log.v("ZZV", "Obteniendo localizaciones");
        location();
        getPlayablePlaces();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ArrayList<Generic> mPlaces = new ArrayList<Generic>();

                placesDB.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot parent) {

                        for(DataSnapshot child : parent.getChildren() ){
                            Generic place = new Generic();
                            place.setIdGeneric(child.child("idPlace").getValue(String.class));
                            place.setNameGeneric(child.child("name").getValue(String.class));
                            place.setPhotoGeneric(child.child("urlPhoto").getValue(String.class));
                            mPlaces.add(place);
                            for(Place trPlace : playablePlaces){
                                if(trPlace.getIdPlace().equals(place.getIdGeneric())){
                                    mPlaces.remove(place);
                                }
                            }

                        }


                        Intent intent = new Intent(GameView.this, GenericView.class);
                        intent.putExtra("genCode", 1);
                        intent.putExtra("places", mPlaces);
                        Log.v("ZZT", mPlaces+"");
                        startActivityForResult(intent, GET_PLACES_REQUEST);


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });



        buttonHandler();
    }

    public void buttonHandler(){

        FirebaseUser user = mAuth.getCurrentUser();
        Log.v("GAMEID", currentGame.getIdGame());
        DatabaseReference gameRef = gamesDB.child(currentGame.getIdGame());
        gameRef.child("players").child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
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

        addUserToGame.setText(R.string.remove_from_your_games);

        addUserToGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser user = mAuth.getCurrentUser();
                Log.v("GAMEID", currentGame.getIdGame());
                DatabaseReference gameRef = gamesDB.child(currentGame.getIdGame());
                gameRef.child("players").child(user.getUid()).removeValue();
                noExiste();
            }
        });
    }

    public void noExiste(){

        addUserToGame.setText(R.string.add_to_your_games);

        addUserToGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser user = mAuth.getCurrentUser();
                Log.v("GAMEID", currentGame.getIdGame());
                DatabaseReference gameRef = gamesDB.child(currentGame.getIdGame());
                gameRef.child("players").child(user.getUid()).setValue(1);
                existe();
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        Log.v("ZZT", "Retornando... "+resultCode);
        if (requestCode == GET_PLACES_REQUEST) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                ArrayList<String> checkeds = data.getStringArrayListExtra("checkeds");
                for(String check : checkeds){
                    DatabaseReference gameRef = placesDB.child(check);
                    gameRef.child("games").child(currentGame.getIdGame()).setValue(1);
                }
                noExiste();
            }
        }
    }

    public void getPlayablePlaces(){
        Log.v("ZZV", "Obteniendo lugares");
        placesDB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot parent) {
                for(DataSnapshot child : parent.getChildren() ) {
                    if (child.child("games").child(currentGame.getIdGame()).exists()) {
                        Place place = new Place();
                        place.setIdPlace(child.child("idPlace").getValue(String.class));
                        place.setName(child.child("name").getValue(String.class));
                        place.setAddress(child.child("address").getValue(String.class));
                        place.setUrlPhoto(child.child("urlPhoto").getValue(String.class));
                        place.setReview(child.child("review").getValue(String.class));
                        playablePlaces.add(place);
                    }
                }
                setDistances();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    public void setDistances(){
        Log.v("ZZV", "Obteniendo distancias");
        if(myLocation!=null){
            Log.v("DISTANCE", playablePlaces.size()+" lugares");
            for(Place place : playablePlaces){

                String[] coordinates = place.getAddress().split(",");
                Double distance = distance(parseDouble(coordinates[0]), parseDouble(coordinates[1]), myLocation.getLatitude(), myLocation.getLongitude());

                place.setCoordinates(df2.format(distance)+" km");
                Log.v("DISTANCE", distance+ "Km.");
            }
        }else{
            Log.v("DISTANCE", "NO hay location");
        }
        initRecycler();

    }

    public void location() {
        LocationManager mLocationManager = (LocationManager) this.getSystemService(this.LOCATION_SERVICE);
        Location bestResult = null;
        float bestAccuracy = Float.MAX_VALUE;
        long bestAge = Long.MIN_VALUE;
        List<String> matchingProviders = mLocationManager.getAllProviders();

        for (String provider : matchingProviders) {

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            Location location = mLocationManager.getLastKnownLocation(provider);

            if (location != null) {

                float accuracy = location.getAccuracy();
                long time = location.getTime();

                if (accuracy < bestAccuracy) {

                    bestResult = location;
                    bestAccuracy = accuracy;
                    bestAge = time;

                }
            }
        }

        // Return best reading or null
        /*if (bestAccuracy > minAccuracy || (System.currentTimeMillis() - bestAge) > maxAge) {
            //return null;
        } else {

        }*/
        Log.v("BESTLOCATION", bestResult+"");
        myLocation = bestResult;

    }

    public double distance(double lat1, double lon1, double lat2, double lon2) {
        if ((lat1 == lat2) && (lon1 == lon2)) {
            return 0;
        }
        else {
            double theta = lon1 - lon2;
            double dist = Math.sin(Math.toRadians(lat1)) * Math.sin(Math.toRadians(lat2)) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.cos(Math.toRadians(theta));
            dist = Math.acos(dist);
            dist = Math.toDegrees(dist);
            dist = dist * 60 * 1.1515;

            return dist * 1.609344;

        }
    }
    private void initRecycler() {
        Log.v("ZZV", "Obteniendo recycler");
        final PlaceAdapter adapter = new PlaceAdapter(this);
        if(rvAvPlaces!=null){
            rvAvPlaces.setAdapter(adapter);
            rvAvPlaces.setLayoutManager(new LinearLayoutManager(this));
            adapter.setPlaces(playablePlaces);
            rvAvPlaces.setAdapter(adapter);
        }

        touchHandler();
    }

    public void touchHandler(){

        final GestureDetector mGestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }
        });

        rvAvPlaces.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {
                View child = recyclerView.findChildViewUnder(motionEvent.getX(), motionEvent.getY());

                if (child != null && mGestureDetector.onTouchEvent(motionEvent)) {

                    int position = recyclerView.getChildAdapterPosition(child);
                    Place item = playablePlaces.get(position);
                    /*if(filtering){
                        item = filteredBooks.get(position);
                    }else {
                        item = myBooks.get(position);
                    }*/
                    Intent intent = new Intent(GameView.this, PlaceView.class);
                    intent.putExtra("place", item);
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

}
