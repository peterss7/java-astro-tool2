package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Scanner;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebClientOptions;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import service.AstroEvents;

public class AstronomyToolApp {

	private static State currentState;
	private static final String [] URLS = {
			"C:\\Users\\peter\\eclipse-workspace\\astro-tool\\src\\main\\res\\ASCII-saturn.txt",
			"C:\\Users\\peter\\eclipse-workspace\\astro-tool\\src\\main\\res\\eclipse-ascii.txt",
			"C:\\Users\\peter\\eclipse-workspace\\astro-tool\\src\\main\\res\\title-screen-ascii.txt"
	};	
	
	private static boolean isRunning;	
	private static AstroEvents astroEvents;	
	
	public static void main(String[] args) throws IOException {
		
		drawFile(URLS[2]);
		System.out.println("@@@@@@@@@@@@@@@@@@@@@@@      WELCOME TO THE ASTRONOMY EVENTS FINDER      ###########################");
		System.out.println();
		
		astroEvents = new AstroEvents();
		
		isRunning = true;
		currentState = State.MAIN_MENU;
		
		while (isRunning) {
			runMainMenu();	
		}				
	}
	
	
	public static void runMainMenu() throws IOException {
		
		
		
		if (currentState.equals(State.MAIN_MENU)) {
		
			System.out.println("You have two options:\n" +
					"1) Find information about lunar and solar eclipses visible on your continent in the decade of your choice.\n" + 
					"2) Enter any date between 1900 and 2500 to find out about naked eye astronomical events on either that date, or the soonest after it.");
			
			System.out.println();
			System.out.println("Enter 1 or 2 to make your choice. Enter \"exit\" to exit.");
			
					
			boolean isGettingInput = true;
			String choice = null;
			int choiceInt = 0;		
			Scanner sc = new Scanner(System.in);
			
			
			while(isGettingInput) {
				
				choice = sc.nextLine();
					
				if (choice.equals("exit")) {
					choice = null;
					isRunning = false;
					isGettingInput = false;
					break;
				}
				try {					
					if (Integer.parseInt(choice) == 1 ||
							Integer.parseInt(choice) == 2) {
						
						isGettingInput = false;
						choiceInt = Integer.parseInt(choice);	
					}
							
				} catch(Exception e) {
					isGettingInput = true;
				}
			}
			
			if (choice != null) {
			
				if (choiceInt == 2) {
					
					drawFile(URLS[0]);
					
					System.out.println("BBBBBBBBBQBBBBBBBBBQBBBQBBBBB     THE ASTRONOMICAL EVENTS FINDER     BBBBBBBBQBBBQBQBQBBBBBBBBBBBB");
					System.out.println();
					
					initAstroEvents();
					currentState = State.ASTRO_EVENTS;
				}
				
				if (choiceInt == 1) {
					
					drawFile(URLS[1]);
					
					System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@     THE ECLIPSE LOOKER UPPER     @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
					System.out.println();
					
					initEclipse();
					currentState = State.ECLIPSES;
				}				
			
			}
		}
		else if (currentState.equals(State.ECLIPSES)) {	
			
			System.out.println("Enter the number of an eclipse to get more information about it. Type \"exit\" to exit.: ");
			
			Scanner sc2 = new Scanner(System.in);
			boolean isGettingInput2 = true;
			
			while(isGettingInput2) {
			
				String choice2 = sc2.nextLine();
				
				if (choice2.equals("exit")) {
					
					isGettingInput2 = false;
					currentState = State.MAIN_MENU;
					
					break;
				}
				else {					
				
					try {
						
						if (choice2.equals("exit")) {
							
							isGettingInput2 = false;
							break;
						}
						else if (Integer.parseInt(choice2) >= 1 && Integer.parseInt(choice2)< 30) {
							
							isGettingInput2 = false;
							
							//astroEvents.getEclipseDetail(choice);
							System.out.println("Feature not available at this time. Returning to main menu.");
							currentState = State.MAIN_MENU;
						}
						else {
							isGettingInput2 = true;
						}
					} catch (Exception e) {
						
						e.printStackTrace();
						
						System.out.println("That was not a valid choice: " + choice2);
						isGettingInput2 = true;
					}
				}
			}
		
		}
		else if (currentState.equals(State.ASTRO_EVENTS)) {
			
			Scanner sc3 = new Scanner (System.in);
			
			System.out.println("Would you like to look up another date? (y/n)");
			
			String choice3 = sc3.nextLine();
			boolean isGettingInput3 = true;
			
			while(isGettingInput3) {
				try {					
					if (choice3.equalsIgnoreCase("y")) {
						initAstroEvents();
						isGettingInput3 = false;
					}
					else if (choice3.equalsIgnoreCase("n")) {
						isGettingInput3 = false;
						currentState = State.MAIN_MENU;
					}
					else {
						isGettingInput3 = true;
						System.out.println("Please choose \"y\" or \"n\"");
					}
					
				} catch (Exception e) {					
					e.printStackTrace();
					System.out.println("Please choose \"y\" or \"n\"");
					isGettingInput3 = true;					
				}				
			}
		}
		else {
			System.out.println("Please do not break my program by taking away its State!");
		}
		
	}
	
	public static void initEclipse() {
		
		
		
		ArrayList<String> eclipseStrings = new ArrayList<>();
		
		System.out.print("Enter a decade to see eclipses in that decade (i.e. enter 30 to see eclipses from the 2030s).\n"
				+ "This program only has information on eclipses between 2000 and 2099, so please enter a number between 0 and 99: ");
		astroEvents.getDate(State.ECLIPSES);
		try {
			eclipseStrings = astroEvents.searchForEclipses();
			astroEvents.printFormattedEclipseData(eclipseStrings); 
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void initAstroEvents() throws IOException {
		
		System.out.print("Enter a date to learn about astronomical events on or near that day (MM/DD/YYYY): ");
		astroEvents.getDate(State.ASTRO_EVENTS);
		astroEvents.searchForEvent();		
		
	}
	
	
	public static void drawFile(String url) {
		
		Scanner sc2 = null;
		
		try {
			sc2 = new Scanner(new File(url));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		while (sc2.hasNext()) {
			System.out.println(sc2.nextLine());
		}		
		
	}

}
