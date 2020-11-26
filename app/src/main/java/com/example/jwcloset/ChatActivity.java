package com.example.jwcloset;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.jwcloset.Adapter.ChatAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;

public class ChatActivity extends AppCompatActivity {

    String email;

    EditText et;
    ListView listView;

    ArrayList<ChatItem> chatItems = new ArrayList<ChatItem>();
    ChatAdapter adapter;

    FirebaseDatabase firebaseDatabase;
    //'chat' 노드의 참조객체 참조변수
    DatabaseReference chatRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            email = user.getEmail();
        }



        et = findViewById(R.id.et);
        listView = findViewById(R.id.listview);
        adapter= new ChatAdapter(chatItems,getLayoutInflater());
        listView.setAdapter(adapter);

        firebaseDatabase= FirebaseDatabase.getInstance();
        chatRef= firebaseDatabase.getReference("chat");


        //Firebase Realtime

        chatRef.addChildEventListener(new ChildEventListener() {
            // ValueListener는 하나의 값만 바뀌어도 처음부터 다시 값을 줌
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                //새로 추가된 데이터(값 : ChatItem) 가져오기
                ChatItem chatItem =  dataSnapshot.getValue(ChatItem.class);

               //Arraylist 추가
                chatItems.add(chatItem);


                adapter.notifyDataSetChanged();
                listView.setSelection(chatItems.size()-1); //리스트뷰의 마지막 위치로 스크롤 위치 이동
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void clickSend(View view) {


        String nickName= Profile.nickname;
        String message= et.getText().toString();
        String pofileUrl= Profile.profileUri;


        //보낸 시간
        Calendar calendar= Calendar.getInstance();
        String time=calendar.get(Calendar.HOUR_OF_DAY)+":"+calendar.get(Calendar.MINUTE); //14:16

        //DB에 저장
        ChatItem chatItem= new ChatItem(nickName,message,time,pofileUrl);
        chatRef.push().setValue(chatItem);

        //EditText에 있는 글씨 지우기
        et.setText("");


        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);

    }

    }
