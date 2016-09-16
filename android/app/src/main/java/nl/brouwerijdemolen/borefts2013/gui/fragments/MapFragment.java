package nl.brouwerijdemolen.borefts2013.gui.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.os.Handler;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.SparseArray;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
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
import nl.brouwerijdemolen.borefts2013.api.Area;
import nl.brouwerijdemolen.borefts2013.api.Brewer;
import nl.brouwerijdemolen.borefts2013.api.Brewers;
import nl.brouwerijdemolen.borefts2013.api.Poi;
import nl.brouwerijdemolen.borefts2013.api.Pois;
import nl.brouwerijdemolen.borefts2013.gui.helpers.ApiQueue;
import nl.brouwerijdemolen.borefts2013.gui.helpers.NavigationManager;


@EFragment
public class MapFragment extends com.google.android.gms.maps.SupportMapFragment
		implements ErrorListener, OnMapReadyCallback, OnInfoWindowClickListener, OnMarkerClickListener, OnMapClickListener {

	private static final int REQUEST_PERMISSION = 0;

	private Brewers brewers;
	private Pois pois;
	private Map<Marker, Poi> poiMarkers;
	private Map<String, Marker> poiIds;
	private Map<Marker, Brewer> brewerMarkers;
	private SparseArray<Marker> brewerIds;

	@Bean
	protected ApiQueue apiQueue;
	@FragmentArg
	protected boolean isMinimap = true;
	@FragmentArg
	protected Integer initFocusBrewer;
	@FragmentArg
	protected String initFocusPoi;

	public MapFragment() {
		setRetainInstance(false);
	}

	@AfterViews
	protected void initMap() {
		getMapAsync(this);

		// If in full screen moe and we have no location access, ask for the runtime permission (unless marked as never ask again)
		if (!isMinimap && !hasLocationPermission()) {
			Snackbar snackbar = Snackbar.make(getView(), R.string.general_location_permission, Snackbar.LENGTH_INDEFINITE);
			snackbar.setActionTextColor(getColor(R.color.yellow));
			snackbar.setAction(R.string.general_location_ok, new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_PERMISSION);
				}
			});
			snackbar.show();
		}
	}

	@Override
	public void onMapReady(final GoogleMap map) {
		// Always centre the map on the festival location in Bodegraven
		// When shown as minimap, no interaction is allowed; the full map screen is started instead
		if (isMinimap) {
			map.moveCamera(CameraUpdateFactory
					.newCameraPosition(new CameraPosition.Builder().target(new LatLng(52.084622, 4.740003)).zoom(17.6f).bearing(313.8f).build()));
			map.getUiSettings().setAllGesturesEnabled(false);
			map.getUiSettings().setZoomControlsEnabled(false);
			map.setOnMarkerClickListener(this);
			map.setOnMapClickListener(this);
		} else {
			map.moveCamera(CameraUpdateFactory
					.newCameraPosition(new CameraPosition.Builder().target(new LatLng(52.085114, 4.740697)).zoom(17.1f).bearing(6f).build()));
			if (hasLocationPermission()) {
				//noinspection MissingPermission Permission checked explicitly
				map.setMyLocationEnabled(true);
			}
			map.getUiSettings().setCompassEnabled(true);
			map.setOnInfoWindowClickListener(this);
			/*map.setOnMapClickListener(new OnMapClickListener() {
				@Override
				public void onMapClick(LatLng latLng) {
					Log.d("BOREFTS", String.format(Locale.US, "LAT: %1$.6f LNG: %2$.6f", latLng.latitude, latLng.longitude));
				}
			});*/
			/*map.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
				@Override
				public void onCameraChange(CameraPosition cameraPosition) {
					Log.d("BOREFTS", String.format(Locale.US, "LAT: %1$.6f LNG: %2$.6f ZOOM: %3$.1f BEA: %4$.1f", cameraPosition.target.latitude,
							cameraPosition.target.longitude, cameraPosition.zoom, cameraPosition.bearing));
				}
			});*/

			// Schedule zooming to festival terrain
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					map.animateCamera(CameraUpdateFactory.newCameraPosition(
							new CameraPosition.Builder().target(new LatLng(52.084515, 4.739977)).zoom(18f).bearing(3.3f).build()));
				}
			}, 1500);
		}

		// Load the areas, pois and brewers markers asynchronously
		apiQueue.requestBrewers(new Listener<Brewers>() {
			@Override
			public void onResponse(Brewers brewers) {
				MapFragment.this.brewers = brewers;
				drawMarkers();
			}
		}, this);
		apiQueue.requestPois(new Listener<Pois>() {
			@Override
			public void onResponse(Pois pois) {
				MapFragment.this.pois = pois;
				drawMarkers();
			}
		}, this);
	}

	private void drawMarkers() {
		if (getActivity() == null || !isAdded() || brewers == null || pois == null)
			return;
		getMapAsync(new OnMapReadyCallback() {
			@Override
			public void onMapReady(final GoogleMap map) {
				for (Area area : pois.getAreas()) {
					addArea(map, area);
				}
				poiMarkers = new HashMap<>();
				poiIds = new HashMap<>();
				for (final Poi poi : pois.getPois()) {
					addPoiMarker(map, poi);
				}
				brewerMarkers = new HashMap<>();
				brewerIds = new SparseArray<>();
				for (final Brewer brewer : brewers.getBrewers()) {
					addBrewerMarker(map, brewer);
				}

				// Set focus on a specific marker
				if (initFocusBrewer != null) {
					Marker marker = brewerIds.get(initFocusBrewer);
					if (marker != null)
						marker.showInfoWindow();
				} else if (initFocusPoi != null) {
					Marker marker = poiIds.get(initFocusPoi);
					if (marker != null)
						marker.showInfoWindow();
				}
			}
		});
	}

	private void addArea(GoogleMap map, Area area) {
		map.addPolygon(new PolygonOptions().addAll(area.getPointLatLngs())
				.strokeColor(getColor(area.getColor())).strokeWidth(5f).fillColor(getFillColor(area.getColor())));
	}

	/**
	 * Adds a marker to the visible map where some point of interest is located. The marker is cached for later lookup.
	 * @param map The map object to draw on
	 * @param poi The point of interest to show, which contains the elements to create a map marker
	 */
	private void addPoiMarker(GoogleMap map, Poi poi) {
		Marker marker = map.addMarker(new MarkerOptions().position(poi.getPointLatLng()).title(getPoiName(poi))
				.icon(BitmapDescriptorFactory.fromResource(getDrawable(poi.getMarker()))));
		poiMarkers.put(marker, poi);
		poiIds.put(poi.getId(), marker);
		//elementMarkers.put(element.focusId, marker);
	}

	/**
	 * Adds a marker to the visible map where a certain brewer is located. The provided bitmap is a logo of the brewer that was already loaded from
	 * the memory cache or internet. The marker is cached for later lookup.
	 * @param map The map object to draw on
	 * @param brewer The brewer to visualise on the map with a marker
	 */
	public void addBrewerMarker(GoogleMap map, Brewer brewer) {
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
		Marker marker = map.addMarker(
				new MarkerOptions().position(new LatLng(brewer.getLatitude(), brewer.getLongitude())).title(brewer.getShortName()).icon
						(bitmapToUse));
		brewerMarkers.put(marker, brewer);
		brewerIds.put(brewer.getId(), marker);
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

	@Override
	public void onInfoWindowClick(Marker marker) {
		if (brewerMarkers.containsKey(marker)) {
			((NavigationManager) getActivity()).openBrewer(this, brewerMarkers.get(marker));
		}
	}

	@Override
	public void onMapClick(LatLng arg0) {
		if (isMinimap)
			((NavigationManager) getActivity()).openMap(this);
	}

	@Override
	public boolean onMarkerClick(Marker marker) {
		if (isMinimap) {
			((NavigationManager) getActivity()).openMap(this);
			return true;
		}
		return false;
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		if (requestCode == REQUEST_PERMISSION && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
			// Permission was now granted; update the map to show the location
			getMapAsync(new OnMapReadyCallback() {
				@Override
				public void onMapReady(GoogleMap googleMap) {
					//noinspection MissingPermission Explicitly just gotten the permission
					googleMap.setMyLocationEnabled(true);
				}
			});
		}
	}

	private boolean hasLocationPermission() {
		return ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
	}

	private String getPoiName(Poi poi) {
		if (Locale.getDefault().getLanguage().equals("nl")) {
			return poi.getName_nl();
		}
		return poi.getName_en();
	}

	private int getDrawable(String resName) {
		return getResources().getIdentifier(resName, "drawable", getActivity().getPackageName());
	}

	@ColorInt
	private int getColor(int res) {
		return ContextCompat.getColor(getContext(), res);
	}

	@ColorInt
	private int getColor(String resName) {
		return ContextCompat.getColor(getContext(),
				getResources().getIdentifier(resName, "color", getActivity().getPackageName()));
	}

	@ColorInt
	private int getFillColor(String resName) {
		return getColor(resName + "_half");
	}

}
