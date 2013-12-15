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
import fucverg.saulmm.gdg.data.db.entities.plus_activity_entities.Attachments;
import fucverg.saulmm.gdg.data.db.entities.plus_activity_entities.Post;
import fucverg.saulmm.gdg.data.db.entities.plus_activity_entities.PostObj;
import fucverg.saulmm.gdg.gui.views.RoundedTransformation;
import fucverg.saulmm.gdg.utils.GuiUtils;

import java.util.List;

import static android.util.Log.d;
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
		public ImageView img_attachment;
		public TextView name;
		public TextView title;
		public TextView content;
		public TextView date;
		public TextView type;
		public TextView plusoners;
		public TextView comments;
	}


	@Override
	public View getView (int position, View convertView, ViewGroup parent) {
		Post currentPost = activities.get(position);
		PostObj object = currentPost.getObject();

		ViewHolder holder;

		if (convertView == null) {
			LayoutInflater inflater = LayoutInflater.from(context);

			convertView = inflater.inflate(R.layout.item_post, parent, false);
			holder = new ViewHolder();

			holder.icon = (ImageView) convertView.findViewById(R.id.ip_icon);
			holder.image = (ImageView) convertView.findViewById(R.id.ip_member_image);
			holder.img_attachment = (ImageView) convertView.findViewById(R.id.ip_image_attachment);
			holder.type = (TextView) convertView.findViewById(R.id.ip_event_type);
			holder.date = (TextView) convertView.findViewById(R.id.ip_date);
			holder.name = (TextView) convertView.findViewById(R.id.ip_member_name);
			holder.title = (TextView) convertView.findViewById(R.id.ip_title);
			holder.content = (TextView) convertView.findViewById(R.id.ip_content);
			holder.plusoners = (TextView) convertView.findViewById(R.id.ip_plusoners);
			holder.comments = (TextView) convertView.findViewById(R.id.ip_comments);
			convertView.setTag(holder);

		} else
			holder = (ViewHolder) convertView.getTag();

		holder.title.setText(currentPost.getTitle());

		if (object != null) {
			holder.plusoners.setText("+" + currentPost.getObject().getPlusoners().totalItems);
			holder.comments.setText(object.getReplies().totalItems);
		}

		switchIconByProvider(currentPost.getProvider().title, holder);

		holder.type.setText(currentPost.getProvider().title);
		GuiUtils.linkifyTextView(holder.title);

		if (currentPost.getContent_description() != null) {
			holder.content.setText(currentPost.getContent_description());
			GuiUtils.linkifyTextView(holder.content);

		} else {
			holder.content.setVisibility(View.GONE);
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



		} else
			e("[ERROR] fucverg.saulmm.gdg.gui.adapters.PostAdapter.getView ",
					"Member: " + member);
		if (object.attachments != null) {
			// DEBUG
			d("[DEBUG] fucverg.saulmm.gdg.gui.adapters.PostAdapter.getView ",
					"Post activity: " + currentPost.getTitle() + "\n" +
							"Number of attachments: " + object.getAttachments().length + "\n");

			for (Attachments attachment : object.getAttachments()) {
				d("[DEBUG] fucverg.saulmm.gdg.gui.adapters.PostAdapter.getView ",
						"Attachment: "+attachment.getObjectType());

				if (attachment.getObjectType().equals("photo")) {
					holder.img_attachment.setVisibility(View.VISIBLE);

					d("[DEBUG] fucverg.saulmm.gdg.gui.adapters.PostAdapter.getView ",
							"This is a photo with the url: " + attachment.image.url + "\n");

					Ion.with(context, attachment.image.url)
							.withBitmap()
							.placeholder(R.drawable.placeholder)
							.intoImageView(holder.img_attachment);
				} else {
					holder.img_attachment.setVisibility(View.GONE);

				}
			}


		}

		return convertView;
	}


	private void switchIconByProvider (String provider, ViewHolder holder) {
		if (provider.equals("Community"))
			holder.icon.setBackgroundResource(R.drawable.icon_communitie);

		 else if (provider.equals("Events"))
			holder.icon.setBackgroundResource(R.drawable.event);

		else if (provider.equals("Reshared Post"))
			holder.icon.setBackgroundResource(R.drawable.icon_reshared);

		else if (provider.equals("Mobile"))
			holder.icon.setBackgroundResource(R.drawable.icon_phone);

		else if (provider.equals("Google+"))
			holder.icon.setBackgroundResource(R.drawable.icon_plus);
	}
}


