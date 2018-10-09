package com.android.launcher3.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.launcher3.R;
import com.android.launcher3.vo.Setting;

/**
 * @author tic
 * created on 18-10-9
 */
public class MangoSettingsAdapter extends BaseAdapter<MangoSettingsAdapter.ViewHolder, Setting> {

    public MangoSettingsAdapter(LayoutInflater inflater) {
        super(inflater);
    }

    public void addHeaderView(View... header) {

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View content = mInflater.inflate(R.layout.item_settings, parent, false);
        return new ViewHolder(content);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Setting setting = mData.get(position);
        holder.title.setText(setting.getTitle());
    }

    class ViewHolder extends BaseAdapter.BaseViewHolder {
        TextView title;
        TextView summary;
        ImageView icon;

        ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            summary = itemView.findViewById(R.id.summary);
            icon = itemView.findViewById(R.id.icon);
        }
    }
}
