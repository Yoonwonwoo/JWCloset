package com.example.jwcloset.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jwcloset.ChatItem;
import com.example.jwcloset.Items.ChatlistItem;
import com.example.jwcloset.Items.TradeItem;
import com.example.jwcloset.R;

import java.util.ArrayList;
import java.util.Random;

public class ChatlistAdapter extends RecyclerView.Adapter<ChatlistAdapter.ViewHolder>{
    private ArrayList<ChatlistItem> list;

    public ChatlistAdapter(ArrayList<ChatlistItem> list) {
        this.list = list;
    }

    private Context mContext;

    public ChatlistAdapter.OnItemClickListener onItemClickListener = null;

    public interface OnItemClickListener{
        void onItemClick(View view, int position);
    }

    @NonNull
    @Override
    public ChatlistAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat, parent,false);
        mContext = parent.getContext();
        return new ChatlistAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatlistAdapter.ViewHolder holder, int position) {
        ChatlistItem item = list.get(position);
        holder.chatItemName.setText(item.getName());
        holder.chatItemLastMsg.setText(item.getLastMsg());
        holder.chatItemIcon.setText(list.get(position).getName().substring(0, 1));
        Random random = new Random();
        int color = Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256));
        ((GradientDrawable)holder.chatItemIcon.getBackground()).setColor(color);
    }

    @Override
    public int getItemCount() {
        return (null != list ? list.size():0);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView chatItemIcon;
        private TextView chatItemName;
        private TextView chatItemLastMsg;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            chatItemIcon = itemView.findViewById(R.id.chatItemIcon);
            chatItemName = itemView.findViewById(R.id.chatItemName);
            chatItemLastMsg = itemView.findViewById(R.id.chatItemLastMsg);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION){
                        if(onItemClickListener != null){
                            onItemClickListener.onItemClick(v, pos);
                        }
                    }
                }
            });
        }
    }

    public void setOnItemClickListener(ChatlistAdapter.OnItemClickListener listener){
        this.onItemClickListener = listener;
    }
}
