package com.jyotirmoy.licagentregister;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.lang.reflect.Type;

public class ViewDetailsActivity extends AppCompatActivity {

    String setName, setPhone, setAddress, setDOB, setPremium, setPolicyTable, setDOC, setDOM, setDLP, setUid;
    EditText name, phone, address, dateOfBirth, premium, policyTable, dateOfCommitment, dateOfMaturity, dateOfLastPayment;
    ImageView delete,edit,saveBtn;
    ProgressDialog pd;
    FirebaseDatabase database;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_details);

        name = findViewById(R.id.viewName);
        phone = findViewById(R.id.viewPhoneNo);
        address = findViewById(R.id.viewAddress);
        dateOfBirth = findViewById(R.id.viewDob);
        premium = findViewById(R.id.viewPremium);
        policyTable = findViewById(R.id.viewPolicyTable);
        dateOfCommitment = findViewById(R.id.viewDoc);
        dateOfMaturity = findViewById(R.id.viewDom);
        dateOfLastPayment = findViewById(R.id.viewDLP);


        setName = getIntent().getStringExtra("name");
        setPhone = getIntent().getStringExtra("phoneNo");
        setAddress = getIntent().getStringExtra("address");
        setDOB = getIntent().getStringExtra("DOB");
        setPremium = getIntent().getStringExtra("premium");
        setPolicyTable = getIntent().getStringExtra("policyTable");
        setDOC = getIntent().getStringExtra("DOC");
        setDOM = getIntent().getStringExtra("DOM");
        setDLP = getIntent().getStringExtra("DLP");
        setUid = getIntent().getStringExtra("uid");


        name.setText(setName);
        phone.setText(setPhone);
        address.setText(setAddress);
        dateOfBirth.setText(setDOB);
        premium.setText(setPremium);
        policyTable.setText(setPolicyTable);
        dateOfCommitment.setText(setDOC);
        dateOfMaturity.setText(setDOM);
        dateOfLastPayment.setText(setDLP);


        pd = new ProgressDialog(this);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        delete = findViewById(R.id.delete_Btn);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(ViewDetailsActivity.this);
                builder.setTitle("Delete Details??");
                builder.setMessage("Are You Want to Delete This Details?");
                builder.setCancelable(false);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        pd.setMessage("Deleting..");
                        pd.setCancelable(false);
                        pd.show();

                        DatabaseReference reference = database.getReference().child("user").child(auth.getUid()).child("PolicyHolder");
                       reference.child(setUid).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                           @Override
                           public void onComplete(@NonNull Task<Void> task) {
                              if(task.isSuccessful()){
                                  pd.dismiss();
                                  Toast.makeText(ViewDetailsActivity.this,"Deleted Successfully",Toast.LENGTH_SHORT).show();

                                  startActivity(new Intent(ViewDetailsActivity.this,SearchActivity.class));

                                  finish();
                              }else
                              {
                                  pd.dismiss();
                                  Toast.makeText(ViewDetailsActivity.this,"Failed to Delete!!!",Toast.LENGTH_SHORT).show();
                              }
                           }
                       }).addOnFailureListener(new OnFailureListener() {
                           @Override
                           public void onFailure(@NonNull Exception e) {
                               pd.dismiss();
                               Toast.makeText(ViewDetailsActivity.this,"Failed!!"+e.getMessage(),Toast.LENGTH_SHORT).show();
                           }
                       });



                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                    }
                });
                builder.create().show();
            }
        });


        saveBtn=findViewById(R.id.save_btn);
        edit=findViewById(R.id.edit_Btn);


        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              enableEditText();
                edit.setVisibility(View.GONE);
                saveBtn.setVisibility(View.VISIBLE);
            }
        });


        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pd.setMessage("Updating..");
                pd.setCancelable(false);
                pd.show();
                String FullName = name.getText().toString().trim();
                String Phone = phone.getText().toString().trim();
                String Address = address.getText().toString().trim();
                String DOB = dateOfBirth.getText().toString().trim();
                String Premium = premium.getText().toString().trim();
                String PolicyTable = policyTable.getText().toString().trim();
                String DOC = dateOfCommitment.getText().toString().trim();
                String DateOfMaturity = dateOfMaturity.getText().toString().trim();
                String DateOfLastPayment = dateOfLastPayment.getText().toString().trim();
                if (TextUtils.isEmpty(FullName)) {
                    pd.dismiss();
                    name.setError("Please Enter Name");
                    Toast.makeText(ViewDetailsActivity.this, "Please Enter Name", Toast.LENGTH_SHORT).show();
                }else{

                    DatabaseReference reference = database.getReference().child("user").child(auth.getUid()).child("PolicyHolder").child(setUid);
                    PolicyHolderDetailsModel policyHolderDetailsModel = new PolicyHolderDetailsModel(setUid, FullName, Phone, Address, DOB, Premium,
                            PolicyTable, DOC, DateOfMaturity, DateOfLastPayment);
                    reference.setValue(policyHolderDetailsModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                pd.dismiss();
                                Toast.makeText(ViewDetailsActivity.this, "Successfully Updated Details", Toast.LENGTH_LONG).show();
                                disableEditText();
                            }else{
                                pd.dismiss();
                                Toast.makeText(ViewDetailsActivity.this, "Failed to Update Details!!!", Toast.LENGTH_LONG).show();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            pd.dismiss();
                            Toast.makeText(ViewDetailsActivity.this, "Failed!!" + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }

            }
        });




    }

    private void disableEditText() {

        edit.setVisibility(View.VISIBLE);
        saveBtn.setVisibility(View.GONE);

        name.setEnabled(false);
        name.setBackgroundColor(Color.TRANSPARENT);
        name.setPadding(0,0,0,0);

        phone.setEnabled(false);
        phone.setBackgroundColor(Color.TRANSPARENT);
        phone.setPadding(0,0,0,0);

        address.setEnabled(false);
        address.setBackgroundColor(Color.TRANSPARENT);
        address.setPadding(0,0,0,0);

        dateOfBirth.setEnabled(false);
        dateOfBirth.setBackgroundColor(Color.TRANSPARENT);
        dateOfBirth.setPadding(0,0,0,0);

        premium.setEnabled(false);
        premium.setBackgroundColor(Color.TRANSPARENT);
        premium.setPadding(0,0,0,0);

        policyTable.setEnabled(false);
        policyTable.setBackgroundColor(Color.TRANSPARENT);
        policyTable.setPadding(0,0,0,0);

        dateOfCommitment.setEnabled(false);
        dateOfCommitment.setBackgroundColor(Color.TRANSPARENT);
        dateOfCommitment.setPadding(0,0,0,0);

        dateOfMaturity.setEnabled(false);
        dateOfMaturity.setBackgroundColor(Color.TRANSPARENT);
        dateOfMaturity.setPadding(0,0,0,0);

        dateOfLastPayment.setEnabled(true);
        dateOfLastPayment.setBackgroundColor(Color.TRANSPARENT);
        dateOfLastPayment.setPadding(0,0,0,0);
    }

    private void enableEditText() {
        name.setEnabled(true);
        name.setBackgroundColor(Color.WHITE);
        name.setPadding(10,2,10,2);

        phone.setEnabled(true);
        phone.setBackgroundColor(Color.WHITE);
        phone.setPadding(10,2,10,2);

        address.setEnabled(true);
        address.setBackgroundColor(Color.WHITE);
        address.setPadding(10,2,10,2);

        dateOfBirth.setEnabled(true);
        dateOfBirth.setBackgroundColor(Color.WHITE);
        dateOfBirth.setPadding(10,2,10,2);

        premium.setEnabled(true);
        premium.setBackgroundColor(Color.WHITE);
        premium.setPadding(10,2,10,2);

        policyTable.setEnabled(true);
        policyTable.setBackgroundColor(Color.WHITE);
        policyTable.setPadding(10,2,10,2);

        dateOfCommitment.setEnabled(true);
        dateOfCommitment.setBackgroundColor(Color.WHITE);
        dateOfCommitment.setPadding(10,2,10,2);

        dateOfMaturity.setEnabled(true);
        dateOfMaturity.setBackgroundColor(Color.WHITE);
        dateOfMaturity.setPadding(10,2,10,2);

       dateOfLastPayment.setEnabled(true);
        dateOfLastPayment.setBackgroundColor(Color.WHITE);
        dateOfLastPayment.setPadding(10,2,10,2);


    }

    @Override
    public void onBackPressed() {
        finishAffinity();
        startActivity(new Intent(ViewDetailsActivity.this,SearchActivity.class));
        finish();
    }
}