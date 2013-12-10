package fucverg.saulmm.gdg.gui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.koushikdutta.ion.Ion;
import fucverg.saulmm.gdg.R;
import fucverg.saulmm.gdg.data.db.entities.Member;
import fucverg.saulmm.gdg.gui.views.RoundedTransformation;

import java.util.List;

public class MembersAdapter extends ArrayAdapter<Member> {
	private final Context context;
	private final List<Member> members;
	private final RoundedTransformation imvTransform;

	public MembersAdapter (Context context, List<Member> members) {
		super(context, R.layout.fragment_member, members);
		this.context = context;
		this.members = members;
		this.imvTransform = new RoundedTransformation(100);
	}

	static class ViewHolder {
		public ImageView image;
		public TextView name;
		public TextView occupation;
	}


	@Override
	public View getView (int position, View convertView, ViewGroup parent) {
		// Viewholder pattern
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

		Ion.with(context, imageURL)
			.withBitmap()
			.transform(imvTransform)
			.intoImageView(holder.image);


		return convertView;
	}
}


