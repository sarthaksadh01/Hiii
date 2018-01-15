package com.cool.sarthak.hiii;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ChangeStatus extends AppCompatActivity {
    private EditText statuschange;
    private Button savechanges;
    private FirebaseUser mCurrentUser;
    private DatabaseReference sDatabase;
    private ProgressDialog progressDialog;
    private Toolbar mToolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_status);
        savechanges=findViewById(R.id.changestatussavechanges);
        statuschange=findViewById(R.id.changestatusstatus);
        progressDialog = new ProgressDialog(this);
       String previousstatus = getIntent().getStringExtra("previous_status");
       statuschange.setText(previousstatus);
        progressDialog.setTitle("Saving Changes");
        progressDialog.setMessage("Please Wait");

        // toolbar


        mToolbar=findViewById(R.id.registerpagetoolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Account Settings");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //firebase

        mCurrentUser= FirebaseAuth.getInstance().getCurrentUser();
        String uid = mCurrentUser.getUid();

        //adding value to data base

        sDatabase = FirebaseDatabase.getInstance().getReference().child("User").child(uid);
        savechanges.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        progressDialog.show();
                        String newstatus = statuschange.getText().toString();
                        sDatabase.child("status").setValue(newstatus).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful())
                                {
                                    progressDialog.hide();
                                }
                            }
                        });
                    }
                }
        );
    }
}
