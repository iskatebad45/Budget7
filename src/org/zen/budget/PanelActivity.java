package org.zen.budget;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;

public abstract class PanelActivity extends Activity implements PanelInterface, OnClickListener {
	private final String TAG = ".PanelActivity";
	protected static final String INTENT_EXTRA_IS_ADD = "is_add", INTENT_EXTRA_PANEL_ID = "panel_id",
			INTENT_EXTRA_UPDATE = "update_total_amt";
	
	/**
	 * Abstract because this is the functionality that subclasses should extend.
	 * 
	 * @see org.zen.budget.PanelInterface#checkAction()
	 */
	public abstract void checkAction();
	
	/**
	 * Abstract because this is the functionality that subclasses should extend.
	 * 
	 * @see org.zen.budget.PanelInterface#exAction()
	 */
	public abstract void exAction();
	
	/**
	 * Not final because sometimes the panel needs to be set up
	 * 
	 * @see android.app.Activity#onCreate()
	 */
	public void onCreate( final Bundle savedInstanceState) {
		super.onCreate( savedInstanceState);
		setContentView( R.layout.panel);
		((ImageButton)findViewById( R.id.btn_panel_check)).setOnClickListener( this);
		((ImageButton)findViewById( R.id.btn_panel_ex)).setOnClickListener( this);
		final Typeface segoe = Typeface.createFromAsset( getAssets(), "fonts/segoe.ttf");
		((EditText)findViewById( R.id.et_panel_amt)).setTypeface( segoe);
		((EditText)findViewById( R.id.et_panel_label)).setTypeface( segoe);
	}
	
	public final void onClick( final View v) {
		switch( v.getId()) {
			case R.id.btn_panel_check:
				checkActionWrapper();
				break;
			case R.id.btn_panel_ex:
				exActionWrapper();
				break;
			default:
				break;
		}
	}
	
	/**
	 * Guarantees the activity finishes after this call. Final because this shouldn't be changed by subclasses.
	 */
	private final void checkActionWrapper() {
		Log.d( TAG, "checkActionWrapper");
		checkAction();
		finish();
	}
	
	/**
	 * Guarantees the activity finishes after this call. Final because this shouldn't be changed by subclasses.
	 */
	private final void exActionWrapper() {
		Log.d( TAG, "exActionWrapper");
		exAction();
		finish();
	}
}
