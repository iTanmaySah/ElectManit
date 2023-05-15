package com.example.electmanit.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.electmanit.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

public class Create_Candidate_Activity extends AppCompatActivity {

    private EditText candidateName, candidateBranch, candidateBatch;
    private Spinner candidateSpinner;
    private Button submitBtn;

    private String[] candPost = {"CR", "TPO", "TPR"};
    StorageReference reference;
    FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_candidate);

        reference = FirebaseStorage.getInstance().getReference();
        firebaseFirestore = FirebaseFirestore.getInstance();

        candidateName = findViewById(R.id.candidate_name);
        candidateBranch = findViewById(R.id.candidate_branch);
        candidateSpinner = findViewById(R.id.candidate_spinner);
        candidateBatch = findViewById(R.id.candidate_batch);
        submitBtn = findViewById(R.id.candidate_submit_btn);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line,candPost);
        candidateSpinner.setAdapter(adapter);

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String name = candidateName.getText().toString().trim();
                String branch = candidateBranch.getText().toString().trim();
                String post = candidateSpinner.getSelectedItem().toString();
                String batch = candidateBatch.getText().toString().trim();


                if(!TextUtils.isEmpty(name) && !TextUtils.isEmpty(branch) && !TextUtils.isEmpty(post)){

                    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    Map<String, Object> map = new HashMap<>();
                    map.put("name", name);
                    map.put("branch", branch);
                    map.put("post", post);
                    map.put("batch",batch);
                    map.put("timestamp", FieldValue.serverTimestamp());

                    firebaseFirestore.collection("Candidate").add(map).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            if (task.isSuccessful()) {

                                Toast.makeText(Create_Candidate_Activity.this, "Candidate Information Saved", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(Create_Candidate_Activity.this, HomeActivity.class));
                                finish();
                            } else {

                                Toast.makeText(Create_Candidate_Activity.this, "Data not stored", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }else{
                    Toast.makeText(Create_Candidate_Activity.this, "Enter Complete Details", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}