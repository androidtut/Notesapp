package com.example.noteapp;

import static es.voghdev.pdfviewpager.library.subscaleview.SubsamplingScaleImageViewConstants.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.example.noteapp.databinding.ActivityDeveloperBinding;


public class DeveloperActivity extends AppCompatActivity {
ActivityDeveloperBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDeveloperBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().setTitle("Developer Details");

        binding.email.setOnClickListener(v->{
            sharedata();
        });

        binding.sendemail.setOnClickListener(v->{
            sharedata();
        });

        binding.Dial.setOnClickListener(v->{
            Intent intent = new Intent(Intent.ACTION_DIAL);
            startActivity(intent);
        });

        binding.channel.setOnClickListener(v->{
            sharewebsite("https://www.youtube.com/channel/UCF5oO43y67XYOfJRiPif1rw/videos");
        });

        binding.website.setOnClickListener(v->{
            sharewebsite("http://coderkharal.blogspot.com/");
        });


        binding.facebook.setOnClickListener(v->{
            sharewebsite("https://www.facebook.com/");
           });

    }

    private void sharedata() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(intent.EXTRA_TEXT,"Hello this is our Note app you can learn programming from this app");
        intent.setType("text/plain");
        startActivity(intent);
    }

    private void sharewebsite(String url){
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }


}