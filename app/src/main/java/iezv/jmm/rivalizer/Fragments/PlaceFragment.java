package iezv.jmm.rivalizer.Fragments;

import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import iezv.jmm.rivalizer.Adapters.PlaceAdapter;
import iezv.jmm.rivalizer.POJO.Place;
import iezv.jmm.rivalizer.R;

public class PlaceFragment extends Fragment {

    private RecyclerView rvPlaces;
    private List<Place> myPlaces = new ArrayList<Place>();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_place, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        rvPlaces = (RecyclerView) getView().findViewById(R.id.rvPlaces);

        fakePlaces();
        initRecycler();

    }



    private void fakePlaces() {
        myPlaces.add(new Place(1, "WarLotus", "13", "1,9 km", "http://warlotus.com/sites/default/files/wl_campeonato_0.jpg", ""));
        myPlaces.add(new Place(2, "Comic Stores", "7", "1,1 km", "https://www.geomediaconsultores.net/wp-content/uploads/2016/12/IMG-20161210-WA0004-1000x750.jpg", ""));
        myPlaces.add(new Place(3, "Dracomic", "15", "94 m", "https://4.bp.blogspot.com/-xsF_xi9Xx0Q/WTQ4_r-cCSI/AAAAAAAAGYk/d-hRGz9D4NU3lkP2hVUa4qBtVpHQ0OpRACLcB/s1600/DRACO.jpg", ""));

    }

    private void initRecycler() {
        final PlaceAdapter adapter = new PlaceAdapter(getActivity());
        if(rvPlaces!=null){
            rvPlaces.setAdapter(adapter);
            rvPlaces.setLayoutManager(new LinearLayoutManager(getActivity()));
            adapter.setPlaces(myPlaces);
            rvPlaces.setAdapter(adapter);
        }

    }
}
