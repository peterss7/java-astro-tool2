package service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Month;
import java.util.ArrayList;
import java.util.Scanner;

import org.jsoup.select.Elements;

import main.State;
import data.UrlHandler;

public class AstroEvents {

	
	private int targetMonth;
	private int targetDay;
	private int targetYear;
	private int targetDecade;

	
	public static final String ANSI_RESET = "\u001B[0m";
	public static final String ANSI_BLACK = "\u001B[30m";
	public static final String ANSI_RED = "\u001B[31m";
	public static final String ANSI_GREEN = "\u001B[32m";
	public static final String ANSI_YELLOW = "\u001B[33m";
	public static final String ANSI_BLUE = "\u001B[34m";
	public static final String ANSI_PURPLE = "\u001B[35m";
	public static final String ANSI_CYAN = "\u001B[36m";
	public static final String ANSI_WHITE = "\u001B[37m";
	
	public static final String ANSI_BLACK_BACKGROUND = "\u001B[40m";
	public static final String ANSI_RED_BACKGROUND = "\u001B[41m";
	public static final String ANSI_GREEN_BACKGROUND = "\u001B[42m";
	public static final String ANSI_YELLOW_BACKGROUND = "\u001B[43m";
	public static final String ANSI_BLUE_BACKGROUND = "\u001B[44m";
	public static final String ANSI_PURPLE_BACKGROUND = "\u001B[45m";
	public static final String ANSI_CYAN_BACKGROUND = "\u001B[46m";
	public static final String ANSI_WHITE_BACKGROUND = "\u001B[47m";
	
	private static final String ECLIPSE_FILEPATH_1 = "C:\\Users\\peter\\eclipse-workspace\\astro-tool\\src\\main\\res\\eclipseTemp01.txt";
	//private static final String COUNTRIES_LIST_FILEPATH = "C:\\Users\\peter\\eclipse-workspace\\astro-tool\\src\\main\\res\\countries.csv";
	//private static final String GOOGLE_TEMP_HTML_FILE = "C:\\Users\\peter\\eclipse-workspace\\astro-tool\\src\\main\\res\\googleSearch.html";
	private static final String BASE_DECADE_ECLIPSE_URL = "https://theskylive.com/solar-eclipses-by-decade?decade=";
	
	
	private static String [] dates;
	private static String [] eclipseType;
	
	public AstroEvents() {
		
	
		
	}
	
	public String parseDate(String element) {
		
		String date = "";
		String month = "";
		String day = "";
		String year = "";
		
		int indexOfFirstSpace = element.indexOf(" ");
		int indexOfSecondSpace = element.indexOf(" ", (indexOfFirstSpace + 1));
		int indexOfThirdSpace = element.indexOf(" ", (indexOfSecondSpace + 1));
		
		String monthSection = element.substring(0, element.indexOf(" "));
		String daySection = element.substring((indexOfFirstSpace + 1), indexOfSecondSpace);
		String yearSection = element.substring((indexOfSecondSpace + 1), indexOfThirdSpace);
		
		if (monthSection.equals("January")) {
			month = "01";
		}
		else if (monthSection.equals("February")) {
			month = "02";
		}
		else if (monthSection.equals("March")) {
			month = "03";
		}
		else if (monthSection.equals("April")) {
			month = "04";
		}
		else if (monthSection.equals("May")) {
			month = "05";
		}
		else if (monthSection.equals("June")) {
			month = "06";
		}
		else if (monthSection.equals("July")) {
			month = "07";
		}
		else if (monthSection.equals("August")) {
			month = "08";
		}
		else if (monthSection.equals("September")) {
			month = "09";
		}
		else if (monthSection.equals("October")) {
			month = "10";
		}
		else if (monthSection.equals("Novemvber")) {
			month = "11";
		}
		else if (monthSection.equals("December")) {
			month = "12";
		}
		
		
		try {
					
			if (Integer.parseInt(daySection) >= 0 && Integer.parseInt(daySection) < 10) day = "0" + daySection;
			if (Integer.parseInt(daySection) >= 10 && Integer.parseInt(daySection) < 32) day = daySection;
			
			if (Integer.parseInt(yearSection) >= 2000 && Integer.parseInt(yearSection) < 3000) year = yearSection;
			
			date = month + "/" + day + "/" + year;
			
			return date;
		}
		catch (NumberFormatException e) {
			
			e.printStackTrace();

			return null;
		}
		
	}
	
	public void parseDate(String date, State state){
		
		
		if (state.equals(State.ASTRO_EVENTS)) {
			targetMonth = Integer.parseInt(date.substring(0, 2));
			targetDay = Integer.parseInt(date.substring(3, 5));
			targetYear = Integer.parseInt(date.substring(6, 10));
		}
		else if (state.equals(State.ECLIPSES)) {
			
			int dateInt = Integer.parseInt(date);
			
			if (dateInt >= 0 && dateInt < 10) {
				targetDecade = 2000;
			}
			else if (dateInt >= 10 && dateInt < 20) {
				targetDecade = 2010;
			}
			else if (dateInt >= 20 && dateInt < 30) {
				targetDecade = 2020;
			}
			else if (dateInt >= 30 && dateInt < 40) {
				targetDecade = 2030;
			}
			else if (dateInt >= 40 && dateInt < 50) {
				targetDecade = 2040;
			}
			else if (dateInt >= 50 && dateInt < 60) {
				targetDecade = 2050;
			}
			else if (dateInt >= 60 && dateInt < 70) {
				targetDecade = 2060;
			}
			else if (dateInt >= 70 && dateInt < 80) {
				targetDecade = 2070;
			}
			else if (dateInt >= 80 && dateInt < 90) {
				targetDecade = 2080;
			}
			else if (dateInt >= 90 && dateInt < 100) {
				targetDecade = 2090;
			}
			else {
				System.out.println("I hope I never see this statement executed because it will really confuse me if that happens.");
			}
		}
		else {
			System.out.println("Somehow the program has no state... How did you manage that?");
		}
		
	}
	
	public void getDate(State state) {
		
		Scanner sc = new Scanner(System.in);
		boolean isGettingDate = true;
		String input = null;
		
		while(isGettingDate) {
			
			input = sc.nextLine();
			
			if (checkValidity(input, state)) {
				isGettingDate = false;
				parseDate(input, state);
			}
			
			
		}
		
		
		
	}
	
	/*
	public ArrayList<String> findLocation(String eclipseString) throws FileNotFoundException {
		
		
		Scanner csvReader = new Scanner(new File(COUNTRIES_LIST_FILEPATH));
		
		System.out.println(eclipseString + " this is the eclipse string.");
		
		String searchDate = parseDate(eclipseString);
		ArrayList<String> foundLocations = new ArrayList<String>();
		
		String googleSearch = null;
		
		try {
			googleSearch = UrlHandler.performGoogleSearch(searchDate, GOOGLE_TEMP_HTML_FILE);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		while(csvReader.hasNext()) {
			
			String csvCurrentLine = csvReader.nextLine();
			
			try {			
				if (googleSearch.contains(csvCurrentLine)){
					foundLocations.add(csvCurrentLine);
				}
			} catch (Exception e) {
				System.out.println("This csv file has no line to find even though hasNext was true");
			}			
		}
		
		for (int i = 0; i < foundLocations.size(); i++) {
			System.out.println(foundLocations.get(i) + " ...found locations");
		}
		
		return foundLocations;
		
	}
	*/
	
	public ArrayList<String> searchForEclipses() throws IOException {
		
		String eclipseUrl = BASE_DECADE_ECLIPSE_URL + targetDecade;
		UrlHandler.createEclipseHtmlTempFile(UrlHandler.renderedJsHtmlString(eclipseUrl), ECLIPSE_FILEPATH_1);
		Elements eclipseElements = UrlHandler.getElements(ECLIPSE_FILEPATH_1, "a");
		
		ArrayList<String> eclipseStrings = new ArrayList<String>(); 
		
		for (int i = 0; i < eclipseElements.size(); i++) {
			
			String element = eclipseElements.get(i).text();		
			
			try {
				if (containsMonth(element)) {
					eclipseStrings.add(element);				
				}
			} catch (StringIndexOutOfBoundsException e) {
				System.out.println(e.toString());
			}
		}
		
		
		return eclipseStrings;
	}
	
	public void printFormattedEclipseData(ArrayList<String> eclipseStrings) {		
		
		dates = new String [eclipseStrings.size()];
		eclipseType = new String [eclipseStrings.size()];		
		
		for (int i = 0; i < eclipseStrings.size(); i++) {
			dates[i] = parseDate(eclipseStrings.get(i));
			eclipseType[i] = findEclipseType(eclipseStrings.get(i));
			System.out.println(dates[i] + " ..... " + eclipseType[i]);			
		}
		
	}		
	
	public String findEclipseType(String element) {
		
		int indexOfFirstSpace = element.indexOf(" ");
		int indexOfSecondSpace = element.indexOf(" ", (indexOfFirstSpace + 1));
		int indexOfThirdSpace = element.indexOf(" ", (indexOfSecondSpace + 1));
		
		return element.substring(indexOfThirdSpace, element.length());
	}
	
	public boolean containsMonth(String eclipseElement) {
		
		if (eclipseElement.contains("January") || eclipseElement.contains("February") || 
				eclipseElement.contains("March") || eclipseElement.contains("April") ||
				eclipseElement.contains("May") || eclipseElement.contains("June") ||
				 eclipseElement.contains("July")  || eclipseElement.contains("August") 
				 || eclipseElement.contains("September") || eclipseElement.contains("October") 
				 || eclipseElement.contains("November") || eclipseElement.contains("December") ) {
			return true;
		}
		else return false;
		
	}

	
	
	public void searchForEvent() throws IOException {
		
		String astroUrl = "https://in-the-sky.org/newscalyear.php?year=" + targetYear + "&maxdiff=1#datesel";
		Elements astroEvents = UrlHandler.getElements(astroUrl,"td");
		
		int day = 0;
		
		boolean foundTargetMonth = false;		
		boolean elementIsNumber = false;
		boolean resolvedQuery = false;
		
		for (int i = 0; i < astroEvents.size(); i++) {
			
			elementIsNumber = false;
			
			String currentEvent = astroEvents.get(i).text();
			
			try {
				if (Integer.parseInt(currentEvent) >= 1 && 
					Integer.parseInt(currentEvent) <= 31) {					
					
					day = Integer.parseInt(currentEvent);
					elementIsNumber = true;
					
				}
			}
			catch (Exception NumberFormatException) {
				//System.out.println("bad int format");
			}
			
			if (day >= targetDay && foundTargetMonth) {
				resolvedQuery = true;
			}
			if (currentEvent.toUpperCase().equals(Month.of(targetMonth).name())) {
				foundTargetMonth = true;
			}
			if (targetMonth != 12 && currentEvent.toUpperCase().equals(Month.of(targetMonth + 1).name())) {
				break;
			}
			if (foundTargetMonth && !elementIsNumber && resolvedQuery) {
				printFormattedResult(targetMonth + "/" + day + "/" + targetYear + ": " + currentEvent);
				break;
			}
		}

	}
	
	
	public void printFormattedResult(String result) {
	
		
		
		if (result.contains("Conjunction") && result.indexOf("Conjunction") >= 10) {			
			String [] conjunctions = result.split("Conjunction");
			result = conjunctions[0] + "\n" + "Conjunction " + conjunctions[1];
		}
		 
		if (result.contains("Close approach") && result.indexOf("Close approach") >= 12) {
			
			String [] approaches = result.split("Close approach");
			result = approaches[0] + "\n" + "Close approach " + approaches[1];
			
		
		}
		
		/*
		 * 
		 * 
		 *   i'll try to finish this later
		 * 
		 * 
		 * 
		 */
		
		
		// This color business below also needs work, I would do it totally different if I had time.
		String newColor = null;
		
		if (result.contains("Mercury")) {
			newColor = ANSI_CYAN;
		}
		else if (result.contains("Venus")) {
			newColor = ANSI_YELLOW;
		}
		else if (result.contains("Moon")) {
			newColor = ANSI_WHITE + ANSI_BLACK_BACKGROUND;
		}
		else if (result.contains("Mars")) {
			newColor = ANSI_RED;
		}
		else if (result.contains("Jupiter")) {
			newColor = ANSI_GREEN;
		}
		else if (result.contains("Saturn")) {
			newColor = ANSI_PURPLE;
		}
		else if (result.contains("meteor shower")) {
			newColor = ANSI_BLUE + ANSI_WHITE_BACKGROUND;
		}
		else {
			newColor = "";
		}
		
		System.out.println(newColor + result + ANSI_GREEN + ANSI_BLACK_BACKGROUND);

	}
	
	
	public static boolean checkValidity(String date, State state) {
			
		if (date == null) {
			return true;
		}
		
		if (state.equals(State.ASTRO_EVENTS)) {
		
			String month = null;
			String day = null;
			String year = null;
			String characters = null;
			
			try {
				month = date.substring(0, 2);
				day = date.substring(3, 5);
				year = date.substring(6, 10);
				characters = date.substring(2, 3) + date.substring(5, 6);
			} catch (Exception e) {
				System.out.println("You probably didn't enter a valid string. ");
				return false;
			}
			boolean validChars = false;			
			
			if (characters.equals("//")) validChars = true; 
			
			try {
				
				int monthInt = Integer.parseInt(month);
				int dayInt = Integer.parseInt(day);
				int yearInt = Integer.parseInt(year);
				
				if (yearInt <= 1900 || yearInt > 2099) {
					System.out.println("Please enter a year between 1900 and 2500: ");
					return false;
				}
				
				if (monthInt >= 1 && monthInt <= 12 && dayInt >= 1 && dayInt <= 31 &&
						yearInt >= 1900 && yearInt <= 2500 && validChars) {
					return true;
				}
					
				
			} catch (Exception e){
				
				e.printStackTrace();				
				System.out.println("Please enter a date in the specified format: ");
				
				return false;
			}
			
			System.out.println("Please enter a date in the specified format: ");
			
			return false;
		}
	
		else if (state.equals(State.ECLIPSES)) {
			
			int decadeInt = 0;
			
			try {				
				decadeInt = Integer.parseInt(date);				
			} catch (Exception e) {
				System.out.println("You did not enter a valid decade.");
				
				return false;
			}
			
			if (decadeInt >= 0 && decadeInt < 100) {
				return true;
			}
			else {
				return false;
			}
			
		}
		else {
			return false;
		}
	}
}
