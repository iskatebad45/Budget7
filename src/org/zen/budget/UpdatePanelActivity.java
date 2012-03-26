package org.zen.budget;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
	
	@Override
	public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Log.d( TAG, "onCreateVeiw");
		return inflater.inflate( R.layout.update_panel_frag, container, false);
	}
}
