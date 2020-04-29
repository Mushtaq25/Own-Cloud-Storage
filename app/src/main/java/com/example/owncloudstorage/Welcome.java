package com.example.owncloudstorage;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;

public class Welcome extends AppCompatActivity {

    TextView welcomename,showName,showEmail,showPhonenumber;
    Button Log_out,btnchangedp;
    FirebaseAuth mfirebaseAuth;
    FirebaseFirestore fstore;
    String user_ID;
    ImageView dp;
    StorageReference mstore;
    //Spinner spin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);


        mfirebaseAuth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();
        mstore = FirebaseStorage.getInstance().getReference();

        Log_out = findViewById(R.id.Log_out);
        welcomename = findViewById(R.id.welcomename);
        showName = findViewById(R.id.showName);
        showEmail = findViewById(R.id.showEmail);
        showPhonenumber = findViewById(R.id.showPhonenumber);
        btnchangedp = findViewById(R.id.changedp);
        dp = findViewById(R.id.dp);


        user_ID = mfirebaseAuth.getCurrentUser().getUid();

        DocumentReference documentReference = fstore.collection("users").document(user_ID);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
               showName.setText("Name:-"+" "+documentSnapshot.getString("name"));
               showEmail.setText("Email:-"+" "+documentSnapshot.getString("email_id"));
               showPhonenumber.setText("Telephone:-"+" "+documentSnapshot.getString("phone_number"));
               welcomename.setText(documentSnapshot.getString("name"));
            }
        });

        /*spin = findViewById(R.id.spinner);
        List<String> item = new ArrayList<String>();
        item.add("Log-OUT");


        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(arrayAdapter);

        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               switch (position){
                   case 0:
                       FirebaseAuth.getInstance().signOut();
                       startActivity(new Intent(getApplicationContext(),Login.class));
                       Toast.makeText(getApplicationContext(), "You are LOG-OUT", Toast.LENGTH_SHORT).show();
                    finish();}
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });*/
        Log_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mfirebaseAuth.signOut();
                startActivity(new Intent(getApplicationContext(),Login.class));
                Toast.makeText(getApplicationContext(), "You are LOG-OUT", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        btnchangedp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent opengalary = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(opengalary,1000);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000){
            if(resultCode == Activity.RESULT_OK){
                Uri Imageuri = data.getData();
                dp.setImageURI(Imageuri );

                upload_imageTO_firebase(Imageuri);
            }
        }
    }

    private void upload_imageTO_firebase(Uri Imageuri) {
        StorageReference filename = mstore.child("profile.jpg");
        filename.putFile(Imageuri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(Welcome.this,"image uploaded",Toast.LENGTH_SHORT).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Welcome.this,"failed to upload",Toast.LENGTH_SHORT).show();
            }
        });
    }
}

