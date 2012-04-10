package org.zen.budget;

public interface PanelInterface {
	
	/**
	 * This does what should happen when the button with a check mark is pressed
	 * 
	 * @return The amount to update the total with (0.0f if no change).
	 */
	void checkAction();
	
	/**
	 * This does what should happen when the button with an 'X' (ex) is pressed
	 * 
	 * @return The amount to update the total with (0.0f if no change).
	 */
	void exAction();
}
