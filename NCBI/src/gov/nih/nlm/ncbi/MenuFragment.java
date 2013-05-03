package gov.nih.nlm.ncbi;

import org.holoeverywhere.widget.Toast;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.MenuItem;

public class MenuFragment extends SherlockFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Context context = getActivity().getApplicationContext();

        switch (item.getItemId()) {
        case android.R.id.home:
            Intent intent = new Intent(context, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            return true;
        case R.id.action_settings_main:
            Toast.makeText(context, "Main", Toast.LENGTH_SHORT).show();
            return true;
        case R.id.action_settings_content:
            Toast.makeText(context, "Content", Toast.LENGTH_SHORT).show();
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }
}