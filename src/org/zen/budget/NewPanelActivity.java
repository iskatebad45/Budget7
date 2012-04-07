package org.zen.budget;

import android.content.ContentValues;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Final because you should extendd PanelActivity instead
 * 
 * @see PanelActivity
 * @author zen
 * 
 */

public final class NewPanelActivity extends PanelActivity {
	@SuppressWarnings( "unused")
	private static final String TAG = ".NewPanelActivity";
	
	@Override
	public void onCreate( final Bundle savedInstanceState) {
		super.onCreate( savedInstanceState);
		((TextView)findViewById( R.id.tv_panel_amtsign)).setText( getIntent().getExtras().getBoolean(
			PanelActivity.INTENT_EXTRA_IS_ADD) ? "$+" : "$-");
	}
	
	/**
	 * Final because you should extend PanelActivity instead
	 * 
	 * @return the amount to update the total by.
	 * @see org.zen.budget.PanelActivity#checkAction()
	 */
	public final float checkAction() {
		final boolean is_add = getIntent().getExtras().getBoolean( PanelActivity.INTENT_EXTRA_IS_ADD);
		String str_amt = ((EditText)findViewById( R.id.et_panel_amt)).getText().toString();
		if( !str_amt.isEmpty()) {
			if( !is_add) {
				str_amt = "-" + str_amt;
			}
			final String str_label = ((EditText)findViewById( R.id.et_panel_label)).getText().toString();
			final ContentValues values = new ContentValues();
			values.put( BudgetProvider.COL_AMT, str_amt);
			values.put( BudgetProvider.COL_LABEL, str_label.isEmpty() ? null : str_label);
			values.put( BudgetProvider.COL_DATE, System.currentTimeMillis());
			getContentResolver().insert( BudgetProvider.CONTENT_URI, values);
		} else {
			/**
			 * NewPanelActivity will be ending it's lifecycle soon, so the Toast needs to go to an active activity (i.e,
			 * the whole application lol)
			 */
			runOnUiThread( new Runnable() {
				@Override
				public void run() {
					Toast.makeText( getApplicationContext(), R.string.toast_need_amt, Toast.LENGTH_LONG).show();
				}
			});
			return 0.0f;
		}
		return Float.parseFloat( str_amt);
	}
	
	/**
	 * Final because you should extend PanelActivity instead
	 * 
	 * @return The amount to update the total by.
	 * @see org.zen.budget.PanelActivity#exAction()
	 */
	public final float exAction() {
		return 0.0f;
	}
}