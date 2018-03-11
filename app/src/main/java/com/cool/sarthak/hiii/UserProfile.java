package com.cool.sarthak.hiii;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.security.Timestamp;
import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;

public class UserProfile extends AppCompatActivity {
    TextView name,status;
    ImageView dp;
    DatabaseReference databaseReference,friends,makingfriends,sendNotification,onlineStatus;
    String namef,statusf,dpf;
    Button sendreq;
    String currentState;
    FirebaseUser firebaseUser;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setTheme(android.R.style.Theme_Dialog);
        setContentView(R.layout.activity_user_profile);
        name=findViewById(R.id.userprofilename);
        status=findViewById(R.id.userprofilestatus);
        dp=findViewById(R.id.userprofiledp);
        sendreq=findViewById(R.id.userprofilesnedrequest);
        currentState="notFriends";
        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        friends=FirebaseDatabase.getInstance().getReference();
        sendNotification=FirebaseDatabase.getInstance().getReference().child("notifications");
        makingfriends=FirebaseDatabase.getInstance().getReference().child("Friends");
        onlineStatus=FirebaseDatabase.getInstance().getReference().child("User").child(firebaseUser.getUid()).child("online");
        onlineStatus.setValue("true");
       // databaseReference.keepSynced(true);



        //getting user uid
        final String Useruid= getIntent().getStringExtra("uid");

        // -------------------checking if Friends or not-------------------------------------

        makingfriends.child(firebaseUser.getUid()).addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.hasChild(Useruid))
                        {
                            currentState="Friends";
                            sendreq.setText("unfriend");
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                }
        );

        // ------------checking if request already sent or accepted----------------------------

        friends.child("FriendReq").child(firebaseUser.getUid()).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChild(Useruid))
                        {
                            String reqTType=dataSnapshot.child(Useruid).child("requestType").getValue().toString();
                            if(reqTType.equals("recieved")){
                                currentState="reqReceived";
                                sendreq.setText("Accept Friend Request");

                            }
                            else {
                                currentState ="friendReqSent";
                                sendreq.setText("Cancel Friend Request");
                            }
                        }
                    }



                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                }
        );





        databaseReference= FirebaseDatabase.getInstance().getReference().child("User").child(Useruid);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                 namef = dataSnapshot.child("name").getValue().toString();
                 statusf = dataSnapshot.child("status").getValue().toString();
                 dpf = dataSnapshot.child("image").getValue().toString();

                name.setText(namef);

                status.setText(statusf);
                if(dpf.equals("default"))
                {
                    dp.setImageResource(R.drawable.blankprofile);
                }
                else
                Picasso.with(getApplicationContext()).load(dpf).into(dp);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

sendreq.setOnClickListener(
        new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendreq.setClickable(false);

                // --------------------------not Friends------------------------------------





                //---------------------------- send request----------------------------


                if(currentState.equals("notFriends"))
                {
                    friends.child("FriendReq").child(firebaseUser.getUid()).child(Useruid).child("requestType").setValue("sent");
                    friends.child("FriendReq").child(Useruid).child(firebaseUser.getUid()).child("requestType").setValue("recieved").addOnCompleteListener(
                            new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful())
                                    {
                                        HashMap<String,String>notificationData= new HashMap<>();
                                        notificationData.put("from",firebaseUser.getUid());
                                        notificationData.put("type","request");

                                        sendNotification.child(Useruid).push().setValue(notificationData);
                                        sendreq.setClickable(true);
                                        sendreq.setText("cancel friend request");
                                        currentState="friendReqSent";
                                    }
                                }
                            }
                    );
                }

                //---------------------------- cancel friend request-------------------------

                if (currentState.equals("friendReqSent"))
                {
                    sendreq.setClickable(false);
                    friends.child("FriendReq").child(firebaseUser.getUid()).removeValue();
                    friends.child("FriendReq").child(Useruid).removeValue();
                    sendreq.setClickable(true);
                    sendreq.setText("send friend request");
                    currentState="notFriends";

                }

                // ------------------------accept request--------------------------------

                if(currentState.equals("reqReceived"))
                {
                    final String currentDate = DateFormat.getDateTimeInstance().format(new Date());

                    makingfriends.child(firebaseUser.getUid()).child(Useruid).child("date").setValue(currentDate);
                    makingfriends.child(Useruid).child(firebaseUser.getUid()).child("date").setValue(currentDate);
                    friends.child("FriendReq").child(firebaseUser.getUid()).removeValue();
                    friends.child("FriendReq").child(Useruid).removeValue();
//                    sendreq.setVisibility(View.INVISIBLE);
//                    sendreq.setClickable(false);

                    currentState="Friends";
                    sendreq.setText("unfriend");


                }

                 // -------------unfriend---------------------------



                if(currentState.equals("Friends"))
                {

                    makingfriends.child("Friends").child(firebaseUser.getUid()).removeValue();
                    makingfriends.child("Friends").child(Useruid).removeValue().addOnCompleteListener(
                            new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    currentState="notFriends";
                                    sendreq.setText("send friend request");

                                }
                            }
                    );

                }




            }
        }
);


    }

    @Override
    protected void onPause() {
        super.onPause();
        onlineStatus.setValue("false");
    }
}
