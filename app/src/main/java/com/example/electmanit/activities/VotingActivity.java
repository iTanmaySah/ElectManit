package com.example.electmanit.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.electmanit.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class VotingActivity extends AppCompatActivity {

    private TextView name, post, branch, batch;
    private Button voteBtn;
    private FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voting);

        firebaseFirestore = FirebaseFirestore.getInstance();

        name = findViewById(R.id.name);
        post = findViewById(R.id.post);
        branch = findViewById(R.id.branch);
        voteBtn = findViewById(R.id.vote_btn);
        batch = findViewById(R.id.batch);

        String uid = FirebaseAuth.getInstance().getUid();

        String nm = getIntent().getStringExtra("name");
        String pos = getIntent().getStringExtra("post");
        String bran = getIntent().getStringExtra("branch");
        String btch = getIntent().getStringExtra("batch");
        String id = getIntent().getStringExtra("id");

        name.setText(nm);
        post.setText(pos);
        branch.setText(bran);
        batch.setText(btch);

        voteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String finish = "voted";
                Map<String, Object> userMap = new HashMap<>();
                userMap.put("finish", finish);
                userMap.put("device", getDeviceIP());
                userMap.put(pos, id);

                assert uid != null;
                firebaseFirestore.collection("Users").document(uid)
                        .update(userMap);

                Map<String, Object> candidateMap = new HashMap<>();
                candidateMap.put("deviceIp", getDeviceIP());
                candidateMap.put("candidatePost", pos);
                candidateMap.put("timestamp", FieldValue.serverTimestamp());

                firebaseFirestore.collection("Candidate/" + id + "/Vote")
                        .document(uid)
                        .set(candidateMap)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if(task.isSuccessful()) {
                                    startActivity(new Intent(VotingActivity.this, ResultActivity.class));
                                    finish();
                                }else{
                                    Toast.makeText(VotingActivity.this, "Voted Failed", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

            }
        });
    }


            private Object getDeviceIP() {
                try{
                    for(Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();){
                        NetworkInterface inf = en.nextElement();
                        for(Enumeration<InetAddress> enumIpAddr = inf.getInetAddresses(); enumIpAddr.hasMoreElements();){
                            InetAddress inetAddress = enumIpAddr.nextElement();
                            if(!inetAddress.isLoopbackAddress()){
                                return inetAddress.getHostAddress().toString();
                            }
                        }
                    }
                }catch (Exception e){
                    Toast.makeText(VotingActivity.this, ""+e, Toast.LENGTH_SHORT).show();
                }
                return null;
            }



    }
