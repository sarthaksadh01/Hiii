package com.cool.sarthak.hiii;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private Toolbar mToolbar;
    private String mcurrentUser;
    private DatabaseReference mUsersDatabase;

    private ViewPager viewPager;
    private SectioPageAdapter sectioPageAdapter;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         //firebase

        mAuth = FirebaseAuth.getInstance();

        //Tabs

        viewPager=findViewById(R.id.tabpager);
        sectioPageAdapter =new SectioPageAdapter(getSupportFragmentManager());
        viewPager.setAdapter(sectioPageAdapter);
        tabLayout =findViewById(R.id.maintabs);
        tabLayout.setupWithViewPager(viewPager);


        //toolbar

        mToolbar=findViewById(R.id.mainpagetoolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Ahoy");
        getSupportActionBar().setIcon(R.drawable.logo);
    }
    @Override
    public void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser==null)
        {
            Intent intent = new Intent(MainActivity.this,StartActivity.class);
            startActivity(intent);
            finish();
        }

if(currentUser!=null) {
    mcurrentUser = currentUser.getUid();
    mUsersDatabase = FirebaseDatabase.getInstance().getReference();
    mUsersDatabase.child("User").child(mcurrentUser).child("online").setValue("true");
}



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
         super.onCreateOptionsMenu(menu);
         getMenuInflater().inflate(R.menu.main_menu,menu);



        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        mUsersDatabase.child("User").child(mcurrentUser).child("online").setValue("false");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
         super.onOptionsItemSelected(item);
         if(item.getItemId()==R.id.logoutbutton)
         {
             mUsersDatabase.child("User").child(mcurrentUser).child("online").setValue("false").addOnCompleteListener(
                     new OnCompleteListener<Void>() {
                         @Override
                         public void onComplete(@NonNull Task<Void> task) {
                             if(task.isSuccessful())
                             {
                                 FirebaseAuth.getInstance().signOut();
                                 Intent intent = new Intent(MainActivity.this,StartActivity.class);
                                 startActivity(intent);
                                 finish();
                             }
                         }
                     }
             );

         }

         if(item.getItemId()==R.id.actsettingbutton)
         {
             Intent intent =new Intent(MainActivity.this,AccountSetting.class);
             startActivity(intent);

         }

        if(item.getItemId()==R.id.alluserbutton) {

            Intent intent =new Intent(MainActivity.this,UsersActivity.class);
            startActivity(intent);


        }

//        if(item.getItemId()==R.id.VS)
//        {
//            Intent intent =new Intent(MainActivity.this,Jarvis.class);
//            startActivity(intent);
//        }











        return true;
    }
}
