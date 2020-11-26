package com.example.jwcloset;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.jwcloset.Adapter.PhotoAdapter;
import com.example.jwcloset.Items.PhotoItem;
import com.example.jwcloset.Items.PostItem;
import com.example.jwcloset.Items.TradeItem;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.yongbeam.y_photopicker.util.photopicker.PhotoPickerActivity;
import com.yongbeam.y_photopicker.util.photopicker.utils.YPhotoPickerIntent;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class TradeAddActivity extends AppCompatActivity {
    ImageButton addImageBtn;
    EditText addTitleEdit, addDes;
    TextView addCategory;
    Button addBtn;
    ArrayList<String> photos = null;
    ArrayList<Uri> uris = new ArrayList<>();

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseUser currentUser;
    private FirebaseAuth mAuth;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private PhotoAdapter adapter;
    private ArrayList<PhotoItem> imgList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trade_add);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        currentUser.getUid();

        addImageBtn = findViewById(R.id.addImageBtn);
        addTitleEdit = findViewById(R.id.addTitleEdit);
        addCategory = findViewById(R.id.addCategory);
        addDes = findViewById(R.id.addDes);
        addBtn = findViewById(R.id.addBtn);

        addImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YPhotoPickerIntent intent = new YPhotoPickerIntent(TradeAddActivity.this);
                intent.setMaxSelectCount(20);
                intent.setShowCamera(true);
                intent.setShowGif(true);
                intent.setSelectCheckBox(true);
                intent.setMaxGrideItemCount(3);
                startActivityForResult(intent, 100);
            }
        });

        addCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TradeAddActivity.this, StylePopupActivity.class);
                startActivityForResult(intent, 0);
            }
        });


        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(addTitleEdit.getText().toString().length() == 0){
                    Toast.makeText(TradeAddActivity.this, "제목을 입력해주세요.", Toast.LENGTH_SHORT).show();
                }else if(addCategory.getText().toString().length() == 0){
                    Toast.makeText(TradeAddActivity.this, "스타일을 입력해주세요.", Toast.LENGTH_SHORT).show();
                }else if(addDes.getText().toString().length() == 0){
                    Toast.makeText(TradeAddActivity.this, "내용을 입력해주세요.", Toast.LENGTH_SHORT).show();
                }else if(photos == null){
                    Toast.makeText(TradeAddActivity.this, "사진을 선택하세요", Toast.LENGTH_SHORT).show();
                }else{
                    long now = System.currentTimeMillis();
                    Date mDate = new Date(now);
                    SimpleDateFormat simpleDate =new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault());
                    String getTime = simpleDate.format(mDate);


                    UploadDB(getTime);

                }
            }
        });

        recyclerView = findViewById(R.id.photoRecycler);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(TradeAddActivity.this,LinearLayoutManager.HORIZONTAL,false);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new PhotoAdapter(imgList);
        recyclerView.setAdapter(adapter);



    }

    public void setImage(ImageView img){
        Glide.with(getApplicationContext()).load(imgList.get(0)).error(R.drawable.ic_launcher_foreground).into(img);
    }

    @SuppressLint("WrongConstant")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            if(requestCode == 0) {
                addCategory.setText(data.getStringExtra("style"));
            }else if(requestCode == 100){
                if(data != null){
                    photos = data.getStringArrayListExtra(PhotoPickerActivity.KEY_SELECTED_PHOTOS);
                    for (int i = 0; i < photos.size(); i++) {
                        PhotoItem photoItem = new PhotoItem(photos.get(i));
                        uris.add(Uri.fromFile(new File(photos.get(i))));
                        Log.d("PhotoPicker", String.valueOf(uris.size()));
                        imgList.add(photoItem);
                        adapter.notifyDataSetChanged();
                    }

                }
            }
        }
    }

    public void UploadDB(String time){
        Map<String, Object> post = new HashMap<>();
        post.put("author", currentUser.getEmail());
        post.put("title", addTitleEdit.getText().toString());
        post.put("category", addCategory.getText().toString());
        post.put("description", addDes.getText().toString());
        post.put("time", time);
        post.put("listSize", uris.size());
//        post.put("photos", uris);

        if(uris.size() != 0){
            for (int i=0; i < uris.size(); i++){
                ProgressDialog progressDialog = new ProgressDialog(this);
                progressDialog.setTitle("업로드 중...");
                progressDialog.show();
                Uri file = uris.get(i);

                FirebaseStorage storage = FirebaseStorage.getInstance();
                StorageReference storageRef = storage.getReference();
                StorageReference riversRef = storageRef.child("images/"+time+"/"+i);
                UploadTask uploadTask = riversRef.putFile(file);

                // Register observers to listen for when the download is done or if it fails
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        progressDialog.cancel();
                        Log.d("TradeAddActivity", "Upload Fail...");
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                        // ...
                        progressDialog.cancel();
                        Log.d("TradeAddActivity", "Upload Success!");

                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent();
                                setResult(RESULT_OK,intent);
                                finish();
                            }
                        }, 1000);
                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                        @SuppressWarnings("VisibleForTests")
                        double progress = (100 * taskSnapshot.getBytesTransferred()) /  taskSnapshot.getTotalByteCount();
                        progressDialog.setMessage("Uploaded " + ((int) progress) + "% ...");
                    }
                });
            }
        }else{
            Toast.makeText(getApplicationContext(), "파일을 선택하세요", Toast.LENGTH_SHORT).show();
        }


        db.collection("posts")
                .add(post)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("TradeAddActivity", "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("TradeAddActivity", "Error adding document", e);
                    }
                });

    }


}