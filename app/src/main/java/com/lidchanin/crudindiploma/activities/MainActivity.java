package com.lidchanin.crudindiploma.activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.lidchanin.crudindiploma.Constants;
import com.lidchanin.crudindiploma.R;
import com.lidchanin.crudindiploma.utils.DownloadTask;
import com.lidchanin.crudindiploma.utils.ThemeManager;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.io.File;


public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener{
    private static final int RC_SIGN_IN = 9001;
    private Button buttonRus;
    private Button buttonEng;
    private ProgressBar progressEng;
    private ProgressBar progressRus;
    private Button buttonGoToCam;
    private GoogleApiClient googleApiClient;
    private SignInButton signInButton;
    private FirebaseAuth mAuth;
    private Uri photoUrl;
    private String accountName;
    private ImageView userPhoto;
    private TextView userName;
    private RelativeLayout googleBackground;
    private FirebaseUser currentUser ;
    private Transformation transformation;
    private static final int REQUEST_CAMERA_PERMISSION = 200;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        new ThemeManager(this);
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_main);
        transformation = new RoundedTransformationBuilder().oval(true).build();
        userPhoto = (ImageView) findViewById(R.id.mail_photo);
        userName = (TextView) findViewById(R.id.mail_name);
        buttonRus = (Button) findViewById(R.id.rus_tessdata);
        buttonEng = (Button) findViewById(R.id.eng_tessdata);
        progressEng = (ProgressBar) findViewById(R.id.eng_tessdata_progress);
        progressRus = (ProgressBar) findViewById(R.id.rus_tessdata_progress);
        buttonGoToCam = (Button) findViewById(R.id.to_camera);
        signInButton = (SignInButton) findViewById(R.id.sing_in_button);
        googleBackground = (RelativeLayout) findViewById(R.id.google_background);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
        progressRus.setVisibility(View.GONE);
        progressEng.setVisibility(View.GONE);
        mAuth = FirebaseAuth.getInstance();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso).build();

        final File rusTessaract = new File(String.valueOf(Environment.getExternalStorageDirectory()) + Constants.Tessaract.SLASH +Constants.Tessaract.TESSDATA+Constants.Tessaract.SLASH+ Constants.Tessaract.RUSTRAIN);
        final File engTessaract = new File(String.valueOf(Environment.getExternalStorageDirectory()) + Constants.Tessaract.SLASH +Constants.Tessaract.TESSDATA+Constants.Tessaract.SLASH+ Constants.Tessaract.ENGTRAIN);

            buttonEng.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (engTessaract.exists()) {
                        Toast.makeText(MainActivity.this, "It's also exists!", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        new DownloadTask(MainActivity.this, progressEng, Constants.Tessaract.ENGTRAIN).execute("https://firebasestorage.googleapis.com/v0/b/testdb-5f32a.appspot.com/o/tessaract%2Feng.traineddata?alt=media&token=58c2aa2d-417f-4d22-87eb-80627577feb8");
                    }
                }
            });

        buttonRus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rusTessaract.exists()) {
                    Toast.makeText(MainActivity.this, "It's also exists!", Toast.LENGTH_SHORT).show();
                }
                else{
                    new DownloadTask(MainActivity.this, progressRus, Constants.Tessaract.RUSTRAIN).execute("https://firebasestorage.googleapis.com/v0/b/testdb-5f32a.appspot.com/o/tessaract%2Frus.traineddata?alt=media&token=9cf09afa-e1bd-4f2c-b0dd-3bc457d2f5f0");
                }
            }
        });
                if ((buttonEng.getVisibility() == View.GONE) && (buttonRus.getVisibility() == View.GONE)) {
                    buttonGoToCam.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                startActivity(new Intent(MainActivity.this, MainScreenActivity.class));
                        }
        });}
        else {
        buttonGoToCam.setOnClickListener(new View.OnClickListener() {
                                             @Override
                                             public void onClick(View v) {
                                                if(engTessaract.exists()||rusTessaract.exists()) {
                                                     startActivity(new Intent(MainActivity.this, MainScreenActivity.class));
                                                 }
                                                 else {
                                                     Toast.makeText(MainActivity.this, "Choose Recognize language!", Toast.LENGTH_SHORT).show();
                                                 }
                                             }
        });
                }
                googleBackground.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        signOut();
                        googleBackground.setVisibility(View.GONE);
                        userName.setVisibility(View.GONE);
                        userPhoto.setVisibility(View.GONE);
                        Log.d("click", "onClick: ");
                    }
                });


    }
    @Override
    public void onRequestPermissionsResult(final int requestCode, @NonNull final String[] permissions, @NonNull final int[] grantResults) {
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                Toast.makeText(this, "Sorry!!!, you can't use this app without granting permission", Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    private void signOut() {
        Auth.GoogleSignInApi.signOut(googleApiClient);
        mAuth.signOut();
        googleBackground.setVisibility(View.GONE);
        userName.setVisibility(View.GONE);
        userPhoto.setVisibility(View.GONE);
        signInButton.setVisibility(View.VISIBLE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
                assert account != null;
                photoUrl=account.getPhotoUrl();
                accountName = account.getDisplayName();
                userName.setText(accountName);
                Picasso.with(getApplicationContext()).load(photoUrl).transform(transformation).into(userPhoto);
                signInButton.setVisibility(View.GONE);
                googleBackground.setVisibility(View.VISIBLE);
                userName.setVisibility(View.VISIBLE);
                userPhoto.setVisibility(View.VISIBLE);

            } else {
                googleBackground.setVisibility(View.GONE);
                userName.setVisibility(View.GONE);
                userPhoto.setVisibility(View.GONE);
                signInButton.setVisibility(View.VISIBLE);
                Toast.makeText(getApplicationContext(),"Что-то пошло не так ;)", Toast.LENGTH_SHORT).show();
            }
        }
    }
    @Override
    public void onStart() {
        super.onStart();
        currentUser = mAuth.getCurrentUser();
        if(currentUser!=null){
            assert currentUser != null;
            photoUrl=currentUser.getPhotoUrl();
            accountName = currentUser.getDisplayName();
            userName.setText(accountName);
            Picasso.with(getApplicationContext()).load(photoUrl).transform(transformation).into(userPhoto);
            signInButton.setVisibility(View.GONE);
        }
        else {
            googleBackground.setVisibility(View.GONE);
            userName.setVisibility(View.GONE);
            userPhoto.setVisibility(View.GONE);
            signInButton.setVisibility(View.VISIBLE);
        }
    }
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                        } else {
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}