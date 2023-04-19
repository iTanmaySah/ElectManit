package com.example.electmanit.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.electmanit.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    private EditText userPassword,userEmail; //variables that would come from xml file
    private Button loginbtn;  //Button variable to set an onclick listener
    private TextView forgetPassword;
    private FirebaseAuth mAuth;

    public static final String PREFERENCES = "prefKey";
    public static final String Name = "nameKey"; //these will be used with putstring(Name,name) later
    public static final String Email = "emailKey";
    public static final String Password = "passwordKey";
    public static final String ScholarId = "scholarIdKey";
    public static final String UploadData = "uploaddata";

    SharedPreferences sharedPreferences;

    StorageReference reference;
    FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);

        sharedPreferences = getApplicationContext().getSharedPreferences(PREFERENCES, MODE_PRIVATE);
        reference = FirebaseStorage.getInstance().getReference();
        firebaseFirestore = FirebaseFirestore.getInstance();

        findViewById(R.id.dont_have_acc).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,SignUpActivity.class));
            }
        });
        //Here, we capture the values that comes from xml file into our java file variables

        userPassword = findViewById(R.id.user_password);
        userEmail = findViewById(R.id.user_email);
        forgetPassword = findViewById(R.id.forget_password);
        loginbtn = findViewById(R.id.login_btn);
        //even taking value into button variable, textView to know the status
        mAuth = FirebaseAuth.getInstance();

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String password = userPassword.getText().toString().trim();
                String email = userEmail.getText().toString().trim();

                mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){
                            verifyEmail();
                        }else{
                            Toast.makeText(LoginActivity.this, "User/Password invalid", Toast.LENGTH_SHORT).show();
                        }
                    }

                    private void verifyEmail() {
                        FirebaseUser user = mAuth.getCurrentUser();
                        assert user != null;
                        if(user.isEmailVerified()) {
                            boolean bol = sharedPreferences.getBoolean(UploadData, false);

                            if (bol) {
                                //if email is verified and data is already uploaded then we do not need to upload it again
                                startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                                finish();
                            } else {

                                startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                String name = sharedPreferences.getString(Name, null);
                                String password = sharedPreferences.getString(Password, null);
                                String email = sharedPreferences.getString(Email, null);
                                String id = sharedPreferences.getString(ScholarId, null);

                                //first we sent email for verification
                                // now store data in shared preference if user verify email
                                // and login then we upload data to Firestore

                                if (name != null && password != null && email != null && id != null) {

                                    String uid = mAuth.getUid();
                                    Map<String, String> map = new HashMap<>();
                                    map.put("name", name);
                                    map.put("email", email);
                                    map.put("password", password);
                                    map.put("scholarId", id);
                                    map.put("uid", uid);
                                    firebaseFirestore.collection("Users").document(uid)
                                            .set(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {

                                                    if (task.isSuccessful()) {
                                                        sharedPreferences = getApplicationContext().getSharedPreferences(PREFERENCES,MODE_PRIVATE);
                                                        SharedPreferences.Editor pref = sharedPreferences.edit();
                                                        pref.putBoolean(UploadData,true);
                                                        pref.commit();

                                                        startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                                                        finish();
                                                    } else {

                                                        Toast.makeText(LoginActivity.this, "Data not stored", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });

                                } else {
                                  //  Toast.makeText(LoginActivity.this, "User data not found", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                else{
                            mAuth.signOut();
                            Toast.makeText(LoginActivity.this, "Verify your email", Toast.LENGTH_SHORT).show();
                        }
            }
        });

    }
});

        forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, ForgetPasswordActivity.class));
            }
        });


    }
}