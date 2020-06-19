package com.example.goalfitness.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.goalfitness.Chat;
import com.example.goalfitness.EditProfileActivity;
import com.example.goalfitness.MessageActivity;
import com.example.goalfitness.Model.ChatMGS;
import com.example.goalfitness.Model.Customer_Register;
import com.example.goalfitness.Model.Post;
import com.example.goalfitness.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class ProfileFragment extends Fragment {

    ImageView image_profile, options;
    TextView posts, followers, following, fullname, bio, username;
    Button edit_profile;


    FirebaseUser firebaseUser;
    String profileid;

    private RecyclerView recyclerView;
    private MyFotosAdapter myFotosAdapter;
    private List<Post> postList;
    private List<Customer_Register> mUser;


    ImageButton my_fotos;



    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        SharedPreferences prefs = getContext().getSharedPreferences("PREFS", MODE_PRIVATE);
        profileid = prefs.getString("profileid", "none");

        image_profile = view.findViewById(R.id.image_profile);
        posts = view.findViewById(R.id.posts);
        followers = view.findViewById(R.id.followers);
        following = view.findViewById(R.id.following);
        fullname = view.findViewById(R.id.fullname);
        bio = view.findViewById(R.id.bio);
        edit_profile = view.findViewById(R.id.edit_profile);
        username = view.findViewById(R.id.username);
        my_fotos = view.findViewById(R.id.my_fotos);


        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new GridLayoutManager(getContext(), 3);
        recyclerView.setLayoutManager(mLayoutManager);
        postList = new ArrayList<>();
        myFotosAdapter = new MyFotosAdapter(getContext(), postList);
        recyclerView.setAdapter(myFotosAdapter);


        recyclerView.setVisibility(View.VISIBLE);


        userInfo();
        getNrPosts();
        myFotos();


        if (profileid.equals(firebaseUser.getUid())){
            edit_profile.setText("Edit Profile");
        } else{
            edit_profile.setText("Chat");
        }

        edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String btn = edit_profile.getText().toString();

                if (btn.equals("Edit Profile")){

                    startActivity(new Intent(getContext(), EditProfileActivity.class));

                } else if (btn.equals("Chat")){

                    startActivity(new Intent(getContext(), MessageActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtra("id", profileid));

                    //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    //intent.putExtra("id", comment.getPublisher());
                    //mContext.startActivity(intent);





                }
            }
        });


        my_fotos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerView.setVisibility(View.VISIBLE);

            }
        });


        return view;
    }

    private void addNotification(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Notifications").child(profileid);

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("userid", firebaseUser.getUid());
        hashMap.put("text", "started following you");
        hashMap.put("postid", "");
        hashMap.put("ispost", false);

        reference.push().setValue(hashMap);
    }

    private void userInfo(){ //
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child("Customers").child(profileid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (getContext() == null){
                    return;
                }
                Customer_Register customer_register = dataSnapshot.getValue(Customer_Register.class);

                Glide.with(getContext()).load(customer_register.getImageURL()).into(image_profile);
                username.setText(customer_register.getName());
                bio.setText(customer_register.getBios());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getNrPosts(){//
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Posts");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int i = 0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Post post = snapshot.getValue(Post.class);
                    if (post.getPublisher().equals(profileid)){
                        i++;
                    }
                }
                posts.setText(""+i);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void myFotos(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Posts");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                postList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Post post = snapshot.getValue(Post.class);
                    if (post.getPublisher().equals(profileid)){
                        postList.add(post);
                    }
                }
                Collections.reverse(postList);
                myFotosAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


}