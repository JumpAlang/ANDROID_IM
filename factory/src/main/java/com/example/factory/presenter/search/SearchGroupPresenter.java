package com.example.factory.presenter.search;

import com.example.common.factory.presenter.BasePresenter;


/**
 * 搜索群的逻辑实现
 *
 * @author 1050483859@qq.com
 * @version 1.0.0
 */
public class SearchGroupPresenter extends BasePresenter<SearchContract.GroupView>
        implements SearchContract.Presenter {
    public SearchGroupPresenter(SearchContract.GroupView view) {
        super(view);
    }

    @Override
    public void search(String content) {

    }
}
