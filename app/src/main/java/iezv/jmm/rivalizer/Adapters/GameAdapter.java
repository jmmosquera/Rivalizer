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

import iezv.jmm.rivalizer.POJO.Game;
import iezv.jmm.rivalizer.R;

public class GameAdapter extends RecyclerView.Adapter<GameAdapter.GameViewHolder> {

    public Context context;

    class GameViewHolder extends RecyclerView.ViewHolder{
        private final TextView gameName;
        private final ImageView gamePhoto;

        private GameViewHolder(View itemView){
            super(itemView);
            gameName = itemView.findViewById(R.id.genericName);
            gamePhoto = itemView.findViewById(R.id.genericPhoto);
        }
    }

    private final LayoutInflater mInflater;

    private List<Game> mGames = Collections.emptyList();

    public GameAdapter(Context context){
        this.context = context;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public GameViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View itemView = mInflater.inflate(R.layout.game_item, parent, false);
        return new GameViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(GameViewHolder holder, int position){
        if(mGames != null){
            Game current = mGames.get(position);
            holder.gameName.setText(current.getName());
            String photoLink = current.getUrlPhoto();
            Picasso.with(context).load(Uri.parse(photoLink)).into(holder.gamePhoto);
        }
    }

    public void setGames(List<Game> games){
        mGames = games;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (mGames != null)
            return mGames.size();
        else return 0;
    }

}
