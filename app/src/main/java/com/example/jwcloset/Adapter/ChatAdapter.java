package com.example.jwcloset.Adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.jwcloset.ChatItem;
import com.example.jwcloset.Profile;
import com.example.jwcloset.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatAdapter extends BaseAdapter {

   // ArrayList<Profile> profileItem;
    ArrayList<ChatItem> chatItems;
    LayoutInflater layoutInflater;
    //LayoutInflater는 XML에 정의된 Resource를 View 객체로 반환해주는 역할을 한다.

    public ChatAdapter(ArrayList<ChatItem> chatItems, LayoutInflater layoutInflater){
        this.chatItems = chatItems;
        this.layoutInflater = layoutInflater;
    }
    @Override
    public int getCount(){
        return chatItems.size();
    }

    @Override
    public Object getItem(int position) {
        return chatItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup){

        //현재 보여줄 포지션 번째의 데이터로 뷰를 생성
        ChatItem item = chatItems.get(position);


        View itemView = view;



//        if(item.getName().equals(Profile.nickname)){

        if(item.getName() == Profile.nickname){

            itemView= layoutInflater.inflate(R.layout.my_msgbox,viewGroup,false);


        }else{
//
            itemView= layoutInflater.inflate(R.layout.other_msgbox,viewGroup,false);
//
}

        CircleImageView iv= itemView.findViewById(R.id.iv);
        TextView tvName= itemView.findViewById(R.id.tv_name);
        TextView tvMsg= itemView.findViewById(R.id.tv_msg);
        TextView tvTime= itemView.findViewById(R.id.tv_time);

        tvName.setText(item.getName());
        tvMsg.setText(item.getMessage());
        tvTime.setText(item.getTime());

        //Glide 라이브러리를 이용하여 사진 연결
        Glide.with(itemView.getContext()).load(item.getProfileUrl()).into(iv);

        return itemView;
    }
}
