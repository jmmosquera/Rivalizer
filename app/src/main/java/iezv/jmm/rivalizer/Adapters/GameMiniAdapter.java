package iezv.jmm.rivalizer.Adapters;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;

import iezv.jmm.rivalizer.R;

public class GameMiniAdapter extends RecyclerView.Adapter<GameMiniAdapter.GameMiniViewHolder> {

    public Context context;

    class GameMiniViewHolder extends RecyclerView.ViewHolder{
        private final ImageView gameMiniPhoto;

        private GameMiniViewHolder(View itemView){
            super(itemView);
            gameMiniPhoto = itemView.findViewById(R.id.gameMiniPhoto);
        }
    }

    private final LayoutInflater mInflater;

    private List<String> mMiniGames = Collections.emptyList();

    public GameMiniAdapter(Context context){
        this.context = context;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public GameMiniViewHolder onCreateViewHolder(ViewGroup parent, int ViewType){
        View itemView = mInflater.inflate(R.layout.game_mini_item, parent, false);
        return new GameMiniViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(GameMiniViewHolder holder, int position){
        if(mMiniGames != null){
            String current = mMiniGames.get(position);
            Picasso.with(context).load(Uri.parse(current)).into(holder.gameMiniPhoto);
        }
    }

    public void setGamesMini(List<String> gamesMini){
        mMiniGames = gamesMini;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (mMiniGames != null)
            return mMiniGames.size();
        else return 0;
    }

}
