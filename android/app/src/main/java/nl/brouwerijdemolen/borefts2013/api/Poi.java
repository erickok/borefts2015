package nl.brouwerijdemolen.borefts2013.api;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

public class Poi implements Parcelable {

	private String id;
	private String name_nl;
	private String name_en;
	private String marker;
	private Point point;

	Poi() {
	}

	public String getId() {
		return id;
	}

	public String getName_nl() {
		return name_nl;
	}

	public String getName_en() {
		return name_en;
	}

	public String getMarker() {
		return marker;
	}

	public Point getPoint() {
		return point;
	}

	public LatLng getPointLatLng() {
		return new LatLng(point.getLatitude(), point.getLongitude());
	}

	protected Poi(Parcel in) {
		this.id = in.readString();
		this.name_nl = in.readString();
		this.name_en = in.readString();
		this.marker = in.readString();
		this.point = in.readParcelable(Point.class.getClassLoader());
	}

	public static final Creator<Poi> CREATOR = new Creator<Poi>() {
		@Override
		public Poi createFromParcel(Parcel source) {return new Poi(source);}

		@Override
		public Poi[] newArray(int size) {return new Poi[size];}
	};

	@Override
	public int describeContents() { return 0; }

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.id);
		dest.writeString(this.name_nl);
		dest.writeString(this.name_en);
		dest.writeString(this.marker);
		dest.writeParcelable(this.point, flags);
	}

}
