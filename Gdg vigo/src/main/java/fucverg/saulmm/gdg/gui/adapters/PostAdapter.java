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
import fucverg.saulmm.gdg.data.db.DBHandler;
import fucverg.saulmm.gdg.data.db.entities.Member;
import fucverg.saulmm.gdg.data.db.entities.plus_activity_entities.Activity;
import fucverg.saulmm.gdg.utils.GuiUtils;
import fucverg.saulmm.gdg.gui.views.RoundedTransformation;

import java.util.List;

import static android.util.Log.e;

public class PostAdapter extends ArrayAdapter<Activity> {
	private final Context context;
	private final List<Activity> activities;
	private final RoundedTransformation imvTransform;
	private String plusURL;
	private final DBHandler dbHandler;


	public PostAdapter (Context context, List<Activity> activities) {
		super(context, R.layout.fragment_posts, activities);
		this.imvTransform = new RoundedTransformation(100);
		this.activities = activities;
		this.context = context;
		this.dbHandler = new DBHandler(context);


		GuiUtils.GUI_DB_HANDLER = this.dbHandler;
//		plusPattern = Pattern.compile("\\+(\\w+[ \\w+]+)");
//		plusURL = "https://plus.google.com/";
//
//		plusTransformFilter = new Linkify.TransformFilter() {
//			@Override
//			public String transformUrl (Matcher matcher, String url) {
//
//				String personName = matcher.group(1);
//				Member member = dbHandler.getMemberByName(personName);
//
//				if (member != null)
//					url = member.getId();
//
//				else {
//					url = "u/0/s/" + personName.replace(" ", "%20");
//				}
//
//				Log.d("[DEBUG] fucverg.saulmm.gdg.gui.adapters.PostAdapter.transformUrl ",
//						"The result of the url is: " + plusURL + url);
//
//				return url;
//			}
//
//
//		};
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

//			holder.icon = (ImageView) convertView.findViewById(R.id.ip_icon);
			holder.image = (ImageView) convertView.findViewById(R.id.ip_member_image);
//			holder.type = (TextView) convertView.findViewById(R.id.ip_type);
			holder.date = (TextView) convertView.findViewById(R.id.ip_date);
			holder.name = (TextView) convertView.findViewById(R.id.ip_member_name);
			holder.title = (TextView) convertView.findViewById(R.id.ip_title);
			holder.content = (TextView) convertView.findViewById(R.id.ip_content);


			convertView.setTag(holder);

		} else
			holder = (ViewHolder) convertView.getTag();



		holder.title.setText(currentActivity.getTitle());
		GuiUtils.addAllLinksLinkify(holder.title);
		GuiUtils.addPlusLinkify(holder.title);

		if (currentActivity.getContent_description() != null) {
			holder.content.setText(currentActivity.getContent_description());

			GuiUtils.addAllLinksLinkify(holder.content);
			GuiUtils.addPlusLinkify(holder.content);
		}

//		holder.type.setText(currentActivity.getContent_type());

		Member member = dbHandler.getMemberbyId(currentActivity.getActor().getId());

		if (member != null) {
			holder.name.setText(member.getName());
			String memberImage = "http:" + member.getImage();

			Ion.with(context, memberImage)
					.withBitmap()
					.placeholder(R.drawable.user)
					.error(R.drawable.user)
					.transform(imvTransform)
					.intoImageView(holder.image);

		} else {
			e("[ERROR] fucverg.saulmm.gdg.gui.adapters.PostAdapter.getView ",
					"Member: " + member);
		}


//		Ion.with(context, imageURL)
//			.withBitmap()
//			.transform(imvTransform)
//			.intoImageView(holder.image);


		return convertView;
	}
}


