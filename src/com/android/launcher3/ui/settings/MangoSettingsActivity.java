package com.android.launcher3.ui.settings;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.android.launcher3.R;
import com.android.launcher3.Utilities;
import com.android.launcher3.ui.BaseCompatActivity;
import com.android.launcher3.ui.adapter.BaseAdapter;
import com.android.launcher3.ui.adapter.MangoSettingsAdapter;
import com.android.launcher3.vo.Setting;

import java.util.ArrayList;
import java.util.List;

/**
 * @author tic
 * created on 18-10-9
 */
public class MangoSettingsActivity extends BaseCompatActivity {

    private MangoSettingsAdapter mAdapter;
    private boolean isDefultHome;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_recycler);

        mAdapter = new MangoSettingsAdapter(getLayoutInflater());
        mAdapter.setOnItemClickListener(onItemClickListener);
        RecyclerView mSettings = findViewById(R.id.recycler_view);

        mSettings.setAdapter(mAdapter);
        mSettings.setLayoutManager(new LinearLayoutManager(this));
        mSettings.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        isDefultHome = Utilities.isOurHome(this);
        if (!isDefultHome) {
            mAdapter.addHeaderView();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private BaseAdapter.OnItemClickListener onItemClickListener = (view, position, data) -> {
        Setting setting = (Setting) data;
        int id = setting.getId();
        switch (id) {
            case R.id.menu_launcher:
                break;
            default:
                break;
        }
    };

    private List<Setting> getSettings(Menu menu) {
        List<Setting> settings = new ArrayList<>();
        int count = menu.size();
        for (int i = 0; i < count; i++) {
            MenuItem item = menu.getItem(i);
            settings.add(new Setting(item.getItemId(), item.getTitle(), item.getIcon()));
        }
        return settings;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mango_settings, menu);
        mAdapter.setData(getSettings(menu));
        return false;
    }

}
