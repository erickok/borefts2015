package nl.brouwerijdemolen.borefts2013.api;

import java.util.List;

public class Pois {

	public static final String POIS_URL = "http://2312.nl/borefts2017/pois.php";

	private List<Area> areas;
	private List<Poi> pois;
	private int revision;

	public List<Area> getAreas() {
		return areas;
	}

	public List<Poi> getPois() {
		return pois;
	}

	public int getRevision() {
		return revision;
	}

}
