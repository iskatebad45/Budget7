package org.zen.budget;

import android.app.Activity;
import android.app.LoaderManager.LoaderCallbacks;
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
	
	private final String TAG = ".MainActivity";
	private SimpleCursorAdapter lv_sca;
	
	/**
	 * onCreate, test AIDE on android!
	 * 
	 * @param savedInstanceState
	 *        The {@link Bundle bundle} of joy and info that has been saved
	 * @see android.app.Activity#onCreate(Bundle)
	 */
	@Override
	public void onCreate( Bundle savedInstanceState) {
		super.onCreate( savedInstanceState);
		setContentView( R.layout.main);
		lv_sca =
				new SimpleCursorAdapter( this, R.layout.item, null, new String[] { BudgetProvider.COL_AMT},
					new int[] { R.id.tv_amt}, 0);
		final ListView lv_list = (ListView)findViewById( android.R.id.list);
		lv_list.setAdapter( lv_sca);
		lv_list.setOnItemClickListener( this);
		lv_sca.setViewBinder( new ViewBinder() {
			public boolean setViewValue( View view, Cursor cursor, int columnIndex) {
				((TextView)view).setText( String.format( "%,+.2f", cursor.getFloat( columnIndex)));
				((TextView)view).setTypeface( Typeface.createFromAsset( getAssets(), "fonts/segoe.ttf"));
				return true;
			}
		});
		getLoaderManager().initLoader( 0, null, this);
	}
	
	/**
	 * onResume
	 * 
	 * @see android.app.Activity#onResume()
	 */
	protected final void onResume() {
		super.onResume();
		updateTotal();
	}
	
	/**
	 * onClick callback for buttons
	 * 
	 * @param v
	 *        The view that was clicked
	 * @see android.view.View.OnClickListener#onClick(View)
	 */
	public void onClick( final View v) {
		final Intent intent = new Intent( this, NewPanelActivity.class);
		switch( v.getId()) {
			case R.id.btn_add:
				intent.putExtra( PanelActivity.INTENT_EXTRA_IS_ADD, true);
				startActivity( intent);
				break;
			case R.id.btn_sub:
				intent.putExtra( PanelActivity.INTENT_EXTRA_IS_ADD, false);
				startActivity( intent);
				break;
			default:
				Log.d( TAG, "onClick: found something else...");
				break;
		}
	}
	
	/**
	 * onItemClick
	 * 
	 * @param parent
	 *        The parent {@link AdapterView ViewGroup}
	 * @param v
	 *        The view clicked
	 * @param pos
	 *        The view's position in the adapter
	 * @param id
	 *        The id of the view, relates to the position in a database
	 * @see android.widget.AdapterView.OnItemClickListener#onItemClick(AdapterView, View, int, long)
	 */
	public void onItemClick( AdapterView<?> parent, View v, int pos, long id) {
		final Intent intent = new Intent( this, UpdatePanelActivity.class);
		intent.putExtra( PanelActivity.INTENT_EXTRA_PANEL_ID, id);
		startActivity( intent);
	}
	
	/**
	 * Convenience method for updating the total, displaying it, and changing the background color appropriately.
	 * 
	 * @param amt
	 *        The amount to include in the total
	 */
	private void updateTotal() {
		final Cursor c = getContentResolver().query( BudgetProvider.TOTAL_URI, new String[] { "ALL"}, null, null, null);
		c.moveToFirst();
		final float total = c.getFloat( 1);
		((TextView)findViewById( R.id.tv_total)).setText( total >= 0.0 ? String.format( "+$%,.2f", total) : String.format(
			"-$%,.2f", Math.abs( total)));
		final int updateColor =
				total >= 0.0 ? getResources().getColor( R.color.color_green) : getResources().getColor( R.color.color_red);
		((RelativeLayout)findViewById( R.id.rl_main)).setBackgroundColor( updateColor);
		((ListView)findViewById( android.R.id.list)).setCacheColorHint( updateColor);
	}
	
	/**
	 * onCreateLoader
	 * 
	 * @param id
	 *        The id of the loader
	 * @param args
	 *        A {@link Bundle bundle} of arguments
	 * @see android.app.LoaderManager.LoaderCallbacks#onCreateLoader(int, Bundle)
	 */
	public Loader<Cursor> onCreateLoader( final int id, final Bundle args) {
		return new CursorLoader( this, BudgetProvider.CONTENT_URI, new String[] { BudgetProvider._ID, BudgetProvider.COL_AMT},
			null, null, null);
	}
	
	/**
	 * onLoadFinished
	 * 
	 * @param l
	 *        The {@link Loader loader} used
	 * @param c
	 *        The {@link Cursor cursor} that is finished
	 * @see android.app.LoaderManager.LoaderCallbacks#onLoadFinished(Loader, Object)
	 */
	public void onLoadFinished( final Loader<Cursor> l, final Cursor c) {
		lv_sca.swapCursor( c);
	}
	
	/**
	 * onLoaderReset
	 * 
	 * @param l
	 *        The loader used
	 * @see android.app.LoaderManager.LoaderCallbacks#onLoaderReset(Loader)
	 */
	public void onLoaderReset( final Loader<Cursor> l) {
		lv_sca.swapCursor( null);
	}
}
