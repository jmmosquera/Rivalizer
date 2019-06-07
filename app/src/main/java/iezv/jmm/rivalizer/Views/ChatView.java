package iezv.jmm.rivalizer.Views;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import iezv.jmm.rivalizer.Adapters.ChatAdapter;
import iezv.jmm.rivalizer.POJO.Message;
import iezv.jmm.rivalizer.R;

public class ChatView extends AppCompatActivity {

    TextView nameRival;
    ImageView avatarRival;
    RecyclerView rvChat;
    Button buttonChatboxSend;
    EditText etChatBox;

    private FirebaseAuth mAuth;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference usersDB = database.getReference("users");

    List<Message> currentMessages = new ArrayList<Message>();

    String myId;
    String idChatRival;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_view);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAuth = FirebaseAuth.getInstance();

        this.nameRival = findViewById(R.id.nameRival);
        this.avatarRival = findViewById(R.id.avatarRival);
        this.rvChat = findViewById(R.id.rvChatMessages);
        this.buttonChatboxSend = findViewById(R.id.button_chatbox_send);
        this.etChatBox = findViewById(R.id.edittext_chatbox);

        Bundle data = getIntent().getExtras();
        nameRival.setText(data.getString("nameRival"));
        String avatar = data.getString("avatarRival");
        this.idChatRival = data.getString("idChatRival");
        this.myId = mAuth.getUid();

        if(avatar!=null && !avatar.equals("")){
            Picasso.with(ChatView.this).load(Uri.parse(avatar)).into(avatarRival);
        }

        chargeMessages();

        buttonHandler();
    }

    private void buttonHandler() {
        buttonChatboxSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg_id = usersDB.push().getKey();
                String timeMilis = System.currentTimeMillis()+"";
                String text_msg = etChatBox.getText().toString();
                Message myMessage = new Message(msg_id, myId, timeMilis, text_msg, 0, 0);
                DatabaseReference rivalRef = usersDB.child(idChatRival);
                rivalRef.child("messages").child(msg_id).child("message").setValue(myMessage);
                etChatBox.setText("");

            }
        });
    }

    private void chargeMessages() {

        usersDB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(idChatRival).child("messages").exists()){
                    for(DataSnapshot child : dataSnapshot.getChildren()){
                        if(child.child("messages").exists()){
                            for(DataSnapshot mess : child.child("messages").getChildren()){
                                if (mess.child("message").child("id_sender").getValue(String.class).equals(myId)){
                                    Message message = new Message();
                                    message.setId_msg(mess.child("message").child("id_msg").getValue(String.class));
                                    message.setText_msg(mess.child("message").child("text_msg").getValue(String.class));
                                    message.setDate_msg(mess.child("message").child("date_msg").getValue(String.class));
                                    message.setSended(1);
                                    message.setStatus(mess.child("message").child("sended").getValue(Integer.class));
                                    currentMessages.add(message);
                                }
                            }
                        }
                    }



                }
                if(dataSnapshot.child(myId).child("messages").exists()){
                    for(DataSnapshot child : dataSnapshot.getChildren()){
                        if(child.child("messages").exists()){
                            for(DataSnapshot mess : child.child("messages").getChildren()){
                                if (mess.child("message").child("id_sender").getValue(String.class).equals(idChatRival)) {
                                    Message message = new Message();
                                    message.setId_msg(mess.child("message").child("id_msg").getValue(String.class));
                                    message.setText_msg(mess.child("message").child("text_msg").getValue(String.class));
                                    message.setDate_msg(mess.child("message").child("date_msg").getValue(String.class));
                                    message.setSended(0);
                                    message.setStatus(mess.child("message").child("sended").getValue(Integer.class));
                                    currentMessages.add(message);
                                }
                            }

                        }

                    }

                }

                resetRecycler();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        usersDB.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Log.v("ZZZ", "SHOOTS FIRED!");
                if(dataSnapshot.child(idChatRival).child("messages").exists()){
                    for(DataSnapshot child : dataSnapshot.getChildren()){
                        if(child.child("messages").exists()){
                            for(DataSnapshot mess : child.child("messages").getChildren()){
                                if (mess.child("message").child("id_sender").getValue(String.class).equals(myId)){
                                    Message message = new Message();
                                    message.setId_msg(mess.child("message").child("id_msg").getValue(String.class));
                                    message.setText_msg(mess.child("message").child("text_msg").getValue(String.class));
                                    message.setDate_msg(mess.child("message").child("date_msg").getValue(String.class));
                                    message.setSended(1);
                                    message.setStatus(mess.child("message").child("sended").getValue(Integer.class));
                                    currentMessages.add(message);
                                }
                            }
                        }
                    }



                }
                if(dataSnapshot.child(myId).child("messages").exists()){
                    for(DataSnapshot child : dataSnapshot.getChildren()){
                        if(child.child("messages").exists()){
                            for(DataSnapshot mess : child.child("messages").getChildren()){
                                if (mess.child("message").child("id_sender").getValue(String.class).equals(idChatRival)) {
                                    Message message = new Message();
                                    message.setId_msg(mess.child("message").child("id_msg").getValue(String.class));
                                    message.setText_msg(mess.child("message").child("text_msg").getValue(String.class));
                                    message.setDate_msg(mess.child("message").child("date_msg").getValue(String.class));
                                    message.setSended(0);
                                    message.setStatus(mess.child("message").child("sended").getValue(Integer.class));
                                    currentMessages.add(message);
                                }
                            }

                        }

                    }

                }

                resetRecycler();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(dataSnapshot.child(idChatRival).child("messages").exists()){
                    for(DataSnapshot child : dataSnapshot.getChildren()){
                        if(child.child("messages").exists()){
                            for(DataSnapshot mess : child.child("messages").getChildren()){
                                if (mess.child("message").child("id_sender").getValue(String.class).equals(myId)){
                                    Message message = new Message();
                                    message.setId_msg(mess.child("message").child("id_msg").getValue(String.class));
                                    message.setText_msg(mess.child("message").child("text_msg").getValue(String.class));
                                    message.setDate_msg(mess.child("message").child("date_msg").getValue(String.class));
                                    message.setSended(1);
                                    message.setStatus(mess.child("message").child("sended").getValue(Integer.class));
                                    currentMessages.add(message);
                                }
                            }
                        }
                    }



                }
                if(dataSnapshot.child(myId).child("messages").exists()){
                    for(DataSnapshot child : dataSnapshot.getChildren()){
                        if(child.child("messages").exists()){
                            for(DataSnapshot mess : child.child("messages").getChildren()){
                                if (mess.child("id_sender").getValue(String.class).equals(idChatRival)) {
                                    Message message = new Message();
                                    message.setId_msg(mess.child("message").child("id_msg").getValue(String.class));
                                    message.setText_msg(mess.child("message").child("text_msg").getValue(String.class));
                                    message.setDate_msg(mess.child("message").child("date_msg").getValue(String.class));
                                    message.setSended(0);
                                    message.setStatus(mess.child("message").child("sended").getValue(Integer.class));
                                    currentMessages.add(message);
                                }
                            }

                        }

                    }

                }

                resetRecycler();

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void resetRecycler(){
        rvChat = (RecyclerView) findViewById(R.id.rvChatMessages);
        final ChatAdapter adapter = new ChatAdapter(this, sortByDate(currentMessages));
        if(rvChat!=null){
            rvChat.setAdapter(adapter);
            rvChat.setLayoutManager(new LinearLayoutManager(this));
            //rvChat.setAdapter(adapter);
        }

    }

    public List<Message> sortByDate(List<Message> messages){
        Collections.sort(messages, new Comparator<Message>() {
            @Override
            public int compare(Message o1, Message o2) {
                if (Double.parseDouble(o1.getDate_msg()) ==
                        Double.parseDouble(o2.getDate_msg()))
                {
                    return 0;
                }
                else if (Double.parseDouble(o1.getDate_msg()) <
                        Double.parseDouble(o2.getDate_msg()))
                {
                    return -1;
                }
                return 1;
            }
        });

        return messages;
    }

}
