package com.example.jwcloset.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.jwcloset.Adapter.ChatlistAdapter;
import com.example.jwcloset.Adapter.MarketAdapter;
import com.example.jwcloset.ChatItem;
import com.example.jwcloset.ChatModel;
import com.example.jwcloset.Items.ChatlistItem;
import com.example.jwcloset.Items.MarketItem;
import com.example.jwcloset.MessageActivity;
import com.example.jwcloset.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ChatFragment extends Fragment {
    private DatabaseReference mDatabase;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseUser currentUser;
    private FirebaseAuth mAuth;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private ChatlistAdapter adapter;
    private ArrayList<ChatlistItem> list = new ArrayList<>();
    private ArrayList<ChatItem> chatItems = new ArrayList<ChatItem>();

    private String destinationUid, destinationEmail;

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
        View v=inflater.inflate(R.layout.fragment_chat,container,false);

        recyclerView = v.findViewById(R.id.chatFragmentList);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ChatlistAdapter(list);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new ChatlistAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(getContext(), MessageActivity.class);
                destinationUid = list.get(position).getUid();
                destinationEmail = list.get(position).getName();
                intent.putExtra("destinationUid", destinationUid);
                intent.putExtra("destinationEmail", destinationEmail);
                startActivity(intent);
            }
        });

        return v;
    }

    private void getData() {
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();
//        Query chatQuery = mDatabase.child("chatroom").orderByChild("users/"+currentUser.getUid());
//        chatQuery.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if (snapshot.exists()) {
//                    for (DataSnapshot issue : snapshot.getChildren()) {
//                        // do with your result
//
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });

        checkChatRoom();

    }
    void checkChatRoom(){
        FirebaseDatabase.getInstance().getReference().child("chatrooms").orderByChild("users/"+currentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot item : dataSnapshot.getChildren() ){
                    try{
                        String userUid = null;
                        String userEmail = null;
                        String commentMsg;

                        Object object = item.getValue();
                        HashMap chatMap = (HashMap)item.getValue();
                        Log.d("cfjakd", String.valueOf(chatMap));

                        ChatModel chatModel = new ChatModel();
                        chatModel.users = (Map<String, String>)chatMap.get("users");
                        chatModel.comments = (Map<String, ChatModel.Comment>)chatMap.get("comments");

                        ArrayList userIdList = new ArrayList(chatModel.users.keySet());
                        ArrayList userEmailList = new ArrayList(chatModel.users.values());
//                        comment.addAll(chatModel.comments.values());
                        ArrayList comment = new ArrayList(chatModel.comments.values());


                        for(int i=0; i<2; i++){
                            if (currentUser.getUid().equals(userIdList.get(i))){
                            }else{
                                userUid = String.valueOf(userIdList.get(i));
                                userEmail = String.valueOf(userEmailList.get(i));
                                Log.d("getting Email", userEmail);
                            }
                        }
                        commentMsg = comment.get(comment.size()-1).toString();
                        list.add(new ChatlistItem(userUid, userEmail,commentMsg));
                        adapter.notifyDataSetChanged();
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}