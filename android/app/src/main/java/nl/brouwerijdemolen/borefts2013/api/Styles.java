package nl.brouwerijdemolen.borefts2013.api;

import java.util.List;

public class Styles {

	public static final String STYLES_URL = "http://2312.nl/borefts2016/styles.php";

	private List<Style> styles;
	private int revision;

	public List<Style> getStyles() {
		return styles;
	}

	public int getRevision() {
		return revision;
	}

}
