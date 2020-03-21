package com.mtm.flowcheck.adapter;
import android.widget.AbsListView;
import java.util.List;

/**
 * Description:版本日志Adapter
 * Version:V1.0
 */

public class VersionLogAdapter extends AppBaseAdapter {
    public VersionLogAdapter(AbsListView listView, List list) {
        super(listView, list);
    }

    @Override
    protected BaseHolder getHolder() {
        return new VersionLogHolder();
    }
}
