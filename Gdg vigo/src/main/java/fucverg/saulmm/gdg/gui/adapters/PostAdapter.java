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
import fucverg.saulmm.gdg.data.db.entities.plus_activity_entities.Post;
import fucverg.saulmm.gdg.gui.views.RoundedTransformation;
import fucverg.saulmm.gdg.utils.GuiUtils;

import java.util.List;

import static android.util.Log.e;

public class PostAdapter extends ArrayAdapter<Post> {
	private final Context context;
	private final List<Post> activities;
	private final RoundedTransformation imvTransform;
	private String plusURL;
	private final DBHandler dbHandler;


	public PostAdapter (Context context, List<Post> activities) {
		super(context, R.layout.fragment_posts, activities);
		this.imvTransform = new RoundedTransformation(100);
		this.activities = activities;
		this.context = context;
		this.dbHandler = new DBHandler(context);

		GuiUtils.GUI_DB_HANDLER = this.dbHandler;
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
		Post currentPost = activities.get(position);

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

		holder.title.setText(currentPost.getTitle());
		GuiUtils.addAllLinksLinkify(holder.title);
		GuiUtils.addPlusLinkify(holder.title);
		GuiUtils.addHashtagLinkify(holder.title);
		GuiUtils.addMentionLinkify(holder.title);

		if (currentPost.getContent_description() != null) {
			holder.content.setText(currentPost.getContent_description());

			GuiUtils.addAllLinksLinkify(holder.content);
			GuiUtils.addPlusLinkify(holder.content);
			GuiUtils.addHashtagLinkify(holder.content);
			GuiUtils.addMentionLinkify(holder.content);
		}

		Member member = dbHandler.getMemberbyId(currentPost.getActor().getId());

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



		return convertView;
	}
}


