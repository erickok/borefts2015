package nl.brouwerijdemolen.borefts2013.api;

import android.os.Parcel;
import android.os.Parcelable;

public class Point implements Parcelable {

	private float latitude;
	private float longitude;

	protected Point() {
	}

	public float getLatitude() {
		return latitude;
	}

	public float getLongitude() {
		return longitude;
	}

	protected Point(Parcel in) {
		this.latitude = in.readFloat();
		this.longitude = in.readFloat();
	}

	public static final Creator<Point> CREATOR = new Creator<Point>() {
		@Override
		public Point createFromParcel(Parcel source) {return new Point(source);}

		@Override
		public Point[] newArray(int size) {return new Point[size];}
	};

	@Override
	public int describeContents() { return 0; }

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeFloat(this.latitude);
		dest.writeFloat(this.longitude);
	}

}
