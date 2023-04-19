package com.example.electmanit.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.electmanit.R;
import com.example.electmanit.activities.VotingActivity;
import com.example.electmanit.model.Candidate;

import java.util.List;

public class CandidateAdapter extends RecyclerView.Adapter<CandidateAdapter.ViewHolder>{

    private Context context;

    public CandidateAdapter(Context context, List<Candidate> list) {
        this.context = context;
        this.list = list;
    }

    private List<Candidate> list;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.candidate_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.name.setText(list.get(position).getName());
        holder.branch.setText(list.get(position).getBranch());
        holder.post.setText(list.get(position).getPosition());

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, VotingActivity.class);
                intent.putExtra("name",list.get(position).getName());
                intent.putExtra("branch",list.get(position).getBranch());
                intent.putExtra("post",list.get(position).getPosition());
                intent.putExtra("id",list.get(position).getId());
                context.startActivity(intent);
//                Activity activity = (Activity) context;
//                activity.finish();

            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView name, post, branch;
        private Button voteBtn;
        private CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            post = itemView.findViewById(R.id.post);
            branch = itemView.findViewById(R.id.branch);
            voteBtn = itemView.findViewById(R.id.vote_btn);
            cardView = itemView.findViewById(R.id.cardview);


        }
    }

}

