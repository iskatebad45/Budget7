package org.zen.budget;

import android.app.Activity;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.ContentUris;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.SimpleCursorAdapter.ViewBinder;
import android.widget.TextView;

public class MainActivity extends Activity implements LoaderCallbacks<Cursor>, OnClickListener, OnItemClickListener {
	
	private final String				TAG	= ".MainActivity";
	private static SimpleCursorAdapter	sca;
	private float						total;
	
	/**
	 * onCreate
	 * 
	 * @param savedInstanceState
	 *            The {@link Bundle bundle} of joy and info that has been saved
	 * @see android.app.Activity#onCreate(Bundle)
	 */
	@Override
	public void onCreate( Bundle savedInstanceState) {
		super.onCreate( savedInstanceState);
		setContentView( R.layout.main);
		Log.d( TAG, "in sca");
		// final Cursor c =
		// getContentResolver()
		// .query( BudgetProvider.CONTENT_URI, BudgetProvider.PROJECTION_ALL, null, null, null);
		sca =
				new SimpleCursorAdapter( this, R.layout.item, null, new String[] { BudgetProvider.COL_AMT},
						new int[] { R.id.tv_amt}, 0);
		Log.d( TAG, "out sca");
		ListView lv_list = (ListView)findViewById( android.R.id.list);
		lv_list.setAdapter( sca);
		lv_list.setOnItemClickListener( this);
		sca.setViewBinder( new ViewBinder() {
			public boolean setViewValue( View view, Cursor cursor, int columnIndex) {
				((TextView)view).setText( String.format( "%,+.2f", cursor.getFloat( columnIndex)));
				((TextView)view).setTypeface( Typeface.createFromAsset( getAssets(), "fonts/segoe.ttf"));
				return true;
			}
		});
		getLoaderManager().initLoader( 0, null, this);
		Log.d( TAG, "onCreate: " + total);
	}
	
	/**
	 * onStart
	 * 
	 * @see android.app.Activity#onStart()
	 */
	@Override
	protected final void onStart() {
		super.onStart();
		Log.d( TAG, "onStart: " + total);
	}
	
	/**
	 * onResume
	 * 
	 * @see android.app.Activity#onResume()
	 */
	protected final void onResume() {
		super.onResume();
		final Cursor c = getContentResolver().query( BudgetProvider.TOTAL_URI, null, null, null, null);
		c.moveToFirst();
		updateTotal( c.getFloat( 1));
		Log.d( TAG, "onResume: " + total);
	}
	
	/**
	 * onPause
	 * 
	 * @see android.app.Activity#onPause()
	 */
	@Override
	protected final void onPause() {
		super.onPause();
		total = 0.0f;
		Log.d( TAG, "onPause: " + total);
	}
	
	/**
	 * onStop
	 * 
	 * @see android.app.Activity#onStop()
	 */
	@Override
	protected final void onStop() {
		super.onStop();
		Log.d( TAG, "onStop: " + total);
	}
	
	/**
	 * onDestroy
	 * 
	 * @see android.app.Activity#onDestroy()
	 */
	@Override
	protected final void onDestroy() {
		super.onDestroy();
		Log.d( TAG, "onDestroy: " + total);
	}
	
	/**
	 * onClick callback for buttons
	 * 
	 * @param v
	 *            The view that was clicked
	 * @see android.view.View.OnClickListener#onClick(View)
	 */
	public void onClick( View v) {
		Log.d( TAG, "onClick");
		final Intent intent = new Intent( this, NewPanelActivity.class);
		switch( v.getId()) {
			case R.id.btn_add:
				intent.putExtra( PanelActivity.INTENT_EXTRA_IS_ADD, true);
				startActivityForResult( intent, Budget7.REQ_ADD);
				Log.d( TAG, "onClick: add");
				break;
			case R.id.btn_sub:
				intent.putExtra( PanelActivity.INTENT_EXTRA_IS_ADD, false);
				startActivityForResult( intent, Budget7.REQ_ADD);
				Log.d( TAG, "onClick: sub");
				break;
			default:
				Log.d( TAG, "onClick: found something else...");
				break;
		}
	}
	
	/**
	 * onActivityResult
	 * 
	 * @param requestCode
	 *            The request code
	 * @param resultCode
	 *            The result code
	 * @param data
	 *            The {@link android.content.Intent intent} that returned
	 * @see android.app.Activity#onActivityResult(int,int,Intent)
	 */
	@Override
	protected void onActivityResult( int requestCode, int resultCode, Intent data) {
		if( resultCode != RESULT_OK) {
			Log.d( TAG, "onActivityResult: bad result code");
		} else {
			switch( requestCode) {
				case Budget7.REQ_ADD:
				case Budget7.REQ_SUB:
					updateTotal( data.getExtras().getFloat( PanelActivity.INTENT_EXTRA_UPDATE, 0.0f));
					Log.d( TAG, "onActivityResult: add");
					break;
				default:
					Log.d( TAG, "onActivityResult: fatal error, found a result req that"
							+ " wasn't started by this activity");
					break;
			}
		}
	}
	
	/**
	 * onItemClick
	 * 
	 * @param parent
	 *            The parent {@link AdapterView ViewGroup}
	 * @param v
	 *            The view clicked
	 * @param pos
	 *            The view's position in the adapter
	 * @param id
	 *            The id of the view, relates to the position in a database
	 * @see android.widget.AdapterView.OnItemClickListener#onItemClick(AdapterView, View, int, long)
	 */
	public void onItemClick( AdapterView<?> parent, View v, int pos, long id) {
		final Cursor c =
				getContentResolver().query( ContentUris.withAppendedId( BudgetProvider.CONTENT_URI, id),
						BudgetProvider.PROJECTION_ALL, BudgetProvider._ID + "=?", new String[] { "" + id}, null);
		c.moveToFirst();
	}
	
	/**
	 * Convenience method for updating the total, displaying it, and changing the background color appropriately.
	 * 
	 * @param amt
	 *            The amount to include in the total
	 */
	private void updateTotal( final float amt) {
		Log.d( TAG, "updateTotal");
		total += amt;
		((TextView)findViewById( R.id.tv_total)).setText( total >= 0.0 ? String.format( "+$%,.2f", total) : String
				.format( "-$%,.2f", Math.abs( total)));
		final int updateColor =
				total >= 0.0 ? getResources().getColor( R.color.color_green) : getResources().getColor(
						R.color.color_red);
		((RelativeLayout)findViewById( R.id.rl_main)).setBackgroundColor( updateColor);
		((ListView)findViewById( android.R.id.list)).setCacheColorHint( updateColor);
	}
	
	/**
	 * onCreateLoader
	 * 
	 * @param id
	 *            The id of the loader
	 * @param args
	 *            A {@link Bundle bundle} of arguments
	 * @see android.app.LoaderManager.LoaderCallbacks#onCreateLoader(int, Bundle)
	 */
	public Loader<Cursor> onCreateLoader( int id, Bundle args) {
		Log.d( TAG, "onCreateLoader");
		return new CursorLoader( this, BudgetProvider.CONTENT_URI, new String[] { BudgetProvider._ID,
				BudgetProvider.COL_AMT}, null, null, null);
	}
	
	/**
	 * onLoadFinished
	 * 
	 * @param l
	 *            The {@link Loader loader} used
	 * @param c
	 *            The {@link Cursor cursor} that is finished
	 * @see android.app.LoaderManager.LoaderCallbacks#onLoadFinished(Loader, Object)
	 */
	public void onLoadFinished( Loader<Cursor> l, Cursor c) {
		Log.d( TAG, "onLoadFinished");
		sca.swapCursor( c);
	}
	
	/**
	 * onLoaderReset
	 * 
	 * @param l
	 *            The loader used
	 * @see android.app.LoaderManager.LoaderCallbacks#onLoaderReset(Loader)
	 */
	public void onLoaderReset( Loader<Cursor> l) {
		Log.d( TAG, "onLoaderReset");
		sca.swapCursor( null);
	}
}