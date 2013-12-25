package fucverg.saulmm.gdg.gui.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.AbsListView;
import android.widget.ListView;

public class PullListView extends ListView implements AbsListView.OnScrollListener {
	private float startY;
	private boolean isInTheFirstItem = true;
	private PullRefreshListener listener;
	private float dragLimit = 601f;
	private int scrollState;
	private int mLastFirstVisibleItem;
	private boolean mIsScrollingUp;


	public PullListView (Context context) {
		super(context);
		init();
	}


	public PullListView (Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}


	public PullListView (Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}


	private void init () {
		this.setOnScrollListener(this);
	}


	public void setDragLimit (float dragLimit) {
		this.dragLimit = dragLimit;
	}


	public void setListener (PullRefreshListener listener) {
		this.listener = listener;
	}



	@Override
	public boolean onTouchEvent (MotionEvent ev) {
		float y = ev.getY();

		switch (ev.getAction()) {
			case MotionEvent.ACTION_MOVE:
				if (isInTheFirstItem && (startY < y)) {
					float ratio = y - startY;

					if (ratio <= dragLimit && ratio >= 0 && // Accurate the drag limit
						listener != null &&  // Check if a listener is up
						scrollState != OnScrollListener.SCROLL_STATE_FLING ) {// Prevent thrown scrolls


						float percent = (ratio * 100f) / dragLimit;

						if (percent <= 100)
							listener.onRefresh(percent + 1);
					}

				}

				break;

			case MotionEvent.ACTION_DOWN:
				startY = ev.getY();
				isInTheFirstItem = getFirstVisiblePosition() == 0; // We are on the first element so we can enable refresh;
				break;

			case MotionEvent.ACTION_UP:
				listener.onUp();
				break;
		}

		return super.onTouchEvent(ev);

	}


	@Override
	public void onScrollStateChanged (AbsListView absListView, int i) {
		switch (i) {
			case SCROLL_STATE_FLING:
				scrollState = SCROLL_STATE_FLING;
				break;
			
			case SCROLL_STATE_IDLE:
				scrollState = SCROLL_STATE_IDLE;
				break;
			
			case SCROLL_STATE_TOUCH_SCROLL:
				scrollState = SCROLL_STATE_TOUCH_SCROLL;
				break;
		}
	}


	@Override
	public void onScroll (AbsListView absListView, int i, int i2, int i3) {}


}
