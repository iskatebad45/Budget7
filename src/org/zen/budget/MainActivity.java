package org.zen.budget;

import android.app.Activity;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.ContentUris;
import android.content.ContentValues;
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
	
	private static final String			TAG			= ".MainActivity";
	private static final String[]		PROJECTION	= new String[] { BudgetProvider._ID, BudgetProvider.COL_AMT},
			FROM = new String[] { BudgetProvider.COL_AMT};
	private static final int[]			TO			= new int[] { R.id.tv_amt};
	private static SimpleCursorAdapter	sca;
	private static float				total		= 0.0f;
	private ListView					lv_list;
	private TextView					tv_total;
	private RelativeLayout				rl_main;
	
	// private ImageButton btn_cancel, btn_delete, btn_enter, btn_update;
	
	@Override
	public void onCreate( Bundle savedInstanceState) {
		super.onCreate( savedInstanceState);
		setContentView( R.layout.main);
		tv_total = (TextView)findViewById( R.id.tv_total);
		rl_main = (RelativeLayout)findViewById( R.id.rl_main);
		// btn_cancel = (ImageButton)findViewById( R.id.btn_panel_cancel);
		// btn_delete = (ImageButton)findViewById( R.id.btn_panel_delete);
		// btn_enter = (ImageButton)findViewById( R.id.btn_panel_enter);
		// btn_update = (ImageButton)findViewById( R.id.btn_panel_update);
		sca = new SimpleCursorAdapter( this, R.layout.item, null, FROM, TO, 0);
		lv_list = (ListView)findViewById( android.R.id.list);
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
		Log.d( TAG, "onCreate");
	}
	
	@Override
	protected final void onStart() {
		super.onStart();
		new Runnable() {
			public void run() {
				final Cursor c =
						getContentResolver().query( BudgetProvider.CONTENT_URI, new String[] { BudgetProvider.COL_AMT},
								null, null, null);
				final short i_a = (short)c.getColumnIndex( BudgetProvider.COL_AMT);
				float u = 0.0f;
				while( c.moveToNext()) {
					u += c.getFloat( i_a);
				}
				updateTotal( u);
			}
		}.run();
		Log.d( TAG, "onStart");
	}
	
	protected final void onResume() {
		super.onResume();
		sca.notifyDataSetChanged();
		Log.d( TAG, "onResume");
	}
	
	@Override
	protected final void onPause() {
		super.onPause();
		total = 0.0f;
		Log.d( TAG, "onPause");
	}
	
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
				intent.putExtra( PanelActivity.INTENT_EXTRA_IS_ADD, true);
				startActivityForResult( intent, Budget7.REQ_ADD);
				Log.d( TAG, "onClick: sub");
				break;
			default:
				Log.d( TAG, "onClick: found something else...");
				break;
		}
	}
	
	@Override
	protected void onActivityResult( int requestCode, int resultCode, Intent data) {
		switch( requestCode) {
			case Budget7.REQ_ADD:
				Log.d( TAG, "onActivityResult: add");
				break;
			case Budget7.REQ_SUB:
				Log.d( TAG, "onActivityResult: add");
				break;
			default:
				break;
		}
	}
	
	// private void showPanelButtons( PanelType type) {
	// et_label.setText( "");
	// et_amt.setText( "");
	// switch( type) {
	// case NEW:
	// rl_panel.setVisibility( View.VISIBLE);
	// btn_cancel.setVisibility( View.VISIBLE);
	// btn_delete.setVisibility( View.GONE);
	// btn_enter.setVisibility( View.VISIBLE);
	// btn_update.setVisibility( View.GONE);
	// break;
	// case UPDATE:
	// rl_panel.setVisibility( View.VISIBLE);
	// btn_cancel.setVisibility( View.GONE);
	// btn_delete.setVisibility( View.VISIBLE);
	// btn_enter.setVisibility( View.GONE);
	// btn_update.setVisibility( View.VISIBLE);
	// break;
	// case HIDE:
	// if( ((InputMethodManager)getSystemService( INPUT_METHOD_SERVICE)).isAcceptingText()) {
	// ((InputMethodManager)getSystemService( INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
	// et_amt.getWindowToken(), 0);
	// }
	// rl_panel.setVisibility( View.GONE);
	// btn_cancel.setVisibility( View.GONE);
	// btn_delete.setVisibility( View.GONE);
	// btn_enter.setVisibility( View.GONE);
	// btn_update.setVisibility( View.GONE);
	// break;
	// }
	// }
	
	public void onItemClick( AdapterView<?> parent, View v, int pos, long id) {
		final Cursor c =
				getContentResolver().query( ContentUris.withAppendedId( BudgetProvider.CONTENT_URI, id),
						BudgetProvider.PROJECTION_ALL, BudgetProvider._ID + "=?", new String[] { "" + id}, null);
		c.moveToFirst();
		// showPanelButtons( PanelType.UPDATE);
	}
	
	private void insert( final float amt) {
		Log.d( TAG, "insert");
		ContentValues values = new ContentValues();
		updateTotal( amt);
		values.put( BudgetProvider.COL_AMT, amt);
		values.put( BudgetProvider.COL_LABEL, "test");
		values.put( BudgetProvider.COL_DATE, System.currentTimeMillis());
		getContentResolver().insert( BudgetProvider.CONTENT_URI, values);
		sca.notifyDataSetChanged();
	}
	
	private void updateTotal( final float amt) {
		Log.d( TAG, "updateTotal");
		total += amt;
		tv_total.setText( total >= 0.0 ? String.format( "+$%,.2f", total) : String.format( "-$%,.2f", Math.abs( total)));
		final int updateColor =
				total >= 0.0 ? getResources().getColor( R.color.color_green) : getResources().getColor(
						R.color.color_red);
		rl_main.setBackgroundColor( updateColor);
		lv_list.setCacheColorHint( updateColor);
	}
	
	public Loader<Cursor> onCreateLoader( int id, Bundle args) {
		Log.d( TAG, "onCreateLoader");
		return new CursorLoader( this, BudgetProvider.CONTENT_URI, PROJECTION, null, null, null);
	}
	
	public void onLoadFinished( Loader<Cursor> l, Cursor c) {
		Log.d( TAG, "onLoadFinished");
		sca.swapCursor( c);
	}
	
	public void onLoaderReset( Loader<Cursor> l) {
		Log.d( TAG, "onLoaderReset");
		sca.swapCursor( null);
	}
}

/*
 * case R.id.btn_panel_enter:
 * Log.d( TAG, "onClick: enter");
 * final String amt = et_amt.getText().toString();
 * if( !amt.isEmpty()) {
 * insert( signed_for_add ? 1.0f * Float.parseFloat( amt) : -1.0f * Float.parseFloat( amt));
 * } else {
 * Toast.makeText( this, R.string.toast_need_amt, Toast.LENGTH_LONG).show();
 * }
 * showPanelButtons( PanelType.HIDE);
 * case R.id.btn_panel_cancel:
 * Log.d( TAG, "onClick: cancel");
 * showPanelButtons( PanelType.HIDE);
 * break;
 * case R.id.btn_panel_delete:
 * if( del_id != -1) {
 * final Cursor c =
 * getContentResolver().query(
 * ContentUris.withAppendedId( BudgetProvider.CONTENT_URI, del_id),
 * BudgetProvider.PROJECTION_ALL, BudgetProvider._ID + "=?",
 * new String[] { "" + del_id}, null);
 * c.moveToFirst();
 * updateTotal( -c.getFloat( c.getColumnIndex( BudgetProvider.COL_AMT)));
 * final int r =
 * getContentResolver().delete(
 * ContentUris.withAppendedId( BudgetProvider.CONTENT_URI, del_id),
 * BudgetProvider._ID + "=?", new String[] { "" + del_id});
 * Log.d( TAG, "onClick: delete: " + r);
 * }
 * sca.notifyDataSetChanged();
 * del_id = -1;
 * showPanelButtons( PanelType.HIDE);
 * break;
 * case R.id.btn_panel_update:
 * Log.d( TAG, "onClick: update");
 * final ContentValues values = new ContentValues();
 * final Cursor c =
 * getContentResolver().query( ContentUris.withAppendedId( BudgetProvider.CONTENT_URI, del_id),
 * BudgetProvider.PROJECTION_ALL, BudgetProvider._ID + "=?", new String[] { "" + del_id},
 * null);
 * c.moveToFirst();
 * updateTotal( Float.parseFloat( et_amt.getText().toString())
 * - c.getFloat( c.getColumnIndex( BudgetProvider.COL_AMT)));
 * values.put( BudgetProvider.COL_AMT, et_amt.getText().toString());
 * values.put( BudgetProvider.COL_LABEL, et_label.getText().toString());
 * values.put( BudgetProvider.COL_DATE, System.currentTimeMillis());
 * getContentResolver().update( ContentUris.withAppendedId( BudgetProvider.CONTENT_URI, del_id), values,
 * BudgetProvider._ID + "=?", new String[] { "" + del_id});
 * showPanelButtons( PanelType.HIDE);
 * break;
 */