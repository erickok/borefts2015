package nl.brouwerijdemolen.borefts2013.api;

import nl.brouwerijdemolen.borefts2013.R;
import android.content.res.Resources;
import android.os.Parcel;
import android.os.Parcelable;

public class Style implements Comparable<Style>, Parcelable {

	private int id;
	private String name;
	private int color;
	private int abv;
	private int body;
	private int bitterness;
	private int sweetness;
	private int acidity;

	protected Style() {
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public int getColor() {
		return color;
	}

	public int getAbv() {
		return abv;
	}

	public int getBody() {
		return body;
	}
	
	public int getBitterness() {
		return bitterness;
	}

	public int getSweetness() {
		return sweetness;
	}

	public int getAcidity() {
		return acidity;
	}

	@Override
	public int compareTo(Style another) {
		return name.compareTo(another.getName());
	}

	public int getColorResource(Resources res) {
		switch (color) {
		case 1:
			return res.getColor(R.color.style_1);
		case 2:
			return res.getColor(R.color.style_2);
		case 3:
			return res.getColor(R.color.style_3);
		case 4:
			return res.getColor(R.color.style_4);
		case 5:
			return res.getColor(R.color.style_5);
		default:
			return res.getColor(R.color.style_unknown);
		}
	}

	public Style(Parcel in) {
		this.id = in.readInt();
		this.name = in.readString();
		this.color = in.readInt();
		this.abv = in.readInt();
		this.bitterness = in.readInt();
		this.sweetness = in.readInt();
		this.acidity = in.readInt();
	}
	
    public static final Creator<Style> CREATOR = new Creator<Style>() {
    	public Style createFromParcel(Parcel in) {
    		return new Style(in);
    	}

		public Style[] newArray(int size) {
		    return new Style[size];
		}
    };

	@Override
	public int describeContents() {
		return 0;
	}
	
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(id);
		dest.writeString(name);
		dest.writeInt(color);
		dest.writeInt(abv);
		dest.writeInt(bitterness);
		dest.writeInt(sweetness);
		dest.writeInt(acidity);
	}
	
}
