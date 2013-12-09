package fucverg.saulmm.gdg.gui.adapters;

import android.content.Context;
import android.graphics.*;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.bitmap.Transform;
import fucverg.saulmm.gdg.R;
import fucverg.saulmm.gdg.data.db.entities.Member;

import java.util.ArrayList;

public class MembersAdapter extends ArrayAdapter<Member> {
	private final Context context;
	private final ArrayList<Member> members;

	public MembersAdapter (Context context, ArrayList<Member> members) {
		super(context, R.layout.fragment_member, members);
		this.context = context;
		this.members = members;
	}

	static class ViewHolder {
		public ImageView image;
		public TextView name;
		public TextView occupation;
	}


	@Override
	public View getView (int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		Member currentEvent = members.get(position);

		if (convertView == null) {

			convertView = LayoutInflater.from(context).inflate(R.layout.item_member, parent, false);

			holder = new ViewHolder();
			holder.name = (TextView) convertView.findViewById(R.id.im_name);
			holder.occupation = (TextView) convertView.findViewById(R.id.im_occupation);
			holder.image = (ImageView) convertView.findViewById(R.id.im_image);

			convertView.setTag(holder);

		} else {

			holder = (ViewHolder) convertView.getTag();
		}

		holder.name.setText(currentEvent.getName());
		holder.occupation.setText(currentEvent.getOccupation());
		String imageURL = "http:" + currentEvent.getImage();

		Log.d("[DEBUG] fucverg.saulmm.gdg.gui.adapters.MembersAdapter.getView ", "Image: " + holder.image);

		Ion.with(context, imageURL)
			.withBitmap()
			.transform(new RoundedTransformation(5,0))
			.intoImageView(holder.image);


		return convertView;
	}
}


// enables hardware accelerated rounded corners
// original idea here : http://www.curious-creature.org/2012/12/11/android-recipe-1-image-with-rounded-corners/
class RoundedTransformation implements Transform{
	private final int radius;
	private final int margin;  // dp

	// radius is corner radii in dp
	// margin is the board in dp
	public RoundedTransformation(final int radius, final int margin) {
		this.radius = radius;
		this.margin = margin;
	}

	@Override
	public Bitmap transform(final Bitmap source) {
		final Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setShader(new BitmapShader(source, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));


		Paint paintBorder = new Paint();
		paintBorder.setColor(Color.WHITE);
		paintBorder.setAntiAlias(true);
		paintBorder.setShadowLayer(2.0f, 1.0f, 1.0f, Color.DKGRAY);

		Bitmap output = Bitmap.createBitmap(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(output);
		canvas.drawRoundRect(new RectF(margin, margin, source.getWidth() + 100, source.getHeight() + 100), radius, radius, paintBorder);
		canvas.drawRoundRect(new RectF(margin, margin, source.getWidth() - margin, source.getHeight() - margin), radius, radius, paint);


		if (source != output) {
			source.recycle();
		}

		return output;
	}

	@Override
	public String key() {
		return "rounded";
	}
}
