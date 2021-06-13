package com.jyotirmoy.licagentregister;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    PolicyHolderAdapter policyHolderAdapter;
     ArrayList<PolicyHolderDetailsModel> policyHolderDetailsModelList;
     FirebaseDatabase database;
     FirebaseAuth auth;
     EditText searchBox;
     CharSequence search="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

         searchBox=findViewById(R.id.searchName);

        auth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();

        policyHolderDetailsModelList =new ArrayList<>();

        DatabaseReference reference=database.getReference().child("user").child(auth.getUid()).child("PolicyHolder");

        recyclerView=findViewById(R.id.recyclerView);
        RecyclerView.LayoutManager layoutManager= new LinearLayoutManager(this,RecyclerView.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        policyHolderAdapter=new PolicyHolderAdapter(this,policyHolderDetailsModelList);
        recyclerView.setAdapter(policyHolderAdapter);


        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    PolicyHolderDetailsModel policyHolderDetailsModel=dataSnapshot.getValue(PolicyHolderDetailsModel.class);
                    policyHolderDetailsModelList.add(policyHolderDetailsModel);
                }
                policyHolderAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



             searchBox.addTextChangedListener(new TextWatcher() {
                 @Override
                 public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {

                 }

                 @Override
                 public void onTextChanged(CharSequence charSequence, int start, int before, int count) {

                     policyHolderAdapter.getFilter().filter(charSequence);
                     search=charSequence;
                 }

                 @Override
                 public void afterTextChanged(Editable s) {

                 }
             });




    }


    @Override
    public void onBackPressed() {
        startActivity(new Intent(SearchActivity.this,MainActivity.class));
        finish();
    }
}