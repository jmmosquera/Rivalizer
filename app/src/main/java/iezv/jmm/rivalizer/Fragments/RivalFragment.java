package iezv.jmm.rivalizer.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import iezv.jmm.rivalizer.Adapters.RivalAdapter;
import iezv.jmm.rivalizer.POJO.Game;
import iezv.jmm.rivalizer.POJO.Rival;
import iezv.jmm.rivalizer.R;
import iezv.jmm.rivalizer.Views.RivalView;

public class RivalFragment extends Fragment {

    private RecyclerView rvRivals;
    private List<Rival> myRivals = new ArrayList<Rival>();
    private List<Rival> filteredList = new ArrayList<Rival>();
    private SearchView searchInRivals;
    public boolean filtering = false;

    private FirebaseAuth mAuth;

    public ArrayList<String> playableRivals = new ArrayList<String>();

    DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    DatabaseReference refPlaces = database.child("places");
    DatabaseReference refPlayers = database.child("users");
    DatabaseReference refGames = database.child("games");

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_rival, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();

        rvRivals = (RecyclerView) getView().findViewById(R.id.rvRivals);
        searchInRivals = (SearchView) getView().findViewById(R.id.searchInRivals);

        fakeRivals();


        touchHandler();
        setupSearch();
    }

    private void fakeRivals(){

        final FirebaseUser user = mAuth.getCurrentUser();
        final List<Rival> allRivals = new ArrayList<Rival>();
        refPlayers.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot parent) {

                for(DataSnapshot child : parent.getChildren() ){
                    Rival rival = new Rival();
                    rival.setCloud_id(child.child("idPlayer").getValue(String.class));
                    rival.setName(child.child("name").getValue(String.class));
                    rival.setUrlPhoto(child.child("urlPhoto").getValue(String.class));
                    allRivals.add(rival);
                }
                getGamesForRivals(allRivals, user);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void getGamesForRivals(final List<Rival> allRivals, final FirebaseUser user){
        refGames.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot parent) {
                for(Rival thisRival : allRivals){
                    ArrayList<Game> ownGames = new ArrayList<Game>();
                    boolean playable = false;
                    for(DataSnapshot child : parent.getChildren() ){
                        if(child.child("players").child(thisRival.getCloud_id()).exists()){
                            if(child.child("players").child(thisRival.getCloud_id()).exists()&&child.child("players").child(user.getUid()).exists()){
                                playable = true;
                            }
                            Game game = new Game();
                            game.setIdGame(child.child("idGame").getValue(String.class));
                            game.setName(child.child("name").getValue(String.class));
                            game.setDescription(child.child("description").getValue(String.class));
                            game.setRules(child.child("rules").getValue(String.class));
                            game.setValidated(child.child("validated").getValue(Integer.class));
                            game.setUrlPhoto(child.child("urlPhoto").getValue(String.class));
                            ownGames.add(game);
                        }
                    }
                    thisRival.setGames(ownGames);
                    if(playable){
                        playableRivals.add(thisRival.getCloud_id());
                    }
                }
                setMyRivals(allRivals, user);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void setMyRivals(final List<Rival> allRivals, final FirebaseUser user){
        refPlaces.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot parent) {
                for(DataSnapshot child : parent.getChildren() ){
                    for(Rival thisRival : allRivals){
                        if(child.child("players").child(thisRival.getCloud_id()).exists()&&child.child("players").child(user.getUid()).exists()){
                            if(!myRivals.contains(thisRival)){
                                myRivals.add(0, thisRival);
                            }
                        }else{
                            for(String idRiv : playableRivals){
                                if(thisRival.getCloud_id().equals(idRiv)){
                                    playableRivals.remove(thisRival.getCloud_id());
                                    myRivals.add(thisRival);
                                }
                            }
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

    private void initRecycler(){
        final RivalAdapter adapter = new RivalAdapter(getActivity());
        if(rvRivals!=null){
            rvRivals.setAdapter(adapter);
            rvRivals.setLayoutManager(new LinearLayoutManager(getActivity()));
            adapter.setmRivals(alphabetize(myRivals));
            rvRivals.setAdapter(adapter);
        }
    }

    public void touchHandler(){
        final GestureDetector mGestureDetector = new GestureDetector(getActivity(), new GestureDetector.SimpleOnGestureListener() {
            @Override public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }
        });

        rvRivals.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {
                View child = recyclerView.findChildViewUnder(motionEvent.getX(), motionEvent.getY());

                if (child != null && mGestureDetector.onTouchEvent(motionEvent)) {

                    int position = recyclerView.getChildAdapterPosition(child);
                    Rival item = myRivals.get(position);
                    if(filtering){
                        item = filteredList.get(position);
                    }else {
                        item = myRivals.get(position);
                    }
                    Intent intent = new Intent(getActivity(), RivalView.class);
                    intent.putExtra("rival", item);
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
        searchInRivals.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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
        List<Rival> searchingRivals = new ArrayList<Rival>();

        for(Rival currentRival : myRivals){
            if(currentRival.getName().toLowerCase().contains(query.toLowerCase())){
                searchingRivals.add(currentRival);
            }
        }

        final RivalAdapter adapter = new RivalAdapter(getActivity());
        filteredList = searchingRivals;
        adapter.setmRivals(alphabetize(filteredList));
        rvRivals.setAdapter(adapter);
        filtering = true;
    }

    public List<Rival> alphabetize(List<Rival> rivals){

        Collections.sort(rivals, new Comparator<Rival>() {
            @Override
            public int compare(Rival o1, Rival o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });

        return rivals;


    }


}
