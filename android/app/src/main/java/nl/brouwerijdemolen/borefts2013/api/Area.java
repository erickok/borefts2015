package nl.brouwerijdemolen.borefts2013.api;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class Area implements Parcelable {

	private String id;
	private String name_nl;
	private String name_en;
	private String color;
	private List<Point> points;

	Area() {}

	public String getId() {
		return id;
	}

	public String getName_nl() {
		return name_nl;
	}

	public String getName_en() {
		return name_en;
	}

	public String getColor() {
		return color;
	}

	public List<Point> getPoints() {
		return points;
	}

	public List<LatLng> getPointLatLngs() {
		List<LatLng> latLngs = new ArrayList<>(points.size());
		for (Point point : points) {
			latLngs.add(new LatLng(point.getLatitude(), point.getLongitude()));
		}
		return latLngs;
	}

	protected Area(Parcel in) {
		this.id = in.readString();
		this.name_nl = in.readString();
		this.name_en = in.readString();
		this.color = in.readString();
		this.points = in.createTypedArrayList(Point.CREATOR);
	}

	public static final Creator<Area> CREATOR = new Creator<Area>() {
		@Override
		public Area createFromParcel(Parcel source) {return new Area(source);}

		@Override
		public Area[] newArray(int size) {return new Area[size];}
	};

	@Override
	public int describeContents() { return 0; }

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.id);
		dest.writeString(this.name_nl);
		dest.writeString(this.name_en);
		dest.writeString(this.color);
		dest.writeTypedList(this.points);
	}

}
