package com.vthree.rentbaseapplication.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.vthree.rentbaseapplication.R;
import com.vthree.rentbaseapplication.ModelClass.UserModel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class RegisterActivity extends AppCompatActivity {
    private String mVerificationId;
    //The edittext to input the code
    private Button btnRegister;
    private EditText editName,editContact,editAddress,editcity,editTaluka;
    private ProgressDialog progressDialog;

    //defining firebaseauth object
    private FirebaseAuth firebaseAuth;
    //firebase auth object
    DatabaseReference databaseReference;
    String user_id ="";
    String token;
    List<UserModel> data;
    int flag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        btnRegister=(Button) findViewById(R.id.buttonSignup);
        editName=(EditText)findViewById(R.id.editTextEmail);
        editContact=(EditText)findViewById(R.id.editTextmobile);
        editAddress=(EditText)findViewById(R.id.editTextAddress);
        editcity=(EditText)findViewById(R.id.editTextcity);
        editTaluka=(EditText)findViewById(R.id.editTexttaluka);
        data = new ArrayList<>();


        //initializing firebase auth object
        firebaseAuth = FirebaseAuth.getInstance();

        databaseReference = FirebaseDatabase.getInstance().getReference("RentBase").child("user");

        progressDialog = new ProgressDialog(this);

        //attaching listener to button
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });
    }

    private void registerUser(){

        //getting email and password from edit texts
        String name = editName.getText().toString().trim();
        String contact  = editContact.getText().toString().trim();
        String address  = editAddress.getText().toString().trim();
        String city  = editcity.getText().toString().trim();
        String taluka  = editTaluka.getText().toString().trim();

        //checking if email and passwords are empty
        if(TextUtils.isEmpty(name)){
            Toast.makeText(this,"Please enter name",Toast.LENGTH_SHORT).show();
            return;
        }else if(TextUtils.isEmpty(contact)){
            Toast.makeText(this,"Please enter contact",Toast.LENGTH_SHORT).show();
            return;
        }else if(TextUtils.isEmpty(address)){
            Toast.makeText(this,"Please enter address",Toast.LENGTH_SHORT).show();
            return;
        }else if(TextUtils.isEmpty(city)){
            Toast.makeText(this,"Please enter city",Toast.LENGTH_SHORT).show();
            return;
        }else if(TextUtils.isEmpty(taluka)){
            Toast.makeText(this,"Please enter taluka",Toast.LENGTH_SHORT).show();
            return;
        }else {
            user_id = databaseReference.push().getKey();

            for (int i = 0; i < data.size(); i++) {
                if (contact.equals(data.get(i).getMobile())) {
                    Log.d("da11", data.get(i).getMobile());
                    user_id = data.get(i).getUser_id();
                    flag = 1;
                }
            }
            Log.d("da11", String.valueOf(flag));
            if (flag == 1) {
                flag = 0;
                Toast.makeText(RegisterActivity.this, "This number already registered", Toast.LENGTH_SHORT).show();

            } else {
                databaseReference.child(user_id).setValue(new UserModel(user_id,name,address,contact,city,taluka,token))
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(RegisterActivity.this, "User Added Successful", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(RegisterActivity.this, "User Not Added Successful", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }

        //if the email and password are not empty
        //displaying a progress dialog

        /*progressDialog.setMessage("Registering Please Wait...");
        progressDialog.show();*/





    }

    @Override
    protected void onResume() {
        super.onResume();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        UserModel model = snapshot.getValue(UserModel.class);
                        data.add(model);

                        Log.d("da", model.getMobile().toString());
                    }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
}
