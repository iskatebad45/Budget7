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
 */

public final class NewPanelActivity extends PanelActivity {
	@SuppressWarnings( "unused")
	private final String	TAG	= ".NewPanelActivity";
	protected boolean		isPositive;
	
	@Override
	public void onCreate( final Bundle savedInstanceState) {
		super.onCreate( savedInstanceState);
		isPositive = getIntent().getExtras().getBoolean( PanelActivity.INTENT_EXTRA_IS_ADD);
		((TextView)findViewById( R.id.tv_panel_amtsign)).setText( isPositive ? "$+" : "$-");
	}
	
	/**
	 * Final because you should extend PanelActivity instead
	 * 
	 * @return the amount to update the total by.
	 * @see org.zen.budget.PanelActivity#checkAction()
	 */
	public final void checkAction() {
		final String str_amt =
				isPositive ? ((EditText)findViewById( R.id.et_panel_amt)).getText().toString() : "-"
						+ ((EditText)findViewById( R.id.et_panel_amt)).getText().toString();
		new Thread( new Runnable() {
			public void run() {
				if( !str_amt.isEmpty()) {
					final String str_label = ((EditText)findViewById( R.id.et_panel_label)).getText().toString();
					final ContentValues values = new ContentValues();
					values.put( BudgetProvider.COL_AMT, str_amt);
					values.put( BudgetProvider.COL_LABEL, str_label.isEmpty() ? null : str_label);
					values.put( BudgetProvider.COL_DATE, System.currentTimeMillis());
					getContentResolver().insert( BudgetProvider.CONTENT_URI, values);
				} else {
					/**
					 * NewPanelActivity will be ending it's lifecycle soon, so the Toast needs to go to an active
					 * activity (i.e, the whole application lol)
					 */
					runOnUiThread( new Runnable() {
						@Override
						public final void run() {
							Toast.makeText( getApplicationContext(), R.string.toast_need_amt, Toast.LENGTH_LONG).show();
						}
					});
				}
			}
		}).start();
	}
	
	/**
	 * Final because you should extend PanelActivity instead
	 * 
	 * @return The amount to update the total by.
	 * @see org.zen.budget.PanelActivity#exAction()
	 */
	public final void exAction() {
		// ignore. nothing is done because this is essentially a cancel
	}
}