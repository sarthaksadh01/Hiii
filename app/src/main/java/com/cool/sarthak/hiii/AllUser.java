package com.cool.sarthak.hiii;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

//import static com.cool.sarthak.hiii.AllUser.ChatHolder.*;

public class AllUser extends AppCompatActivity {
    int options =1;
    Toolbar mToolbar;
    RecyclerView recyclerView;
    private DatabaseReference databaseReference;
     static String TAG = "qwerty";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_user);
        databaseReference= FirebaseDatabase.getInstance().getReference().child("User");


        // toolbar


        mToolbar=findViewById(R.id.alluserpagetoolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("All Users");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //recycler view

        recyclerView=findViewById(R.id.alluserusers);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerAdapter<Users,UsersViewHolder> firebaseRecyclerAdapter=
                new FirebaseRecyclerAdapter<Users, UsersViewHolder>(
                        Users.class,
                        R.layout.single_user,
                        UsersViewHolder.class,
                        databaseReference

                ) {
                    @Override
                    protected void populateViewHolder(UsersViewHolder viewHolder, Users model, int position) {
                        Log.d(TAG, "populateViewHolder: here");
                        viewHolder.setName(model.getName());

                    }
                };


        recyclerView.setAdapter(firebaseRecyclerAdapter);

    }
//    Query query = FirebaseDatabase.getInstance()
//            .getReference()
//            .child("User")
//            .limitToLast(50);

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: lol");



        //firebaseRecyclerAdapter.startListening();


    }

    public static class UsersViewHolder extends RecyclerView.ViewHolder {
        View view;
        public UsersViewHolder(View itemView) {

            super(itemView);
            Log.d(TAG, "UsersViewHolder: here");
            view=itemView;
        }

        public void setName(String name){
            TextView usernameView=view.findViewById(R.id.singleusername);
            Log.d(TAG, "setName: "+name);
            usernameView.setText(name);

        }
    }
}
