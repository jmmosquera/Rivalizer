package iezv.jmm.rivalizer.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;

import java.util.ArrayList;
import java.util.List;

import iezv.jmm.rivalizer.Adapters.GameAdapter;
import iezv.jmm.rivalizer.POJO.Game;
import iezv.jmm.rivalizer.R;

public class GameFragment extends Fragment {

    private RecyclerView rvGames;
    private List<Game> myGames = new ArrayList<Game>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_game, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        rvGames = (RecyclerView) getView().findViewById(R.id.rvGames);

        fakeGames();
        initRecycler();
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
    }

    private void fakeGames(){
        myGames.add(new Game(1,"",  "Risk", "https://images-na.ssl-images-amazon.com/images/I/81kgxqmzvoL._SX679_.jpg", "", ""));
        myGames.add(new Game(1,"",  "Ajedrez", "https://si.wsj.net/public/resources/images/BN-GJ136_chess_P_20150109120327.jpg", "", ""));
        myGames.add(new Game(1,"",  "Magic, the Gathering", "https://i.ebayimg.com/images/i/141014598087-0-1/s-l1000.jpg", "", ""));
        myGames.add(new Game(1,"",  "Dungeons & Dragons", "https://img.apmcdn.org/c0e5a68d0a004c21c6eef836ba64ebb31dcec18c/uncropped/3b6675-20090410-dungeonsdragons.jpg", "", ""));
        myGames.add(new Game(1,"",  "Risk", "https://images-na.ssl-images-amazon.com/images/I/81kgxqmzvoL._SX679_.jpg", "", ""));
        myGames.add(new Game(1,"",  "Ajedrez", "https://si.wsj.net/public/resources/images/BN-GJ136_chess_P_20150109120327.jpg", "", ""));
        myGames.add(new Game(1,"",  "Magic, the Gathering", "https://i.ebayimg.com/images/i/141014598087-0-1/s-l1000.jpg", "", ""));
        myGames.add(new Game(1,"",  "Dungeons & Dragons", "https://img.apmcdn.org/c0e5a68d0a004c21c6eef836ba64ebb31dcec18c/uncropped/3b6675-20090410-dungeonsdragons.jpg", "", ""));
    }




}

