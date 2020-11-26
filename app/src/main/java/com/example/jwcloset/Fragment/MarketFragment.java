package com.example.jwcloset.Fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.jwcloset.Adapter.MarketAdapter;
import com.example.jwcloset.Adapter.TradeAdapter;
import com.example.jwcloset.Items.MarketItem;
import com.example.jwcloset.Items.PostItem;
import com.example.jwcloset.Items.TradeItem;
import com.example.jwcloset.R;
import com.example.jwcloset.StylePopupActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;


public class MarketFragment extends Fragment {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseUser currentUser;
    private FirebaseAuth mAuth;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private MarketAdapter adapter;
    private ArrayList<MarketItem> list = new ArrayList<>();
    Button styleMarketBtn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(list.size() != 0){
            list.clear();
        }
        getData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_market,container,false);

        recyclerView = v.findViewById(R.id.marketRecycler);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new MarketAdapter(list);
        recyclerView.setAdapter(adapter);

        styleMarketBtn = v.findViewById(R.id.styleMarketBtn);
        styleMarketBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), StylePopupActivity.class);
                startActivityForResult(intent, 0);
            }
        });

        adapter.setOnItemClickListener(new MarketAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(list.get(position).getUri()));
                startActivity(intent);
            }
        });


        return v;

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            if(requestCode == 0){
                adapter.getFilter().filter(data.getStringExtra("style"));
            }
        }
    }

    private void getData(){
        db.collection("market").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for (QueryDocumentSnapshot document : task.getResult()){

                                String image = document.get("image").toString();
                                String name = document.get("name").toString();
                                String category = document.get("category").toString();
                                String uri = document.get("uri").toString();

                                list.add(new MarketItem(image, name, category, uri));

                                Log.d("MarketFragment", document.getId() + " => " + document.getData());
                            }
                            adapter.notifyDataSetChanged();
                        } else{
                            Log.w("MarketFragment", "Error getting documents.", task.getException());
                        }
                    }
                });
    }

}