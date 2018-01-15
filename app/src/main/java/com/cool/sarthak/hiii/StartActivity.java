package com.cool.sarthak.hiii;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class StartActivity extends AppCompatActivity {

    private Button mRegBtn,mloginbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        mRegBtn=findViewById(R.id.startregbutton);
        mloginbutton=findViewById(R.id.startlogin);
        mRegBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent =new Intent(StartActivity.this,RegisterActivity.class);
                        startActivity(intent);
                    }
                }
        );
        mloginbutton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent =new Intent(StartActivity.this,LoginActivity.class);
                        startActivity(intent);
                    }
                }
        );



    }

}
