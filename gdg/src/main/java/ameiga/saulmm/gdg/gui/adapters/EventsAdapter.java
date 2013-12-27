package ameiga.saulmm.gdg.gui.adapters;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import ameiga.saulmm.gdg.data.db.entities.Event;
import ameiga.saulmm.gdg.utils.GuiUtils;
import ameiga.saulmm.gdg.R;

import java.util.List;

public class EventsAdapter extends ArrayAdapter<Event> {
	private final Context context;
	private final List<Event> events;
	private final EventAdapterListener listener;


	public EventsAdapter (Context context, List<Event> events, EventAdapterListener listener) {
		super(context, R.layout.fragment_event, events);
		this.context = context;
		this.events = events;
		this.listener = listener;
	}

	static class ViewHolder {
		public TextView title;
		public TextView content;
		public TextView date;
		public TextView hours;
		public TextView map;
	}


	@Override
	public View getView (int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		final Event currentEvent = events.get(position);

		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.item_event, parent, false);

			holder = new ViewHolder();
			holder.title = (TextView) convertView.findViewById(R.id.ie_title);
			holder.content = (TextView) convertView.findViewById(R.id.ie_content);
			holder.date = (TextView) convertView.findViewById(R.id.ie_date);
			holder.hours = (TextView) convertView.findViewById(R.id.ie_hours);
			holder.map = (TextView) convertView.findViewById(R.id.ie_map_textview);
			holder.map.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick (View view) {
					listener.mapPressed(currentEvent.getLocation());
				}
			});

			convertView.setTag(holder);

	} else

		holder = (ViewHolder) convertView.getTag();
		String [] startElements = currentEvent.getStart().split(" ");
		String eventTyme = startElements[0] + " " + startElements[1] + " " + startElements[2] + "  "+startElements[3];

		holder.date.setText(eventTyme);
		holder.title.setText(currentEvent.getTitle());
		holder.content.setText(Html.fromHtml(currentEvent.getDescription()));
		holder.content.setMovementMethod(null);
		GuiUtils.linkifyTextView(holder.title);
		GuiUtils.linkifyTextView(holder.content);
		return convertView;
	}
}
