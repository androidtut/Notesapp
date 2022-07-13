package com.example.noteapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.noteapp.NoteDetails;
import com.example.noteapp.R;
import com.example.noteapp.models.BookModels;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class BookAdapters extends RecyclerView.Adapter<BookAdapters.ViewHolder>{
    ArrayList<BookModels>list;
    Context context;

    public BookAdapters(ArrayList<BookModels> list, Context context) {
        this.list = list;
        this.context = context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sample_note,parent,false);
        return new ViewHolder(view);
    }

    public void setFilterList(ArrayList<BookModels> FilterList){
        list = FilterList;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
     final BookModels models = list.get(position);
     holder.text.setText(models.getTitle());
//     holder.image.setImageResource(models.getImage());
     Picasso.get().load(models.getImage()).into(holder.image);

     holder.itemView.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
//             Toast.makeText(context, "click image", Toast.LENGTH_SHORT).show();
             Intent intent = new Intent(context, NoteDetails.class);
             intent.putExtra("pdfurl",models.getUrl());
             intent.putExtra("toolbartitle",models.getTitle());
             intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
             context.startActivity(intent);
         }
     });
        setAnimation(holder.itemView);
    }

    public void setAnimation(View v){
        Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.fade_out);
        v.setAnimation(animation);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView image;
        TextView text;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.coursetitle);
            image = itemView.findViewById(R.id.courseimg);
        }
    }
}
