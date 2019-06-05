package iezv.jmm.rivalizer.Adapters;

import android.support.v7.widget.RecyclerView;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import iezv.jmm.rivalizer.POJO.Message;
import iezv.jmm.rivalizer.R;

public class ChatAdapter extends RecyclerView.Adapter {

    private Context context;
    private List<Message> messages;

    public ChatAdapter(Context context, List<Message> messages){
        this.context = context;
        this.messages = messages;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view;

        if(viewType == 0){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_sent, parent, false);
            return new SentMessageHolder(view);
        } else if (viewType == 1){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_received, parent, false);
            return new ReceiveMessageHolder(view);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position){
        Message message = (Message) messages.get(position);

        switch (holder.getItemViewType()){
            case 0:
                ((SentMessageHolder) holder).bind(message);
                break;
            case 1:
                ((ReceiveMessageHolder) holder).bind(message);
        }
    }

    private class SentMessageHolder extends RecyclerView.ViewHolder{
        TextView messageText, timeText;

        SentMessageHolder(View itemView){
            super(itemView);

            messageText = (TextView) itemView.findViewById(R.id.text_message_body_s);
            timeText = (TextView) itemView.findViewById(R.id.text_message_time_s);
        }

        void bind(Message message){
            messageText.setText(message.getText_msg());

            long minute = (Long.parseLong(message.getDate_msg()) / (1000 * 60)) % 60;
            long hour = (Long.parseLong(message.getDate_msg()) / (1000 * 60 * 60)) % 24;

            timeText.setText(hour+":"+minute);
        }
    }

    @Override
    public int getItemCount(){
        return messages.size();
    }

    private class ReceivedMessageHolder extends RecyclerView.ViewHolder{
        TextView messageText, timeText;

        ReceivedMessageHolder(View itemView){
            super(itemView);

            messageText = (TextView) itemView.findViewById(R.id.text_message_body_r);
            timeText = (TextView) itemView.findViewById(R.id.text_message_time_r);
        }

        void bind(Message message){
            messageText.setText(message.getText_msg());

            int minute = (int) (Long.parseLong(message.getDate_msg()) / (1000 * 60)) % 60;
            int hour = (int) (Long.parseLong(message.getDate_msg()) / (1000 * 60 * 60)) % 24;

            timeText.setText(String.format("%02d",hour)+":"+String.format("%02d",minute));
        }
    }

}
