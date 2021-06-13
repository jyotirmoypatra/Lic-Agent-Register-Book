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
    Context context;
    ArrayList<PolicyHolderDetailsModel> policyHolderDetailsModelArrayList;
    ArrayList<PolicyHolderDetailsModel> filterItemList;


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

       holder.name.setText(filterItemList.get(position).getName());
       ItemAnimation.animateFadeIn(holder.itemView,position);
       holder.itemView.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent = new Intent(context, ViewDetailsActivity.class);
               intent.putExtra("name", filterItemList.get(position).getName());
               intent.putExtra("phoneNo", filterItemList.get(position).getPhone());
               intent.putExtra("address", filterItemList.get(position).getAddress());
               intent.putExtra("DOB", filterItemList.get(position).getDob());
               intent.putExtra("PolicyNumber", filterItemList.get(position).getPolicyNo());
               intent.putExtra("premium", filterItemList.get(position).getPremium());
               intent.putExtra("policyTable", filterItemList.get(position).getPolicyTableTerm());
               intent.putExtra("DOC", filterItemList.get(position).getDoc());
               intent.putExtra("DOM", filterItemList.get(position).getDateMaturity());
               intent.putExtra("DLP", filterItemList.get(position).getDateLastPayment());

               intent.putExtra("uid", filterItemList.get(position).getUid());
               context.startActivity(intent);
           }
       });
    }

    @Override
    public int getItemCount() {
        return filterItemList.size();
    }

      class ViewHolder extends RecyclerView.ViewHolder{
        TextView name;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name=itemView.findViewById(R.id.namePolicyHolder);
        }
    }
    public Filter getFilter(){
        return  new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String Key =charSequence.toString();
                if(Key.isEmpty()){
                    filterItemList=policyHolderDetailsModelArrayList;
                }else {
                    ArrayList<PolicyHolderDetailsModel> filtered = new ArrayList<>();
                    for(PolicyHolderDetailsModel row:policyHolderDetailsModelArrayList){
                        if(row.getName().toLowerCase().contains(Key.toLowerCase())){
                            filtered.add(row);
                        }
                    }
                    filterItemList=filtered;
                }
                FilterResults results=new FilterResults();
                results.values=filterItemList;
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                filterItemList=(ArrayList<PolicyHolderDetailsModel>)results.values;
                notifyDataSetChanged();

            }
        };
    }
    }

