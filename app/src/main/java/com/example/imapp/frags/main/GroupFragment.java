package com.example.imapp.frags.main;


import com.example.common.common.app.Fragment;
import com.example.common.common.widget.recycler.RecyclerAdapter;
import com.example.imapp.R;

public class GroupFragment extends Fragment implements RecyclerAdapter.AdapterListener<String> {

    public GroupFragment() {
        // Required empty public constructor
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_group;
    }

    @Override
    public void onItemClick(RecyclerAdapter.ViewHolder holder, String s) {

    }

    @Override
    public void onItemLongClick(RecyclerAdapter.ViewHolder holder, String s) {

    }
}
