package com.cool.sarthak.hiii;


import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


public class RequestFragment extends Fragment {
    private DatabaseReference req;
    private DatabaseReference userinfo;
    private View reqView;
    private RecyclerView reqlist;
    private FirebaseUser firebaseUser;
    FirebaseAuth firebaseAuth;


    public RequestFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseUser= firebaseAuth.getCurrentUser();
        req = FirebaseDatabase.getInstance().getReference().child("FriendReq").child(firebaseUser.getUid());

        userinfo=FirebaseDatabase.getInstance().getReference().child("User");
        reqView = inflater.inflate(R.layout.fragment_request, container, false);


        //recycler view

        reqlist = reqView.findViewById(R.id.requestlist);
        reqlist.setHasFixedSize(true);
        reqlist.setLayoutManager(new LinearLayoutManager(getContext()));



        return reqView;

    }

    @Override
    public void onStart() {
        super.onStart();


        FirebaseRecyclerAdapter<getRequest,reqViewHolder>firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<getRequest, reqViewHolder>(
                getRequest.class,
                R.layout.users_single_layout,
                reqViewHolder.class,
                req

        )
        {


            @Override
            protected void populateViewHolder(final reqViewHolder viewHolder, final getRequest model, int position) {

                String req_type= model.getRequestType();



                    final String list_user_id = getRef(position).getKey();

                    userinfo.child(list_user_id).addValueEventListener(
                            new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    //if (model.getRequestType().equals("recieved")) {
                                        final String userName = dataSnapshot.child("name").getValue().toString();
                                        String userThumb = dataSnapshot.child("thumbnail_image").getValue().toString();
                                        String userStatus = dataSnapshot.child("status").getValue().toString();
                                        viewHolder.setName(userName);
                                        viewHolder.setUserImage(userThumb, getContext());
                                        viewHolder.setStatus(userStatus);
                                        viewHolder.mView.setOnClickListener(
                                                new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View view) {

                                                        Intent profileIntent = new Intent(getContext(), UserProfile.class);
                                                        profileIntent.putExtra("uid", list_user_id);

                                                        View sharedView = viewHolder.mView;
                                                        String transitionName = "username";
                                                        ActivityOptions transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation(getActivity(), sharedView, transitionName);
                                                        startActivity(profileIntent,transitionActivityOptions.toBundle());
                                                    }
                                                }
                                        );





                                }


                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            }

                    );

                }



        };
        reqlist.setAdapter(firebaseRecyclerAdapter);
    }

    public static class reqViewHolder extends RecyclerView.ViewHolder {
        View mView;
        public reqViewHolder(View itemView) {
            super(itemView);
            mView=itemView;
        }
        public void setStatus(String status){

            TextView userStatusView = (TextView) mView.findViewById(R.id.user_single_status);
            userStatusView.setText(status);

        }

        public void setName(String name){

            TextView userNameView = (TextView) mView.findViewById(R.id.user_single_name);
            userNameView.setText(name);

        }

        public void setUserImage(String thumb_image, Context ctx){

            CircleImageView userImageView = (CircleImageView) mView.findViewById(R.id.user_single_image);
            Picasso.with(ctx).load(thumb_image).placeholder(R.drawable.blankprofile).into(userImageView);

        }
    }



}
