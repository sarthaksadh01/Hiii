package com.cool.sarthak.hiii;
//
//
//import android.app.AlertDialog;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.v4.app.Fragment;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.firebase.ui.database.FirebaseRecyclerAdapter;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;
//import com.squareup.picasso.Picasso;
//
//import de.hdodenhof.circleimageview.CircleImageView;
//
//
///**
// * A simple {@link Fragment} subclass.
// */
//public class FriendsFragment extends Fragment {
//    private RecyclerView mFriendsList;
//
//    private DatabaseReference mFriendsDatabase;
//    private DatabaseReference mUsersDatabase;
//
//    private FirebaseAuth mAuth;
//
//    private String mCurrent_user_id;
//
//    //private View mMainView;
//
//
//    private View mMainView;
//
//
//    public FriendsFragment() {
//        // Required empty public constructor
//    }
//
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//
//        mMainView=inflater.inflate(R.layout.fragment_friends, container, false);
//
//        //recycler view
//        mFriendsList = (RecyclerView) mMainView.findViewById(R.id.friends_list);
//        mAuth = FirebaseAuth.getInstance();
//
//        mCurrent_user_id = mAuth.getCurrentUser().getUid();
//
//        mFriendsDatabase = FirebaseDatabase.getInstance().getReference().child("Friends").child(mCurrent_user_id);
//        mFriendsDatabase.keepSynced(true);
//        mUsersDatabase = FirebaseDatabase.getInstance().getReference().child("User");
//        mUsersDatabase.keepSynced(true);
//
//
//        mFriendsList.setHasFixedSize(true);
//        mFriendsList.setLayoutManager(new LinearLayoutManager(getContext()));
//
//
//
//
//
//        return mMainView;
//    }
//
//    @Override
//    public void onStart() {
//        super.onStart();
//
//        FirebaseRecyclerAdapter<Friends,FriendsViewHolder>firebaseRecyclerAdapter= new FirebaseRecyclerAdapter<Friends, FriendsViewHolder>(
//                Friends.class,
//                R.layout.single_user,
//                FriendsViewHolder.class,
//                mFriendsDatabase
//
//
//        ) {
//            @Override
//            protected void populateViewHolder(final FriendsViewHolder viewHolder, Friends model, int position) {
//                //viewHolder.setDate(model.getDate());
//               final String list_user_id = getRef(position).getKey();
//                mUsersDatabase.child(list_user_id).addValueEventListener(
//                        new ValueEventListener() {
//                            @Override
//                            public void onDataChange(DataSnapshot dataSnapshot) {
//                                String userName = dataSnapshot.child("name").getValue().toString();
//                               // String userThumb = dataSnapshot.child("image").getValue().toString();
//
//                                viewHolder.setName(userName);
//                               // viewHolder.setUserImage(userThumb, getContext());
//
////                                viewHolder.mView.setOnClickListener(
////                                        new
////                                                View.OnClickListener() {
////                                                    @Override
////                                                    public void onClick(View view) {
////                                                        CharSequence options[] = new CharSequence[]{"Open Profile", "Send message"};
////
////                                                        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
////
////                                                        builder.setTitle("Select Options");
////                                                        builder.setItems(options, new DialogInterface.OnClickListener() {
////                                                            @Override
////                                                            public void onClick(DialogInterface dialogInterface, int i) {
////                                                                if ((i==0))
////                                                                {
////                                                                    Intent profileIntent = new Intent(getContext(), UserProfile.class);
////                                                                    profileIntent.putExtra("uid", list_user_id);
////                                                                    startActivity(profileIntent);
////
////                                                                }
////                                                            }
////                                                        });
////                                                        builder.show();
////                                                    }
////                                                }
////                                );
//                            }
//
//                            @Override
//                            public void onCancelled(DatabaseError databaseError) {
//
//                            }
//                        }
//                );
//
//            }
//        };
//        mFriendsList.setAdapter(firebaseRecyclerAdapter);
//    }
//
//    public static class FriendsViewHolder extends RecyclerView.ViewHolder {
//
//        View mView;
//
//        public FriendsViewHolder(View itemView) {
//            super(itemView);
//
//            mView = itemView;
//
//        }
////        public void setDate(String date){
////
////            TextView userStatusView = (TextView) mView.findViewById(R.id.user_single_status);
////            userStatusView.setText(date);
////
////        }
//
//        public void setName(String name){
//
//            TextView userNameView = (TextView) mView.findViewById(R.id.user_single_name);
//            userNameView.setText(name);
//
//        }
//
////        public void setUserImage(String thumb_image, Context ctx){
////
////            CircleImageView userImageView = (CircleImageView) mView.findViewById(R.id.user_single_image);
////            Picasso.with(ctx).load(thumb_image).placeholder(R.drawable.blankprofile).into(userImageView);
////
////        }
//
//    }
//}






//-------------------------------------------TRY AGAIN-----------------------------------------------















import android.app.ActivityOptions;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class FriendsFragment extends Fragment {

    private RecyclerView mFriendsList;

    private DatabaseReference mFriendsDatabase;
    private DatabaseReference mUsersDatabase;

    private FirebaseAuth mAuth;

    private String mCurrent_user_id;

    private View mMainView;


    public FriendsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mMainView = inflater.inflate(R.layout.fragment_friends, container, false);

        mFriendsList = (RecyclerView) mMainView.findViewById(R.id.friends_list);
        mAuth = FirebaseAuth.getInstance();

        mCurrent_user_id = mAuth.getCurrentUser().getUid();

        mFriendsDatabase = FirebaseDatabase.getInstance().getReference().child("Friends").child(mCurrent_user_id);
        mFriendsDatabase.keepSynced(true);
        mUsersDatabase = FirebaseDatabase.getInstance().getReference().child("User");
        mUsersDatabase.keepSynced(true);


        mFriendsList.setHasFixedSize(true);
        mFriendsList.setLayoutManager(new LinearLayoutManager(getContext()));

        // Inflate the layout for this fragment
        return mMainView;
    }


    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<abcde, FriendsViewHolder> friendsRecyclerViewAdapter = new FirebaseRecyclerAdapter<abcde, FriendsViewHolder>(

                abcde.class,
                R.layout.users_single_layout,
                FriendsViewHolder.class,
                mFriendsDatabase


        ) {
            @Override
            protected void populateViewHolder(final FriendsViewHolder friendsViewHolder, abcde friends, int i) {

                //friendsViewHolder.setDate(friends.getDate());

                final String list_user_id = getRef(i).getKey();

                mUsersDatabase.child(list_user_id).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        final String userName = dataSnapshot.child("name").getValue().toString();
                        String userThumb = dataSnapshot.child("thumbnail_image").getValue().toString();
                        String userStatus = dataSnapshot.child("status").getValue().toString();

                        if (dataSnapshot.hasChild("online")) {

                            String userOnline = dataSnapshot.child("online").getValue().toString();
                            friendsViewHolder.setUserOnline(userOnline);

                        }

                        friendsViewHolder.setName(userName);
                        friendsViewHolder.setUserImage(userThumb, getContext());
                        friendsViewHolder.setStatus(userStatus);

                        friendsViewHolder.mView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                CharSequence options[] = new CharSequence[]{"Open Profile", "Send message"};

                                final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                                builder.setTitle("Select Options");
                                builder.setItems(options, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                        //Click Event for each item.
                                        if (i == 0) {

                                            Intent profileIntent = new Intent(getContext(), UserProfile.class);
                                            profileIntent.putExtra("uid", list_user_id);
                                            View sharedView = friendsViewHolder.mView;
                                            String transitionName = "username";
                                            ActivityOptions transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation(getActivity(), sharedView, transitionName);

                                            startActivity(profileIntent, transitionActivityOptions.toBundle());

                                        }

                                        if (i == 1) {

                                            Intent chatIntent = new Intent(getContext(), ChatActivity.class);
                                            chatIntent.putExtra("user_id", list_user_id);
                                            chatIntent.putExtra("user_name", userName);
                                            startActivity(chatIntent);

                                        }

                                    }
                                });

                                builder.show();

                            }
                        });


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        };

        mFriendsList.setAdapter(friendsRecyclerViewAdapter);


    }


    public static class FriendsViewHolder extends RecyclerView.ViewHolder {

        View mView;

        public FriendsViewHolder(View itemView) {
            super(itemView);

            mView = itemView;

        }

        public void setStatus(String status) {

            TextView userStatusView = (TextView) mView.findViewById(R.id.user_single_status);
            userStatusView.setText(status);

        }

        public void setName(String name) {

            TextView userNameView = (TextView) mView.findViewById(R.id.user_single_name);
            userNameView.setText(name);

        }

        public void setUserImage(String thumb_image, Context ctx) {

            CircleImageView userImageView = (CircleImageView) mView.findViewById(R.id.user_single_image);
            Picasso.with(ctx).load(thumb_image).placeholder(R.drawable.blankprofile).into(userImageView);

        }

        public void setUserOnline(String online_status) {

            ImageView userOnlineView = (ImageView) mView.findViewById(R.id.onlinestatus);

            if (online_status.equals("true")) {

                userOnlineView.setVisibility(View.VISIBLE);

            } else {

                userOnlineView.setVisibility(View.INVISIBLE);

            }

        }


    }
}


