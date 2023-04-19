package com.example.electmanit.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.electmanit.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUpActivity extends AppCompatActivity {

    private EditText userName, userPassword,userEmail, userScholarID; //variables that would come from xml file
    private Button signUpbtn;  //Button variable to set an onclick listener
    private FirebaseAuth mAuth; //Firebase variable'
    public static final String PREFERENCES = "prefKey";
    public static final String Name = "nameKey"; //these will be used with putstring(Name,name) later
    public static final String Email = "emailKey";
    public static final String Password = "passwordKey";
    public static final String ScholarId = "scholarIdKey";

    SharedPreferences sharedPreferences;
    String name, password, email, scholarid; //global variables used by shared preferences and other functions like onClick

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mAuth = FirebaseAuth.getInstance();
        //shared preferences
        sharedPreferences = getApplicationContext().getSharedPreferences(PREFERENCES, MODE_PRIVATE);

        //To go back to login activity when clicked on "already have an account textView
        findViewById(R.id.already_have_acc).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        //Here, we capture the values that comes from xml file into our java file variables
        userName = findViewById(R.id.user_name);
        userPassword = findViewById(R.id.user_password);
        userEmail = findViewById(R.id.user_email);
        userScholarID = findViewById(R.id.scholarid);
        signUpbtn = findViewById(R.id.signup_btn);  //even taking value into button variable to know the status

        //working on signup button, notice we used java variable here
        signUpbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //on click listener uses it's own variables and their values are given by java file variables
                //later we made these global so that shared preferences can also use them
                name = userName.getText().toString().trim();
                password = userPassword.getText().toString().trim();
                email = userEmail.getText().toString().trim();
                scholarid = userScholarID.getText().toString().trim();

                //checking if any null value or error in credentials entered by the user
                if(!TextUtils.isEmpty(name) && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(scholarid) && Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    createUser(email,password); //self made function which will invoke a firebase function
                }else{
                    Toast.makeText(SignUpActivity.this, "Enter all credentials", Toast.LENGTH_SHORT).show(); //that is when any credential is missing
                }
            }
        });

    }

    private void createUser(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() { //onComplete listener means after function is called and gets output
            //You create a new user in your Firebase project by calling the createUserWithEmailAndPassword method
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){ //task comes from parameter and has task report
                    Toast.makeText(SignUpActivity.this, "Account Created", Toast.LENGTH_SHORT).show();
                    verifyEmail(); //self made function which will invoke firebase function
                }else{
                    Toast.makeText(SignUpActivity.this, "Failure, account not created", Toast.LENGTH_SHORT).show();
                }
            }

            private void verifyEmail() {
                FirebaseUser user = mAuth.getCurrentUser(); //gives details of current user,
                //The recommended way to get the current user is by calling the getCurrentUser method. If no user is signed in, getCurrentUser returns null:

                if(user != null){
                    //the user is retrieved from mAuth.getCurrentUser(), and the sendEmailVerification() method is called on it.
                    //This gets Firebase to send the mail - and if the task was successful, you’ll see that a toast will pop up showing that it happened.
                    //sendEmailVerification() method, which returns as Task used to asynchronously send the email, and report on the status.

                    user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                //On success, the user's information is saved in the SharedPreferences using the SharedPreferences.Editor class.
                                //shared preferences should be used if successfully signed in or successfully verified email
                                SharedPreferences.Editor pref = sharedPreferences.edit(); //You can save something in the sharedpreferences by using SharedPreferences.Editor class.
                                //// You will call the edit method of SharedPreference instance and will receive it in an editor object.
                                pref.putString(Name,name);
                                pref.putString(Password,password);
                                pref.putString(Email,email);
                                pref.putString(ScholarId,scholarid);
                                pref.commit();

                                //email sent
                                Toast.makeText(SignUpActivity.this, "Email Sent", Toast.LENGTH_SHORT).show();
                                FirebaseAuth.getInstance().signOut();
                                startActivity(new Intent(SignUpActivity.this,LoginActivity.class));
                                finish();
                            }else{
                                //If the task fails, the mAuth.signOut() method is called to sign out the user, and the finish() method is called to close the activity.
                                Toast.makeText(SignUpActivity.this, "Failed, email not sent", Toast.LENGTH_SHORT).show();
                                mAuth.signOut();
                                finish();
                            }
                        }
                    });
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(SignUpActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }
}