package iezv.jmm.rivalizer.Fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.SearchView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import iezv.jmm.rivalizer.Adapters.PlaceAdapter;
import iezv.jmm.rivalizer.POJO.Place;
import iezv.jmm.rivalizer.R;
import iezv.jmm.rivalizer.Views.PlaceRegister;
import iezv.jmm.rivalizer.Views.PlaceView;

import static java.lang.Double.parseDouble;

public class PlaceFragment extends Fragment {

    private RecyclerView rvPlaces;
    private List<Place> myPlaces = new ArrayList<Place>();
    private List<Place> filteredList = new ArrayList<Place>();
    private ImageButton goRegister;
    private SearchView searchInPlaces;
    public boolean filtering = false;


    Location myLocation = null;
    DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    DatabaseReference ref = database.child("places");

    private static DecimalFormat df2 = new DecimalFormat("#.##");



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_place, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        rvPlaces = (RecyclerView) getView().findViewById(R.id.rvPlaces);
        goRegister = (ImageButton) getView().findViewById(R.id.btn_register_place);
        searchInPlaces = (SearchView) getView().findViewById(R.id.searchInPlaces);

        location();
        fakePlaces();
        //setDistances();
        setupSearch();

    }

    public void location() {
        LocationManager mLocationManager = (LocationManager) getActivity().getSystemService(getActivity().LOCATION_SERVICE);
        Location bestResult = null;
        float bestAccuracy = Float.MAX_VALUE;
        long bestAge = Long.MIN_VALUE;
        List<String> matchingProviders = mLocationManager.getAllProviders();

        for (String provider : matchingProviders) {

            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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

    private void listener(){
        goRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PlaceRegister.class);
                startActivity(intent);

            }
        });
    }



    private void fakePlaces() {

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot parent) {
                for(DataSnapshot child : parent.getChildren() ){
                    Place place = new Place();
                    place.setIdPlace(child.child("idPlace").getValue(String.class));
                    place.setName(child.child("name").getValue(String.class));
                    place.setAddress(child.child("address").getValue(String.class));
                    place.setUrlPhoto(child.child("urlPhoto").getValue(String.class));
                    place.setReview(child.child("review").getValue(String.class));
                    int playersAdscribed = (int) child.child("players").getChildrenCount();
                    place.setPlayersAdscribed(playersAdscribed);

                    myPlaces.add(place);
                }

                // initRecycler();
                setDistances();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void initRecycler() {
        final PlaceAdapter adapter = new PlaceAdapter(getActivity());
        if(rvPlaces!=null){
            rvPlaces.setAdapter(adapter);
            rvPlaces.setLayoutManager(new LinearLayoutManager(getActivity()));
            adapter.setPlaces(sortByProximity(myPlaces));
            rvPlaces.setAdapter(adapter);
        }

        touchHandler();
    }

    public void setDistances(){

        if(myLocation!=null){
            for(Place place : myPlaces){

                String[] coordinates = place.getAddress().split(",");
                Double distance = distance(parseDouble(coordinates[0]), parseDouble(coordinates[1]), myLocation.getLatitude(), myLocation.getLongitude());

                place.setCoordinates(df2.format(distance)+" km");
            }
        }
        initRecycler();

        listener();

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

    public void touchHandler(){

        final GestureDetector mGestureDetector = new GestureDetector(getActivity(), new GestureDetector.SimpleOnGestureListener() {
            @Override public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }
        });

        rvPlaces.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {
                View child = recyclerView.findChildViewUnder(motionEvent.getX(), motionEvent.getY());

                if (child != null && mGestureDetector.onTouchEvent(motionEvent)) {

                    int position = recyclerView.getChildAdapterPosition(child);
                    Place item = myPlaces.get(position);
                    /*if(filtering){
                        item = filteredBooks.get(position);
                    }else {
                        item = myBooks.get(position);
                    }*/
                    Intent intent = new Intent(getActivity(), PlaceView.class);
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

    public void setupSearch(){
        searchInPlaces.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                processQuery(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                processQuery(query);
                return false;
            }
        });
    }

    private void processQuery(String query){
        List<Place> searchingGames = new ArrayList<Place>();

        for(Place currentPlace : myPlaces){
            if(currentPlace.getName().toLowerCase().contains(query.toLowerCase())){
                searchingGames.add(currentPlace);
            }
        }

        final PlaceAdapter adapter = new PlaceAdapter(getActivity());
        filteredList = searchingGames;
        adapter.setPlaces(sortByProximity(filteredList));
        rvPlaces.setAdapter(adapter);
        filtering = true;
    }

    public List<Place> sortByProximity(List<Place> places){
        Collections.sort(places, new Comparator<Place>() {
            @Override
            public int compare(Place o1, Place o2) {
                if(o1.getCoordinates()!=null&&o2.getCoordinates()!=null){
                    if (Double.parseDouble(o1.getCoordinates().substring(0, o1.getCoordinates().length()-3).replaceAll(",",".")) ==
                            Double.parseDouble(o2.getCoordinates().substring(0, o1.getCoordinates().length()-3).replaceAll(",",".")))
                    {
                        return 0;
                    }
                    else if (Double.parseDouble(o1.getCoordinates().substring(0, o1.getCoordinates().length()-3).replaceAll(",",".")) <
                            Double.parseDouble(o2.getCoordinates().substring(0, o1.getCoordinates().length()-3).replaceAll(",",".")))
                    {
                        return -1;
                    }
                }

                return 1;
            }
        });

        return places;
    }
}
