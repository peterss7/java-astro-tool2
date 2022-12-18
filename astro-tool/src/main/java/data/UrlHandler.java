package data;

import java.io.BufferedWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

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

public class UrlHandler {
	
	/*
	public static final String GOOGLE_SEARCH_URL = "https://www.google.com/search";
	
	public static String performGoogleSearch(String date, String filepath) throws IOException {
		
		String searchTerm = date + " eclipse country";
		int num = 10;
		
		
		String searchUrl = GOOGLE_SEARCH_URL + "?q="+searchTerm+"&num="+num;
		
		System.out.println(searchUrl + " is url");
		
		try (final WebClient webClient = new WebClient(BrowserVersion.BEST_SUPPORTED)) {
		    WebClientOptions options = webClient.getOptions();
		    options.setCssEnabled(true);
		    webClient.setAjaxController(new NicelyResynchronizingAjaxController());
		    options.setTimeout(30000);
		    webClient.addRequestHeader("Access-Control-Allow-Origin", "*");

		    HtmlPage page = null;
		    
			try {
				page = webClient.getPage(searchUrl);
			} catch (FailingHttpStatusCodeException | IOException e) {				
				System.out.println("Loading.... ");
			}
			
			String googleSearchHtmlResult = page.asXml();		
				
			//System.out.println(googleSearchHtmlResult);	
			
			return googleSearchHtmlResult;
			
			
		}
		
	}
	*/
	
	public static Document getDocument(String url) {
		
		Connection conn = Jsoup.connect(url);
		
		Document doc = null;
		
		try {
			doc = Jsoup.connect(url).get();
		} catch (IOException e) {		
			e.printStackTrace();
		}
		
		return doc;		
	}
	
	public static Elements getElements(String url, String tag) throws IOException {		 
		
		if (url.substring(0,2).equals("C:")) {
			
			File renderedHtml = new File(url);			
			Document htmlDocument = Jsoup.parse(renderedHtml, "UTF-8", ""); 
			
			return htmlDocument.getElementsByTag(tag);
			
		}
		else return getDocument(url).getElementsByTag(tag);
		
	}
	
	public static void createEclipseHtmlTempFile(String eclipseHtml, String filepath) throws IOException {		
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(filepath));
		writer.write(eclipseHtml);				    
		writer.close();		
	}
	public static String renderedJsHtmlString(String url) {
		
		// this code takes the code of a url and renders it so text produced by JS can be scraped.
		try (final WebClient webClient = new WebClient(BrowserVersion.BEST_SUPPORTED)) {
		    WebClientOptions options = webClient.getOptions();
		    options.setCssEnabled(true);
		    webClient.setAjaxController(new NicelyResynchronizingAjaxController());
		    options.setTimeout(30000);
		    webClient.addRequestHeader("Access-Control-Allow-Origin", "*");

		    HtmlPage page = null;
			try {
				page = webClient.getPage(url);
			} catch (FailingHttpStatusCodeException | IOException e) {
				
				System.out.println("Loading.... ");
				
				//e.printStackTrace();
			}

		    // give the javascript time to render
		    webClient.setJavaScriptTimeout(10000);
		    webClient.waitForBackgroundJavaScript(10000);
		    // wait...
		    try {
				Thread.sleep(8000);
			} catch (InterruptedException e) {		
				System.out.println("Loading.... ");				
				//e.printStackTrace();
			}

		    return page.asXml();
		    
		} catch (Exception e) {
			
			e.printStackTrace();			
			System.out.println("Loading failed.... ");
			
			return null;
		}
		
	}
	
}
