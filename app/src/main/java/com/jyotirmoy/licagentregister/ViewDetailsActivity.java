package com.jyotirmoy.licagentregister;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ViewDetailsActivity extends AppCompatActivity {

    String setName, setPhone, setAddress, setDOB, setPremium, setPolicyTable, setDOC, setDOM, setDLP, setUid;
    TextView name, phone, address, dateOfBirth, premium, policyTable, dateOfCommitment, dateOfMaturity, dateOfLastPayment;
    ImageView delete;
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


    }
}