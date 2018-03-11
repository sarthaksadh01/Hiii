package com.cool.sarthak.hiii;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    private EditText lEmail,lPassword;
    private Button llogin;
    private ProgressDialog loginprogress;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();

        //tool bar

        mToolbar=findViewById(R.id.loginpagetoolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Login");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //progresbar

        loginprogress= new ProgressDialog(this);

        lEmail=findViewById(R.id.loginemail);
        lPassword=findViewById(R.id.loginpassword);
        llogin=findViewById(R.id.loginLoginbutton);
        llogin.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String l_email=lEmail.getText().toString();
                        String l_password=lPassword.getText().toString();
                        if(l_email.equals("")||l_password.equals(""))
                        {
                            Toast.makeText(LoginActivity.this,"all fields are must",Toast.LENGTH_SHORT).show();
                        }
                        else {
                            loginuser(l_email, l_password);

                            //progress initialising


                            loginprogress.setTitle("loging user..");
                            loginprogress.setMessage("Please Wait While We Verify Your Data .");
                            loginprogress.setCanceledOnTouchOutside(false);
                            loginprogress.show();
                        }
                        
                    }
                }
        );
    }

    private void loginuser(String l_email, String l_password) {

        mAuth.signInWithEmailAndPassword(l_email, l_password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();

                            FirebaseUser user = mAuth.getCurrentUser();

                        } else {

                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            loginprogress.hide();
                            //updateUI(null);
                        }

                        // ...
                    }
                });
    }


     public void forgotPassword(View view)
     {
         Intent intent = new Intent(this,ForgetPassword.class);
         startActivity(intent);
     }
}
