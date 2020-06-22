package com.example.goalfitness;

import android.content.Intent;
import android.os.Bundle;
import android.renderscript.Sampler;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
//import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.goalfitness.Adapter.MessageAdapter;
import com.example.goalfitness.Fragments.APIService;
import com.example.goalfitness.Model.ChatMGS;
import com.example.goalfitness.Model.Customer_Register;
import com.example.goalfitness.Notification.Client;
import com.example.goalfitness.Notification.Data;
import com.example.goalfitness.Notification.MyResponse;
import com.example.goalfitness.Notification.Sender;
import com.example.goalfitness.Notification.Token;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MessageActivity extends AppCompatActivity {

    CircleImageView profile_pic;
    TextView username;

    FirebaseUser fuser;
    DatabaseReference ref;

    ImageButton btn_send;
    EditText text_send;

    MessageAdapter messageAdapter;
    List<ChatMGS> mchat;

    RecyclerView recyclerView;

    ValueEventListener readedListener;

    String id;

    Intent intent;

    APIService apiService;

    boolean notify = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        profile_pic = findViewById(R.id.profile_pic);
        username = findViewById(R.id.username);
        btn_send = findViewById(R.id.btn_send);
        text_send = findViewById(R.id.text_send);

        intent = getIntent();
        id = intent.getStringExtra("id");

        fuser = FirebaseAuth.getInstance().getCurrentUser();

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notify = true;
                String msg = text_send.getText().toString();
                if (!msg.equals("")){
                    sendMessage(fuser.getUid(), id, msg);
                } else{
                    Toast.makeText(MessageActivity.this, "you can't send empty message ", Toast.LENGTH_SHORT).show();
                }
                text_send.setText("");
            }
        });



        if(TextUtils.isEmpty(id)){
            Log.d("TAG","isempty");
        } else{
            Log.i("TAG",id);
        }
        ref = FirebaseDatabase.getInstance().getReference().child("Users").child("Personal trainer").child(id);


        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Customer_Register customer_register = dataSnapshot.getValue(Customer_Register.class);
                username.setText(customer_register.getName());
                if (customer_register.getImageURL().equals("default")) {
                    profile_pic.setImageResource(R.mipmap.ic_launcher);
                } else {
                    Glide.with(MessageActivity.this).load(customer_register.getImageURL()).into(profile_pic);
                }

                readMessage(fuser.getUid(), id, customer_register.getImageURL());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        readedMessage(id);
    }

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.message_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){

            case R.id.todolist:
                startActivity(new Intent(getApplicationContext(), TodoActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtra("id", id));
                return true;

            case R.id.btn_paypal:
                startActivity(new Intent(this, Paypal.class));
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void readedMessage(final String id){
        ref = FirebaseDatabase.getInstance().getReference("chat");
        readedListener = ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    ChatMGS chatMGS = snapshot.getValue(ChatMGS.class);
                    if (chatMGS.getReceiver().equals(fuser.getUid()) && chatMGS.getSender().equals(id)){
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("readed", true);
                        snapshot.getRef().updateChildren(hashMap);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void sendMessage (String sender, final String receiver, String message){

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", sender);
        hashMap.put("receiver", receiver);
        hashMap.put("message", message);
        hashMap.put("readed", false);

        ref.child("chat").push().setValue(hashMap);

        DatabaseReference chatref = FirebaseDatabase.getInstance().getReference("ChatList")
                .child(fuser.getUid())
                .child(id);

        chatref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()){
                    chatref.child("id").setValue(id);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

       // final DatabaseReference chatrefReciver = FirebaseDatabase.getInstance().getReference("ChatList").child(id).child(fuser.getUid());
        //chatrefReciver.child("id").setValue(fuser.getUid());

        final String msg = message;

        ref = FirebaseDatabase.getInstance().getReference().child("Users").child("Customers").child(fuser.getUid()); //notification to pt but the child is "customers", because the notification must show the customer name that send to PT
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Customer_Register customer_register = dataSnapshot.getValue(Customer_Register.class);
                if (notify) {
                    sendNotification(receiver, customer_register.getName(), msg);
                }
                    notify = false;


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void sendNotification(String receiver, final String username, final String message){
        //final String id = intent.getStringExtra("id");
        DatabaseReference tokens = FirebaseDatabase.getInstance().getReference("Tokens");
        Query query = tokens.orderByKey().equalTo(receiver);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Token token = snapshot.getValue(Token.class);
                    Data data = new Data(fuser.getUid(), R.mipmap.ic_launcher, username+": "+message, "New Message", id);

                    Sender sender = new Sender(data, token.getToken());

                    apiService.sendNotidication(sender)
                            .enqueue(new Callback<MyResponse>() {
                                @Override
                                public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                                    if (response.code() == 200){
                                        if (response.body().success != 1){
                                            Toast.makeText(MessageActivity.this, "Faild!", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<MyResponse> call, Throwable t) {

                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }



    private void readMessage (final String myid, final String id, final String imageurl){
        mchat = new ArrayList<>();

        ref = FirebaseDatabase.getInstance().getReference("chat");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mchat.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    ChatMGS chatMGS = snapshot.getValue(ChatMGS.class);
                    if (chatMGS.getReceiver().equals(myid) && chatMGS.getSender().equals(id) || chatMGS.getReceiver().equals(id) && chatMGS.getSender().equals(myid)){
                        mchat.add(chatMGS);
                    }

                    messageAdapter = new MessageAdapter(MessageActivity.this, mchat, imageurl);
                    recyclerView.setAdapter(messageAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}




