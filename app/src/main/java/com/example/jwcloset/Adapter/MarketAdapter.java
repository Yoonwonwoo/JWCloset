package com.example.jwcloset.Adapter;

import android.content.Context;
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
import com.example.jwcloset.Items.MarketItem;
import com.example.jwcloset.Items.TradeItem;
import com.example.jwcloset.R;

import java.util.ArrayList;
import java.util.List;

public class MarketAdapter extends RecyclerView.Adapter<MarketAdapter.ViewHolder> implements Filterable {
    private ArrayList<MarketItem> list;
    private Context mContext;

    public MarketAdapter(ArrayList<MarketItem> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.market_item, parent,false);
        mContext = parent.getContext();
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MarketItem item = list.get(position);

        Glide.with(holder.marketImg.getContext())
                .load(item.getImage())
                .error(R.drawable.ic_launcher_foreground)
                .into(holder.marketImg);

        holder.marketName.setText(item.getName());
        holder.marketCategory.setText(item.getCategory());
    }

    @Override
    public int getItemCount() {
        return (null != list ? list.size():0);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView marketImg;
        private TextView marketName;
        private TextView marketCategory;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            marketImg = itemView.findViewById(R.id.marketImg);
            marketName = itemView.findViewById(R.id.marketName);
            marketCategory = itemView.findViewById(R.id.marketCategory);

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

    public void setOnItemClickListener(MarketAdapter.OnItemClickListener listener){
        this.onItemClickListener = listener;
    }

    public MarketAdapter.OnItemClickListener onItemClickListener = null;

    public interface OnItemClickListener{
        void onItemClick(View view, int position);
    }

    @Override
    public Filter getFilter() {
        return exampleFilter;
    }

    private Filter exampleFilter = new Filter() {
        //Automatic on background thread
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<MarketItem> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(list);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (MarketItem item : list) {
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
