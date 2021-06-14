package com.jyotirmoy.licagentregister;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.UUID;

public class AddNewDetailsActivity extends AppCompatActivity {

    EditText name, phoneNo, address, dob,policyNo, premium, policyTable, Doc, dateMaturity, dateLastPayment,sumAssured;
    TextView save_btn;
    final Calendar myCalendar = Calendar.getInstance();
    FirebaseDatabase database;
    FirebaseAuth auth;
    ProgressDialog pd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_details);
        pd = new ProgressDialog(this);

        name = findViewById(R.id.enterName);
        phoneNo = findViewById(R.id.enterPhoneNo);
        address = findViewById(R.id.enterAddress);
        dob = findViewById(R.id.enterDob);
        policyNo=findViewById(R.id.enterPolicyNo);
        premium = findViewById(R.id.enterPremium);
        policyTable = findViewById(R.id.enterPolicyTable);
        Doc = findViewById(R.id.enterDoc);
        dateMaturity = findViewById(R.id.enterDateMaturity);
        dateLastPayment = findViewById(R.id.enterLastPayment);
        sumAssured=findViewById(R.id.enterSumAssured);


        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateDobLabel();
            }

        };

        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(AddNewDetailsActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        DatePickerDialog.OnDateSetListener date1 = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateDocLabel();
            }

        };
        Doc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(AddNewDetailsActivity.this, date1, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        DatePickerDialog.OnDateSetListener date2 = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateDateMaturityLabel();
            }

        };
        dateMaturity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(AddNewDetailsActivity.this, date2, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        DatePickerDialog.OnDateSetListener date3 = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateDateLastPaymentLabel();
            }

        };
        dateLastPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(AddNewDetailsActivity.this, date3, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();


        save_btn = findViewById(R.id.saveBtn);
        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd.setMessage("Please Wait..");
                pd.setCancelable(false);
                pd.show();

                String FullName = name.getText().toString().trim();
                String Phone = phoneNo.getText().toString().trim();
                String Address = address.getText().toString().trim();
                String DOB = dob.getText().toString().trim();
                String PolicyNo =policyNo.getText().toString().trim();
                String Premium = premium.getText().toString().trim();
                String PolicyTable = policyTable.getText().toString().trim();
                String DOC = Doc.getText().toString().trim();
                String DateOfMaturity = dateMaturity.getText().toString().trim();
                String DateOfLastPayment = dateLastPayment.getText().toString().trim();
                String SumAssured = sumAssured.getText().toString().trim();

                if (TextUtils.isEmpty(FullName)) {
                    pd.dismiss();
                    name.setError("Please Enter Name");
                    Toast.makeText(AddNewDetailsActivity.this, "Please Enter Name", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(Phone)) {
                    pd.dismiss();
                    phoneNo.setError("Please Enter Phone no");
                    Toast.makeText(AddNewDetailsActivity.this, "Please Enter Phone", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(Address)) {
                    pd.dismiss();
                    address.setError("Please Enter Address");
                    Toast.makeText(AddNewDetailsActivity.this, "Please Enter Address", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(DOB)) {
                    pd.dismiss();
                    dob.setError("Please Enter Date Of Birth");
                    Toast.makeText(AddNewDetailsActivity.this, "Please Enter DOB", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(PolicyNo)) {
                    pd.dismiss();
                    policyNo.setError("Please Enter Date Of Birth");
                    Toast.makeText(AddNewDetailsActivity.this, "Please Enter DOB", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(Premium)) {
                    pd.dismiss();
                    premium.setError("Please Enter Premium");
                    Toast.makeText(AddNewDetailsActivity.this, "Please Enter Premium", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(PolicyTable)) {
                    pd.dismiss();
                    policyTable.setError("Please Enter Policy Table");
                    Toast.makeText(AddNewDetailsActivity.this, "Please Enter Policy Table", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(DOC)) {
                    pd.dismiss();
                    Doc.setError("Please Enter DOC");
                    Toast.makeText(AddNewDetailsActivity.this, "Please Enter DOC", Toast.LENGTH_SHORT).show();
                } else {

                    String RandomUid = UUID.randomUUID().toString();
                    DatabaseReference reference = database.getReference().child("user").child(auth.getUid()).child("PolicyHolder").child(RandomUid);
                    PolicyHolderDetailsModel policyHolderDetailsModel = new PolicyHolderDetailsModel(RandomUid, FullName, Phone, Address, DOB, PolicyNo,Premium,
                            PolicyTable, DOC, DateOfMaturity, DateOfLastPayment,SumAssured);
                    reference.setValue(policyHolderDetailsModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()) {
                                pd.dismiss();
                                Toast.makeText(AddNewDetailsActivity.this, "Successfully Add New Details", Toast.LENGTH_LONG).show();
                                name.setText("");
                                phoneNo.setText("");
                                address.setText("");
                                dob.setText("");
                                policyNo.setText("");
                                premium.setText("");
                                policyTable.setText("");
                                Doc.setText("");
                                dateMaturity.setText("");
                                dateLastPayment.setText("");
                                sumAssured.setText("");
                                startActivity(new Intent(AddNewDetailsActivity.this, MainActivity.class));
                                finish();

                            } else {
                                pd.dismiss();
                                Toast.makeText(AddNewDetailsActivity.this, "Failed to Add Details!!!", Toast.LENGTH_LONG).show();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            pd.dismiss();
                            Toast.makeText(AddNewDetailsActivity.this, "Failed!!" + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });

                }

            }
        });


    }

    private void updateDateLastPaymentLabel() {
        String myFormat = "dd/MM/YY"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);


        dateLastPayment.setText(sdf.format(myCalendar.getTime()));
    }

    private void updateDateMaturityLabel() {
        String myFormat = "dd/MM/YY"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);


        dateMaturity.setText(sdf.format(myCalendar.getTime()));
    }

    private void updateDocLabel() {
        String myFormat = "dd/MM/YY"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);


        Doc.setText(sdf.format(myCalendar.getTime()));
    }

    private void updateDobLabel() {
        String myFormat = "dd/MM/YY"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);


        dob.setText(sdf.format(myCalendar.getTime()));
    }
}