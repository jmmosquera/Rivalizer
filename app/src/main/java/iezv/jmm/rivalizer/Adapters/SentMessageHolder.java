package iezv.jmm.rivalizer.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

import iezv.jmm.rivalizer.POJO.Message;
import iezv.jmm.rivalizer.R;

public class SentMessageHolder extends RecyclerView.ViewHolder {


    TextView messageText, timeText;

    SentMessageHolder(View itemView){
        super(itemView);
        messageText = (TextView) itemView.findViewById(R.id.text_message_body_s);
        timeText = (TextView) itemView.findViewById(R.id.text_message_time_s);
    }

    void bind(Message message){
        messageText.setText(message.getText_msg());
        timeText.setText(formatDateTime(message.getDate_msg()).getHours());
    }

    private Date formatDateTime(String date_msg) {

        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
        return new Date(System.currentTimeMillis());
    }

}
