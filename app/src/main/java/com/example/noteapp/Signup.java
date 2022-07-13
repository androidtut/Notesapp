package com.example.noteapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.noteapp.databinding.ActivitySignupBinding;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthCredential;

public class Signup extends AppCompatActivity {
    int RC_SIGN_IN = 10;
    ActivitySignupBinding binding;
  String Email, Password;
  FirebaseAuth mAuth;
  GoogleSignInClient mGoogleSignInClient;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.server_client_id))
                .requestEmail()
                .build();


        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        mAuth = FirebaseAuth.getInstance();

        binding.signInButton.setSize(SignInButton.SIZE_STANDARD);

        binding.signInButton.setOnClickListener(v->{
            signIn();
        });


        binding.signins.setOnClickListener(v->{
            startActivity(new Intent(Signup.this,LoginActivity.class));
        });

        binding.signup.setOnClickListener(v->{
            Email = binding.email.getText().toString();
            Password = binding.password.getText().toString();
            if(Email.isEmpty()){
                Toast.makeText(this, "Email Can't be Blank", Toast.LENGTH_SHORT).show();
            }else if(Password.isEmpty()){
                Toast.makeText(this, "Password Can't be Blank", Toast.LENGTH_SHORT).show();
            }else{
                mAuth.createUserWithEmailAndPassword(Email, Password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    sendEmailVerification();
                                }else{
                                    Toast.makeText(Signup.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

        if(mAuth.getCurrentUser() != null){
            startActivity(new Intent(Signup.this,MainActivity.class));
        }

    }


    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Toast.makeText(this,account.getEmail(), Toast.LENGTH_SHORT).show();
                // Signed in successfully, show authenticated UI.
                firebaseAuthwithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // The ApiException status code indicates the detailed failure reason.
                // Please refer to the GoogleSignInStatusCodes class reference for more information.
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseAuthwithGoogle(String idToken) {
        Toast.makeText(this, idToken, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(Signup.this,MainActivity.class);
        startActivity(intent);
    }

    private void sendEmailVerification(){
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if(user != null) {
            user.sendEmailVerification()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(Signup.this, "send verification to your email", Toast.LENGTH_SHORT).show();
                            Toast.makeText(Signup.this, "Successfully Register", Toast.LENGTH_SHORT).show();
                            mAuth.signOut();
                            finish();
                            startActivity(new Intent(Signup.this,LoginActivity.class));
                        }
                    });
        }

    }

}