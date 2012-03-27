package org.zen.budget;

import android.util.Log;

public class UpdatePanelActivity extends PanelActivity {
	private static final String	TAG	= ".UpdatePanelActivity";
	
	@Override
	public void checkAction() {
		Log.d( TAG, "checkAction");
	}
	
	@Override
	public void exAction() {
		Log.d( TAG, "exAction");
	}
}
