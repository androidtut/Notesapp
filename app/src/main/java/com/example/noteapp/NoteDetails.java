package com.example.noteapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;

import com.example.noteapp.databinding.ActivityNoteDetailsBinding;
import com.github.barteksc.pdfviewer.PDFView;

import es.voghdev.pdfviewpager.library.RemotePDFViewPager;
import es.voghdev.pdfviewpager.library.adapter.PDFPagerAdapter;
import es.voghdev.pdfviewpager.library.remote.DownloadFile;

public class NoteDetails extends AppCompatActivity implements DownloadFile.Listener{
    PDFView pdfView;
    TextView text;
    private ActivityNoteDetailsBinding binding;
    private RemotePDFViewPager remotePDFViewPager;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNoteDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Wait While Loading");
        progressDialog.show();

        String toolbartitle = getIntent().getStringExtra("toolbartitle");
       getSupportActionBar().setTitle(toolbartitle);

        String url = getIntent().getStringExtra("pdfurl");

        remotePDFViewPager =
                new RemotePDFViewPager(NoteDetails.this, url, this);

    }


    @Override
    public void onSuccess(String url, String destinationPath) {
        progressDialog.dismiss();
        PDFPagerAdapter adapter = new PDFPagerAdapter(this, extractFileNameFromURL(url));
        remotePDFViewPager.setAdapter(adapter);
        setContentView(remotePDFViewPager);
    }

    public static String extractFileNameFromURL(String url){
      return url.substring(url.lastIndexOf('/')+1);
    }

    @Override
    public void onFailure(Exception e) {

    }

    @Override
    public void onProgressUpdate(int progress, int total) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}