package com.example.goalfitness.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.goalfitness.Model.ChatMGS;
import com.example.goalfitness.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;

    private Context mContext;
    private List<ChatMGS> mChat;
    private static final String TAG = "UserAdapter";
    private String imageurl;

    FirebaseUser fuser;


    public MessageAdapter(Context mContext, List<ChatMGS> mChat, String imageurl) {
        this.mChat = mChat;
        this.mContext = mContext;
        this.imageurl = imageurl;

    }

    @NonNull
    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == MSG_TYPE_RIGHT) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_right, parent, false);
            return new MessageAdapter.ViewHolder(view);
        } else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_left, parent, false);
            return new MessageAdapter.ViewHolder(view);
        }
    }


    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.ViewHolder holder, final int position) {


        ChatMGS chatMGS = mChat.get(position);

        holder.show_message.setText(chatMGS.getMessage());

        if(imageurl.equals("default")){
            holder.profile_pic.setImageResource(R.mipmap.ic_launcher);
        } else {
            Glide.with(mContext).load(imageurl).into(holder.profile_pic);
        }
        if(position == mChat.size()-1){
            if (chatMGS.isReaded()){
                holder.readedtext.setText("Readed");
            } else {
                holder.readedtext.setText("Sended");
            }
        } else{
            holder.readedtext.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return mChat.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView show_message;
        public ImageView profile_pic;

        public TextView readedtext;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            show_message = itemView.findViewById(R.id.show_meg);
            profile_pic = itemView.findViewById(R.id.profile_pic);
            readedtext = itemView.findViewById(R.id.readtext);
        }
    }

    @Override
    public int getItemViewType(int position) {
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        if (mChat.get(position).getSender().equals(fuser.getUid())){
            return MSG_TYPE_RIGHT;
        } else{
            return MSG_TYPE_LEFT;
        }

    }
}
