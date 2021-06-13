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
}