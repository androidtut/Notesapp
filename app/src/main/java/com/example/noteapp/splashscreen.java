package com.example.noteapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

public class splashscreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);

        Thread thread = new Thread(){
            @Override
            public void run() {
                try{
                 sleep(5 * 1000);
                    Intent intent = new Intent(splashscreen.this,Signup.class);
                    startActivity(intent);
                    finish();
                }catch(Exception e){
                    Toast.makeText(splashscreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                super.run();
            }
        };
        thread.start();


    }
}