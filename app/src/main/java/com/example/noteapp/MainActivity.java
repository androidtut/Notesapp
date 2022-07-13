package com.example.noteapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.SearchView;
import android.widget.Toast;

import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.noteapp.Adapters.BookAdapters;
import com.example.noteapp.databinding.ActivityMainBinding;
import com.example.noteapp.models.BookModels;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private  ArrayList<BookModels>list;
    private ProgressDialog progressDialog;
    private ActionBarDrawerToggle toggle;
    private ActivityMainBinding binding;
    private BookAdapters adapters;
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("books");
//        getSupportActionBar().hide();
//        setSupportActionBar(binding.toolbar);
//        binding.toolbar.setTitle("Note App");

//        slider image
        List<SlideModel> imagelist = new ArrayList<>();
        imagelist.add(new SlideModel(R.drawable.laptop1,null));
        imagelist.add(new SlideModel(R.drawable.laptop2,null));
        imagelist.add(new SlideModel(R.drawable.laptop3,null));
        binding.imageSlider.setImageList(imagelist, ScaleTypes.FIT);


//        recyclerview search bar
        binding.edittext.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                FilterList(s);
                return false;
            }
        });


        toggle = new ActionBarDrawerToggle(this,binding.drawer,binding.toolbar,R.string.start,R.string.close);
        binding.drawer.addDrawerListener(toggle);
        toggle.syncState();

       //logout user
        binding.Logout.setOnClickListener(v->{
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(MainActivity.this,Signup.class));
        });

        binding.navigation.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                binding.drawer.closeDrawer(GravityCompat.START);
                switch(item.getItemId()){
                    case R.id.share:
                        shareapp();
                        break;
                    case R.id.video:
                        openwebsite("https://www.youtube.com/channel/UCF5oO43y67XYOfJRiPif1rw/videos");
                        break;
                    case R.id.website:
//                        openwebsite("https://coderkharal.blogspot.com/");
                        startActivity(new Intent(MainActivity.this,ShowWebsite.class));
                        break;
                    case R.id.developer:
                        Intent intent = new Intent(MainActivity.this,DeveloperActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.exit:
                        finish();
                        break;
                }
                return false;
            }
        });


        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Wait While Loading...");
        progressDialog.show();

        list = new ArrayList<>();

        adapters = new BookAdapters(list,getApplicationContext());
        binding.BookRecyclerview.setAdapter(adapters);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                progressDialog.dismiss();
                for (DataSnapshot snapshot1:snapshot.getChildren()){
                    BookModels models = snapshot1.getValue(BookModels.class);
                    list.add(models);
                }

                adapters.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this,error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        binding.BookRecyclerview.setLayoutManager(staggeredGridLayoutManager);

//        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,2);
//        binding.BookRecyclerview.setLayoutManager(gridLayoutManager);

    }

    @Override
    public void onBackPressed() {
        if(binding.drawer.isDrawerOpen(GravityCompat.START)){
            binding.drawer.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }
    }

    private void openwebsite(String url){
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }

    private void FilterList(String s) {
        ArrayList<BookModels> filterList = new ArrayList<>();
        for(BookModels item:list){
            if(item.getTitle().toLowerCase().contains(s.toLowerCase())){
                filterList.add(item);
            }
        }
        if(filterList.isEmpty()){
            Toast.makeText(this, "No data found", Toast.LENGTH_SHORT).show();
        }else{
            adapters.setFilterList(filterList);
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.share:
                shareapp();
                break;
            case R.id.exit:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void shareapp(){
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT,"our Note app you can learn different course from these app");
        intent.setType("text/plain");
        startActivity(intent);
    }

    private void showalert(String title,String subtitle){
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(subtitle)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).show();
    }


}