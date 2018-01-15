package com.cool.sarthak.hiii;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {
    private EditText mName,mEmail,mPassword;
    Button mRegister;
    String m_Name,m_Email,m_Password;
    private FirebaseAuth mAuth;
    private Toolbar mToolbar;
    private DatabaseReference mDatabase;

    private ProgressDialog mprogress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mName=findViewById(R.id.regname);
        mEmail=findViewById(R.id.regemailid);
        mPassword=findViewById(R.id.regpassword);
        mRegister=findViewById(R.id.regcreateaccount);

        // toolbar


        mToolbar=findViewById(R.id.registerpagetoolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Create Account");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //progress bar

        mprogress= new ProgressDialog(this);

        //firebase


        mAuth = FirebaseAuth.getInstance();
        mRegister.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        m_Name=mName.getText().toString();
                        m_Email=mEmail.getText().toString();
                        m_Password=mPassword.getText().toString();
                        register_user(m_Name,m_Email,m_Password);

                        //progress initialising

                        mprogress.setTitle("registering user..");
                        mprogress.setMessage("Please Wait While We Create Your Account.");
                        mprogress.setCanceledOnTouchOutside(false);
                        mprogress.show();





                    }
                }
        );

    }

    private void register_user(final String m_name, String m_email, String m_password) {
        mAuth.createUserWithEmailAndPassword(m_email, m_password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {


                            // getting uid

                            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                            String uid = currentUser.getUid();

                            //database


                            mDatabase = FirebaseDatabase.getInstance().getReference().child("User").child(uid);
                            HashMap<String ,String> mHashMap = new HashMap<>();
                            mHashMap.put("name",m_name);
                            mHashMap.put("status","Hi There!");
                            mHashMap.put("image","default");
                            mHashMap.put("thumbnail_image","default");
                     mDatabase.setValue(mHashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                         @Override
                         public void onComplete(@NonNull Task<Void> task) {
                             Intent intent = new Intent(RegisterActivity.this,MainActivity.class);
                             intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                             startActivity(intent);
                             finish();

                         }
                     });







                            FirebaseUser user = mAuth.getCurrentUser();
                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            //Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }

                        // ...
                    }
                });
    }


}
