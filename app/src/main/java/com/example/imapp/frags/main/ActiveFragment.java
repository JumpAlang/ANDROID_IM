package com.example.imapp.frags.main;


import android.provider.DocumentsContract;
import android.view.View;
import android.widget.TextView;

import com.example.common.common.app.Fragment;
import com.example.common.common.widget.recycler.RecyclerAdapter;
import com.example.factory.model.card.UserCard;
import com.example.imapp.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;


public class ActiveFragment extends Fragment implements RecyclerAdapter.AdapterListener<String>{

    @BindView(R.id.call_list)
    RecyclerView mRecyclerView;

    public ActiveFragment() {
        // Required empty public constructor
    }


    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_active;
    }
    private ArrayList<String> cards=new ArrayList<String>();

    @Override
    protected void initData() {
        super.initData();
        for (int i = 0; i < 10; i++) {
            cards.add("asghrweffwefwf");
        }

        RecyclerAdapter<String> adapter=new RecyclerAdapter<String>(cards,this) {
            @Override
            protected int getItemViewType(int position, String s) {
                return R.layout.item_call;
            }

            @Override
            protected ViewHolder<String> onCreateViewHolder(View root, int viewType) {
                View view = root.inflate(getContext(),viewType,null);
                return new ViewHolder<String>(view) {
                    @Override
                    protected void onBind(String s) {
                        //绑定数据的回掉
                        TextView textId = view.findViewById(R.id.item_text);
                        textId.setText(s);
                    }
                };
            }
        } ;
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    public void onItemClick(RecyclerAdapter.ViewHolder holder, String s) {

    }

    @Override
    public void onItemLongClick(RecyclerAdapter.ViewHolder holder, String s) {

    }
}
