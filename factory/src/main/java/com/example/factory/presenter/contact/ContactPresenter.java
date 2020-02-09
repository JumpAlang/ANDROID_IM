package com.example.factory.presenter.contact;


import android.service.autofill.UserData;
        import android.util.Log;

        import androidx.annotation.NonNull;
        import androidx.recyclerview.widget.DiffUtil;
        import androidx.recyclerview.widget.RecyclerView;
        import com.example.common.common.app.Application;
        import com.example.common.common.widget.recycler.RecyclerAdapter;
        import com.example.common.factory.data.DataSource;
        import com.example.common.factory.presenter.BaseContract;
        import com.example.common.factory.presenter.BasePresenter;
        import com.example.factory.BaseObserver;
        import com.example.factory.Factory;
        import com.example.factory.R;
        import com.example.factory.data.helper.UserHelper;
        import com.example.factory.model.api.RspModel;
        import com.example.factory.model.card.UserCard;
        import com.example.factory.model.db.AppDatabase;
        import com.example.factory.model.db.User;
        import com.example.factory.model.db.User_Table;
        import com.example.factory.persistence.Account;
        import com.example.factory.presenter.BaseSourcePresenter;
        import com.example.factory.presenter.user.ContactDataSource;
        import com.example.factory.presenter.user.ContactRepository;
        import com.example.factory.utils.DiffUiDataCallback;
        import com.raizlabs.android.dbflow.config.DatabaseDefinition;
        import com.raizlabs.android.dbflow.config.FlowManager;
        import com.raizlabs.android.dbflow.sql.language.SQLite;
        import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;
        import com.raizlabs.android.dbflow.structure.database.transaction.ITransaction;
        import com.raizlabs.android.dbflow.structure.database.transaction.QueryTransaction;
        import java.util.ArrayList;
        import java.util.List;

/**
 * 联系人的Presenter实现
 *
 * @author qiujuer Email:qiujuer@live.cn
 * @version 1.0.0
 */
public class ContactPresenter extends BaseSourcePresenter<User, User, ContactDataSource, ContactContract.View>
        implements ContactContract.Presenter, DataSource.SucceedCallback<List<User>> {

    public ContactPresenter(ContactContract.View view) {
        // 初始化数据仓库
        super(new ContactRepository(), view);
    }


    @Override
    public void start() {
        super.start();

        // 加载网络数据
        UserHelper.refreshContacts();
    }

    // 运行到这里的时候是子线程
    @Override
    public void onDataLoaded(List<User> users) {
        // 无论怎么操作，数据变更，最终都会通知到这里来
        final ContactContract.View view = getView();
        if (view == null)
            return;

        RecyclerAdapter<User> adapter = view.getRecyclerAdapter();
        List<User> old = adapter.getItems();

        // 进行数据对比
        DiffUtil.Callback callback = new DiffUiDataCallback<>(old, users);
        DiffUtil.DiffResult result = DiffUtil.calculateDiff(callback);

        // 调用基类方法进行界面刷新
        refreshData(result, users);
    }
}
