package com.example.mojitongxin.widget;

import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mojitongxin.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public abstract class RecyclerAdapter<Data> extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder>
implements  View.OnClickListener,View.OnLongClickListener{
    private static final String TAG="RecyclerAdapter<Data>";
    private final List<Data> mDataList;
    private AdaterCallback<Data> callback;

    /**
     * 构造函数模块
     */
    public RecyclerAdapter() {
        this(null);
    }

    public RecyclerAdapter(AdaterCallback<Data> listener) {
        this(new ArrayList<Data>(), listener);
    }

    public RecyclerAdapter(List<Data> dataList, AdaterCallback<Data> listener) {
        this.mDataList = dataList;
        this.callback = listener;
    }

    /**
     *
     * @param position
     * @return item布局的id
     */
    @Override
    public int getItemViewType(int position) {
        return getViewType(position);
    }
    public abstract int getViewType(int position);
    /**
     * 约定viewType就是xml的id
     * @param parent
     * @param viewType
     * @return
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(parent.getContext()).inflate(viewType,parent,false);
        ViewHolder<Data> holder=onCreateHolder(root,viewType);
        root.setTag(R.id.aaa,holder);
        root.setOnClickListener(this);
        root.setOnLongClickListener(this);
        return holder;
    }
    protected abstract ViewHolder<Data> onCreateHolder(View root,int viewType);
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Data data=mDataList.get(position);
        holder.bind(data);
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }
    public abstract static class ViewHolder<Data> extends RecyclerView.ViewHolder{
        protected Data mData;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        /**
         * 绑定数据，触发回调
         * @param data
         */
        void bind(Data data){
            this.mData=data;
            onbind(data);
        }
        protected abstract void onbind(Data data);

    }
    /**
     * 插入一条数据并通知插入
     *
     * @param data Data
     */
    public void add(Data data) {
        mDataList.add(data);
        notifyItemInserted(mDataList.size() - 1);
    }

    /**
     * 插入一堆数据，并通知这段集合更新
     *
     * @param dataList Data
     */
    public void add(Data... dataList) {
        if (dataList != null && dataList.length > 0) {
            int startPos = mDataList.size();
            Collections.addAll(mDataList, dataList);
            notifyItemRangeInserted(startPos, dataList.length);
        }
    }

    /**
     * 插入一堆数据，并通知这段集合更新
     *
     * @param dataList Data
     */
    public void add(Collection<Data> dataList) {
        if (dataList != null && dataList.size() > 0) {
            int startPos = mDataList.size();
            mDataList.addAll(dataList);
            notifyItemRangeInserted(startPos, dataList.size());
        }
    }

    /**
     * 删除操作
     */
    public void clear() {
        mDataList.clear();
        notifyDataSetChanged();
    }

    /**
     * 替换为一个新的集合，其中包括了清空
     *
     * @param dataList 一个新的集合
     */
    public void replace(Collection<Data> dataList) {
        mDataList.clear();
        if (dataList == null || dataList.size() == 0)
            return;
        mDataList.addAll(dataList);
        notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        Log.d(TAG, "onClick: ");
        if(callback!=null){
            ViewHolder holder = (ViewHolder) v.getTag(R.id.aaa);
            int adapterPosition = holder.getAdapterPosition();
            callback.onItemClick(holder,mDataList.get(adapterPosition));
        }
    }

    @Override
    public boolean onLongClick(View v) {
        if(callback!=null){
            ViewHolder holder = (ViewHolder) v.getTag(R.id.aaa);
            int adapterPosition = holder.getAdapterPosition();
            callback.onItemLongClick(holder,mDataList.get(adapterPosition));
            return true;
        }
        return false;
    }

    public void setCallback(AdaterCallback callback) {
        this.callback = callback;
    }

    public interface AdaterCallback<Data> {
        // 当Cell点击的时候触发
        void onItemClick(RecyclerAdapter.ViewHolder holder, Data data);

        // 当Cell长按时触发
        void onItemLongClick(RecyclerAdapter.ViewHolder holder, Data data);
    }
}
