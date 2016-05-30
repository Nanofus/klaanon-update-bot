package fi.bioklaani.klaanonbot.siteparser;

import java.util.List;
import java.util.stream.Collectors;

import fi.bioklaani.klaanonbot.Post;
import fi.bioklaani.klaanonbot.PostList;

/** Parses the Klaanon site.*/
public class SiteParser {

	/** Reads and parses the Klaanon site into a {@code PostList}.*/
	public static PostList parsePosts() {
		List<String> postStrings = ParserTools.extractPosts(ParserTools.readSite());
		
		return new PostList(postStrings.parallelStream()
			.map(ParserTools::parsePost)
			.collect(Collectors.toList()));
	}
}
