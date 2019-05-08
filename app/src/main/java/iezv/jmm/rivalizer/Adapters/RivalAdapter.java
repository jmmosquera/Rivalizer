package iezv.jmm.rivalizer.Adapters;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import iezv.jmm.rivalizer.POJO.Rival;

import iezv.jmm.rivalizer.R;

public class RivalAdapter extends RecyclerView.Adapter<RivalAdapter.RivalViewHolder> {

    public Context context;
    class RivalViewHolder extends RecyclerView.ViewHolder{
        private final TextView rivalName;
        private final ImageView rivalPhoto;
        private final TextView rivalPlace;
        private final RecyclerView rivalGames;
        private final ArrayList<String> gamesAdapter = new ArrayList<String>();

        private RivalViewHolder (View itemView){
            super(itemView);
            rivalName = itemView.findViewById(R.id.rivalName);
            rivalPhoto = itemView.findViewById(R.id.gameMiniPhoto);
            rivalPlace = itemView.findViewById(R.id.rivalPlace);
            rivalGames = itemView.findViewById(R.id.rivalGames);
        }
    }

    private final LayoutInflater mInflater;

    private List<Rival> mRivals = Collections.emptyList();

    public RivalAdapter(Context context){
        this.context = context;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public RivalViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.rival_item, parent, false);
        return new RivalViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(RivalViewHolder holder, int position) {
        if(mRivals != null){
            Rival current = mRivals.get(position);
            holder.rivalName.setText(current.getName());
            holder.rivalPlace.setText(current.getfPlace());
            String photoLink = current.getUrlPhoto();
            Picasso.with(context).load(Uri.parse(photoLink)).into(holder.rivalPhoto);
            final GameMiniAdapter adapter = new GameMiniAdapter(context);
            if(current.getGames()!=null){
                GridLayoutManager grManager = new GridLayoutManager(context, 5);
                holder.rivalGames.setLayoutManager(grManager);
                adapter.setGamesMini(current.getGames());
                holder.rivalGames.setAdapter(adapter);
            }

        }
    }

    public void setmRivals(List<Rival> rivals){
        mRivals = rivals;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (mRivals != null) {
            return mRivals.size();
        }else {
            return 0;
        }
    }


}
