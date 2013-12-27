package ameiga.saulmm.gdg.gui.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.koushikdutta.ion.Ion;
import ameiga.saulmm.gdg.data.api.entities.Actor;
import ameiga.saulmm.gdg.data.api.entities.Attachments;
import ameiga.saulmm.gdg.data.api.entities.Post;
import ameiga.saulmm.gdg.data.api.entities.PostObj;
import ameiga.saulmm.gdg.data.db.DBHandler;
import ameiga.saulmm.gdg.data.db.entities.Member;
import ameiga.saulmm.gdg.gui.views.RoundedTransformation;
import ameiga.saulmm.gdg.utils.GuiUtils;
import ameiga.saulmm.gdg.R;

import java.util.List;

public class PostAdapter extends ArrayAdapter<Post> {
	private final Context context;
	private final List<Post> activities;
	private final RoundedTransformation imvTransform;
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
		public ImageView img_attachment;
		public TextView name;
		public TextView title;
		public TextView content;
		public TextView date;
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

			holder.image = (ImageView) convertView.findViewById(R.id.ip_member_image);
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
		final Actor postActor = activities.get(position).getActor();
		final Member member = dbHandler.getMemberById(
				currentPost.getActor().getId());

		if (currentPost != null) {
			PostObj object = currentPost.getObject();

			holder.title.setText(currentPost.getTitle());
			holder.content.setText(currentPost.getContent_description());

			GuiUtils.linkifyTextView(holder.content);
			GuiUtils.linkifyTextView(holder.title);

			String [] timeStampElements = currentPost.getPublished().split("T");
			String [] dateElements = timeStampElements[0].split("-");

			String postLastUpdated = dateElements[2] + "-" + dateElements[1] + "-" + dateElements[0] + " ";
			holder.date.setText(postLastUpdated);


			// Member stuff
			if (member != null) { {
				loadAdapterMemberStuff(member, holder);
				Log.d("[DEBUG] ameiga.saulmm.gdg.gui.adapters.PostAdapter.getView ",
						"The member: " + member.getName() + " has : " + currentPost.object.attachments);
			}

			} else {
				holder.name.setText(postActor.displayName);

				// Todo move all urls to a static class
				Ion.with(context, "https://plus.google.com/s2/photos/profile/" + postActor.getId() + "?sz=100")
						.withBitmap()
						.placeholder(R.drawable.user)
						.error(R.drawable.user)
						.transform(imvTransform)
						.intoImageView(holder.image);
			}


			// Plus object stuff
			if (object != null) {
				holder.plusoners.setText("+" + currentPost.getObject().getPlusoners().totalItems);
				holder.comments.setText(object.getReplies().totalItems);

				// Post Attachments
				if (currentPost.object.attachments != null) {
					loadAdapterAttachmentStuff(object.getAttachments(), holder);

				} else {
					holder.img_attachment.setVisibility(View.GONE);
					holder.attach_content.setVisibility(View.GONE);
				}
			}


		}

		return convertView;
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




	public List<Post> getActivities () {
		return activities;
	}
}

