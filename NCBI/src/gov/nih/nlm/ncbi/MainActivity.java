package gov.nih.nlm.ncbi;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.actionbarsherlock.app.SherlockFragmentActivity;

public class MainActivity extends SherlockFragmentActivity {

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
	}
}
