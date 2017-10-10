package nl.brouwerijdemolen.borefts2013.api;

import android.content.res.Resources;
import android.os.Parcel;
import android.os.Parcelable;

import nl.brouwerijdemolen.borefts2013.R;

public class Beer {

	private int id;
	private String name;
	private int brewerId;
	private int styleId;
	private float abv;
	private boolean oakAged;
	private boolean festivalBeer;
	private String tags;
	private int ratebeerId;
	private String untappdId;
	private int serving;
	private int colour;
	private int body;
	private int bitterness;
	private int sweetness;
	private int acidity;

	// These are not provided by the server, but loaded manually and attached with their setters
	private volatile Brewer brewer;
	private volatile Style style;

	Beer() {
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public int getBrewerId() {
		return brewerId;
	}

	public int getStyleId() {
		return styleId;
	}

	public float getAbv() {
		return abv;
	}

	public boolean isOakAged() {
		return oakAged;
	}

	public boolean isFestivalBeer() {
		return festivalBeer;
	}

	public String getTags() {
		return tags;
	}

	public int getRatebeerId() {
		return ratebeerId;
	}

	public int getUntappdId() {
		return untappdId.equals("")? -1: Integer.parseInt(untappdId);
	}

	public int getServing() {
		return serving;
	}

	public int getColour() {
		return colour;
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

	public void setBrewer(Brewer brewer) {
		this.brewer = brewer;
	}

	public Brewer getBrewer() {
		return brewer;
	}

	public void setStyle(Style style) {
		this.style = style;
	}

	public Style getStyle() {
		return style;
	}

	public int getAbvIndication() {
		if (abv >= 0) {
			return Math.min(Math.max((int) Math.round(abv / 2.3), 1), 5);
		}
		return style.getAbv();
	}

	public int getBitternessIndication() {
		if (bitterness > 0) {
			return bitterness;
		}
		return style.getBitterness();
	}

	public int getSweetnessIndication() {
		if (sweetness > 0) {
			return sweetness;
		}
		return style.getSweetness();
	}

	public int getAcidityIndication() {
		if (acidity > 0) {
			return acidity;
		}
		return style.getAcidity();
	}

	public int getColorIndicationResource(Resources res) {
		int c = style.getColor();
		if (colour > 0) {
			c = colour;
		}
		switch (c) {
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

	public String getServingResource(Resources res) {
		switch (serving) {
			case 1:
				return res.getString(R.string.info_serving_cask);
			case 2:
				return res.getString(R.string.info_serving_bottle);
			default:
				return res.getString(R.string.info_serving_keg);
		}
	}

}
