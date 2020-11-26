package com.example.jwcloset.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.example.jwcloset.Items.PhotoItem;
import com.example.jwcloset.R;

import java.util.ArrayList;

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.ViewHolder>{
    private Context mContext;
    private ArrayList<PhotoItem> list;
    private RequestManager requestManager;
    public PhotoAdapter(ArrayList<PhotoItem> list) {
        this.list = list;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView photoImg;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            photoImg = (ImageView) itemView.findViewById(R.id.photoImg);
        }
    }

    @NonNull
    @Override
    public PhotoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.photo_item, parent,false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }



    @Override
    public void onBindViewHolder(@NonNull PhotoAdapter.ViewHolder holder, int position) {
        PhotoItem item = list.get(position);
//        Glide.with(mContext)
//                .load("http://static.hubzum.zumst.com/hubzum/2018/02/06/09/962ec338ca3b4153b037168ec92756ac.jpg")
//                .error(R.drawable.ic_launcher_foreground)
//                .into(holder.photoImg);
        Log.e("asd",holder.photoImg+"");

        Glide.with(holder.itemView.getContext())
                .load(item.getUrl())
                .error(R.drawable.ic_launcher_foreground)
                .into(holder.photoImg);
    }


    @Override
    public int getItemCount() {
        return (null != list ? list.size():0);
    }
}
