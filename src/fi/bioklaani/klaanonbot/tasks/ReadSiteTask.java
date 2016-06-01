package fi.bioklaani.klaanonbot.tasks;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;
import java.time.Duration;

import fi.bioklaani.klaanonbot.BotException;
import fi.bioklaani.klaanonbot.Post;
import fi.bioklaani.klaanonbot.PostList;
import fi.bioklaani.klaanonbot.siteparser.SiteParser;
import fi.bioklaani.klaanonbot.storage.PostCache;

/** Downloads current posts and updates the local cache. Returns
* newly found posts.*/
public class ReadSiteTask implements BotTask<Void, PostList> {

	private final static List<Supplier<BotTask<PostList, ?>>> THEN =
			Arrays.asList(SendUpdatesTask::new);

	public PostList run(Void ignore) {
		PostList webPosts = SiteParser.parsePosts();
		PostList newPosts = PostCache.update(webPosts);
		
		return newPosts;
	}
	
	public RunPolicy retry() {
		return RunPolicy.in(Duration.ofSeconds(30));
	}
	
	public RunPolicy chained() {
		return RunPolicy.in(Duration.ofSeconds(60));
	}
	
	public List<Supplier<BotTask<PostList, ?>>> then() {
		return THEN;
	}
}
