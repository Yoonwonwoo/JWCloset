package com.example.jwcloset.Fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.jwcloset.Adapter.TradeAdapter;
import com.example.jwcloset.Items.PostItem;
import com.example.jwcloset.PostActivity;
import com.example.jwcloset.R;
import com.example.jwcloset.StylePopupActivity;
import com.example.jwcloset.TradeAddActivity;
import com.example.jwcloset.Items.TradeItem;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

public class TradeFragment extends Fragment {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseUser currentUser;
    private FirebaseAuth mAuth;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private TradeAdapter adapter;
    private ArrayList<TradeItem> list = new ArrayList<>();
    ArrayList<PostItem> postlist = new ArrayList<>();
    FloatingActionButton tradeFab;
    Button styleBtn;
    private ArrayList<String> photos = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_trade,container,false);

        recyclerView = v.findViewById(R.id.tradeRecycler);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new TradeAdapter(list);
        recyclerView.setAdapter(adapter);

        tradeFab = v.findViewById(R.id.tradeFab);
        tradeFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), TradeAddActivity.class);
                startActivityForResult(intent, 0);
            }
        });

        adapter.setOnItemClickListener(new TradeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent2 = new Intent(getContext(), PostActivity.class);
                intent2.putExtra("position", position);
                intent2.putExtra("list", postlist);
                startActivityForResult(intent2, 1);
            }
        });

        adapter.setOnItemLongClickListener(new TradeAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(View view, int position) {
                mAuth = FirebaseAuth.getInstance();
                currentUser = mAuth.getCurrentUser();
                if (currentUser.getEmail().equals(list.get(position).getName())){

                    FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
                    StorageReference storageRef = firebaseStorage.getReference();
                    for (int i=0;i<postlist.get(position).getImages().size(); i++){
                        StorageReference desertRef = storageRef.child("images/"+list.get(position).getDate()+"/"+i);
                        desertRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                // File deleted successfully
                                Log.d("TradeFragment_delete", "Success");
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                // Uh-oh, an error occurred!
                                Log.d("TradeFragment_delete", "Failed");

                            }
                        });
                    }

                    db.collection("posts").document(list.get(position).getUid())
                            .delete()
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d("TradeFragment", "documet delete!");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w("TradeFragment", "document delete failed");
                                }
                            });

                    adapter.removeItem(position);
                }
            }
        });

        styleBtn = v.findViewById(R.id.styleBtn);
        styleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent3 = new Intent(getContext(), StylePopupActivity.class);
                startActivityForResult(intent3, 2);
            }
        });

        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        list.clear();
        postlist.clear();
        getPost();
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            if (requestCode == 0){

                list.clear();
                postlist.clear();

                getPost();

//                list.add(new TradeItem(data.getStringExtra("uid"), data.getStringExtra("url"), data.getStringExtra("time"), data.getStringExtra("title"),
//                        data.getStringExtra("description"), data.getStringExtra("category"), data.getStringExtra("name")));
//
//                postlist.add(new PostItem(data.getStringExtra("name"), data.getStringExtra("title"), data.getStringExtra("time"),
//                        data.getStringExtra("description"), data.getStringExtra("category"), (List) data.getSerializableExtra("photos")));
//
//                adapter.notifyDataSetChanged();
            }else if(requestCode == 2){
//                Toast.makeText(getContext(), data.getStringExtra("style"), Toast.LENGTH_SHORT).show();
                adapter.getFilter().filter(data.getStringExtra("style"));
            }
        }
    }

    private void getPost(){

        db.collection("posts").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for (QueryDocumentSnapshot document : task.getResult()){
                                String uid = document.getId();
                                String title = document.get("title").toString();
                                String time = document.get("time").toString();
                                String des = document.get("description").toString();
                                String cat = document.get("category").toString();
                                String name = document.get("author").toString();
                                long size = (long) document.get("listSize");


                                FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
                                StorageReference storageRef = firebaseStorage.getReference();
                                for(int i=0;i<size;i++){
                                    storageRef.child("images/"+time+"/"+i).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            // Got the download URL for 'users/me/profile.png'
                                            photos.add(uri.toString());
                                            Log.d("TradeFragment", uri.toString());
                                            Log.d("TradeFragment_photo_list", photos.get(0));
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception exception) {
                                            // Handle any errors
                                        }
                                    });
                                }

                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        list.add(new TradeItem(uid, photos.get(0), time, title, des, cat, name));
                                        postlist.add(new PostItem(name, title, time, cat, des, photos, uid));


                                        adapter.notifyDataSetChanged();
                                    }
                                }, 1000);


                                Log.d("TradeFragment", document.getId() + " => " + document.getData());
                            }
                        } else{
                            Log.w("TradeFragment", "Error getting documents.", task.getException());
                        }
                    }
                });
    }
}