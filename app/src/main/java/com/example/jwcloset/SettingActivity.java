package com.example.jwcloset;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.jwcloset.Fragment.TradeFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingActivity extends AppCompatActivity {

    private FirebaseUser currentUser;
    private FirebaseAuth mAuth;


    EditText user_name;
    CircleImageView user_image;

    //프로필 이미지 경로 uri
    Uri imgUri;

    boolean isFirst = true; //앱 최초 실행 여부부
    boolean isChanged = false; //프로필 변경 여부

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);



        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName("Jane Q. User")
                .setPhotoUri(Uri.parse("https://example.com/jane-q-user/profile.jpg"))
                .build();

        currentUser.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            loadData();
                        }
                    }
                });

        user_name = findViewById(R.id.et_name);
        user_image = findViewById(R.id.iv_profile);


        //Profile 클래스 사용자 이름, 사진 - xml
        //Picasso 라이브러리를 이용하여 폰에 저장되어 있는 프로필 사진 읽어오기
        if(Profile.nickname!= null){

            //user_name.setText(Profile.nickname);
            user_name.setText(currentUser.getEmail());
            Picasso.get().load(Profile.profileUri).into(user_image);

            isFirst = false;
        }

    }

    //프로필 이미지 선택하도록 갤러리 앱을 실행
    //나중에 경로를 image/ ~  로
    public void clickImage(View view){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,10);
    }

    @Override
    //picasso 라이브러리를 통하여(*동적 퍼미션 필요 x) 프로필 이미지 uri와 xml 연결
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 10:
                if(resultCode==RESULT_OK){
                    imgUri= data.getData();
                    Picasso.get().load(imgUri).into(user_image);


                    isChanged=true;
                }
                break;
        }
    }
    public void clickBtn(View view){

        //프로필 변경 x and 처음 접속 x
        if(!isChanged && !isFirst){
            //인텐트 어디로 갈지 몰라서 일단 메인으로 해둠,,, **변경**무조건 ,,
            Intent intent = new Intent(this, ChatActivity.class);
            startActivity(intent);
            finish();
        }
        else{
            saveData();
        }
    }

    void saveData(){

        //EditText user의 name 가져오기 전역변수에 (toString() 필수)
        Profile.nickname = user_name.getText().toString();

        if(imgUri == null){
            return;
        }

        //Firebase storage에 이미지 저장을 위한 파일명 생성 (날짜)
        SimpleDateFormat sdf = new SimpleDateFormat("yyMMddhhmmss");
        String fileName = sdf.format(new Date()) + ".png";

        //Firebase storage save
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        final StorageReference imgRef = firebaseStorage.getReference("profileImages/" + fileName);

        //file upload
        UploadTask uploadTask = imgRef.putFile(imgUri);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                //image upload 성공한 경우
                //..firebase storage 이미지 파일 다운로드 url 가져오기
                imgRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        //피라미터를 통해 firebase 저장소에 있는 이미지 다운로드 URL을 String으로 가져오기
                        Profile.profileUri = uri.toString();
                        Toast.makeText(SettingActivity.this,"프로필 저장 성공",Toast.LENGTH_LONG).show();


                        //**********Firebase Realtime***********


                        //Firebase Database에 username, userimage 저장
                        //Firebase DB manager 객체 부르기
                        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                        //'profiles'라는 이름의 자식 노드 참조 객체 얻어오기
                        DatabaseReference profileRef= firebaseDatabase.getReference("profiles");


                        //nickname을 key 식별자로 한 후 프로필 이미지의 주소를 값으로 저장
                        profileRef.child(Profile.nickname).setValue(Profile.profileUri);

                        //2. 내 phone에 nickName, profileUrl을 저장
                        //SharedPreferences는 간단한 값 저장을 위해 사용
                        //..보통 초기 설정값이나 자동로그인 여부 등 간단한 값을 저장하기 위해 사용됨
                        SharedPreferences preferences= getSharedPreferences("account",MODE_PRIVATE);
                        SharedPreferences.Editor editor=preferences.edit();

                        editor.putString("nickName",Profile.nickname);
                        editor.putString("profileUrl",Profile.profileUri);

                        editor.commit();

                        //저장 후 Intent - 일단 main으로 옮길게,,,,
                        Intent intent=new Intent(SettingActivity.this, ChatActivity.class);
                        startActivity(intent);
                        finish();



                    }
                });
            }
        });
    }

    //SharedPreferences를 사용하여 내 핸드폰에 저장되어 있는 프로필를 읽어오게 함
    void loadData() {
        SharedPreferences preferences = getSharedPreferences("account", MODE_PRIVATE);
        Profile.nickname = preferences.getString("nickName", null);
        Profile.profileUri = preferences.getString("profileUrl", null);
    }


}
