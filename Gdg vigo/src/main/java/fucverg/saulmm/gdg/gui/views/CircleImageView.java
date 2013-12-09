package fucverg.saulmm.gdg.gui.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.*;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Reference: https://github.com/wisemandesigns/CircularImageView/blob/master/CircularImageView.java
 */
public class CircleImageView extends ImageView {
	private int borderWidth = 5;
	private int viewWidth;
	private int viewHeight;
	private Bitmap image;
	private Paint paint;
	private Paint paintBorder;
	private BitmapShader shader;


	public CircleImageView (Context context) {
		super(context);
		modify();
	}


	public CircleImageView (Context context, AttributeSet attrs) {
		super(context, attrs);
		modify();
	}


	public CircleImageView (Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		modify();
	}


	private void modify () {

		// init paint
		paint = new Paint();
		paint.setAntiAlias(true);

		paintBorder = new Paint();
		setBorderColor(Color.WHITE);
		paintBorder.setAntiAlias(true);
		this.setLayerType(LAYER_TYPE_SOFTWARE, paintBorder);
		paintBorder.setShadowLayer(2.0f, 0.0f, 1.0f, Color.DKGRAY);
	}


	public void setBorderColor (int borderColor) {

		if(paintBorder != null) {
			paintBorder.setColor(borderColor);
		}

		// Redraw the whole view calling to the onDraw method
		this.invalidate();
	}


	public void setBorderWidth (int borderWidth) {
		this.borderWidth = borderWidth;
		this.invalidate();
	}


	private void loadBitmap() {
		BitmapDrawable bitmapDrawable = (BitmapDrawable) this.getDrawable();

		if(bitmapDrawable != null)
			image = bitmapDrawable.getBitmap();
	}


	@SuppressLint("DrawAllocation")
	@Override
	protected void onDraw (Canvas canvas) {
		loadBitmap();

		// creates the shader, the shader is used to draw
		// a bitmap as a texture.
		if(image != null) {
			shader = new BitmapShader(Bitmap.createScaledBitmap(
					image, canvas.getWidth(), canvas.getHeight(), false),
					Shader.TileMode.CLAMP,
					Shader.TileMode.CLAMP);

			paint.setShader(shader);
			int circleCenter = viewWidth / 2;

			canvas.drawCircle(circleCenter + borderWidth,
					circleCenter + borderWidth,
					circleCenter + borderWidth,
					paintBorder);

			canvas.drawCircle(circleCenter + borderWidth,
					circleCenter + borderWidth,
					circleCenter, paint);
		}
	}


	@Override
	protected void onMeasure (int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		int width = measureWidth(widthMeasureSpec);
		int height = measureHeight(heightMeasureSpec, widthMeasureSpec);

		viewWidth = width - (borderWidth * 2);
		viewHeight = height - (borderWidth * 2);

		setMeasuredDimension(width, height);
	}


	private int measureWidth (int measureSpec) {
		int result = 0;
		int specMode = MeasureSpec.getMode(measureSpec);
		int specSize = MeasureSpec.getSize(measureSpec);

		if (specMode == MeasureSpec.EXACTLY) {
			result = specSize;

		} else {
			result = viewWidth;
		}

		return result;
	}


	private int measureHeight (int measureSpecHeight, int measureSpecWidth) {
		int result = 0;

		int specMode = MeasureSpec.getMode(measureSpecHeight);
		int specSize = MeasureSpec.getSize(measureSpecHeight);

		if (specMode == MeasureSpec.EXACTLY) {
			result = specSize;

		} else {
			result = viewHeight;
		}

		return result;
	}
}
