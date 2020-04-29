package com.example.owncloudstorage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import static java.lang.System.in;

public class NewUser extends AppCompatActivity {

    public static final String TAG = "TAG";
    FirebaseAuth  mFirebaseAuth;
    EditText name1,pass1,num1,email1;
    TextView tvview;
    Button btnSub;
    ProgressBar progressBar;
    FirebaseFirestore fstore;
    String user_ID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);

        mFirebaseAuth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();
        name1 = findViewById(R.id.name);
        pass1 = findViewById(R.id.pass);
        num1 = findViewById(R.id.num);
        email1 = findViewById(R.id.email);
        btnSub = findViewById(R.id.btnSub);
        tvview = findViewById(R.id.tvview);
        progressBar = findViewById(R.id.progressBar);



        btnSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name = name1.getText().toString();
                String pass = pass1.getText().toString();
                final String num = num1.getText().toString();
                final String email = email1.getText().toString();


                if (name.isEmpty())
                {
                    name1.setError("Please Enter your name");
                    name1.requestFocus();
                }
                //name.isEmpty() is equal to name1.getText().toString().equals("")

                if (pass1.getText().toString().equals("") )
                {
                    pass1.setError("Please Enter New Password ");
                    pass1.requestFocus();
                }
                if(pass1.length() < 8){
                    pass1.setError("It should be minimum 8 character");
                    pass1.requestFocus();
                }
                if(email1.getText().toString().equals(""))
                {
                    email1.setError("Please Enter your EMAIL-ID");
                    email1.requestFocus();
                }
                if (name1.getText().toString().equals("") && pass1.getText().toString().equals("") && email1.getText().toString().equals(""))
                {
                    Toast.makeText(getApplicationContext(),"Please fill all the credential above",Toast.LENGTH_SHORT).show();
                }
                if (!name1.getText().toString().equals("") && !pass1.getText().toString().equals("") && !email1.getText().toString().equals(""))
                {
                    progressBar.setVisibility(View.VISIBLE);
                    mFirebaseAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(NewUser.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task)
                        {
                          if(task.isSuccessful()){

                              Toast.makeText(getApplicationContext(),"Your account is succesfully created",Toast.LENGTH_SHORT).show();

                              user_ID = mFirebaseAuth.getCurrentUser().getUid();
                              DocumentReference documentReference = fstore.collection("users").document(user_ID);
                              Map<String,Object> user = new HashMap<>();
                              user.put("name",name);
                              user.put("phone_number",num);
                              user.put("email_id",email);
                              documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                  @Override
                                  public void onSuccess(Void aVoid) {
                                      Log.d(TAG,"On SUCCESS: user profile is created for user_ID" + user_ID);
                                  }
                              });

                              Intent i = new Intent(NewUser.this,Login.class);
                              i.putExtra("view_text","Please enter your name Email and password");
                              startActivity(i);


                          }
                          else
                        {
                            Toast.makeText(getApplicationContext(),"Something went WRONG \n Please try again",Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                        }
                    });

                    /*if (mFirebaseAuth.getCurrentUser() != null){
                        Toast.makeText(getApplicationContext(),"You are already registered\n Go-to Login page",Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(NewUser.this,Login.class);
                        startActivity(i);
                        finish();
                    }*/
                }

            }
        });

    }
}
