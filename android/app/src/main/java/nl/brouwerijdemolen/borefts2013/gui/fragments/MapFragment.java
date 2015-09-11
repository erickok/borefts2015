package nl.brouwerijdemolen.borefts2013.gui.fragments;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.os.Handler;
import android.util.Log;
import android.util.SparseArray;
import android.widget.Toast;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import nl.brouwerijdemolen.borefts2013.R;
import nl.brouwerijdemolen.borefts2013.api.Brewer;
import nl.brouwerijdemolen.borefts2013.api.Brewers;
import nl.brouwerijdemolen.borefts2013.gui.helpers.ApiQueue;
import nl.brouwerijdemolen.borefts2013.gui.helpers.NavigationManager;

@EFragment
public class MapFragment extends com.google.android.gms.maps.SupportMapFragment
		implements OnInfoWindowClickListener, Listener<Brewers>, ErrorListener, OnMarkerClickListener, OnMapClickListener {

	public static final MapElement ELEMENT_TRAINS =
			new MapElement(0, new LatLng(52.081515, 4.746145), R.string.map_trains, R.drawable.ic_marker_trains);
	public static final MapElement ELEMENT_ENTRANCE =
			new MapElement(1, new LatLng(52.084871, 4.740536), R.string.map_entrance, R.drawable.ic_marker_entrance);
	public static final MapElement ELEMENT_FTOILET1 =
			new MapElement(2, new LatLng(52.085153, 4.740472), R.string.map_ftoilet, R.drawable.ic_marker_toilet); // In bottle house
	public static final MapElement ELEMENT_FTOILET2 =
			new MapElement(3, new LatLng(52.084255, 4.739361), R.string.map_ftoilet, R.drawable.ic_marker_toilet); // In storage
	public static final MapElement ELEMENT_FTOILET3 =
			new MapElement(4, new LatLng(52.084176, 4.739313), R.string.map_ftoilet, R.drawable.ic_marker_toilet); // In the back
	public static final MapElement ELEMENT_FTOILET4 =
			new MapElement(5, new LatLng(52.085104, 4.740724), R.string.map_ftoilet, R.drawable.ic_marker_toilet); // In brewery
	public static final MapElement ELEMENT_FTOILET5 =
			new MapElement(6, new LatLng(52.085741, 4.742175), R.string.map_ftoilet, R.drawable.ic_marker_toilet); // In mill
	/*public static final MapElement ELEMENT_MTOILET1 =
			new MapElement(7, new LatLng(52.084291, 4.739415), R.string.map_mtoilet, R.drawable.ic_marker_toilet); // In storage*/
	/*public static final MapElement ELEMENT_MTOILET2 =
			new MapElement(8, new LatLng(52.084210, 4.739361), R.string.map_mtoilet, R.drawable.ic_marker_toilet); // In the back*/
	/*public static final MapElement ELEMENT_MTOILET3 =
			new MapElement(9, new LatLng(52.085741, 4.742175), R.string.map_mtoilet, R.drawable.ic_marker_toilet); // In mill*/
	public static final MapElement ELEMENT_TOKENS =
			new MapElement(10, new LatLng(52.085053, 4.740407), R.string.map_tokens, R.drawable.ic_marker_tokens);
	public static final MapElement ELEMENT_MERCH =
			new MapElement(11, new LatLng(52.084326, 4.739069), R.string.map_merch, R.drawable.ic_marker_tokens);
	public static final MapElement ELEMENT_MILL = new MapElement(12, new LatLng(52.085649, 4.742070), R.string.map_mill, R.drawable.ic_marker_mill);
	public static final MapElement ELEMENT_FIRSTAID =
			new MapElement(13, new LatLng(52.084879, 4.740230), R.string.map_firstaid, R.drawable.ic_marker_firstaid);
	public static final MapElement ELEMENT_FOODPLAZA =
			new MapElement(14, new LatLng(52.084367, 4.739764), R.string.map_foodplaza, R.drawable.ic_marker_food);
	public static final MapElement ELEMENT_FOODSNACK =
			new MapElement(15, new LatLng(52.085700, 4.742221), R.string.map_foodplaza, R.drawable.ic_marker_food);
	public static final MapElement ELEMENT_BEERBAR =
			new MapElement(16, new LatLng(52.085763, 4.742025), R.string.map_beerbar, R.drawable.ic_marker_beer);
	public static final MapElement ELEMENT_SHOP = new MapElement(17, new LatLng(52.085643, 4.741969), R.string.map_shop, R.drawable.ic_marker_shop);
	public static final int BREWER_ID_THRESHOLD = 100;

	private SparseArray<Marker> elementMarkers;
	private Map<Marker, Brewer> brewerMarkers;

	@Bean
	protected ApiQueue apiQueue;
	@FragmentArg
	protected boolean isMinimap = true;
	@FragmentArg
	protected int initFocusId;

	public MapFragment() {
		setRetainInstance(false);
	}

	@AfterViews
	protected void initMap() {

		if (getMap() == null)
			return;

		// Always centre the map on the festival location in Bodegraven
		// When shown as minimap, no interaction is allowed; the full map screen is started instead
		if (isMinimap) {
			getMap().moveCamera(CameraUpdateFactory
					.newCameraPosition(new CameraPosition.Builder().target(new LatLng(52.084754, 4.739858)).zoom(17.6f).bearing(314f).build()));
			getMap().getUiSettings().setAllGesturesEnabled(false);
			getMap().getUiSettings().setZoomControlsEnabled(false);
			getMap().setOnMarkerClickListener(this);
			getMap().setOnMapClickListener(this);
		} else {
			getMap().moveCamera(CameraUpdateFactory
					.newCameraPosition(new CameraPosition.Builder().target(new LatLng(52.085114, 4.740697)).zoom(17.1f).bearing(6f).build()));
			getMap().setMyLocationEnabled(true);
			getMap().getUiSettings().setCompassEnabled(true);
			getMap().setOnInfoWindowClickListener(this);
			/*getMap().setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
				@Override
				public void onCameraChange(CameraPosition cameraPosition) {
					Log.d("BOREFTS", String.format(Locale.US, "LAT: %1$.6f LNG: %2$.6f ZOOM: %3$.1f BEA: %4$.1f", cameraPosition.target.latitude,
							cameraPosition.target.longitude, cameraPosition.zoom, cameraPosition.bearing));
				}
			});*/
			// Schedule zooming to festival terrain (except when searching for the trains)
			if (initFocusId >= BREWER_ID_THRESHOLD) {
				new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						getMap().animateCamera(CameraUpdateFactory.newCameraPosition(
								new CameraPosition.Builder().target(new LatLng(52.084723, 4.739909)).zoom(18f).bearing(3.3f).build()));
					}
				}, 1500);
			}
		}

		// Load the festival outline
		// Brewery building
		getMap().addPolygon(new PolygonOptions()
				.add(new LatLng(52.085212, 4.740633), new LatLng(52.085081, 4.740858), new LatLng(52.085008, 4.740740),
						new LatLng(52.085143, 4.740525)).strokeColor(getResources().getColor(R.color.darkred)).strokeWidth(5f)
				.fillColor(getResources().getColor(R.color.darkred_half)));
		// Bottling building
		getMap().addPolygon(new PolygonOptions()
				.add(new LatLng(52.085262, 4.740338), new LatLng(52.085163, 4.740525), new LatLng(52.084992, 4.740257),
						new LatLng(52.084929, 4.740257), new LatLng(52.084718, 4.739925), new LatLng(52.084850, 4.739705))
				.strokeColor(getResources().getColor(R.color.darkred)).strokeWidth(5f).fillColor(getResources().getColor(R.color.darkred_half)));
		// Storage building
		getMap().addPolygon(new PolygonOptions()
				.add(new LatLng(52.084718, 4.739925), new LatLng(52.084843, 4.739715), new LatLng(52.084369, 4.738948),
						new LatLng(52.084177, 4.739243), new LatLng(52.084392, 4.739614), new LatLng(52.084527, 4.739619))
				.strokeColor(getResources().getColor(R.color.darkred)).strokeWidth(5f).fillColor(getResources().getColor(R.color.darkred_half)));
		// Festival area 1
		getMap().addPolygon(new PolygonOptions()
				.add(new LatLng(52.084171, 4.739249), new LatLng(52.084388, 4.739624), new LatLng(52.084523, 4.739624),
						new LatLng(52.084929, 4.740263), new LatLng(52.084995, 4.740257), new LatLng(52.085146, 4.740515),
						new LatLng(52.084992, 4.740762), new LatLng(52.084072, 4.739372)).strokeColor(getResources().getColor(R.color.yellow))
				.strokeWidth(5f).fillColor(getResources().getColor(R.color.yellow_half)));
		// Entrance area
		getMap().addPolygon(new PolygonOptions()
				.add(new LatLng(52.084856, 4.740461), new LatLng(52.084921, 4.740550), new LatLng(52.084888, 4.740609),
						new LatLng(52.084825, 4.740515)).strokeColor(getResources().getColor(R.color.blue)).strokeWidth(5f)
				.fillColor(getResources().getColor(R.color.blue_half)));
		// Mill building
		getMap().addPolygon(new PolygonOptions()
				.add(new LatLng(52.085812, 4.742054), new LatLng(52.085746, 4.742215), new LatLng(52.085687, 4.742151),
						new LatLng(52.085677, 4.742178), new LatLng(52.085578, 4.742060), new LatLng(52.085641, 4.741910),
						new LatLng(52.085690, 4.741947), new LatLng(52.085704, 4.741920)).strokeColor(getResources().getColor(R.color.darkred))
				.strokeWidth(5f).fillColor(getResources().getColor(R.color.darkred_half)));
		// Festival area 2
		getMap().addPolygon(new PolygonOptions()
				.add(new LatLng(52.085651, 4.742486), new LatLng(52.085470, 4.742285), new LatLng(52.085575, 4.742062),
						new LatLng(52.085672, 4.742183), new LatLng(52.085687, 4.742164), new LatLng(52.085745, 4.742226))
				.strokeColor(getResources().getColor(R.color.yellow)).strokeWidth(5f).fillColor(getResources().getColor(R.color.yellow_half)));

		// Load the POI markers
		elementMarkers = new SparseArray<>(6);
		addPoiMarker(ELEMENT_TRAINS);
		addPoiMarker(ELEMENT_ENTRANCE);
		addPoiMarker(ELEMENT_FTOILET1);
		addPoiMarker(ELEMENT_FTOILET2);
		addPoiMarker(ELEMENT_FTOILET3);
		addPoiMarker(ELEMENT_FTOILET4);
		addPoiMarker(ELEMENT_FTOILET5);
		/*addPoiMarker(ELEMENT_MTOILET1);
		addPoiMarker(ELEMENT_MTOILET2);
		addPoiMarker(ELEMENT_MTOILET3);*/
		addPoiMarker(ELEMENT_TOKENS);
		addPoiMarker(ELEMENT_MILL);
		addPoiMarker(ELEMENT_FIRSTAID);
		addPoiMarker(ELEMENT_MERCH);
		addPoiMarker(ELEMENT_FOODPLAZA);
		addPoiMarker(ELEMENT_FOODSNACK);
		addPoiMarker(ELEMENT_BEERBAR);
		addPoiMarker(ELEMENT_SHOP);
		if (initFocusId >= 0 && initFocusId < BREWER_ID_THRESHOLD) {
			focusOnMarker(initFocusId);
		}

		// Load the brewers markers asynchronously
		apiQueue.requestBrewers(this, this);

	}

	@Override
	public void onResponse(Brewers brewers) {
		if (getActivity() == null || !isAdded())
			return;
		brewerMarkers = new HashMap<>();
		for (final Brewer brewer : brewers.getBrewers()) {
			addBrewerMarker(brewer);
		}
	}

	/**
	 * Adds a marker to the visible map where some point of interest is located. The marker is cached for later lookup.
	 * @param element The meta data of the marker to show, including the marker resource graphic id
	 */
	protected void addPoiMarker(MapElement element) {
		Marker marker = getMap().addMarker(new MarkerOptions().position(element.latLng).title(getString(element.titleResource))
				.icon(BitmapDescriptorFactory.fromResource(element.markerResource)));
		elementMarkers.put(element.focusId, marker);
	}

	/**
	 * Adds a marker to the visible map where a certain brewer is located. The provided bitmap is a logo of the brewer that was already loaded from
	 * the memory cache or internet. The marker is cached for later lookup.
	 * @param brewer The brewer to visualise on the map with a marker
	 */
	public void addBrewerMarker(Brewer brewer) {
		BitmapDescriptor bitmapToUse;
		if (brewer.getLogoUrl() == null)
			bitmapToUse = BitmapDescriptorFactory.fromResource(R.drawable.ic_marker_mask);
		else {
			Bitmap brewerMarker = drawBrewerMarker(brewer);
			if (brewerMarker == null)
				bitmapToUse = BitmapDescriptorFactory.fromResource(R.drawable.ic_marker_mask);
			else
				bitmapToUse = BitmapDescriptorFactory.fromBitmap(brewerMarker);
		}
		Marker marker = getMap().addMarker(
				new MarkerOptions().position(new LatLng(brewer.getLatitude(), brewer.getLongitude())).title(brewer.getShortName()).icon(bitmapToUse));
		// Also open the info window if a focus ID for this brewer was supplied
		if (initFocusId == BREWER_ID_THRESHOLD + brewer.getId())
			marker.showInfoWindow();
		elementMarkers.put(BREWER_ID_THRESHOLD + brewer.getId(), marker);
		brewerMarkers.put(marker, brewer);
	}

	/**
	 * Draws a bitmap that has the shape and outline of a map marker but the contents of the brewer's logo.
	 * @param brewer The brewer to create a marker for on the basis of its logo
	 * @return A marker bitmap; this is not cached
	 */
	private Bitmap drawBrewerMarker(Brewer brewer) {
		// TODO Cache the drawn compound bitmap?
		try {
			Bitmap mask = BitmapFactory.decodeResource(getResources(), R.drawable.ic_marker_mask);
			Bitmap outline = BitmapFactory.decodeResource(getResources(), R.drawable.ic_marker_outline);
			Bitmap logo = BitmapFactory.decodeStream(getResources().getAssets().open("images/" + brewer.getLogoUrl()));
			Bitmap bmp = Bitmap.createBitmap(mask.getWidth(), mask.getHeight(), Config.ARGB_8888);
			Canvas canvas = new Canvas(bmp);
			Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
			canvas.drawBitmap(mask, 0, 0, paint);
			paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
			canvas.drawBitmap(logo, null, new Rect(0, 0, mask.getWidth(), mask.getHeight()), paint);
			paint.setXfermode(null);
			canvas.drawBitmap(outline, 0, 0, paint);
			return bmp;
		} catch (IOException e) {
			return null; // Should never happen, as the brewer logo always exists
		}
	}

	@Override
	public void onErrorResponse(VolleyError error) {
		Toast.makeText(getActivity(), R.string.error_nolocations, Toast.LENGTH_LONG).show();
	}

	public void focusOnMarker(int focusId) {
		Marker marker = elementMarkers.get(focusId);
		if (marker != null)
			marker.showInfoWindow();
	}

	@Override
	public void onInfoWindowClick(Marker marker) {
		if (brewerMarkers.containsKey(marker)) {
			((NavigationManager) getActivity()).openBrewer(this, brewerMarkers.get(marker));
		}
	}

	public static class MapElement {

		public final int focusId;
		public final LatLng latLng;
		public final int titleResource;
		public final int markerResource;

		public MapElement(int focusId, LatLng latLng, int titleResource, int markerResource) {
			this.focusId = focusId;
			this.latLng = latLng;
			this.titleResource = titleResource;
			this.markerResource = markerResource;
		}

	}

	@Override
	public void onMapClick(LatLng arg0) {
		if (isMinimap)
			((NavigationManager) getActivity()).openMap(this, -1, null);
	}

	@Override
	public boolean onMarkerClick(Marker marker) {
		if (isMinimap) {
			((NavigationManager) getActivity()).openMap(this, -1, null);
			return true;
		}
		return false;
	}

}
