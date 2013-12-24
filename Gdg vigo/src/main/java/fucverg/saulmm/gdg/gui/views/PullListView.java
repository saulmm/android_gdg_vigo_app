package fucverg.saulmm.gdg.gui.views;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ListView;

public class PullListView extends ListView {
	private float startY;
	private boolean isInTheFirstItem = true;
	private PullRefreshListener listener;
	private float dragLimit = 601f;


	public PullListView (Context context) {
		super(context);
	}


	public PullListView (Context context, AttributeSet attrs) {
		super(context, attrs);
	}


	public PullListView (Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}


	public void setDragLimit (float dragLimit) {
		this.dragLimit = dragLimit;
	}


	public void setListener (PullRefreshListener listener) {
		this.listener = listener;
	}


	float percent = 0;
	@Override
	public boolean onTouchEvent (MotionEvent ev) {
		float y = ev.getY();

			switch (ev.getAction()) {
				case MotionEvent.ACTION_MOVE:

					if (isInTheFirstItem) {
						float ratio = y - startY;

						if (ratio <= dragLimit && listener != null && ratio >= 0) {
							percent = (ratio * 100f) / dragLimit;
							Log.d("[DEBUG] fucverg.saulmm.gdg.gui.views.PullListView.onTouchEvent ",
									"Percent: " + percent + " %");
							listener.onRefresh(percent + 1);
						}

					}

					break;

				case MotionEvent.ACTION_DOWN:
					startY = ev.getY();
					isInTheFirstItem = getFirstVisiblePosition() == 0; // We are on the first element so we can enable refresh;


				case MotionEvent.ACTION_UP:

					if(percent < 99 && percent > 0)
						listener.onUp();

			}

		return super.onTouchEvent(ev);

	}
}
