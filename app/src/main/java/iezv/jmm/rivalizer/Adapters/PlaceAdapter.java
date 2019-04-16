package iezv.jmm.rivalizer.Adapters;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;

import iezv.jmm.rivalizer.POJO.Place;
import iezv.jmm.rivalizer.R;

public class PlaceAdapter extends RecyclerView.Adapter<PlaceAdapter.PlaceViewHolder> {

    public Context context;

    class PlaceViewHolder extends RecyclerView.ViewHolder{
        private final TextView placeName;
        private final ImageView photoPlace;
        private final TextView placeAdscribed;
        private final TextView placeDistance;

        private PlaceViewHolder(View itemView){
            super(itemView);
            placeName = itemView.findViewById(R.id.placeName);
            photoPlace = itemView.findViewById(R.id.photoPlace);
            placeAdscribed = itemView.findViewById(R.id.placeAdscribed);
            placeDistance = itemView.findViewById(R.id.placeDistance);
        }
    }

    private final LayoutInflater mInflater;

    private List<Place> mPlaces = Collections.emptyList();

    public PlaceAdapter(Context context){
        this.context = context;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public PlaceViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View itemView = mInflater.inflate(R.layout.place_item, parent, false);
        return new PlaceViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PlaceViewHolder holder, int position){
        if(mPlaces != null){
            Place current = mPlaces.get(position);
            holder.placeName.setText(current.getName());
            holder.placeDistance.setText(current.getCoordinates());
            holder.placeAdscribed.setText(current.getAddress());
            String photoLink = current.getUrlPhoto();
            Picasso.with(context).load(Uri.parse(photoLink)).into(holder.photoPlace);
        }
    }

    public void setPlaces(List<Place> places){
        mPlaces = places;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (mPlaces != null)
            return mPlaces.size();
        else return 0;
    }


}
