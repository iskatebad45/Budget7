package org.zen.budget;

import android.content.ContentValues;
import android.widget.EditText;
import android.widget.Toast;

public class NewPanelActivity extends PanelActivity {
	private static final String	TAG	= ".NewPanelActivity";
	
	/*
	 * Final because you should extend PanelActivity instead
	 * (non-Javadoc)
	 * @see org.zen.budget.PanelActivity#checkAction()
	 */
	public final float checkAction() {
		final boolean is_add = getIntent().getExtras().getBoolean( PanelActivity.INTENT_EXTRA_IS_ADD);
		final String str_amt =
				is_add ? ((EditText)findViewById( R.id.et_panel_amt)).getText().toString() : "-"
						+ ((EditText)findViewById( R.id.et_panel_amt)).getText().toString();
		if( !str_amt.isEmpty()) {
			final String str_label = ((EditText)findViewById( R.id.et_panel_label)).getText().toString();
			final ContentValues values = new ContentValues();
			values.put( BudgetProvider.COL_AMT, str_amt);
			values.put( BudgetProvider.COL_LABEL, str_label.isEmpty() ? null : str_label);
			values.put( BudgetProvider.COL_DATE, System.currentTimeMillis());
			getContentResolver().insert( BudgetProvider.CONTENT_URI, values);
		} else {
			Toast.makeText( this, R.string.toast_need_amt, Toast.LENGTH_LONG).show();
		}
		// Log.d( TAG, "checkAction" + is_add);
		return Float.parseFloat( str_amt);
	}
	
	/*
	 * Final because you should extend PanelActivity instead
	 * (non-Javadoc)
	 * @see org.zen.budget.PanelActivity#exAction()
	 */
	public final float exAction() {
		// Log.d( TAG, "exAction: do nothing");
		return 0.0f;
	}
}