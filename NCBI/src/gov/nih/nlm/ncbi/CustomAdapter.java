package gov.nih.nlm.ncbi;

import gov.nih.nlm.ncbi.model.Summary;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CustomAdapter extends BaseAdapter {

	private List<Summary> summaryList = null;
	private LayoutInflater inflater = null;

	public CustomAdapter(Context context) {
		this.summaryList = new ArrayList<Summary>();
		this.inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return summaryList.size();
	}

	@Override
	public Object getItem(int position) {
		return summaryList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return summaryList.get(position).getId();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		convertView = inflater.inflate(R.layout.listview_item_row, null);

		((TextView) convertView.findViewById(R.id.TextViewList))
				.setText(summaryList.get(position).getData());
		return convertView;
	}

	public void add(List<Summary> summaryList) {
		this.summaryList.addAll(summaryList);
		notifyDataSetChanged();
	}

	public void clear() {
		this.summaryList.clear();
	}
}