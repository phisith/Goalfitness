package com.example.goalfitness;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.goalfitness.Adapter.UserAdapter;
import com.example.goalfitness.Model.ChatList;
import com.example.goalfitness.Model.ChatMGS;
import com.example.goalfitness.Model.Customer_Register;
import com.example.goalfitness.Notification.Token;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.List;

public class Chat extends AppCompatActivity {

        private RecyclerView recyclerView;
        private UserAdapter userAdapter;
        private List<Customer_Register> customer_registers;

        FirebaseUser fuser;
        DatabaseReference ref;

        private List<ChatList> userlist;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.chat_layout);

                recyclerView = findViewById(R.id.recycler_view_chat);
                recyclerView.setHasFixedSize(true);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
                //linearLayoutManager.setStackFromEnd(true);
                recyclerView.setLayoutManager(linearLayoutManager);


                fuser = FirebaseAuth.getInstance().getCurrentUser();

                userlist = new ArrayList<>();

                ref = FirebaseDatabase.getInstance().getReference("ChatList").child(fuser.getUid());
                ref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                userlist.clear();
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                        ChatList chatList = snapshot.getValue(ChatList.class);
                                        userlist.add(chatList);
                                }
                                chatList();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                });
                updateToken(FirebaseInstanceId.getInstance().getToken());



        }
        private void updateToken(String token) {
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Tokens");
                Token token1 = new Token(token);
                ref.child(fuser.getUid()).setValue(token1);
        }

        private void chatList() {
                customer_registers = new ArrayList<>();
                ref = FirebaseDatabase.getInstance().getReference().child("Users").child("Customers");
                ref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                customer_registers.clear();
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                        Customer_Register customer_register = snapshot.getValue(Customer_Register.class);
                                        for (ChatList chatList : userlist) {
                                                if (customer_register.getId().equals(chatList.getId())) {
                                                        customer_registers.add(customer_register);
                                                }
                                        }
                                }
                                userAdapter = new UserAdapter(getApplicationContext(), customer_registers, true, true);
                                recyclerView.setAdapter(userAdapter);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                });
        }



}




