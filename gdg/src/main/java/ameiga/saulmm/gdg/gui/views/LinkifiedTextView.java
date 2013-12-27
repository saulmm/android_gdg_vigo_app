package ameiga.saulmm.gdg.gui.views;

import android.content.Context;
import android.text.Layout;
import android.text.Selection;
import android.text.Spannable;
import android.text.Spanned;
import android.text.style.ClickableSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;


/**
 * Solution by weakwire:
 * http://stackoverflow.com/questions/7236840/android-textview-linkify-intercepts-with-parent-view-gestures
 *
 * Necessary to handle gesture methods in 'linkified' TextViews, in this case
 * is necessary to the correct implementation of the pull to refresh feature.
 */
@SuppressWarnings("UnusedDeclaration")
public class LinkifiedTextView extends TextView implements View.OnTouchListener {

	public LinkifiedTextView (Context context) {
		super(context);
	}

	public LinkifiedTextView (Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public LinkifiedTextView (Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}


	@Override
	public boolean onTouchEvent (MotionEvent event) {
		try {
			TextView widget = this;
			Object text = widget.getText();
			if (text instanceof Spanned) {
				Spannable buffer = (Spannable) text;

				int action = event.getAction();

				if (action == MotionEvent.ACTION_UP
						|| action == MotionEvent.ACTION_DOWN) {
					int x = (int) event.getX();
					int y = (int) event.getY();

					x -= widget.getTotalPaddingLeft();
					y -= widget.getTotalPaddingTop();

					x += widget.getScrollX();
					y += widget.getScrollY();

					Layout layout = widget.getLayout();
					int line = layout.getLineForVertical(y);
					int off = layout.getOffsetForHorizontal(line, x);

					ClickableSpan[] link = buffer.getSpans(off, off,
							ClickableSpan.class);

					if (link.length != 0) {
						if (action == MotionEvent.ACTION_UP) {
							link[0].onClick(widget);
						} else if (action == MotionEvent.ACTION_DOWN) {
							Selection.setSelection(buffer,
									buffer.getSpanStart(link[0]),
									buffer.getSpanEnd(link[0]));
						}
						return true;
					}
				}

			}
		} catch (ClassCastException e) {
			Log.e("[ERROR] fucverg.saulmm.gdg.gui.views.LinkifiedTextView.onTouchEvent ",
					"Error casting the link..");
		}

		return false;
	}


	@Override
	public boolean onTouch (View view, MotionEvent motionEvent) {
		return false;
	}
}

