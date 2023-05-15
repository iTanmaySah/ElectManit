package com.example.electmanit.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.electmanit.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class HomeActivity extends AppCompatActivity {

    public static final String PREFERENCES = "prefKey";
    SharedPreferences sharedPreferences;
    public static final String IsLogIn = "islogin";

    private TextView nameTxt, scholarIdTxt;
    private String uid;
    private FirebaseFirestore firebaseFirestore;
    private Button createBtn, resultBtn, viewBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        firebaseFirestore = FirebaseFirestore.getInstance()
;
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        nameTxt = findViewById(R.id.name);
        scholarIdTxt = findViewById(R.id.scholar_id);
        createBtn = findViewById(R.id.create_candidate_btn);
        resultBtn = findViewById(R.id.result_btn);
        viewBtn = findViewById(R.id.view_candidates_btn);


        sharedPreferences = getApplicationContext().getSharedPreferences(PREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor pref = sharedPreferences.edit();
        pref.putBoolean(IsLogIn, true);
        pref.commit();
        //user do not need to login again if he has already logged in

//        findViewById(R.id.logout_btn).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                FirebaseAuth.getInstance().signOut();
//                pref.putBoolean(IsLogIn, false);
//                pref.commit();
//                startActivity(new Intent(HomeActivity.this,LoginActivity.class));
//                finish();
//            }
//        });


        firebaseFirestore.collection("Users").document(uid).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                        if(task.isSuccessful()){

                            String name = task.getResult().getString("name");
                            String scholarId = task.getResult().getString("scholarId");

                            //assert name != null;

                            if(name.equals("admin")){
                                createBtn.setVisibility(View.VISIBLE);


                            }else{
                                createBtn.setVisibility(View.GONE);

                            }

                            nameTxt.setText(name);
                            scholarIdTxt.setText(scholarId);

                        }else{
                            Toast.makeText(HomeActivity.this, "User not found", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this,Create_Candidate_Activity.class));
            }
        });

        viewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this,AllCandidateActivity.class));

            }
        });

        resultBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, ResultActivity.class));
            }
        });




    }//end of oncreate function

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        SharedPreferences.Editor pref = sharedPreferences.edit();
        switch(id){
            case R.id.show_result:
                 startActivity(new Intent(HomeActivity.this, ResultActivity.class));
                 return true;

             case R.id.log_out:
                 FirebaseAuth.getInstance().signOut();
                 pref.putBoolean(IsLogIn, false);
                 pref.commit();
                 startActivity(new Intent(HomeActivity.this,LoginActivity.class));
                 finish();
                 return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }
}