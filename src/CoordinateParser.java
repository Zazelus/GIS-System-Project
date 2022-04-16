/**
 * Skeleton class containing parsing methods used by GISCommandParser.
 * @author Mansour Najah
 */
public class CoordinateParser {
	
	/**
	 * Empty constructor, we just want to use the static methods.
	 */
	public CoordinateParser() {

	}
	
	/**
	 * Parses string into DMS format for latitude.
	 * @param latitude the string to be parsed
	 * @return a formatted string in DMS
	 */
	public static String parseLatitude(String latitude) {
		if (latitude.length() == 0 || latitude.equals("Unknown")) {
			return "Coordinate is not given";
		}
		
		String d = latitude.substring(0, 2);
		String m;
		String s;
		String h = "";
		
		// 1
		if (latitude.charAt(2) == '0') {
			m = String.valueOf(latitude.charAt(3));
		} else {
			m = latitude.substring(2, 4);
		}
		
		// 2
		if (latitude.charAt(4) == '0') {
			s = String.valueOf(latitude.charAt(5));
		} else {
			s = latitude.substring(4, 6);
		}
		
		// 1 & 2 ignore the 0 if necessary.
		
		switch(latitude.charAt(6)) {
		case 'N':
			h = "North";
			break;
		case 'S':
			h = "South";
			break;
		}
		
		return d + "d " + m + "m " + s + "s " + h;
	} 
	
	/**
	 * Parses string into DMS format for longitude.
	 * @param latitude the string to be parsed
	 * @return a formatted string in DMS
	 */
	public static String parseLongitude(String longitude) {
		if (longitude.length() == 0 || longitude.equals("Unknown")) {
			return "Coordinate is not given";
		}
		
		String d = longitude.substring(1, 3);
		String m;
		String s;
		String h = "";
		
		if (longitude.charAt(3) == '0') {
			m = String.valueOf(longitude.charAt(4));
		} else {
			m = longitude.substring(3, 5);
		}
		
		if (longitude.charAt(5) == '0') {
			s = String.valueOf(longitude.charAt(6));
		} else {
			s = longitude.substring(5, 7);
		}
		
		switch(longitude.charAt(7)) {
		case 'E':
			h = "East";
			break;
		case 'W':
			h = "West";
			break;
		}
		
		return d + "d " + m + "m " + s + "s " + h;
	}

}
