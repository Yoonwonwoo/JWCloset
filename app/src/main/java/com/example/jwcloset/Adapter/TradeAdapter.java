package com.example.jwcloset.Adapter;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.jwcloset.R;
import com.example.jwcloset.Items.TradeItem;

import java.util.ArrayList;
import java.util.List;

public class TradeAdapter extends RecyclerView.Adapter<TradeAdapter.ViewHolder> implements Filterable {
    private Context mContext;
    private ArrayList<TradeItem> list;

    ArrayList<TradeItem> unFilteredlist;
    ArrayList<TradeItem> filteredList;

    public OnItemClickListener onItemClickListener = null;
    public OnItemLongClickListener onItemLongClickListener = null;

    public interface OnItemClickListener{
        void onItemClick(View view, int position);
    }

    public interface OnItemLongClickListener{
        void onItemLongClick(View view, int position);
    }

    public TradeAdapter(ArrayList<TradeItem> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.trade_item, parent,false);
        mContext = parent.getContext();
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TradeItem item = list.get(position);

        Glide.with(holder.tradeImg.getContext())
                .load(Uri.parse(item.getUrl()))
                .error(R.drawable.ic_launcher_foreground)
                .into(holder.tradeImg);

        holder.tradeTitle.setText(item.getTitle());
        holder.tradeDes.setText(item.getDescription());
        holder.tradeDate.setText(item.getDate());
        holder.tradeCategory.setText(item.getCategory());
        holder.postName.setText(item.getName());
    }

    @Override
    public int getItemCount() {
        return (null != list ? list.size():0);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView tradeImg;
        private TextView tradeTitle;
        private TextView tradeDes;
        private TextView tradeDate;
        private TextView tradeCategory;
        private TextView postName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tradeImg = itemView.findViewById(R.id.tradeImg);
            tradeTitle = itemView.findViewById(R.id.tradeTitle);
            tradeDes = itemView.findViewById(R.id.tradeDes);
            tradeDate = itemView.findViewById(R.id.tradeDate);
            tradeCategory = itemView.findViewById(R.id.tradeCategory);
            postName = itemView.findViewById(R.id.postName);

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

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int pos = getAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION){
                        if(onItemLongClickListener != null){
                            onItemLongClickListener.onItemLongClick(v, pos);
                        }
                    }
                    return true;
                }
            });
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.onItemClickListener = listener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener){
        this.onItemLongClickListener = listener;
    }

    public void removeItem(int position){
        list.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public Filter getFilter() {
        return exampleFilter;
    }

    private Filter exampleFilter = new Filter() {
        //Automatic on background thread
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<TradeItem> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(list);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (TradeItem item : list) {
                    //TODO filter 대상 setting
                    if (item.getCategory().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        //Automatic on UI thread
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            list.clear();
            list.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };


}
