package iezv.jmm.rivalizer.Views;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import iezv.jmm.rivalizer.Adapters.GenericAdapter;
import iezv.jmm.rivalizer.POJO.Game;
import iezv.jmm.rivalizer.POJO.Generic;
import iezv.jmm.rivalizer.POJO.Place;
import iezv.jmm.rivalizer.R;

public class GenericView extends AppCompatActivity {

    private RecyclerView rvGeneric;
    private Button genericForward;
    private List<Generic> myGenerics = new ArrayList<Generic>();
    private List<Place> myPlaces = new ArrayList<Place>();
    private List<Game> myGames = new ArrayList<Game>();
    public List<String> checkedIds = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generic_view);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        rvGeneric = findViewById(R.id.rvGeneric);
        genericForward = findViewById(R.id.genericForward);


        Bundle data = getIntent().getExtras();
        int genericCode = data.getInt("genCode");


        switch (genericCode){
            case 1:
                myGenerics = data.getParcelableArrayList("places");
                checkPlaces();
                break;
            case 2:
                myGenerics = data.getParcelableArrayList("games");

                checkGames();
                break;
        }


        touchHandler();
    }

    private void touchHandler() {
        final GestureDetector mGestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }
        });

        rvGeneric.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {
                View child = recyclerView.findChildViewUnder(motionEvent.getX(), motionEvent.getY());
                if (child != null && mGestureDetector.onTouchEvent(motionEvent)) {
                    int position = recyclerView.getChildAdapterPosition(child);

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

        genericForward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                touchHandler();
                Intent checkIntent = new Intent();
                ArrayList<String> checkeds = new ArrayList<String>(checkedIds);
                checkIntent.putStringArrayListExtra("checkeds", checkeds);
                setResult(Activity.RESULT_OK, checkIntent);
                finish();
            }
        });
    }

    private void checkPlaces() {
        final GenericAdapter adapter = new GenericAdapter(this);
        this.checkedIds = adapter.getCheckedIds();
        if(myGenerics!=null&&myGenerics.size()!=0){
            rvGeneric.setAdapter(adapter);
            rvGeneric.setLayoutManager(new LinearLayoutManager(this));
            adapter.setGenerics(myGenerics);
            rvGeneric.setAdapter(adapter);
        }
    }

    private void checkGames() {
        final GenericAdapter adapter = new GenericAdapter(this);
        this.checkedIds = adapter.getCheckedIds();
        if(myGenerics!=null&&myGenerics.size()!=0){
            rvGeneric.setAdapter(adapter);
            rvGeneric.setLayoutManager(new LinearLayoutManager(this));
            adapter.setGenerics(myGenerics);
            rvGeneric.setAdapter(adapter);
        }
    }

}
