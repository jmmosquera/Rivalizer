package iezv.jmm.rivalizer.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import iezv.jmm.rivalizer.Adapters.RivalAdapter;
import iezv.jmm.rivalizer.POJO.Rival;
import iezv.jmm.rivalizer.R;

public class RivalFragment extends Fragment {

    private RecyclerView rvRivals;
    private List<Rival> myRivals = new ArrayList<Rival>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_rival, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        rvRivals = (RecyclerView) getView().findViewById(R.id.rvRivals);

        fakeRivals();
        initRecycler();
    }

    private void fakeRivals(){
        ArrayList<String> games1 = new ArrayList<String>();
        games1.add("https://images-na.ssl-images-amazon.com/images/I/81kgxqmzvoL._SX679_.jpg");
        games1.add("https://i.ebayimg.com/images/i/141014598087-0-1/s-l1000.jpg");
        games1.add("https://si.wsj.net/public/resources/images/BN-GJ136_chess_P_20150109120327.jpg");
        games1.add("https://img.apmcdn.org/c0e5a68d0a004c21c6eef836ba64ebb31dcec18c/uncropped/3b6675-20090410-dungeonsdragons.jpg");
        myRivals.add(new Rival(1, "2314124", "Pepe el Frikazo", "https://i.ytimg.com/vi/ZGhGUqOAWdA/maxresdefault.jpg", "WarLotus", games1));
        ArrayList<String> games2 = new ArrayList<String>();
        games2.add("https://si.wsj.net/public/resources/images/BN-GJ136_chess_P_20150109120327.jpg");
        games2.add("https://i.ebayimg.com/images/i/141014598087-0-1/s-l1000.jpg");
        games2.add("https://si.wsj.net/public/resources/images/BN-GJ136_chess_P_20150109120327.jpg");
        myRivals.add(new Rival(2, "2434124", "Juancho Zafarrancho", "https://www.buenamusica.com/media/fotos/cantantes/biografia/juancho-de-la-espriella.jpg", "Comic Stores", games2));
        ArrayList<String> games3 = new ArrayList<String>();
        games3.add("https://si.wsj.net/public/resources/images/BN-GJ136_chess_P_20150109120327.jpg");
        games3.add("https://images-na.ssl-images-amazon.com/images/I/81kgxqmzvoL._SX679_.jpg");
        games3.add("https://img.apmcdn.org/c0e5a68d0a004c21c6eef836ba64ebb31dcec18c/uncropped/3b6675-20090410-dungeonsdragons.jpg");
        games3.add("https://si.wsj.net/public/resources/images/BN-GJ136_chess_P_20150109120327.jpg");
        games3.add("https://images-na.ssl-images-amazon.com/images/I/81kgxqmzvoL._SX679_.jpg");
        myRivals.add(new Rival(3, "2434444", "Plinio el Sin Piños", "https://i.ytimg.com/vi/lojbD_1y6I8/hqdefault.jpg", "Comic Stores", games3));
    }

    private void initRecycler(){
        final RivalAdapter adapter = new RivalAdapter(getActivity());
        if(rvRivals!=null){
            rvRivals.setAdapter(adapter);
            rvRivals.setLayoutManager(new LinearLayoutManager(getActivity()));
            adapter.setmRivals(myRivals);
            rvRivals.setAdapter(adapter);
        }
    }





}
