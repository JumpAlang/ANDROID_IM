package com.example.imapp.frags.search;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.common.common.app.PresenterFragment;
import com.example.common.common.widget.EmptyView;
import com.example.common.common.widget.PortraitView;
import com.example.common.common.widget.recycler.RecyclerAdapter;
import com.example.factory.model.card.UserCard;
import com.example.factory.presenter.contact.FollowContract;
import com.example.factory.presenter.search.SearchContract;
import com.example.factory.presenter.search.SearchUserPresenter;
import com.example.imapp.R;
import com.example.imapp.activities.SearchActivity;

import net.qiujuer.genius.ui.Ui;
import net.qiujuer.genius.ui.compat.UiCompat;
import net.qiujuer.genius.ui.drawable.LoadingCircleDrawable;
import net.qiujuer.genius.ui.drawable.LoadingDrawable;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 搜索人的界面实现
 */
public class SearchUserFragment extends PresenterFragment<SearchContract.Presenter>
        implements SearchActivity.SearchFragment, SearchContract.UserView{
    public static final String TAG="SearchUserFragment";

    @BindView(R.id.empty)
    EmptyView mEmptyView;

    @BindView(R.id.recycler)
    RecyclerView mRecycler;

    private RecyclerAdapter<UserCard> mAdapter;

    public SearchUserFragment() {
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_search_user;
    }

    @Override
    protected void initWidget(View root) {
        super.initWidget(root);

        // 初始化Recycler
        mRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecycler.setAdapter(mAdapter = new RecyclerAdapter<UserCard>() {
            @Override
            protected int getItemViewType(int position, UserCard userCard) {
                // 返回cell的布局id
                return R.layout.cell_search_list;
            }

            @Override
            protected ViewHolder<UserCard> onCreateViewHolder(View root, int viewType) {
                return new SearchUserFragment.ViewHolder(root);
            }
        });

        // 初始化占位布局
        mEmptyView.bind(mRecycler);
        setPlaceHolderView(mEmptyView);
    }

    @Override
    protected void initData() {
        super.initData();
        // 发起首次搜索
        search("");
    }

    @Override
    public void search(String content) {
        // Activity->Fragment->Presenter->Net
        mPresenter.search(content);
    }

    @Override
    public void onSearchDone(List<UserCard> userCards) {
        // 数据成功的情况下返回数据
        mAdapter.replace(userCards);
        // 如果有数据，则是OK，没有数据就显示空布局
        mPlaceHolderView.triggerOkOrEmpty(mAdapter.getItemCount() > 0);
    }

    @Override
    protected SearchContract.Presenter initPresenter() {
        // 初始化Presenter
        return new SearchUserPresenter(this);
    }

    /**
     * 每一个Cell的布局操作
     */
    class ViewHolder extends RecyclerAdapter.ViewHolder<UserCard>
            implements FollowContract.View {
        @BindView(R.id.im_portrait)
        PortraitView mPortraitView;

        @BindView(R.id.txt_name)
        TextView mName;

        @BindView(R.id.add_contact)
        Button mFollow;

        @BindView(R.id.added_contact)
        Button mFollowed;
        private FollowContract.Presenter mPresenter;


        public ViewHolder(View itemView) {
            super(itemView);
            // 当前View和Presenter绑定
//            new FollowPresenter(this);
        }

        void refreshFollow(UserCard userCard){
            if(userCard.isFollow()){
                mFollow.setVisibility(View.GONE);
                mFollowed.setVisibility(View.VISIBLE);
            }else {
                mFollow.setVisibility(View.VISIBLE);
                mFollowed.setVisibility(View.GONE);
            }
        }

        @Override
        protected void onBind(UserCard userCard) {
            mPortraitView.setup(Glide.with(SearchUserFragment.this), userCard.getPortrait());
            mName.setText(userCard.getName());
            refreshFollow(userCard);
        }

        @OnClick(R.id.im_portrait)
        void onPortraitClick() {
            // 显示信息
//            PersonalActivity.show(getContext(), mData.getId());
        }

        @OnClick(R.id.add_contact)
        void onFollowClick() {
            // 发起关注
            mPresenter.follow(mData.getId());
        }
//
        @Override
        public void showError(int str) {
            // 更改当前界面状态
//            if (mFollow.getDrawable() instanceof LoadingDrawable) {
//                // 失败则停止动画，并且显示一个圆圈
//                LoadingDrawable drawable = (LoadingDrawable) mFollow.getDrawable();
//                drawable.setProgress(1);
//                drawable.stop();
//            }
        }

        @Override
        public void showLoading() {
//            int minSize = (int) Ui.dipToPx(getResources(), 22);
//            int maxSize = (int) Ui.dipToPx(getResources(), 30);
//            // 初始化一个圆形的动画的Drawable
//            LoadingDrawable drawable = new LoadingCircleDrawable(minSize, maxSize);
//            drawable.setBackgroundColor(0);
//
//            int[] color = new int[]{UiCompat.getColor(getResources(), R.color.white_alpha_208)};
//            drawable.setForegroundColor(color);
//            // 设置进去
//            mFollow.setImageDrawable(drawable);
//            // 启动动画
//            drawable.start();
        }

        @Override
        public void setPresenter(FollowContract.Presenter presenter) {
            mPresenter = presenter;
        }

        @Override
        public void onFollowSucceed(UserCard userCard) {
            Log.d(TAG, "onFollowSucceed: ");
//             更改当前界面状态
//            if (mFollow.getDrawable() instanceof LoadingDrawable) {
//                ((LoadingDrawable) mFollow.getDrawable()).stop();
//                 设置为默认的
//                mFollow.setImageResource(R.drawable.sel_opt_done_add);
//            }
            // 发起更新
            updateData(userCard);
        }
    }
}
