package com.kopisenja.floodmonitoring.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kopisenja.floodmonitoring.R;
import com.kopisenja.floodmonitoring.base.Flood;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

    private ClickHandler mClickHandler;
    private Context mContext;
    private ArrayList<Flood> mData;
    private ArrayList<String> mDataId;
    private ArrayList<String> mSelectedId;
    private View mEmptyView;

    public HistoryAdapter(Context context, ArrayList<Flood> data, ArrayList<String> dataId, View emptyView, ClickHandler handler) {
        mContext = context;
        mData = data;
        mDataId = dataId;
        mEmptyView = emptyView;
        mClickHandler = handler;
        mSelectedId = new ArrayList<>();
    }

    public void updateEmptyView() {
        if (mData.size() == 0)
            mEmptyView.setVisibility(View.VISIBLE);
        else
            mEmptyView.setVisibility(View.GONE);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_item_history, parent, false);
        return new HistoryAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Flood flood = mData.get(position);

        int category = flood.getCategory();

        if (category == 1) {
            holder.categoryTextView.setText(mContext.getString(R.string.category_normal));
            holder.categoryTextView.setTextColor(mContext.getResources().getColor(R.color.colorTextCategoryNormal));
        } else if (category == 2) {
            holder.categoryTextView.setText(mContext.getString(R.string.category_standby));
            holder.categoryTextView.setTextColor(mContext.getResources().getColor(R.color.colorTextCategoryStandBy));
        } else {
            holder.categoryTextView.setText(mContext.getString(R.string.category_danger));
            holder.categoryTextView.setTextColor(mContext.getResources().getColor(R.color.colorTextCategoryDanger));
        }

//        holder.categoryTextView.setText(flood.getCategory() + "");
        holder.locationTextView.setText(flood.getLocation());
        holder.dateTextView.setText(flood.getDate());
        holder.timeTextView.setText(flood.getTime());
        holder.debitTextView.setText(flood.getDebit());
        holder.levelTextView.setText(flood.getLevel());

        holder.itemView.setSelected(mSelectedId.contains(mDataId.get(position)));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void toggleSelection(String dataId) {
        if (mSelectedId.contains(dataId))
            mSelectedId.remove(dataId);
        else
            mSelectedId.add(dataId);
        notifyDataSetChanged();
    }

    public int selectionCount() {
        return mSelectedId.size();
    }

    public void resetSelection() {
        mSelectedId = new ArrayList<>();
        notifyDataSetChanged();
    }

    public ArrayList<String> getSelectedId() {
        return mSelectedId;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        final TextView categoryTextView;
        final TextView locationTextView;
        final TextView dateTextView;
        final TextView timeTextView;
        final TextView debitTextView;
        final TextView levelTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            categoryTextView = itemView.findViewById(R.id.textView_history_category);
            locationTextView = itemView.findViewById(R.id.textView_history_location);
            dateTextView = itemView.findViewById(R.id.textView_history_date);
            timeTextView = itemView.findViewById(R.id.textView_history_time);
            debitTextView = itemView.findViewById(R.id.textView_history_debit);
            levelTextView = itemView.findViewById(R.id.textView_history_level);

            // focusable sengaja di false biar ngga bisa di klik
            itemView.setFocusable(false);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View view) {

        }

        @Override
        public boolean onLongClick(View view) {
            return false;
        }
    }

    public interface ClickHandler {
        void onItemClick(int position);
        boolean onItemLongClick(int position);
    }
}
