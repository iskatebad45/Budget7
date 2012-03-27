package org.zen.budget;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

public abstract class PanelActivity extends Activity implements PanelInterface {
	private static final String		TAG						= ".PanelActivity";
	protected static final String	INTENT_EXTRA_IS_ADD		= "is_add";
	protected static final String	INTENT_EXTRA_PANEL_ID	= "panel_id";
	
	public abstract void checkAction();
	
	public abstract void exAction();
	
	/*
	 * Final because the only functionality extended is what happens on each button click
	 */
	public final void onCreate( final Bundle savedInstanceState) {
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
	
	/*
	 * Guarantees the activity finishes after this call
	 * Final because this shouldn't be changed by subclasses
	 */
	private final void checkActionWrapper() {
		checkAction();
		setResult( RESULT_OK, null);
		finish();
	}
	
	/*
	 * Guarantees the activity finishes after this call
	 * Final because this shouldn't be changed by subclasses
	 */
	private final void exActionWrapper() {
		exAction();
		setResult( RESULT_OK, null);
		finish();
	}
}
