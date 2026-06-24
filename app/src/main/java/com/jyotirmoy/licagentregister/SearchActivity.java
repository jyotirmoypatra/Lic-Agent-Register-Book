package com.jyotirmoy.licagentregister;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
public class SearchActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    PolicyHolderAdapter policyHolderAdapter;
     ArrayList<PolicyHolderDetailsModel> policyHolderDetailsModelList;
     FirebaseDatabase database;
     FirebaseAuth auth;
     EditText searchBox;
     TextView totalPolicyHolder;
     TextView emptyState;
     ImageButton backButton;
     ProgressBar loadingProgress;
     CharSequence search="";
     boolean listLoaded=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(getColor(R.color.search_header_start));
        getWindow().getDecorView().setSystemUiVisibility(0);
        setContentView(R.layout.activity_search);

         searchBox=findViewById(R.id.searchName);
         totalPolicyHolder=findViewById(R.id.totalPolicyHolder);
         emptyState=findViewById(R.id.emptyState);
         backButton=findViewById(R.id.backButton);
         loadingProgress=findViewById(R.id.loadingProgress);
         backButton.setOnClickListener(view -> onBackPressed());


        auth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();

        policyHolderDetailsModelList =new ArrayList<>();

        DatabaseReference reference=database.getReference().child("user").child(auth.getUid()).child("PolicyHolder");

        recyclerView=findViewById(R.id.recyclerView);
        RecyclerView.LayoutManager layoutManager= new LinearLayoutManager(this,RecyclerView.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        policyHolderAdapter=new PolicyHolderAdapter(this,policyHolderDetailsModelList);
        policyHolderAdapter.setOnFilterResultListener(this::updateEmptyState);
        recyclerView.setAdapter(policyHolderAdapter);



        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                policyHolderDetailsModelList.clear();
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    PolicyHolderDetailsModel policyHolderDetailsModel=dataSnapshot.getValue(PolicyHolderDetailsModel.class);
                    if (policyHolderDetailsModel != null) {
                        policyHolderDetailsModelList.add(policyHolderDetailsModel);
                    }
                }
                totalPolicyHolder.setText(getString(
                        R.string.total_policy_holder,
                        policyHolderDetailsModelList.size()
                ));
                listLoaded=true;
                loadingProgress.setVisibility(ProgressBar.GONE);
                policyHolderAdapter.getFilter().filter(searchBox.getText());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                listLoaded=true;
                loadingProgress.setVisibility(ProgressBar.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                emptyState.setText(R.string.unable_to_load_policy_holders);
                emptyState.setVisibility(View.VISIBLE);
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

        searchBox.setOnEditorActionListener((view, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                hideKeyboard();
                searchBox.clearFocus();
                return true;
            }
            return false;
        });



    }

    private void updateEmptyState(int resultCount) {
        if (!listLoaded) {
            return;
        }

        boolean hasResults = resultCount > 0;
        recyclerView.setVisibility(View.VISIBLE);
        emptyState.setVisibility(hasResults ? View.GONE : View.VISIBLE);

        if (!hasResults) {
            boolean isSearching = searchBox.getText().toString().trim().length() > 0;
            emptyState.setText(isSearching
                    ? R.string.search_result_not_found
                    : R.string.no_policy_holders_available);
        }
    }

    private void hideKeyboard() {
        InputMethodManager inputMethodManager =
                (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        View focusedView = getCurrentFocus();
        if (focusedView != null) {
            inputMethodManager.hideSoftInputFromWindow(focusedView.getWindowToken(), 0);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN && getCurrentFocus() == searchBox) {
            Rect searchBounds = new Rect();
            searchBox.getGlobalVisibleRect(searchBounds);
            if (!searchBounds.contains((int) event.getRawX(), (int) event.getRawY())) {
                hideKeyboard();
                searchBox.clearFocus();
            }
        }
        return super.dispatchTouchEvent(event);
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
