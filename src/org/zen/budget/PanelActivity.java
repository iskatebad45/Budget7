package org.zen.budget;

import android.app.Fragment;

public abstract class PanelActivity extends Fragment implements PanelInterface {
	private static final String	TAG	= ".PanelActivity";
	
	public abstract void checkAction();
	
	public abstract void exAction();
}
