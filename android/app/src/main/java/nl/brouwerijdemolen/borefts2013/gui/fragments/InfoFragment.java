package nl.brouwerijdemolen.borefts2013.gui.fragments;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.widget.FrameLayout;
import android.widget.Toast;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.GregorianCalendar;

import nl.brouwerijdemolen.borefts2013.R;
import nl.brouwerijdemolen.borefts2013.gui.helpers.NavigationManager;

@EFragment(R.layout.fragment_info)
public class InfoFragment extends Fragment {

	@ViewById
	protected FrameLayout minimap;

	public InfoFragment() {
		setRetainInstance(true);
	}

	@AfterViews
	protected void init() {
		getFragmentManager().beginTransaction().add(R.id.minimap, MapFragment_.builder().isMinimap(true).build()).commit();
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		// TODO Fix this (maybe keep the map fragment in memory)
	}

	@Click
	protected void timesButtonClicked() {
		// Try to start the calendar application
		Intent intent = new Intent(Intent.ACTION_EDIT);
		intent.setType("vnd.android.cursor.item/event");
		intent.putExtra("title", getString(R.string.app_name));
		intent.putExtra("eventLocation", "Doortocht 4, Bodegraven, The Netherlands");
		intent.putExtra("beginTime", new GregorianCalendar(2013, 8, 27, 12, 0).getTimeInMillis());
		intent.putExtra("endTime", new GregorianCalendar(2013, 8, 27, 22, 0).getTimeInMillis());
		intent.putExtra("rrule", "FREQ=DAILY;COUNT=2");
		if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
			startActivity(intent);
		} else {
			Toast.makeText(getActivity(), R.string.error_nocalendar, Toast.LENGTH_LONG).show();
		}

	}

	@Click
	protected void getmoreButtonClicked() {
		((NavigationManager) getActivity()).openMap(this, "tokensale");
	}

	@Click
	protected void nstimesButtonClicked() {
		startActivity(
				new Intent(Intent.ACTION_VIEW, Uri.parse("http://m.ns.nl/actvertrektijden.action?from=BDG")).setFlags(Intent
						.FLAG_ACTIVITY_NEW_TASK));
	}

	@Click
	protected void taxisButtonClicked() {
		startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("geo:52.084802,4.740689?z=14&q=taxi")).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
	}

	@Click
	protected void findmillButtonClicked() {
		((NavigationManager) getActivity()).openMap(this, "mill");
	}

}
