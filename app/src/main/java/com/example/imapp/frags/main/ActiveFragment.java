package com.example.imapp.frags.main;


import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.common.common.app.Fragment;
import com.example.common.common.app.PresenterFragment;
import com.example.common.common.widget.DragBubbleView;
import com.example.common.common.widget.EmptyView;
import com.example.common.common.widget.PortraitView;
import com.example.common.common.widget.recycler.RecyclerAdapter;
import com.example.common.utils.DateTimeUtil;
import com.example.factory.model.db.Session;
import com.example.factory.presenter.contact.ContactContract;
import com.example.factory.presenter.message.SessionContract;
import com.example.factory.presenter.message.SessionPresenter;
import com.example.imapp.R;
import com.example.imapp.activities.MessageActivity;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;


public class ActiveFragment extends PresenterFragment<SessionContract.Presenter> implements SessionContract.View{
    public static final String TAG="ActiveFragment";

    @BindView(R.id.call_list)
    RecyclerView mRecycler;

    @BindView(R.id.empty)
    EmptyView mEmptyView;

    RecyclerAdapter<Session> mAdapter;

    public ActiveFragment() {
        // Required empty public constructor
    }


    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_active;
    }

    @Override
    protected void initData() {
        super.initData();
    }

    @Override
    protected void initWidget(View root) {
        super.initWidget(root);
        mEmptyView.bind(mRecycler);
        setPlaceHolderView(mEmptyView);

        mRecycler.setAdapter(mAdapter = new RecyclerAdapter<Session>() {
            @Override
            protected int getItemViewType(int position, Session session) {
                // 返回cell的布局id
                return R.layout.cell_conversation_list;
            }

            @Override
            protected ViewHolder<Session> onCreateViewHolder(View root, int viewType) {
                return new ActiveFragment.ViewHolder(root);
            }
        });
        mRecycler.setAdapter(mAdapter);
        mRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecycler.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
                Log.d(TAG, "onInterceptTouchEvent: ");
                rv.requestDisallowInterceptTouchEvent(true);
                return false;
            }

            @Override
            public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });
        mAdapter.setListener(new RecyclerAdapter.AdapterListener<Session>() {
            @Override
            public void onItemClick(RecyclerAdapter.ViewHolder holder, Session session) {
                MessageActivity.show(getActivity(),session);
            }

            @Override
            public void onItemLongClick(RecyclerAdapter.ViewHolder holder, Session session) {

            }
        });
    }

    @Override
    protected void onFirstInit() {
        super.onFirstInit();
        // 进行一次数据加载
        mPresenter.start();
    }
    // 界面数据渲染
    class ViewHolder extends RecyclerAdapter.ViewHolder<Session> {
        @BindView(R.id.im_portrait)
        PortraitView mPortraitView;

        @BindView(R.id.txt_name)
        TextView mName;

        @BindView(R.id.text_detail)
        TextView mContent;

        @BindView(R.id.txt_time)
        TextView mTime;

        @BindView(R.id.unReadCount)
        DragBubbleView unReadCount;

        ViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void onBind(Session session) {
            session.load();
            Log.d(TAG, "onBind: "+session.toString());
            mPortraitView.setup(Glide.with(ActiveFragment.this), session.getPicture());
            mName.setText(session.getTitle());
            mContent.setText(TextUtils.isEmpty(session.getContent()) ? "" : session.getContent());
            mTime.setText(DateTimeUtil.getSampleDate(session.getModifyAt()));
            unReadCount.setmTextStr(String.valueOf(session.getUnReadCount()));
        }
    }
    @Override
    protected SessionContract.Presenter initPresenter() {
        mPresenter=new SessionPresenter(this);
        return mPresenter;
    }

    @Override
    public RecyclerAdapter<Session> getRecyclerAdapter() {
        return mAdapter;
    }

    @Override
    public void onAdapterDataChanged() {
        mPlaceHolderView.triggerOkOrEmpty(mAdapter.getItemCount() > 0);
    }
}
