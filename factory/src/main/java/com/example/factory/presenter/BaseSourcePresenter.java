package com.example.factory.presenter;

import com.example.common.factory.data.DataSource;
import com.example.common.factory.data.DbDataSource;
import com.example.common.factory.presenter.BaseContract;
import com.example.common.factory.presenter.BaseRecyclerPresenter;

import java.util.List;

/**
 * 基础的仓库源的Presenter定义
 *
 * @author 1050483859@qq.com
 * @version 1.0.0
 */
public abstract class BaseSourcePresenter<Data, ViewModel,
        Source extends DbDataSource<Data>,
        View extends BaseContract.RecyclerView>
        extends BaseRecyclerPresenter<ViewModel, View>
        implements DataSource.SucceedCallback<List<Data>> {

    protected Source mSource;

    public BaseSourcePresenter(Source source, View view) {
        super(view);
        this.mSource = source;
    }

    @Override
    public void start() {
        super.start();
        if (mSource != null)
            mSource.load(this);
    }

    @Override
    public void destroy() {
        super.destroy();
        if (mSource != null){
            mSource.dispose();
            mSource = null;
        }
    }
}
