package com.crime.cout.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.crime.cout.Models.CrimeModel;
import com.crime.cout.R;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.List;

public class CrimeAdapter extends RecyclerView.Adapter<CrimeAdapter.CustomViewHolder> {
    List<CrimeModel> crimeModels;
    Context context;




    private  onItemClickListener mListener;
      public  interface onItemClickListener{
          void  delete(int position);
          void  comments(int position);
        }

     public  void setOnItemClickListener(onItemClickListener listener){//item click listener initialization
          mListener=listener;
     }
     public static class  CustomViewHolder extends RecyclerView.ViewHolder{
        TextView textViewName
                ,textViewType,textViewZipCode
                ,textViewLocation;



          public CustomViewHolder(View itemView, final onItemClickListener listener) {
             super(itemView);
              textViewName=itemView.findViewById(R.id.textViewName);
              textViewType=itemView.findViewById(R.id.textViewType);
              textViewZipCode=itemView.findViewById(R.id.textViewZipCode);
              textViewLocation=itemView.findViewById(R.id.textViewLocation);

          }
    }
    public CrimeAdapter( List<CrimeModel> crimeModels, Context context) {
        this.crimeModels=crimeModels;
        this.context = context;


    }
    @Override
    public int getItemViewType(int position) {
            return R.layout.crime_item;
    }
    @Override
    public int getItemCount() {
        return  crimeModels.size();
    }
    
    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CustomViewHolder(LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false),mListener);
    }
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        holder.textViewName.setText(crimeModels.get(position).getCrimeName());
        holder.textViewType.setText(crimeModels.get(position).getCrimeType());
        holder.textViewZipCode.setText(crimeModels.get(position).getZipCode());
        holder.textViewLocation.setText(crimeModels.get(position).getLatitude()+","+crimeModels.get(position).getLongitude());

      }
}
