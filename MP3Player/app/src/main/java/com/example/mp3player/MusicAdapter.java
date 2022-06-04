package com.example.mp3player;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.MusicViewHolder> {

    ArrayList<String> audioList;
    Context mContext;

    public MusicAdapter(ArrayList<String> audioList, Context mContext) {
        this.audioList = audioList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public MusicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.music_card, parent, false);
        return new MusicViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MusicViewHolder holder, int position) {

        String filePath = audioList.get(position);
        Log.e("File Path : ", filePath);
        String title = filePath.substring(filePath.lastIndexOf("/")+1);
        holder.fileNameTextview.setText(title);
        holder.musicCardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, MusicActivity.class);
                intent.putExtra("audioList", audioList);
                intent.putExtra("filePath", filePath);
                intent.putExtra("title", title);
                intent.putExtra("position", position);
                mContext.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return audioList.size();
    }

    public class MusicViewHolder extends RecyclerView.ViewHolder {

        TextView fileNameTextview;
        CardView musicCardview;

        public MusicViewHolder(@NonNull View itemView) {
            super(itemView);
            fileNameTextview = itemView.findViewById(R.id.textview_filename);
            musicCardview = itemView.findViewById(R.id.music_cardview);
        }
    }
}
