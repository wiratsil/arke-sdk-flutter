package com.arke.sdk.view;

import android.content.Context;
import android.os.Bundle;
import android.os.RemoteException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.arke.sdk.R;
import com.arke.sdk.api.SystemStatistics;
import com.usdk.apiservice.aidl.systemstatistics.StatisticInfo;

import java.util.List;

/**
 * System statistics activity.
 */
public class SystemStatisticActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_statistic);

        ListView list = (ListView) findViewById(R.id.list);

        try {
            SystemStatistics systemStatistics = SystemStatistics.getInstance();
            list.setAdapter(new MyAdapter(this, systemStatistics.getAllStatisticsAndStatus()));
        } catch (RemoteException e) {
            showToast(e.getMessage());
        }
    }

    /**
     * Adapter for list.
     */
    private class MyAdapter extends BaseAdapter {
        List<StatisticInfo> statisticInfos;
        private LayoutInflater layoutInflater;

        MyAdapter(Context context, List<StatisticInfo> statisticInfos) {
            this.layoutInflater = LayoutInflater.from(context);
            this.statisticInfos = statisticInfos;
        }

        public int getCount() {
            return statisticInfos == null ? 0 : statisticInfos.size();
        }

        public Object getItem(int position) {
            return statisticInfos == null ? null : statisticInfos.get(position);
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = layoutInflater.inflate(R.layout.list_item, null);
            }

            StatisticInfo info = statisticInfos.get(position);
            String name = getString(R.string.statistic_item) + ": " + info.getDisplayName();
            String value = getString(R.string.value) + ": " + info.getValue();

            ((TextView) convertView.findViewById(R.id.displayName)).setText(name);
            ((TextView) convertView.findViewById(R.id.value)).setText(value);
            return convertView;
        }
    }
}
