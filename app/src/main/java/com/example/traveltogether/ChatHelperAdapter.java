package com.example.traveltogether;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ChatHelperAdapter extends RecyclerView.Adapter {
    Context context;
    TextView textView;
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
    public class ViewHolderClass extends RecyclerView.ViewHolder{

        public ViewHolderClass(@NonNull View itemView) {
            super(itemView);
        }
    }
}
