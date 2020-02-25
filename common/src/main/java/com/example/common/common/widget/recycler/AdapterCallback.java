package com.example.common.common.widget.recycler;

/**
 * @author 1050483859@qq.com
 * @version 1.0.0
 */
public interface AdapterCallback<Data> {
    void update(Data data, RecyclerAdapter.ViewHolder<Data> holder);
}
