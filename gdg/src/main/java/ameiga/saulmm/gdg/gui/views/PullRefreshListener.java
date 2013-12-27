package ameiga.saulmm.gdg.gui.views;

/**
 * Interface to manage the PullToRefresh callbacks
 */
public interface PullRefreshListener {

	/**
	 * Its fired when the user is pulling down.
	 * @param percent the percentage of how many the user pulled down
	 */
	void onRefresh (float percent);

	/**
	 * Its fired when the user lifts the finger from the screen.
	 */
	void onUp ();
}
