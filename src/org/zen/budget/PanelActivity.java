package org.zen.budget;

import android.app.Activity;
import android.os.Bundle;

public abstract class PanelActivity extends Activity implements PanelInterface {
	@SuppressWarnings( "unused")
	private static final String		TAG						= ".PanelActivity";
	protected static final String	INTENT_EXTRA_IS_ADD		= "is_add";
	protected static final String	INTENT_EXTRA_PANEL_ID	= "panel_id";
	
	public abstract void checkAction();
	
	public abstract void exAction();
	
	public void onCreate( Bundle savedInstanceState) {
		super.onCreate( savedInstanceState);
	}
	
	public PanelActivity() {
		
	}
}
