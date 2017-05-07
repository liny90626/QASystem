package org.linky.qasystem.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.linky.qasystem.R;

import java.util.List;
import java.util.Map;

public class MyErrorQuestionListAdapter extends BaseAdapter {
    private ListView mListView;
    private List<Map<String, Object>> mList;
    private LayoutInflater mInflater;
    private Context mContext;

    public MyErrorQuestionListAdapter(Context context, List<Map<String, Object>> list,
                                      ListView listView) {
        mListView = listView;
        mContext = context;
        mList = list;
        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        final Map<String, Object> map = mList.get(position);
        if (convertView != null) {
            viewHolder = (ViewHolder) convertView.getTag();
        } else {
            viewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.my_error_question_item, null);

            viewHolder.title = (TextView) convertView.findViewById(R.id.my_error_item_name);
        }
        if (map != null && map.size() > 0) {

            viewHolder.title.setText(position + 1 + "." + map.get("title").toString());

        }

        convertView.setTag(viewHolder);
        return convertView;
    }

    /**
     * 得到数据
     *
     * @return
     */
    public List<Map<String, Object>> GetData() {
        return mList;
    }

    public int getCount() {
        return mList.size();
    }

    public Object getItem(int position) {
        return mList.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public class ViewHolder {
        TextView title;
    }
}
