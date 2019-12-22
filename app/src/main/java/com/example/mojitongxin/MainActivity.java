package com.example.mojitongxin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mojitongxin.widget.RecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerAdapter<String> adapter=new RecyclerAdapter<String>() {
            @Override
            public int getViewType(int position) {
                if(position<5){
                    return R.layout.item1;
                }else{
                    return R.layout.item2;
                }
            }

            @Override
            protected ViewHolder<String> onCreateHolder(final View root, final int viewType) {
                ViewHolder<String> viewHolder=new ViewHolder<String>(root) {
                    @Override
                    protected void onbind(String s) {
                        TextView textView=root.findViewById(R.id.text11);
                        textView.setText(s);
                    }
                };
                return viewHolder;
            }
        };
        adapter.setCallback(new RecyclerAdapter.AdaterCallback<String>() {
            @Override
            public void onItemClick(RecyclerAdapter.ViewHolder holder, String s) {
                Log.d("TAG", "onItemClick: ");
                Toast.makeText(MainActivity.this,"onItemClick" + s,Toast.LENGTH_SHORT);
            }

            @Override
            public void onItemLongClick(RecyclerAdapter.ViewHolder holder, String s) {
                Toast.makeText(MainActivity.this,"nItemLongClick" +s,Toast.LENGTH_SHORT);
            }
        });

        RecyclerView viewById = findViewById(R.id.recyclerView);
        viewById.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        viewById.setLayoutManager(layoutManager);

        List<String> list=new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add("i="+i);
        }
        adapter.add(list);

    }
}
