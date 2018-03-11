package com.cool.sarthak.hiii;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgetPassword extends AppCompatActivity {
    FirebaseAuth auth;
    EditText email;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

         auth = FirebaseAuth.getInstance();
         email=findViewById(R.id.forgetpassemail);
         textView=findViewById(R.id.forgetpasswordtext);





    }

    public void resetpassword(View view)

    {

        String emailAddress = email.getText().toString();
        auth.sendPasswordResetEmail(emailAddress)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            //Log.d(TAG, "Email sent.");
                            textView.setText("A Reset Link is sent to your email address");
                            email.setText("");
                        }
                        else Toast.makeText(ForgetPassword.this,"no such id registered",Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
