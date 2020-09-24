package com.example.onix.androideat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.onix.androideat.Common.Common;
import com.example.onix.androideat.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

public class SignIn extends AppCompatActivity {
    EditText OnixPhone,OnixPassword;
    Button btnSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        OnixPassword = (MaterialEditText) findViewById(R.id.OnixPassword);
        OnixPhone = (MaterialEditText) findViewById(R.id.OnixPhone);
        btnSignIn = (Button) findViewById(R.id.btnSignIn);

        //Init Firebase
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = database.getReference("User");

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final ProgressDialog mDialog = new ProgressDialog(SignIn.this);
                 mDialog.setMessage("Please wait");
                 mDialog.show();

                table_user.addValueEventListener(new ValueEventListener() {


                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        //Check if user don't exist in database
                        if(dataSnapshot.child(OnixPhone.getText().toString()).exists()) {
                            //Get User information
                            mDialog.dismiss();
                            User user = dataSnapshot.child(OnixPhone.getText().toString()).getValue(User.class);
                            user.setPhone(OnixPhone.getText().toString()); //Set Phone
                            if (user.getPassword().equals(OnixPassword.getText().toString())) {
                                {
                                    Intent homeIntent = new Intent(SignIn.this,Home.class);
                                    Common.currentUser = user;
                                    startActivity(homeIntent);
                                    finish();
                                }
                            } else {
                                Toast.makeText(SignIn.this, "Wrong password!", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else
                        {
                            mDialog.dismiss();
                            Toast.makeText(SignIn.this, "User don't exist", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });
    }
}
