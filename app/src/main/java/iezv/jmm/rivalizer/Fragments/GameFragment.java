package iezv.jmm.rivalizer.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ImageButton;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import iezv.jmm.rivalizer.Adapters.GameAdapter;
import iezv.jmm.rivalizer.MainActivity;
import iezv.jmm.rivalizer.POJO.Game;
import iezv.jmm.rivalizer.R;
import iezv.jmm.rivalizer.Views.GameRegister;
import iezv.jmm.rivalizer.Views.GameView;

public class GameFragment extends Fragment {

    private RecyclerView rvGames;
    private List<Game> myGames = new ArrayList<Game>();
    private ImageButton goRegister;

    DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    DatabaseReference ref = database.child("games");

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_game, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        rvGames = (RecyclerView) getView().findViewById(R.id.rvGames);
        goRegister = (ImageButton) getView().findViewById(R.id.btn_register_game);

        fakeGames();
        initRecycler();
        listener();
    }

    private void initRecycler() {
        final GameAdapter adapter = new GameAdapter(getActivity());
        if(rvGames!=null) {
            rvGames.setAdapter(adapter);
            GridLayoutManager grManager = new GridLayoutManager(getActivity(), 2);

            rvGames.setLayoutManager(grManager /*new GridLayoutManager(getActivity(), 2)*/);
            adapter.setGames(myGames);
            rvGames.setAdapter(adapter);
        }

        touchHandler();
    }

    private void listener(){
        goRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), GameRegister.class);
                startActivity(intent);

            }
        });
    }

    private void fakeGames(){

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot parent) {


                for(DataSnapshot child : parent.getChildren()){
                    Game game = new Game();
                    game.setIdGame(child.child("idGame").getValue(String.class));
                    game.setName(child.child("name").getValue(String.class));
                    game.setDescription(child.child("description").getValue(String.class));
                    game.setRules(child.child("rules").getValue(String.class));
                    game.setValidated(child.child("validated").getValue(Integer.class));
                    game.setUrlPhoto(child.child("urlPhoto").getValue(String.class));
                    myGames.add(game);
                    Log.v("ValDataSnap: ", game.toString());
                }

                initRecycler();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void touchHandler(){

        final GestureDetector mGestureDetector = new GestureDetector(getActivity(), new GestureDetector.SimpleOnGestureListener() {
            @Override public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }
        });

    rvGames.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
        @Override
        public boolean onInterceptTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {
            View child = recyclerView.findChildViewUnder(motionEvent.getX(), motionEvent.getY());

            if (child != null && mGestureDetector.onTouchEvent(motionEvent)) {

                int position = recyclerView.getChildAdapterPosition(child);
                Game item = myGames.get(position);
                /*if(filtering){
                    item = filteredBooks.get(position);
                }else {
                    item = myBooks.get(position);
                }*/
                Intent intent = new Intent(getActivity(), GameView.class);
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

