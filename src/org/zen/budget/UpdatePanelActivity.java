package org.zen.budget;

import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

public class UpdatePanelActivity extends PanelActivity {
	@SuppressWarnings( "unused")
	private final String TAG = ".UpdatePanelActivity";
	private long id;
	protected boolean isPositive;
	
	@Override
	public final void onCreate( final Bundle savedInstanceState) {
		super.onCreate( savedInstanceState);
		id = getIntent().getExtras().getLong( INTENT_EXTRA_PANEL_ID, 0);
		new UpdateOnCreateWorker().execute( id);
	}
	
	public final class UpdateOnCreateWorker extends AsyncTask<Long, Void, Cursor> {
		@Override
		protected final Cursor doInBackground( final Long... params) {
			final Cursor c =
					getContentResolver().query( ContentUris.withAppendedId( BudgetProvider.CONTENT_URI, params[ 0]),
						BudgetProvider.PROJECTION_ALL, BudgetProvider._ID + "=?", new String[] { "" + params[ 0]}, null);
			return c;
		}
		
		@Override
		protected final void onPostExecute( final Cursor c) {
			c.moveToFirst();
			final float amt = c.getFloat( c.getColumnIndex( BudgetProvider.COL_AMT));
			isPositive = amt >= 0.0f;
			((EditText)findViewById( R.id.et_panel_amt)).setText( String.format( "%,.2f", Math.abs( amt)));
			((EditText)findViewById( R.id.et_panel_label)).setText( c.getString( c.getColumnIndex( BudgetProvider.COL_LABEL)));
			((TextView)findViewById( R.id.tv_panel_amtsign)).setText( isPositive ? "$+" : "$-");
		}
	}
	
	@Override
	public final void checkAction() {
		final String str_label = ((EditText)findViewById( R.id.et_panel_label)).getText().toString();
		final String str_amt =
				isPositive ? ((EditText)findViewById( R.id.et_panel_amt)).getText().toString() : "-"
						+ ((EditText)findViewById( R.id.et_panel_amt)).getText().toString();
		new Thread( new Runnable() {
			public final void run() {
				final ContentValues values = new ContentValues();
				values.put( BudgetProvider.COL_AMT, str_amt);
				values.put( BudgetProvider.COL_LABEL, str_label.isEmpty() ? null : str_label);
				values.put( BudgetProvider.COL_DATE, System.currentTimeMillis());
				getContentResolver().update( ContentUris.withAppendedId( BudgetProvider.CONTENT_URI, id), values,
					BudgetProvider._ID + "=?", new String[] { "" + id});
			}
		}).start();
	}
	
	@Override
	public final void exAction() {
		new Thread( new Runnable() {
			public final void run() {
				getContentResolver().delete( ContentUris.withAppendedId( BudgetProvider.CONTENT_URI, id),
					BudgetProvider._ID + "=?", new String[] { "" + id});
			}
		}).start();
	}
}
