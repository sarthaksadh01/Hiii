package com.cool.sarthak.hiii;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;

public class AccountSetting extends AppCompatActivity {
    private DatabaseReference databaseReference;
    private FirebaseUser mCurrentUser;
    private TextView mname,mstatus;
    private Button mstatuschange,mdpchange;
    private final static int req=1;
    private StorageReference mStorageRef;
    private String uid;
    private TextView notice;
    private CircleImageView circleImageView;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_setting);
        mstatuschange=findViewById(R.id.actsettingstatuschange);
        mstatus=findViewById(R.id.actsettingstatus);
        mname =findViewById(R.id.actsettingname);
        mdpchange=findViewById(R.id.actsettingdpchange);
        circleImageView=findViewById(R.id.actsettingdp);


        //cloud storage
        mStorageRef = FirebaseStorage.getInstance().getReference();


          //setting previous dp
        Picasso.with(getApplicationContext()).setLoggingEnabled(true);




        // fire basea

        mCurrentUser= FirebaseAuth.getInstance().getCurrentUser();
         uid = mCurrentUser.getUid();

         // cloud storage

        mStorageRef.child("profile_images/"+uid+".jpeg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Got the download URL for 'users/me/profile.png'
                Picasso.with(getApplicationContext()).load(uri).placeholder(R.drawable.blankprofile).into(circleImageView);
               // Log.d("lol", "onCreate: "+ uri);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
                Toast.makeText(getApplicationContext(),""+exception,Toast.LENGTH_SHORT).show();
            }
        });

// user info


        databaseReference= FirebaseDatabase.getInstance().getReference().child("User").child(uid);
        databaseReference.child("online").setValue("true");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("name").getValue().toString();
                String status = dataSnapshot.child("status").getValue().toString();
                String image = dataSnapshot.child("image").getValue().toString();

                mname.setText(name);

                mstatus.setText(status);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        mstatuschange.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String StatusValue = mstatus.getText().toString();
                        Intent intent =new Intent(AccountSetting.this,ChangeStatus.class);
                        //intent.putExtra("previous_status",mstatus.getText().toString());
                        intent.putExtra("previous_status",StatusValue);
                        startActivity(intent);
                    }
                }
        );

        mdpchange.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//

                        CropImage.activity()
                                .setGuidelines(CropImageView.Guidelines.ON).setAspectRatio(1,1)
                                .start(AccountSetting.this);
                    }
                }
        );



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if( resultCode==req&&requestCode==RESULT_OK) {
         Uri imageUri = data.getData();

    }

            if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if (resultCode == RESULT_OK) {
                    final ProgressDialog mprogress;
                    mprogress = new ProgressDialog(this);
                    mprogress.setTitle("Changing Profile Picture");
                    mprogress.setCanceledOnTouchOutside(false);
                    mprogress.show();
                    Uri resultUri = result.getUri();

//--------------------------creating thumbnail----------------------------------------------------------------------


                    final File thumb_filePath = new File(resultUri.getPath());
                    Bitmap thumb_bitmap = null;
                    try {
                        thumb_bitmap = new Compressor(this)
                                .setMaxWidth(200)
                                .setMaxHeight(200)
                                .setQuality(75)
                                .compressToBitmap(thumb_filePath);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    thumb_bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    final byte[] thumb_byte = baos.toByteArray();


                    StorageReference filepath =mStorageRef.child("profile_images").child(uid+".jpeg");
                    final StorageReference thumb_filepath = mStorageRef.child("profile_images").child("thumbs").child(uid + ".jpg");
                    filepath.putFile(resultUri).addOnCompleteListener(
                            new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                    if(task.isSuccessful())
                                    {


                                        //DO nothing
                                        mStorageRef.child("profile_images/"+uid+".jpeg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {


                                                // Got the download URL for 'users/me/profile.png'
                                                Picasso.with(getApplicationContext()).load(uri).into(circleImageView);
                                                Log.d("lol", "onCreate: "+ uri);
                                                String imagelocation=uri.toString();
                                                databaseReference.child("image").setValue(imagelocation).addOnCompleteListener(
                                                        new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful())
                                                                {
//


                                                                    UploadTask uploadTask = (UploadTask) thumb_filepath.putBytes(thumb_byte).addOnCompleteListener(
                                                                            new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                                                                @Override
                                                                                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                                                                    if(task.isSuccessful())
                                                                                    {
                                                                                      thumb_filepath.getDownloadUrl().addOnSuccessListener(
                                                                                              new OnSuccessListener<Uri>() {
                                                                                                  @Override
                                                                                                  public void onSuccess(Uri uri) {
                                                                                                      String thumbLocation=uri.toString();
                                                                                                      databaseReference.child("thumbnail_image").setValue(thumbLocation).addOnCompleteListener(
                                                                                                              new OnCompleteListener<Void>() {
                                                                                                                  @Override
                                                                                                                  public void onComplete(@NonNull Task<Void> task) {
                                                                                                                      if(task.isSuccessful())
                                                                                                                      {
                                                                                                                          mprogress.hide();
                                                                                                                      }
                                                                                                                      else {
                                                                                                                          mprogress.hide();
                                                                                                                          Toast.makeText(AccountSetting.this, "try again", Toast.LENGTH_SHORT).show();
                                                                                                                      }
                                                                                                                  }
                                                                                                              }
                                                                                                      );
                                                                                                  }
                                                                                              }
                                                                                      );
                                                                                        //databaseReference.child("thumbnail_image").setValue(thumbLocation);
                                                                                    }

                                                                                }
                                                                            }
                                                                    );


                                                                }
                                                            }
                                                        }
                                                );



                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception exception) {
                                                // Handle any errors
                                                Toast.makeText(getApplicationContext(),""+exception,Toast.LENGTH_SHORT).show();
                                            }
                                        });


                                    }

                                    }

                                }



                    );



                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    Exception error = result.getError();
                }
            }
        }
    @Override
    protected void onPause() {
        super.onPause();
        databaseReference.child("online").setValue("false");
    }


}
