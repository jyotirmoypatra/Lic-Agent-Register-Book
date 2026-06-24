package com.jyotirmoy.licagentregister;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class PolicyHolderAdapter extends RecyclerView.Adapter<PolicyHolderAdapter.ViewHolder> {
    public interface OnFilterResultListener {
        void onFilterResult(int resultCount);
    }

    Context context;
    ArrayList<PolicyHolderDetailsModel> policyHolderDetailsModelArrayList;
    ArrayList<PolicyHolderDetailsModel> filterItemList;
    OnFilterResultListener filterResultListener;


    public PolicyHolderAdapter(Context context, ArrayList<PolicyHolderDetailsModel> policyHolderDetailsModelArrayList) {
        this.context =context;
        this.policyHolderDetailsModelArrayList = policyHolderDetailsModelArrayList;
        this.filterItemList=policyHolderDetailsModelArrayList;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_policy_holder_raw, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

       PolicyHolderDetailsModel policyHolder = filterItemList.get(position);
       String name = policyHolder.getName();
       holder.name.setText(name);
       holder.avatar.setText(getInitials(name));

       String policyNumber = policyHolder.getPolicyNo();
       holder.policyNumber.setText(isBlank(policyNumber)
               ? context.getString(R.string.policy_number_unavailable)
               : context.getString(R.string.policy_number_format, policyNumber));

       String phoneNumber = policyHolder.getPhone();
       holder.phoneNumber.setText(isBlank(phoneNumber)
               ? context.getString(R.string.phone_unavailable)
               : phoneNumber);
      // ItemAnimation.animateFadeIn(holder.itemView,position);
       holder.itemView.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent = new Intent(context, ViewDetailsActivity.class);
               intent.putExtra("name", policyHolder.getName());
               intent.putExtra("phoneNo", policyHolder.getPhone());
               intent.putExtra("address", policyHolder.getAddress());
               intent.putExtra("DOB", policyHolder.getDob());
               intent.putExtra("PolicyNumber", policyHolder.getPolicyNo());
               intent.putExtra("premium", policyHolder.getPremium());
               intent.putExtra("policyTable", policyHolder.getPolicyTableTerm());
               intent.putExtra("DOC", policyHolder.getDoc());
               intent.putExtra("DOM", policyHolder.getDateMaturity());
               intent.putExtra("DLP", policyHolder.getDateLastPayment());
               intent.putExtra("SumAssured", policyHolder.getSumAssured());
               intent.putExtra("uid", policyHolder.getUid());
               context.startActivity(intent);
               if (context instanceof SearchActivity) {
                   ((SearchActivity) context).overridePendingTransition(
                           R.anim.slide_in_right,
                           R.anim.slide_out_left
                   );
               }
           }
       });
    }

    @Override
    public int getItemCount() {
        return filterItemList.size();
    }

    public void setOnFilterResultListener(OnFilterResultListener listener) {
        this.filterResultListener = listener;
    }

      class ViewHolder extends RecyclerView.ViewHolder{
        TextView name, avatar, policyNumber, phoneNumber;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name=itemView.findViewById(R.id.namePolicyHolder);
            avatar=itemView.findViewById(R.id.avatar);
            policyNumber=itemView.findViewById(R.id.policyNumber);
            phoneNumber=itemView.findViewById(R.id.phoneNumber);
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    private String getInitials(String name) {
        if (isBlank(name)) {
            return "?";
        }

        String[] words = name.trim().split("\\s+");
        StringBuilder initials = new StringBuilder();
        initials.append(Character.toUpperCase(words[0].charAt(0)));
        if (words.length > 1) {
            initials.append(Character.toUpperCase(words[words.length - 1].charAt(0)));
        }
        return initials.toString();
    }
    public Filter getFilter(){
        return  new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String Key =charSequence.toString();
                ArrayList<PolicyHolderDetailsModel> filtered;
                if(Key.isEmpty()){
                    filtered = new ArrayList<>(policyHolderDetailsModelArrayList);
                }else {
                    filtered = new ArrayList<>();
                    for(PolicyHolderDetailsModel row:policyHolderDetailsModelArrayList){
                        if(!isBlank(row.getName())
                                && row.getName().toLowerCase().contains(Key.toLowerCase())){
                            filtered.add(row);
                        }
                    }
                    filterItemList=filtered;
                }
                FilterResults results=new FilterResults();
                results.values=filtered;
                results.count=filtered.size();
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                filterItemList=(ArrayList<PolicyHolderDetailsModel>)results.values;
                notifyDataSetChanged();
                if (filterResultListener != null) {
                    filterResultListener.onFilterResult(results.count);
                }

            }
        };
    }
    }
