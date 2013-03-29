package gov.nih.nlm.ncbi;

import java.util.Locale;

import org.holoeverywhere.widget.Spinner;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.markupartist.android.widget.PullToRefreshListView;
import com.markupartist.android.widget.PullToRefreshListView.OnRefreshListener;

public class MainActivity extends SherlockFragmentActivity implements
		OnClickListener, OnItemClickListener
{
	private Handler handler = null;
	private ListView listView = null;
	private CustomAdapter adapter = null;

	private int start = 0;
	private String db = null;
	private String term = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Drawable drawable = getResources().getDrawable(R.color.holo_blue_ncbi);

		getSupportActionBar().setLogo(R.drawable.logo_ncbi);
		getSupportActionBar().setDisplayShowTitleEnabled(false);
		getSupportActionBar().setBackgroundDrawable(drawable);

		FragmentManager manager = getSupportFragmentManager();
		FragmentTransaction transaction = manager.beginTransaction();

		Fragment fragment = manager.findFragmentByTag("fragment");
		if (fragment == null) {
			fragment = new MenuFragment();
			transaction.add(fragment, "fragment");
		}
		transaction.commit();

		handler = new Handler();
		adapter = new CustomAdapter(this);

		listView = (ListView) findViewById(R.id.ListViewSummary);
		listView.setEmptyView((View) findViewById(R.id.ViewEmpty));
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(this);

		((PullToRefreshListView) getListView())
				.setOnRefreshListener(new OnRefreshListener()
		{
			@Override
			public void onRefresh() {
				start += 5;
				new MainHandler(MainActivity.this).execute(db, term,
						String.valueOf(start), "5");
			}
		});

		Button button = (Button) findViewById(R.id.ButtonFetch);
		button.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		adapter.clear();
		listView.setAdapter(null);
		findViewById(R.id.ProgressBar).setVisibility(View.VISIBLE);

		Spinner spinner = (Spinner) findViewById(R.id.SpinnerDatabase);
		EditText editText = (EditText) findViewById(R.id.EditTextTerm);

		Locale locale = Locale.getDefault();
		db = spinner.getSelectedItem().toString().toLowerCase(locale);
		term = editText.getText().toString();

		new MainHandler(this).execute(db, term, "0", "5");
		start = 0;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View v, int pos, long id) {
		Bundle extras = new Bundle();
		extras.putString("id", String.valueOf(id));
		extras.putString("db", db);
		extras.putString("mode", "text");
		extras.putString("type", "abstract");

		Intent intent = new Intent(this, ContentActivity.class);
		intent.putExtras(extras);
		startActivity(intent);
	}

	public synchronized ListView getListView() {
		return this.listView;
	}

	public synchronized Handler getHandler() {
		return this.handler;
	}

	public synchronized CustomAdapter getAdapter() {
		return this.adapter;
	}
}