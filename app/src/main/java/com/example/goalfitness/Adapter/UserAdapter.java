package com.example.goalfitness.Adapter;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.goalfitness.Chat;
import com.example.goalfitness.Fragments.ProfileFragment;
import com.example.goalfitness.MessageActivity;
import com.example.goalfitness.Model.ChatMGS;
import com.example.goalfitness.Model.Customer_Register;
import com.example.goalfitness.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private Context mContext;
    private List<Customer_Register> mUser;
    private static final String TAG = "UserAdapter";
    private boolean isChat;
    private boolean isFragmnet;


    String theLastMessage;


    public UserAdapter(Context mContext, List<Customer_Register> mUser, boolean isChat, boolean isFragmnet ) {
        this.mUser = mUser;
        this.mContext = mContext;
        this.isChat = isChat;
        this.isFragmnet = isFragmnet;


    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.activity_userlist, parent, false);
        return new UserAdapter.ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final Customer_Register customer_register = mUser.get(position);
        holder.username.setText(customer_register.getName());

        if (customer_register.getImageURL().equals("default")){
            holder.profile_pic.setImageResource(R.mipmap.ic_launcher);
        }

        else{
            Glide.with(mContext).load(customer_register.getImageURL()).into(holder.profile_pic);
        }

        if (isChat){
            lastMessage(customer_register.getId(), holder.last_msg);
        }else{
            holder.last_msg.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFragmnet){
                    SharedPreferences.Editor editor = mContext.getSharedPreferences("PREFS",Context.MODE_PRIVATE).edit();
                    editor.putString("profileid", customer_register.getId());
                    editor.apply();

                    ((FragmentActivity) mContext).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            new ProfileFragment()).commit();
                }
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, MessageActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("id", customer_register.getId());
                mContext.startActivity(intent);


            }

        });




    }

    @Override
    public int getItemCount() {
        return mUser.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView username;
        public ImageView profile_pic;
        private TextView last_msg;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            username = itemView.findViewById(R.id.username);
            profile_pic = itemView.findViewById(R.id.profile_pic);
            last_msg = itemView.findViewById(R.id.last_msg);
        }
    }

    //check for last message
    private void lastMessage(final String id, final TextView last_msg){
        theLastMessage="default";
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("chat");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    ChatMGS chatMGS = snapshot.getValue(ChatMGS.class);
                    if (chatMGS.getReceiver().equals(firebaseUser.getUid()) && chatMGS.getSender().equals(id) || chatMGS.getReceiver().equals(id) && chatMGS.getSender().equals(firebaseUser.getUid())){
                        theLastMessage = chatMGS.getMessage();
                    }
                }
                switch (theLastMessage){
                    case "default":
                        last_msg.setText("No Message");
                        break;

                    default:
                        last_msg.setText(theLastMessage);
                        break;
                }
                theLastMessage = "default";
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {

        public TextView username;
        public TextView fullname;
        public CircleImageView image_profile;

        public ImageViewHolder(View itemView) {
            super(itemView);

            username = itemView.findViewById(R.id.username);
            fullname = itemView.findViewById(R.id.fullname);
            image_profile = itemView.findViewById(R.id.image_profile);
        }
    }


}
