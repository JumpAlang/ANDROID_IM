package com.example.factory.presenter.contact;


import android.util.Log;

import androidx.recyclerview.widget.DiffUtil;

import com.example.common.common.widget.recycler.RecyclerAdapter;
        import com.example.common.factory.data.DataSource;
import com.example.factory.data.helper.UserHelper;
import com.example.factory.model.db.User;
import com.example.factory.presenter.BaseSourcePresenter;
        import com.example.factory.data.user.ContactDataSource;
        import com.example.factory.data.user.ContactRepository;
        import com.example.factory.utils.DiffUiDataCallback;
import com.example.factory.utils.HanyuToPinyin;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 联系人的Presenter实现
 *
 * @author qiujuer Email:qiujuer@live.cn
 * @version 1.0.0
 */
public class ContactPresenter extends BaseSourcePresenter<User, User, ContactDataSource, ContactContract.View>
        implements ContactContract.Presenter, DataSource.SucceedCallback<List<User>> {
    public static final String TAG="ContactPresenter";
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
        Log.d(TAG, "onDataLoaded: ");
        // 无论怎么操作，数据变更，最终都会通知到这里来
        final ContactContract.View view = getView();
        if (view == null)
            return;

        RecyclerAdapter<User> adapter = view.getRecyclerAdapter();
        List<User> old = adapter.getItems();

        Collections.sort(users,new UploadPhoneComparator());

        setUserFlag(users);

        // 进行数据对比
        DiffUtil.Callback callback = new DiffUiDataCallback<>(old, users);
        DiffUtil.DiffResult result = DiffUtil.calculateDiff(callback);

        // 调用基类方法进行界面刷新
        refreshData(result, users);
    }

    /**
     * 给每个字母首个User，添加标志
     * @param users 联系人列表
     */
    private void setUserFlag(List<User> users){
        for (int i = 0; i < users.size(); i++) {
            if(i==0){
                users.get(i).setFrist(true);
            }else {
                String name1 = HanyuToPinyin.hanziToCapital(users.get(i-1).getName());
                String name2 = HanyuToPinyin.hanziToCapital(users.get(i).getName());
                Log.d(TAG, "setUserFlag: "+name1+" "+name2);
                if(name1.charAt(0)==name2.charAt(0)){
                    users.get(i).setFrist(false);
                }else {
                    users.get(i).setFrist(true);
                }
            }
        }
    }

    public class UploadPhoneComparator implements Comparator<User> {
        @Override
        public int compare(User o1, User o2) {
            String name1 = HanyuToPinyin.hanziToCapital(o1.getName());
            String name2 = HanyuToPinyin.hanziToCapital(o2.getName());
            return name1.compareTo(name2);
        }
    }
}
