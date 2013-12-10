package fucverg.saulmm.gdg.gui.views;

import android.graphics.*;
import com.koushikdutta.ion.bitmap.Transform;

// enables hardware accelerated rounded corners
// original idea here : http://www.curious-creature.org/2012/12/11/android-recipe-1-image-with-rounded-corners/
public class RoundedTransformation implements Transform {
	private final int radius;
	private final int margin;  // dp

	// radius is corner radii in dp
	// margin is the board in dp
	public RoundedTransformation(final int radius) {
		this.radius = radius;
		this.margin = 0;
	}

	@Override
	public Bitmap transform(final Bitmap bitmap) {
		final Paint paint = new Paint();
		paint.setAntiAlias(true);

		BitmapShader shader = new BitmapShader(bitmap,
				Shader.TileMode.CLAMP,
				Shader.TileMode.CLAMP);

		paint.setShader(shader);


		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(output);
		canvas.drawRoundRect(new RectF(0, 0,
				bitmap.getWidth(),
				bitmap.getHeight()),
				radius, radius, paint);


		if (bitmap != output) {
			bitmap.recycle();
		}

		return output;
	}

	@Override
	public String key() {
		return "rounded";
	}
}
