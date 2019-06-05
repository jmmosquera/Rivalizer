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
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import iezv.jmm.rivalizer.Adapters.GameAdapter;
import iezv.jmm.rivalizer.Adapters.PlaceAdapter;
import iezv.jmm.rivalizer.POJO.Game;
import iezv.jmm.rivalizer.POJO.Place;
import iezv.jmm.rivalizer.POJO.Rival;
import iezv.jmm.rivalizer.R;

import static java.lang.Double.parseDouble;

public class RivalView extends AppCompatActivity {

    Rival currentRival;
    TextView rivalName;
    TextView rivalCommonPlaces;
    ImageView rivalPhotograph;
    RecyclerView rvCommonGames;
    RecyclerView rvCommonPlaces;

    Location myLocation = null;
    private static DecimalFormat df2 = new DecimalFormat("#.##");

    private List<Game> rivalGames = new ArrayList<Game>();
    private List<Place> rivalPlaces = new ArrayList<Place>();

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference placesDB = database.getReference("places");
    DatabaseReference gamesDB = database.getReference("games");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rival_view);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.rivalName = findViewById(R.id.rivalTrueName);
        this.rivalCommonPlaces = findViewById(R.id.rivalClosestPlace);
        this.rivalPhotograph = findViewById(R.id.rivalPhotograph);
        this.rvCommonGames = findViewById(R.id.rvCommonGames);
        this.rvCommonPlaces = findViewById(R.id.rvCommonPlaces);

        Bundle data = getIntent().getExtras();
        currentRival = data.getParcelable("rival");
        rivalName.setText(currentRival.getName());
        rivalCommonPlaces.setText("2");
        Picasso.with(RivalView.this).load(Uri.parse(currentRival.getUrlPhoto())).into(rivalPhotograph);

        location();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(RivalView.this, ChatView.class);
                i.putExtra("nameRival", currentRival.getName());
                i.putExtra("avatarRival", currentRival.getUrlPhoto());
                i.putExtra("idChatRival", currentRival.getCloud_id());
                startActivity(i);
            }
        });

        setDistances();
        setRecyclerViews();
    }

    private void setRecyclerViews() {

        placesDB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot parent) {
                for(DataSnapshot child : parent.getChildren() ){
                    if(child.child("players").child(currentRival.getCloud_id()).exists()){
                        Place place = new Place();
                        place.setIdPlace(child.child("idPlace").getValue(String.class));
                        place.setName(child.child("name").getValue(String.class));
                        place.setAddress(child.child("address").getValue(String.class));
                        place.setUrlPhoto(child.child("urlPhoto").getValue(String.class));
                        place.setReview(child.child("review").getValue(String.class));
                        int playersAdscribed = (int) child.child("players").getChildrenCount();
                        place.setPlayersAdscribed(playersAdscribed);
                        rivalPlaces.add(place);
                    }
                }

                setRecyclerPlaces();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        gamesDB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot parent) {
                for(DataSnapshot child : parent.getChildren() ){
                    if(child.child("players").child(currentRival.getCloud_id()).exists()){
                        Game game = new Game();
                        game.setIdGame(child.child("idGame").getValue(String.class));
                        game.setName(child.child("name").getValue(String.class));
                        game.setDescription(child.child("description").getValue(String.class));
                        game.setRules(child.child("rules").getValue(String.class));
                        game.setValidated(child.child("validated").getValue(Integer.class));
                        game.setUrlPhoto(child.child("urlPhoto").getValue(String.class));
                        rivalGames.add(game);
                    }
                }

                setRecyclerGames();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void setRecyclerGames() {
        final GameAdapter adapter = new GameAdapter(RivalView.this);
        if(rvCommonGames!=null) {
            rvCommonGames.setAdapter(adapter);
            GridLayoutManager grManager = new GridLayoutManager(this, 2);

            rvCommonGames.setLayoutManager(grManager /*new GridLayoutManager(getActivity(), 2)*/);
            adapter.setGames(rivalGames);
            rvCommonGames.setAdapter(adapter);
        }

        touchHandlerB();

    }

    public void setDistances(){
        if(myLocation!=null){
            for(Place place : rivalPlaces){

                String[] coordinates = place.getAddress().split(",");
                Double distance = distance(parseDouble(coordinates[0]), parseDouble(coordinates[1]), myLocation.getLatitude(), myLocation.getLongitude());

                place.setCoordinates(df2.format(distance)+" km");
            }
        }
        setRecyclerPlaces();

    }

    public void setRecyclerPlaces(){
        final PlaceAdapter adapter = new PlaceAdapter(this);
        if(rvCommonPlaces!=null){
            rvCommonPlaces.setAdapter(adapter);
            rvCommonPlaces.setLayoutManager(new LinearLayoutManager(this));
            adapter.setPlaces(rivalPlaces);
            rvCommonPlaces.setAdapter(adapter);
        }

        touchHandlerA();
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

    public void touchHandlerA(){

        final GestureDetector mGestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }
        });

        rvCommonPlaces.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {
                View child = recyclerView.findChildViewUnder(motionEvent.getX(), motionEvent.getY());

                if (child != null && mGestureDetector.onTouchEvent(motionEvent)) {

                    int position = recyclerView.getChildAdapterPosition(child);
                    Place item = rivalPlaces.get(position);
                    Intent intent = new Intent(RivalView.this, PlaceView.class);
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

    public void touchHandlerB(){
        final GestureDetector mGestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }
        });

        rvCommonGames.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {
                View child = recyclerView.findChildViewUnder(motionEvent.getX(), motionEvent.getY());

                if (child != null && mGestureDetector.onTouchEvent(motionEvent)) {

                    int position = recyclerView.getChildAdapterPosition(child);
                    Game item = rivalGames.get(position);
                    Intent intent = new Intent(RivalView.this, GameView.class);
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
}
