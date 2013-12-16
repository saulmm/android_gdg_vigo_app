package fucverg.saulmm.gdg.gui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
		public LinearLayout memberLayout;
		public LinearLayout contentLayout;
		public TextView attach_content;

	}

	@Override
	public View getView (int position, View convertView, ViewGroup parent) {
		ViewHolder holder;

		if (convertView == null) {
			LayoutInflater inflater = LayoutInflater.from(context);

			convertView = inflater.inflate(R.layout.item_post, parent, false);
			holder = new ViewHolder();

			holder.icon = (ImageView) convertView.findViewById(R.id.ip_icon);
			holder.image = (ImageView) convertView.findViewById(R.id.ip_member_image);
			holder.type = (TextView) convertView.findViewById(R.id.ip_event_type);
			holder.date = (TextView) convertView.findViewById(R.id.ip_date);
			holder.name = (TextView) convertView.findViewById(R.id.ip_member_name);
			holder.title = (TextView) convertView.findViewById(R.id.ip_title);
			holder.content = (TextView) convertView.findViewById(R.id.ip_content);
			holder.plusoners = (TextView) convertView.findViewById(R.id.ip_plusoners);
			holder.comments = (TextView) convertView.findViewById(R.id.ip_comments);
			holder.memberLayout = (LinearLayout) convertView.findViewById(R.id.ip_member_layout);
			holder.contentLayout = (LinearLayout) convertView.findViewById(R.id.ip_content_layout);
			holder.img_attachment = (ImageView) convertView.findViewById(R.id.ip_image_attachment);
			holder.attach_content = (TextView) convertView.findViewById(R.id.ip_attach_content);

			convertView.setTag(holder);

		} else
			holder = (ViewHolder) convertView.getTag();

		final Post currentPost = activities.get(position);
		final Member member = dbHandler.getMemberbyId(
				currentPost.getActor().getId());

		if (currentPost != null) {
			PostObj object = currentPost.getObject();

			switchIconByProvider(currentPost.getProvider().title, holder);
			holder.type.setText(currentPost.getProvider().title);
			holder.title.setText(currentPost.getTitle());
			holder.content.setText(currentPost.getContent_description());

			GuiUtils.linkifyTextView(holder.content);
			GuiUtils.linkifyTextView(holder.title);

			// Plus object stuff
			if (object != null) {
				holder.plusoners.setText("+" + currentPost.getObject().getPlusoners().totalItems);
				holder.comments.setText(object.getReplies().totalItems);
			}

			// Member stuff
			if (member != null) {
				loadAdapterMemberStuff(member, holder);

			} else {
				d("[DEBUG] fucverg.saulmm.gdg.gui.adapters.PostAdapter.getView ",
						"The member is null in the post: " + currentPost.getTitle());
				loadNullMember(holder);
			}

			// Post Attachments
			if (object.getAttachments() != null) {
				loadAdapterAttachmentStuff(object.getAttachments(), holder);

			} else {
				holder.img_attachment.setVisibility(View.GONE);
			}
		}

		return convertView;
	}


	private void loadNullMember (ViewHolder holder) {
		holder.image.setImageResource(R.drawable.placeholder);
		holder.name.setText("Anónimo");
	}


	private void loadAdapterMemberStuff (Member member, ViewHolder holder) {
		holder.name.setText(member.getName());
		String memberImage = "http:" + member.getImage();

		Ion.with(context, memberImage)
				.withBitmap()
				.placeholder(R.drawable.user)
				.error(R.drawable.user)
				.transform(imvTransform)
				.intoImageView(holder.image);
	}


	private void loadAdapterAttachmentStuff (Attachments[] attachments, ViewHolder holder) {
		for (Attachments attachment : attachments) {
			holder.attach_content.setText(attachment.content);
			GuiUtils.linkifyTextView(holder.attach_content);

			if (attachment.fullImage != null) {
				holder.img_attachment.setVisibility(View.VISIBLE);

				Ion.with(context, attachment.fullImage.url)
						.withBitmap()
						.placeholder(R.drawable.placeholder)
						.intoImageView(holder.img_attachment);


			} else if (attachment.getObjectType().equals("photo")) {
				holder.img_attachment.setVisibility(View.VISIBLE);

				Ion.with(context, attachment.image.url)
						.withBitmap()
						.placeholder(R.drawable.placeholder)
						.intoImageView(holder.img_attachment);

			} else {
				holder.img_attachment.setVisibility(View.GONE);
			}
		}

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


	public List<Post> getActivities () {
		return activities;
	}
}


