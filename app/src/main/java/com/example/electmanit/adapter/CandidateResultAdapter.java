package com.example.electmanit.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.electmanit.R;
import com.example.electmanit.activities.VotingActivity;
import com.example.electmanit.model.Candidate;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class CandidateResultAdapter extends RecyclerView.Adapter<CandidateResultAdapter.ViewHolder>{

    private Context context;
    private List<Candidate> list;
    private FirebaseFirestore firebaseFirestore;

    public CandidateResultAdapter(Context context, List<Candidate> list) {
        this.context = context;
        this.list = list;
        firebaseFirestore = FirebaseFirestore.getInstance();
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.candidate_result_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.name.setText(list.get(position).getName());
        holder.branch.setText(list.get(position).getBranch());
        holder.post.setText(list.get(position).getPosition());

        firebaseFirestore.collection("Candidate/" + list.get(position).getId() + "/Vote")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot documentSnapshots, @Nullable FirebaseFirestoreException error) {

                        if(!documentSnapshots.isEmpty()){
                            int count = documentSnapshots.size();
                            Candidate candidate = list.get(position);
                            candidate.setCount(count);
                            list.set(position,candidate);

                            notifyDataSetChanged();
                        }
                    }
                });

                holder.result.setText("Votes: "+list.get(position).getCount());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView name, post, branch, result;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            post = itemView.findViewById(R.id.post);
            branch = itemView.findViewById(R.id.branch);
            result = itemView.findViewById(R.id.candidate_result);


        }
    }

}

