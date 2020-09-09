package com.lifu.aadtop20;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.lifu.aadtop20.dto.SkillIqDTO;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ContentsAdapter extends RecyclerView.Adapter<ContentsAdapter.ContentsViewHolder> implements Filterable {

    String TAG = getClass().getSimpleName();

    private TopIQFragment mParent;
    private ArrayList<SkillIqDTO> mContents;
    //private final boolean mTwoPane;
    private int mAdapterPosition;
    public static final List<SkillIqDTO> mITEMS = new ArrayList<>();
    public static final Map<String, SkillIqDTO> mITEM_MAP = new HashMap<>();
    private ArrayList<SkillIqDTO> mContentsFull;
    Context mContext;

    public ContentsAdapter(Context context, TopIQFragment mParent, ArrayList<SkillIqDTO> mContents){
        this.mParent = mParent;
        this.mContents = mContents;
        this.mContentsFull = new ArrayList<>(mContents);
        mContext = context;
    }

    @NonNull
    @Override
    public ContentsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.item_list_card, parent,false);
        return new ContentsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ContentsViewHolder holder, int position) {
        SkillIqDTO content = mContents.get(position);
        holder.bind(content);
        mITEMS.add(content);
        mITEM_MAP.put(content.name, content);

        holder.itemView.setTag(mContents.get(position));
        //holder.itemView.setOnClickListener(mOnClickListener);
    }

    @Override
    public int getItemCount() {
        return mContents.size();
    }

    @Override
    public Filter getFilter() {
        return contentsFilter;
    }

    private Filter contentsFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<SkillIqDTO> filteredContents = new ArrayList<>();
            if(constraint == null || constraint.length() == 0){
                filteredContents.addAll(mContentsFull);
            }else{
                String pattern = constraint.toString().toLowerCase().trim();
                for(SkillIqDTO item : mContentsFull){
                    if(item.name.toLowerCase().trim().contains(pattern)){
                        filteredContents.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredContents;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            mContents.clear();
            mContents.addAll((ArrayList) results.values);
            notifyDataSetChanged();
        }
    };

    public class ContentsViewHolder extends RecyclerView.ViewHolder{
        TextView mTvName;
        TextView mTvScore;
        TextView mTvCountry;
        ImageView mIvImage;

        public ContentsViewHolder(@NonNull View itemView) {
            super(itemView);

            mTvName = (TextView)itemView.findViewById(R.id.tvName);
            mTvScore = (TextView)itemView.findViewById(R.id.tvScore);
            mTvCountry = (TextView)itemView.findViewById(R.id.tvCountry);
            mIvImage = (ImageView)itemView.findViewById(R.id.ivImage);


            /*itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mAdapterPosition = getAdapterPosition();
                    int position = mAdapterPosition; //getAdapterPosition();
                    SkillIqDTO selectedContent = mContents.get(position);
                    if (mTwoPane) {
                        Bundle arguments = new Bundle();
                        arguments.putString(ItemDetailFragment.ARG_ITEM_ID, selectedContent.getCountry());
                        ItemDetailFragment fragment = new ItemDetailFragment();
                        fragment.setArguments(arguments);
                        mParent.getSupportFragmentManager().beginTransaction()
                                .replace(R.id.item_detail_container, fragment)
                                .commit();
                    } else {
                        Context context = v.getContext();
                        Intent intent = new Intent(context, ItemDetailActivity.class);
                        intent.putExtra(ItemDetailFragment.ARG_ITEM_ID, selectedContent.getCountry());

                        context.startActivity(intent);
                    }
                }
            });*/
        }

        public void bind(SkillIqDTO content){

            mTvName.setText(content.name);
            mTvScore.setText(content.score);
            mTvCountry.setText(content.country);
            Picasso.with(mContext).load(content.badgeUrl).into(mIvImage);
        }
    }
}