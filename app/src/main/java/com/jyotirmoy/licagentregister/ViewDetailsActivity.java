package com.jyotirmoy.licagentregister;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ViewDetailsActivity extends AppCompatActivity {

    String setName, setPhone, setAddress, setDOB, setPolicyNo,setPremium, setPolicyTable, setDOC, setDOM, setDLP, setSumAssured,setUid;
    EditText name, phone, address, dateOfBirth, policyNumber,premium, policyTable, dateOfCommitment, dateOfMaturity, dateOfLastPayment,sumAssured;
    ImageView delete,edit,saveBtn;
    ProgressDialog pd;
    FirebaseDatabase database;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(getColor(R.color.search_header_start));
        getWindow().getDecorView().setSystemUiVisibility(0);
        setContentView(R.layout.activity_view_details);

        ImageButton backButton = findViewById(R.id.detailsBackButton);
        backButton.setOnClickListener(view -> onBackPressed());

        name = findViewById(R.id.viewName);
        phone = findViewById(R.id.viewPhoneNo);
        address = findViewById(R.id.viewAddress);
        dateOfBirth = findViewById(R.id.viewDob);
        policyNumber=findViewById(R.id.viewPolicyNo);
        premium = findViewById(R.id.viewPremium);
        policyTable = findViewById(R.id.viewPolicyTable);
        dateOfCommitment = findViewById(R.id.viewDoc);
        dateOfMaturity = findViewById(R.id.viewDom);
        dateOfLastPayment = findViewById(R.id.viewDLP);
        sumAssured=findViewById(R.id.viewSumAssured);


        setName = getIntent().getStringExtra("name");
        setPhone = getIntent().getStringExtra("phoneNo");
        setAddress = getIntent().getStringExtra("address");
        setDOB = getIntent().getStringExtra("DOB");
        setPolicyNo=getIntent().getStringExtra("PolicyNumber");
        setPremium = getIntent().getStringExtra("premium");
        setPolicyTable = getIntent().getStringExtra("policyTable");
        setDOC = getIntent().getStringExtra("DOC");
        setDOM = getIntent().getStringExtra("DOM");
        setDLP = getIntent().getStringExtra("DLP");
        setSumAssured = getIntent().getStringExtra("SumAssured");
        setUid = getIntent().getStringExtra("uid");


        name.setText(setName);
        phone.setText(setPhone);
        address.setText(setAddress);
        dateOfBirth.setText(setDOB);
        policyNumber.setText(setPolicyNo);
        premium.setText(setPremium);
        policyTable.setText(setPolicyTable);
        dateOfCommitment.setText(setDOC);
        dateOfMaturity.setText(setDOM);
        dateOfLastPayment.setText(setDLP);
        sumAssured.setText(setSumAssured);


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

                                  finish();
                                  overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
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
                String PolicyNo =policyNumber.getText().toString().trim();
                String Premium = premium.getText().toString().trim();
                String PolicyTable = policyTable.getText().toString().trim();
                String DOC = dateOfCommitment.getText().toString().trim();
                String DateOfMaturity = dateOfMaturity.getText().toString().trim();
                String DateOfLastPayment = dateOfLastPayment.getText().toString().trim();
                String SumAssured = sumAssured.getText().toString().trim();
                if (TextUtils.isEmpty(FullName)) {
                    pd.dismiss();
                    name.setError("Please Enter Name");
                    Toast.makeText(ViewDetailsActivity.this, "Please Enter Name", Toast.LENGTH_SHORT).show();
                }else{

                    DatabaseReference reference = database.getReference().child("user").child(auth.getUid()).child("PolicyHolder").child(setUid);
                    PolicyHolderDetailsModel policyHolderDetailsModel = new PolicyHolderDetailsModel(setUid, FullName, Phone, Address, DOB, PolicyNo,Premium,
                            PolicyTable, DOC, DateOfMaturity, DateOfLastPayment,SumAssured);
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

        setFieldsEditable(false);
    }

    private void enableEditText() {
        setFieldsEditable(true);
        name.requestFocus();
        name.setSelection(name.getText().length());
    }

    private void setFieldsEditable(boolean editable) {
        EditText[] fields = {
                name, phone, address, dateOfBirth, policyNumber, premium,
                policyTable, dateOfCommitment, dateOfMaturity,
                dateOfLastPayment, sumAssured
        };

        int horizontalPadding = editable
                ? (int) (12 * getResources().getDisplayMetrics().density)
                : 0;
        int verticalPadding = editable
                ? (int) (8 * getResources().getDisplayMetrics().density)
                : 0;

        for (EditText field : fields) {
            field.setEnabled(editable);
            field.setBackgroundResource(editable
                    ? R.drawable.modern_form_field
                    : android.R.color.transparent);
            field.setPadding(horizontalPadding, verticalPadding,
                    horizontalPadding, verticalPadding);
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
