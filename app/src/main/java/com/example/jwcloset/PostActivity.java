package com.example.jwcloset;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.jwcloset.Adapter.ViewPagerAdapter;
import com.example.jwcloset.Items.PostItem;
import com.example.jwcloset.Items.UserModel;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class PostActivity extends AppCompatActivity {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private int position;
    ArrayList<PostItem> list = new ArrayList<PostItem>();
    TextView traderName, postTitle, postCategory, postTime, postDes;
    ViewPager postVp;
    Button postBtn;
    ArrayList<String> imageList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

//        initData();

        position = getIntent().getIntExtra("position", 0);

        list = (ArrayList<PostItem>) getIntent().getSerializableExtra("list");

//        list.add(new PostItem("nae", "nae", "nae", "nae", "nae"));

        traderName = findViewById(R.id.traderName);
        postTitle = findViewById(R.id.postTitle);
        postCategory = findViewById(R.id.postCategory);
        postTime = findViewById(R.id.postTime);
        postDes = findViewById(R.id.postDes);
        postVp = findViewById(R.id.postVp);
        postVp.setClipToPadding(false);
        postBtn = findViewById(R.id.postBtn);

        traderName.setText(list.get(position).getAuthor());
        postTitle.setText(list.get(position).getTitle());
        postCategory.setText(list.get(position).getCategory());
        postTime.setText(list.get(position).getTime());
        postDes.setText(list.get(position).getDescription());



        for (int i=0; i<list.get(position).getImages().size(); i++){
            imageList.add(list.get(position).getImages().get(i).toString());
        }

        postVp.setAdapter(new ViewPagerAdapter(this, imageList));

        postBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                UserModel userModel = new UserModel();
                String username = traderName.toString();


                Intent intent = new Intent(PostActivity.this, MessageActivity.class);
                intent.putExtra("destinationUid", list.get(position).getUid());
                intent.putExtra("destinationEmail", list.get(position).getAuthor());
                startActivity(intent);


            }


        });

    }


//    public void click(View view){
//        position = getIntent().getIntExtra("position", 0);
//        list = (ArrayList<PostItem>) getIntent().getSerializableExtra("list");
//
//
//
//
//        UserModel userModel = new UserModel();
//        String username = traderName.toString();
//
//            Intent intent = new Intent(PostActivity.this, ChatActivity.class);
//            intent.putExtra("destinationUid", userModel.list.get(position).uid);
//
//    }
//    private void initData(){
//
//        db.collection("posts").get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if(task.isSuccessful()){
//                            for (QueryDocumentSnapshot document : task.getResult()){
//
//                                String title = document.get("title").toString();
//                                String time = document.get("time").toString();
//                                String des = document.get("description").toString();
//                                String cat = document.get("category").toString();
//                                String name = document.get("author").toString();
//                                List photos = (List)document.getData().get("photos");
//
//                                list.add(new PostItem(name, title, time, cat, des, photos));
//
//                                Log.d("PostActivity", document.getId() + " => " + document.getData());
//                            }
//                        } else{
//                            Log.w("PostActivity", "Error getting documents.", task.getException());
//                        }
//                    }
//                });
//
//    }
}