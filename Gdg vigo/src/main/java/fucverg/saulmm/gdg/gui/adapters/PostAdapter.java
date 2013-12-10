package fucverg.saulmm.gdg.gui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import fucverg.saulmm.gdg.R;
import fucverg.saulmm.gdg.data.db.entities.plus_activity_entities.*;
import fucverg.saulmm.gdg.gui.views.RoundedTransformation;

import java.util.List;

public class PostAdapter extends ArrayAdapter<Activity> {
	private final Context context;
	private final List<Activity> activities;
	private final RoundedTransformation imvTransform;

	public PostAdapter (Context context, List<Activity> activities) {
		super(context, R.layout.fragment_posts, activities);
		this.imvTransform = new RoundedTransformation(100);
		this.activities = activities;
		this.context = context;
	}

	static class ViewHolder {
		public ImageView image;
		public ImageView icon;
		public TextView name;
		public TextView title;
		public TextView content;
		public TextView date;
		public TextView type;
	}


	@Override
	public View getView (int position, View convertView, ViewGroup parent) {
		Activity currentActivity = activities.get(position);

		ViewHolder holder;

		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.item_post, parent, false);
			holder = new ViewHolder();

			holder.icon = (ImageView) convertView.findViewById(R.id.ip_icon);
			holder.image = (ImageView) convertView.findViewById(R.id.ip_member_image);
			holder.type = (TextView) convertView.findViewById(R.id.ip_type);
			holder.date = (TextView) convertView.findViewById(R.id.ip_date);
			holder.title = (TextView) convertView.findViewById(R.id.ip_title);
			holder.content = (TextView) convertView.findViewById(R.id.ip_content);

			convertView.setTag(holder);

		} else
			holder = (ViewHolder) convertView.getTag();


		holder.title.setText(currentActivity.getTitle());

		holder.content.setText(currentActivity.getContent_description());
		holder.type.setText(currentActivity.getContent_type());

//		Ion.with(context, imageURL)
//			.withBitmap()
//			.transform(imvTransform)
//			.intoImageView(holder.image);


		return convertView;
	}
}


