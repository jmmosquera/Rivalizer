package iezv.jmm.rivalizer.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

import iezv.jmm.rivalizer.POJO.Message;
import iezv.jmm.rivalizer.R;


public class ReceiveMessageHolder extends RecyclerView.ViewHolder {

    TextView messageText, timeText;

    ReceiveMessageHolder(View itemView){
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

    private Date formatDateTime(String date_msg) {

        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
        return new Date(System.currentTimeMillis());
    }


}
