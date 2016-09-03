package nl.brouwerijdemolen.borefts2013.api;

import android.os.Parcel;
import android.os.Parcelable;

public class Brewer implements Comparable<Brewer>, Parcelable {

	private int id;
	private String logoUrl;
	private String name;
	private String shortName;
	private String sortName;
	private String city;
	private String country;
	private String description;
	private String website;
	private float latitude;
	private float longitude;

	Brewer() {
	}
	
	public int getId() {
		return id;
	}
	
	public String getLogoUrl() {
		return logoUrl;
	}

	public String getName() {
		return name;
	}

	public String getShortName() {
		return shortName;
	}
	
	public String getSortName() {
		return sortName;
	}

	public String getCity() {
		return city;
	}

	public String getCountry() {
		return country;
	}

	public String getDescription() {
		return description;
	}
	
	public String getWebsite() {
		return website;
	}
	
	public float getLatitude() {
		return latitude;
	}
	
	public float getLongitude() {
		return longitude;
	}

	@Override
	public int compareTo(Brewer another) {
		return sortName.compareTo(another.getSortName());
	}

	public Brewer(Parcel in) {
		this.id = in.readInt();
		this.logoUrl = in.readString();
		this.name = in.readString();
		this.shortName = in.readString();
		this.sortName = in.readString();
		this.city = in.readString();
		this.country = in.readString();
		this.description = in.readString();
		this.website = in.readString();
		this.latitude = in.readFloat();
		this.longitude = in.readFloat();
	}
	
    public static final Creator<Brewer> CREATOR = new Creator<Brewer>() {
    	public Brewer createFromParcel(Parcel in) {
    		return new Brewer(in);
    	}

		public Brewer[] newArray(int size) {
		    return new Brewer[size];
		}
    };

	@Override
	public int describeContents() {
		return 0;
	}
	
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(id);
		dest.writeString(logoUrl);
		dest.writeString(name);
		dest.writeString(shortName);
		dest.writeString(sortName);
		dest.writeString(city);
		dest.writeString(country);
		dest.writeString(description);
		dest.writeString(website);
		dest.writeFloat(latitude);
		dest.writeFloat(longitude);
	}
	
}
