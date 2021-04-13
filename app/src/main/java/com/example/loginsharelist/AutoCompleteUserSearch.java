package com.example.loginsharelist;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class AutoCompleteUserSearch extends AppCompatActivity {
    public static final String TAG = "AutoCompleteUserSearch";

    private DatabaseReference databaseReference;
    private DatabaseReference databaseReferenceUser;
    private RecyclerView autoUserSearchList;
    private EditText autoUserEmailInput;

    FirebaseRecyclerAdapter<User, UserDisplay> firebaseRecyclerAdapter;

    private FirebaseAuth auth;
    String currUserID;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto_complete_user_search);

        auth = FirebaseAuth.getInstance();
        currUserID = auth.getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReferenceUser = FirebaseDatabase.getInstance().getReference().child("User");

        autoUserSearchList = (RecyclerView) findViewById(R.id.autoCompleteUserSearchList);
        autoUserSearchList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        autoUserSearchList.setHasFixedSize(true);

        autoUserEmailInput = (EditText) findViewById(R.id.autoCompleteUserEmailInput);
        autoUserEmailInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString() != null){
                    UserSearch(s.toString().trim());
                } else {
                    UserSearch("");
                }
            }
        });
    }

    private void UserSearch(String userEmailStr){
        Query query = databaseReferenceUser.orderByChild("/emailAddress").equalTo(userEmailStr);

//        FirebaseRecyclerOptions firebaseRecyclerOptions = new FirebaseRecyclerOptions
//                .Builder<User>()
//                .setQuery(query, new SnapshotParser<User>() {
//                    @NonNull
//                    @Override
//                    public User parseSnapshot(@NonNull DataSnapshot snapshot) {
//                        User tempUser = snapshot.getValue(User.class);
//                        if (tempUser.getUserName().equals(userEmailStr)){
//                            return tempUser;
//                        } else {
//                            return new User();
//                        }
//                    }
//                })
//                .build();

        FirebaseRecyclerOptions firebaseRecyclerOptions = new FirebaseRecyclerOptions
                .Builder<User>()
                .setQuery(query, User.class)
                .build();

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<User, UserDisplay>(firebaseRecyclerOptions) {
            @NonNull
            @Override
            public UserDisplay onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                // Inflate the user card view
                View view = LayoutInflater
                        .from(parent.getContext())
                        .inflate(R.layout.activity_display_user_database, parent, false);
                return new UserDisplay(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull UserDisplay holder, int position, @NonNull User model) {
                holder.setUserName(model.getUserName());
                holder.setUserEmail(model.getEmailAddress());

                // If you click the user name it should add it to the group HERE:
                //TODO: add the user to the group as a group member HERE.
            }
        };

        // Attach the adapter
        firebaseRecyclerAdapter.startListening();
        autoUserSearchList.setAdapter(firebaseRecyclerAdapter);
    }
}
