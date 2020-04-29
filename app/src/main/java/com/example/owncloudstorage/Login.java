package com.example.owncloudstorage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {
    TextView tvview;
    Button btnLogin;
    EditText Lname,Lpass;
    FirebaseAuth mFirebaseAuth;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        tvview = findViewById(R.id.tvview);
        btnLogin = findViewById(R.id.btnLogin);
        Lname = findViewById(R.id.Lname);
        Lpass = findViewById(R.id.Lpass);
        mFirebaseAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progressBar2);

        Intent i = getIntent();
        String view_text = i.getStringExtra("view_text");
        tvview.setText(view_text);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = Lname.getText().toString().trim();
                String pass = Lpass.getText().toString().trim();



                if(email.isEmpty()){
                    Lname.setError("Please enter your email_ID");
                }
                if(pass.isEmpty()){
                    Lpass.setError("Please enter your Password");
                }
                if(email.isEmpty() && pass.isEmpty()){
                    Toast.makeText(getApplicationContext(),"Please Enter your details",Toast.LENGTH_SHORT).show();
                }
                if(!email.isEmpty() && !pass.isEmpty()){
                    progressBar.setVisibility(View.VISIBLE);
                    mFirebaseAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                Intent i = new Intent(Login.this,Welcome.class);
                                startActivity(i);
                                Toast.makeText(getApplicationContext(),"Logged-IN succesfully",Toast.LENGTH_SHORT).show();

                            }
                            else {
                                Toast.makeText(getApplicationContext(),"Please check your Email and password\nTry Again",Toast.LENGTH_LONG).show();
                                progressBar.setVisibility(View.INVISIBLE);
                            }
                        }
                    });
                }
            }
        });
    }
}
