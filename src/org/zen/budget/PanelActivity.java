package org.zen.budget;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;

public abstract class PanelActivity extends Activity implements PanelInterface {
	private static final String TAG = ".PanelActivity";
	protected static final String INTENT_EXTRA_IS_ADD = "is_add", INTENT_EXTRA_PANEL_ID = "panel_id",
			INTENT_EXTRA_UPDATE = "update_total_amt";
	
	/**
	 * Abstract because this is the functionality that subclasses should extend.
	 * 
	 * @see org.zen.budget.PanelInterface#checkAction()
	 */
	public abstract float checkAction();
	
	/**
	 * Abstract because this is the functionality that subclasses should extend.
	 * 
	 * @see org.zen.budget.PanelInterface#exAction()
	 */
	public abstract float exAction();
	
	/**
	 * Not final because sometimes the panel needs to be set up
	 * 
	 * @see android.app.Activity#onCreate()
	 */
	public void onCreate( final Bundle savedInstanceState) {
		super.onCreate( savedInstanceState);
		setContentView( R.layout.panel);
		((ImageButton)findViewById( R.id.btn_panel_check)).setOnClickListener( new OnClickListener() {
			@Override
			public void onClick( final View v) {
				if( v.getId() == R.id.btn_panel_check) {
					checkActionWrapper();
				} else {
					Log.d( TAG, "onClick: not btn_panel_ex, this should be unreachable but it's not!");
				}
			}
		});
		((ImageButton)findViewById( R.id.btn_panel_ex)).setOnClickListener( new OnClickListener() {
			@Override
			public void onClick( final View v) {
				if( v.getId() == R.id.btn_panel_ex) {
					exActionWrapper();
				} else {
					Log.d( TAG, "onClick: not btn_panel_ex, this should be unreachable but it's not!");
				}
			}
		});
		final Typeface segoe = Typeface.createFromAsset( getAssets(), "fonts/segoe.ttf");
		((EditText)findViewById( R.id.et_panel_amt)).setTypeface( segoe);
		((EditText)findViewById( R.id.et_panel_label)).setTypeface( segoe);
	}
	
	/**
	 * Guarantees the activity finishes after this call. Final because this shouldn't be changed by subclasses.
	 */
	private final void checkActionWrapper() {
		Log.d( TAG, "checkActionWrapper");
		final Intent i = new Intent();
		i.putExtra( INTENT_EXTRA_UPDATE, checkAction());
		setResult( RESULT_OK, i);
		finish();
	}
	
	/**
	 * Guarantees the activity finishes after this call. Final because this shouldn't be changed by subclasses.
	 */
	private final void exActionWrapper() {
		Log.d( TAG, "exActionWrapper");
		final Intent i = new Intent();
		i.putExtra( INTENT_EXTRA_UPDATE, exAction());
		setResult( RESULT_OK, i);
		finish();
	}
}
