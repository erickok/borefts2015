package nl.brouwerijdemolen.borefts2013.api;

import java.util.List;

public class Brewers {

	public static final String BREWERS_URL = "http://2312.nl/borefts2015/brewers.php";

	private List<Brewer> brewers;
	private int revision;

	public List<Brewer> getBrewers() {
		return brewers;
	}

	public int getRevision() {
		return revision;
	}

}
