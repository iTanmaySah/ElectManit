package com.example.electmanit.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.electmanit.R;
import com.example.electmanit.adapter.CandidateAdapter;
import com.example.electmanit.model.Candidate;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class AllCandidateActivity extends AppCompatActivity {

    private RecyclerView candidateRV;
    private Button castVoteBtn;
    private List<Candidate> list;
    private CandidateAdapter adapter;

    private FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_candidate);

        candidateRV = findViewById(R.id.candidates_rv);
        //startBtn = findViewById(R.id.start);
        firebaseFirestore = FirebaseFirestore.getInstance();

        list = new ArrayList<>();
        adapter = new CandidateAdapter(this,list);
        candidateRV.setLayoutManager(new LinearLayoutManager(this));
        candidateRV.setAdapter(adapter);



        if(FirebaseAuth.getInstance().getCurrentUser() != null){

            firebaseFirestore.collection("Candidate")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful()){
                                for(DocumentSnapshot snapshot : task.getResult()){

                                    list.add(new Candidate(
                                            snapshot.getString("name"),
                                            snapshot.getString("branch"),
                                            snapshot.getString("post"),
                                            snapshot.getId() //this will give back document id
                                    ));
                                }

                                adapter.notifyDataSetChanged();
                            }else{
                                Toast.makeText(AllCandidateActivity.this, "Candidate not found error", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }

    }

    @Override
    protected void onStart() {
        super.onStart();

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseFirestore.getInstance().collection("Users")
                .document(uid)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                        String finish = task.getResult().getString("finish");

//                        assert finish != null;
                        if(finish != null) {
                            if (finish.equals("voted")) {
                                Toast.makeText(AllCandidateActivity.this, "Your vote is already counted, You can view results", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(AllCandidateActivity.this, ResultActivity.class));
                                finish();
                            }
                        }
                    }
                });
    }
}