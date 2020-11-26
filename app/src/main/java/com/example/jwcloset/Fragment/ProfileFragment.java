package com.example.jwcloset.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jwcloset.Adapter.TradeAdapter;
import com.example.jwcloset.ChatActivity;
import com.example.jwcloset.Items.PostItem;
import com.example.jwcloset.Items.TradeItem;
import com.example.jwcloset.MainActivity;
import com.example.jwcloset.Profile;
import com.example.jwcloset.R;
import com.example.jwcloset.SettingActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;


public class ProfileFragment extends Fragment {
//
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser currentUser;
    FirebaseAuth mAuth;

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private TradeAdapter adapter;
    private ArrayList<TradeItem> list = new ArrayList<>();
    private ArrayList<String> photos = new ArrayList<>();

    ArrayList<PostItem> postlist = new ArrayList<>();

    TextView user_name;
    TextView user_image;
    Button button;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_profile,container,false);



        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        recyclerView = v.findViewById(R.id.profileRecycler);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new TradeAdapter(list);
        recyclerView.setAdapter(adapter);


        user_name = v.findViewById(R.id.et_name);
        user_image = v.findViewById(R.id.profileIcon);

        user_name.setText(currentUser.getEmail());

        Random random = new Random();
        int color = Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256));
        ((GradientDrawable)user_image.getBackground()).setColor(color);
        user_image.setText(user_name.getText().subSequence(0, 1));



        adapter.setOnItemLongClickListener(new TradeAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(View view, int position) {

                FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
                StorageReference storageRef = firebaseStorage.getReference();
                for (int i=0;i<postlist.get(position).getImages().size(); i++){
                    StorageReference desertRef = storageRef.child("images/"+list.get(position).getDate()+"/"+i);
                    Log.d("tlqkf", list.get(position).getDate());
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
        });



        return v;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        list.clear();
        getPost();
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
                                        if(currentUser.getEmail().equals(name)){
                                            list.add(new TradeItem(uid, photos.get(0), time, title, des, cat, name));
                                        }

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







