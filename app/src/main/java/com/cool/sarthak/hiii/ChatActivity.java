package com.cool.sarthak.hiii;

import android.app.ProgressDialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.zip.Inflater;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.google.firebase.database.ServerValue.TIMESTAMP;

public class ChatActivity extends AppCompatActivity {

    //--------------------------------varaibles------------------------------------------------------




    private Toolbar mToolbar;
    private TextView mTitleView;
    private TextView mLastSeenView;
    private CircleImageView mProfileImage;
    private String uid, uNmame, myuid;
    private DatabaseReference mRootRef,profileref,onl9ref;
    private ImageView send;
    private EditText mChatMessageView;
    private FirebaseUser Usr;
    private RecyclerView msglist;
    private DatabaseReference chatknow;
    private LinearLayoutManager mLinearLayout;

    @Override
    protected void onPause() {
        super.onPause();
        onl9ref.child(Usr.getUid()).child("online").setValue("false");
    }

    @Override
    protected void onStart() {
        super.onStart();
        onl9ref=FirebaseDatabase.getInstance().getReference().child("User");
        Usr=FirebaseAuth.getInstance().getCurrentUser();
        onl9ref.child(Usr.getUid()).child("online").setValue("true");
        chatknow.keepSynced(true);



    }

    private StorageReference mStorageRef;
    private String randm= "abcdefghijklmnopqrstuvwxyz";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);


        uid = getIntent().getStringExtra("user_id");
        uNmame = getIntent().getStringExtra("user_name");
        Usr=FirebaseAuth.getInstance().getCurrentUser();
        myuid=Usr.getUid();


        mRootRef = FirebaseDatabase.getInstance().getReference();
        profileref=FirebaseDatabase.getInstance().getReference().child("User").child(uid);


        msglist=findViewById(R.id.chatlist);
        msglist.setHasFixedSize(true);


        mLinearLayout= new LinearLayoutManager(this);
        mLinearLayout.setSmoothScrollbarEnabled(true);
       // mLinearLayout.setReverseLayout(true);
        mLinearLayout.setStackFromEnd(true);


       //----------------------------custom app bar-------------------------------------------------------------------------------

       mToolbar = findViewById(R.id.chatactivitypagetoolbar);
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View action_bar_view = inflater.inflate(R.layout.chateverything,null);
        actionBar.setCustomView(action_bar_view);

        // ---- Custom Action bar Items ----

        mTitleView = (TextView) findViewById(R.id.custom_bar_title);
        mLastSeenView = (TextView) findViewById(R.id.custom_bar_seen);
        mProfileImage = (CircleImageView) findViewById(R.id.custom_bar_image);
        mTitleView.setText(uNmame);

        profileref.addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String profileimage= dataSnapshot.child("thumbnail_image").getValue().toString();
                        if(dataSnapshot.hasChild("online")) {
                            String onlineStatus = dataSnapshot.child("online").getValue().toString();
                            if (onlineStatus.equals("true")) {
                                mLastSeenView.setText("online");
                            } else {
                                mLastSeenView.setText("offline");
                            }
                        }
                        Picasso.with(getApplicationContext()).load(profileimage).placeholder(R.drawable.blankprofile).into(mProfileImage);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                }
        );



        msglist.setLayoutManager(mLinearLayout);
        mStorageRef= FirebaseStorage.getInstance().getReference();
        chatknow= FirebaseDatabase.getInstance().getReference().child("messages").child(myuid).child(uid);


        // toolbar



//
        mChatMessageView = findViewById(R.id.chatactivitymsg);
        send = findViewById(R.id.chatactivitysend);

        //----------------------------------------- CREATING CHAT---------------------------------------------


        mRootRef.child("Chat").child(myuid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(!dataSnapshot.hasChild(uid)){

                    Map chatAddMap = new HashMap();
                    chatAddMap.put("seen", false);
                    chatAddMap.put("timestamp", TIMESTAMP);

                    Map chatUserMap = new HashMap();
                    chatUserMap.put("Chat/" + myuid + "/" + uid, chatAddMap);
                    chatUserMap.put("Chat/" + uid + "/" + myuid, chatAddMap);

                    mRootRef.updateChildren(chatUserMap, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                            if(databaseError != null){

                                Log.d("CHAT_LOG", databaseError.getMessage().toString());

                            }

                        }
                    });

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        send.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String message = mChatMessageView.getText().toString();
                        sendmessage(message);
                    }
                }
        );
        loadmsg();


    }

    //-------------------------------------------------SENDING MSG---------------------------------------------------------

    private void sendmessage( String message) {






      //  if(!TextUtils.isEmpty(message)){

            String current_user_ref = "messages/" + myuid + "/" + uid;
            String chat_user_ref = "messages/" + uid + "/" + myuid;

            DatabaseReference user_message_push = mRootRef.child("messages")
                    .child(myuid).child(uid).push();

            String push_id = user_message_push.getKey();

            Map messageMap = new HashMap();
            messageMap.put("message", message);
            messageMap.put("seen", false);
            messageMap.put("type", "text");
            messageMap.put("time", TIMESTAMP);
            messageMap.put("from", myuid);

            Map messageUserMap = new HashMap();
            messageUserMap.put(current_user_ref + "/" + push_id, messageMap);
            messageUserMap.put(chat_user_ref + "/" + push_id, messageMap);

            mChatMessageView.setText("");

            mRootRef.child("Chat").child(myuid).child(uid).child("seen").setValue(true);
            mRootRef.child("Chat").child(myuid).child(uid).child("timestamp").setValue(TIMESTAMP);

            mRootRef.child("Chat").child(uid).child(myuid).child("seen").setValue(false);
            mRootRef.child("Chat").child(uid).child(myuid).child("timestamp").setValue(TIMESTAMP);


            mRootRef.updateChildren(messageUserMap, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                    if(databaseError != null){

                        Log.d("CHAT_LOG", databaseError.getMessage().toString());

                    }

                }
            });

       // }


    }


    //---------------------------random image name------------------------------

    String randomString( int length) {
        Random r = new Random(); // perhaps make it a class variable so you don't make a new one every time
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < length; i++) {
            char c = (char)(r.nextInt((int)(Character.MAX_VALUE)));
            sb.append(c);
        }
        return sb.toString();
    }

    //-------------------------------------------------SENDING IMAGE---------------------------------------------------------

public void sendImage(View view)
{



    CropImage.activity()
            .setGuidelines(CropImageView.Guidelines.ON)
            .start(ChatActivity.this);

}

//------------------------ OPENING GALLERY-------------------------------
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if( requestCode==RESULT_OK) {
            Uri imageUri = data.getData();

        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                final ProgressDialog mprogress;
                mprogress = new ProgressDialog(this);
                mprogress.setTitle("sending pic");
                mprogress.setCanceledOnTouchOutside(false);
                mprogress.show();
                Uri resultUri = result.getUri();




                final String demo= randomString(10);

                StorageReference filepath =mStorageRef.child("chat_images").child(myuid).child(uid).child(demo+".jpeg");
                filepath.putFile(resultUri).addOnCompleteListener(
                        new OnCompleteListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                if(task.isSuccessful())
                                {

                                    //DO nothing
                                    mStorageRef.child("chat_images").child(myuid).child(uid).child(demo+".jpeg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            String imagelocation=uri.toString();
                                            sendmessage(imagelocation);


                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception exception) {
                                            // Handle any errors
                                            Toast.makeText(getApplicationContext(),""+exception,Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    mprogress.hide();

                                }

                            }

                        }



                );



            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }



//-------------------------------------------------LOADING MSG---------------------------------------------------------


       public void loadmsg(){



        final FirebaseRecyclerAdapter<Messages, chats> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Messages, chats>(
                Messages.class,
                R.layout.message_single_layout,
                chats.class,
                chatknow


        )




        {


            @Override
            protected void populateViewHolder(ChatActivity.chats viewHolder, Messages model, int position) {






                if(model.getFrom().equals(myuid))
                {
                    if(model.getMessage().contains("firebasestorage"))
                    {


                        viewHolder.setImage(model.getMessage());

                    }

                   else
                       viewHolder.setMessage2(model.getMessage());

               }
               else {

                    if(model.getMessage().contains("firebasestorage"))
                    {
                        viewHolder.setImage2(model.getMessage());
                    }



                    else
                        viewHolder.setMessage(model.getMessage());

                }


            }
        };



        msglist.setAdapter(firebaseRecyclerAdapter);

          // msglist.smoothScrollToPosition(firebaseRecyclerAdapter.getItemCount());





    }




    public static class chats extends RecyclerView.ViewHolder {
        View mmview;
        public chats(View itemView) {
            super(itemView);
            mmview=itemView;
        }

        //-------------------------------------------------SETTING VIEWS---------------------------------------------------------

        public void setImage(String image)
        {
            TextView msg,msg2;
            msg= mmview.findViewById(R.id.messagesingle);
            msg.setVisibility(View.INVISIBLE);
            msg2= mmview.findViewById(R.id.messagesingle2);
            msg2.setVisibility(View.INVISIBLE);
            ImageView imageView1,imageView2;
            imageView2 = mmview.findViewById(R.id.messagesinglepic2);
            imageView2.setVisibility(View.GONE);

            imageView1=mmview.findViewById(R.id.messagesinglepic);
            imageView1.setVisibility(View.VISIBLE);
            Picasso.with(mmview.getContext()).load(image).into(imageView1);


        }
        public void setImage2(final String image)
        {
            TextView msg,msg2;
            msg= mmview.findViewById(R.id.messagesingle);
            msg.setVisibility(View.INVISIBLE);
            msg2= mmview.findViewById(R.id.messagesingle2);
            msg2.setVisibility(View.INVISIBLE);

            ImageView imageView2,imageView1;
            imageView1 = mmview.findViewById(R.id.messagesinglepic);
            imageView1.setVisibility(View.GONE);

            imageView2=mmview.findViewById(R.id.messagesinglepic2);
            imageView2.setVisibility(View.VISIBLE);
            Picasso.with(mmview.getContext()).load(image).into(imageView2);



        }


        public void setMessage(String message){
            TextView msg2;
            msg2= mmview.findViewById(R.id.messagesingle2);
            msg2.setVisibility(View.INVISIBLE);
            ImageView imageView2,imageView1;
            imageView1 = mmview.findViewById(R.id.messagesinglepic);
            imageView1.setVisibility(View.GONE);
            imageView2=mmview.findViewById(R.id.messagesinglepic2);
            imageView2.setVisibility(View.GONE);


            TextView msg;

            msg= mmview.findViewById(R.id.messagesingle);
            msg.setVisibility(View.VISIBLE);
            msg.setText(message);
        }
        public  void setMessage2(String message2)
        {
            TextView msg;
            msg= mmview.findViewById(R.id.messagesingle);
            msg.setVisibility(View.INVISIBLE);
            ImageView imageView2,imageView1;
            imageView1 = mmview.findViewById(R.id.messagesinglepic);
            imageView1.setVisibility(View.GONE);
            imageView2=mmview.findViewById(R.id.messagesinglepic2);
            imageView2.setVisibility(View.GONE);


            TextView msg2;
            msg2= mmview.findViewById(R.id.messagesingle2);
            msg2.setVisibility(View.VISIBLE);
            msg2.setText(message2);
        }
    }


///----------------------opening profile on clicking photo------------------------

    public void openprofile(View view) {
        Intent intent = new Intent(ChatActivity.this,UserProfile.class);
        intent.putExtra("uid",uid);
        startActivity(intent);
    }


//    public void scroll()
//    {
//        msglist.smoothScrollToPosition(firebaseRecyclerAdapter.getItemCount());
//
//    }


}




