package gov.nih.nlm.ncbi;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.actionbarsherlock.app.SherlockFragmentActivity;

public class MainActivity extends SherlockFragmentActivity implements
		OnClickListener 
{
	private Handler handler = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setTheme(R.style.Theme_Sherlock_Light);

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		getSupportActionBar().setLogo(R.drawable.logo_ncbi);
		getSupportActionBar().setDisplayShowTitleEnabled(false);

		FragmentManager manager = getSupportFragmentManager();
		FragmentTransaction transaction = manager.beginTransaction();

		Fragment fragment = manager.findFragmentByTag("fragment");
		if (fragment == null) {
			fragment = new MenuFragment();
			transaction.add(fragment, "fragment");
		}
		transaction.commit();
		
		handler = new Handler();

		Button button = (Button) findViewById(R.id.ButtonFetch);
		button.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
	}

	public synchronized Handler getHandler() {
		return this.handler;
	}
}
