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
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.markupartist.android.widget.PullToRefreshListView;
import com.markupartist.android.widget.PullToRefreshListView.OnRefreshListener;

public class MainActivity extends SherlockFragmentActivity implements
        OnItemClickListener, OnEditorActionListener
{
    private Handler handler = null;
    private EditText editText = null;
    private ListView listView = null;
    private CustomAdapter adapter = null;

    private int index = 0;
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
                index += 5;
                String start = String.valueOf(index);
                new MainHandler(MainActivity.this).execute(db, term, start);
            }
        });

        editText = (EditText) findViewById(R.id.EditTextTerm);
        editText.setOnEditorActionListener(this);
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            adapter.clear();
            listView.setAdapter(null);
            findViewById(R.id.TextViewLoad).setVisibility(View.VISIBLE);
            findViewById(R.id.ProgressBarLoad).setVisibility(View.VISIBLE);

            Spinner spinner = (Spinner) findViewById(R.id.SpinnerDatabase);

            Locale locale = Locale.getDefault();
            db = spinner.getSelectedItem().toString().toLowerCase(locale);
            term = editText.getText().toString();

            new MainHandler(this).execute(db, term, "0");
            index = 0;

            return true;
        }
        return false;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View v, int pos, long id) {
        Bundle extras = new Bundle();
        extras.putString("id", String.valueOf(id));
        extras.putString("db", db);

        Intent intent = new Intent(this, ContentActivity.class);
        intent.putExtras(extras);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getSupportMenuInflater();
        inflater.inflate(R.menu.main, menu);

        return super.onCreateOptionsMenu(menu);
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