package fucverg.saulmm.gdg.gui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import fucverg.saulmm.gdg.R;
import fucverg.saulmm.gdg.data.db.entities.Event;
import fucverg.saulmm.gdg.utils.GuiUtils;

import java.util.List;

public class EventsAdapter extends ArrayAdapter<Event> {
	private final Context context;
	private final List<Event> events;

	public EventsAdapter (Context context, List<Event> events) {
		super(context, R.layout.fragment_event, events);
		this.context = context;
		this.events = events;
	}

	static class ViewHolder {
		public TextView title;
		public TextView content;
		public TextView date;
		public TextView hours;
	}


	@Override
	public View getView (int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		Event currentEvent = events.get(position);

		if (convertView == null) {

			convertView = LayoutInflater.from(context).inflate(R.layout.item_event, parent, false);

			holder = new ViewHolder();
			holder.title = (TextView) convertView.findViewById(R.id.ie_title);
			holder.content = (TextView) convertView.findViewById(R.id.ie_content);
			holder.date = (TextView) convertView.findViewById(R.id.ie_date);
			holder.hours = (TextView) convertView.findViewById(R.id.ie_hours);

			GuiUtils.linkifyTextView(holder.title);
			GuiUtils.linkifyTextView(holder.content);


			convertView.setTag(holder);

		} else {

			holder = (ViewHolder) convertView.getTag();
		}





		holder.title.setText(currentEvent.getTitle());
		holder.content.setText(currentEvent.getDescription());

		return convertView;
	}
}
