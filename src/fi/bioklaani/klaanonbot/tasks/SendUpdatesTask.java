package fi.bioklaani.klaanonbot.tasks;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;
import java.time.Duration;

import fi.bioklaani.klaanonbot.Post;
import fi.bioklaani.klaanonbot.PostList;

/** Posts new posts to Telegram.*/
public class SendUpdatesTask implements BotTask<PostList, Void> {

	private final static List<Supplier<BotTask<Void, ?>>> THEN =
			Arrays.asList(ReadSiteTask::new);

	public Void run(PostList newPosts) {
		for(Post post : newPosts) {
			System.out.println("New post: " + post.name);
		}
		return null;
	}
	
	public RunPolicy retry() {
		return RunPolicy.in(Duration.ofSeconds(30));
	}
	
	public RunPolicy chained() {
		return RunPolicy.in(Duration.ofSeconds(0));
	}
	
	public List<Supplier<BotTask<Void, ?>>> then() {
		return THEN;
	}
}
