package iezv.jmm.rivalizer.Adapters;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import iezv.jmm.rivalizer.POJO.Generic;
import iezv.jmm.rivalizer.R;

public class GenericAdapter extends RecyclerView.Adapter<GenericAdapter.GenericViewHolder> {

    public Context context;
    public List<String> checkedIds = new ArrayList<String>();

    class GenericViewHolder extends RecyclerView.ViewHolder{
        private final TextView genericName;
        private final ImageView genericPhoto;
        private final CheckBox genericCheck;

        private GenericViewHolder (View itemView){
            super(itemView);
            genericName = itemView.findViewById(R.id.genericName);
            genericPhoto = itemView.findViewById(R.id.genericPhoto);
            genericCheck = itemView.findViewById(R.id.checkBox);
        }

    }

    private final LayoutInflater mInflater;

    private List<Generic> mGenerics = Collections.emptyList();

    public GenericAdapter(Context context){
        this.context = context;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public GenericViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View itemView = mInflater.inflate(R.layout.generic_item, parent, false);
        return new GenericViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final GenericViewHolder holder, int position){
        if(mGenerics!=null){
            final Generic current = mGenerics.get(position);
            holder.genericName.setText(current.getNameGeneric());
            String photoLink = current.getPhotoGeneric();
            Picasso.with(context).load(Uri.parse(photoLink)).into(holder.genericPhoto);
            holder.genericCheck.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(holder.genericCheck.isChecked()){
                        checkedIds.add(current.getIdGeneric());
                    }else{
                        checkedIds.remove(current.getIdGeneric());
                    }

                }
            });
        }
    }

    public void setGenerics(List<Generic> generics){
        mGenerics = generics;
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        if (mGenerics != null)
            return mGenerics.size();
        else return 0;
    }

    public List<String> getCheckedIds(){
        return this.checkedIds;
    }

}
