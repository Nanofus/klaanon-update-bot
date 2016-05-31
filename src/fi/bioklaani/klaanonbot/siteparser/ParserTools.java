package fi.bioklaani.klaanonbot.siteparser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import fi.bioklaani.klaanonbot.BotException;
import fi.bioklaani.klaanonbot.Post;
import fi.bioklaani.klaanonbot.Utils;

/** Contains tools related to parsing the Klaanon website.*/
public class ParserTools {

	final static String SITE_URL = "http://klaanon.bioklaani.fi/kaikki-ropeosat/";
	
	final static Pattern POST_DATA_PATTERN =
			Pattern.compile("<ul class=\"lcp_catlist\" id=\"lcp_instance_0\">"
			+ "(.*?)"
			+ "<\\/ul>");
			
	final static Pattern SINGLE_POST_PATTERN =
			Pattern.compile("<li>(.*?)<\\/li>");
			
	final static Pattern POST_URL_PATTERN =
			Pattern.compile("href=\"(.*?)\"");
			
	final static Pattern POST_TITLE_PATTERN =
			Pattern.compile("title=\"(.*?)\"");
			
	final static Pattern POST_SPAN_PATTERN =
			Pattern.compile("<span>(.*?)<\\/span>");
	
	static String readSite() {
		try {
			URLConnection connection = new URL(SITE_URL).openConnection();
			
			return Utils.readBufferedReader(new BufferedReader(
					new InputStreamReader(connection.getInputStream(), "UTF-8")));
		} catch(IOException exc) {
			throw new BotException(exc, "Failed to load " + SITE_URL);
		}
	}
	
	static List<String> extractPosts(String html) {
		Matcher matcher = POST_DATA_PATTERN.matcher(html);
		
		if(!matcher.find()) {
			throw new BotException("Failed to parse site HTML");
		}
		
		String postData = matcher.group();
		
		List<String> posts = new ArrayList<>();
		Matcher postMatcher = SINGLE_POST_PATTERN.matcher(postData);
		
		while(postMatcher.find()) {
			posts.add(postMatcher.group(1));
		}
		
		if(posts.isEmpty()) { throw new BotException("No posts found"); }
		
		return posts;
	}
	
	static Post parsePost(String html) {
		Matcher urlMatcher = POST_URL_PATTERN.matcher(html);
		Matcher titleMatcher = POST_TITLE_PATTERN.matcher(html);
		Matcher spanMatcher = POST_SPAN_PATTERN.matcher(html);
		
		if(!urlMatcher.find()) { throw new BotException("Couldn't parse URL in " + html); }
		String url = urlMatcher.group(1);
		
		if(!titleMatcher.find()) { throw new BotException("Couldn't parse title in " + html); }
		String title = titleMatcher.group(1);
		
		if(!spanMatcher.find()) { throw new BotException("Couldn't parse date in " + html); }
		String date = spanMatcher.group(1);
		
		if(!spanMatcher.find()) { throw new BotException("Couldn't parse author in " + html); }
		String author = spanMatcher.group(1);
		
		return new Post(title, author, DateTools.toInstant(date.trim()), url);
	}
}
