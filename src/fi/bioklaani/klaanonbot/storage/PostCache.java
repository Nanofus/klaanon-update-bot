package fi.bioklaani.klaanonbot.storage;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.FileSystems;

import com.google.gson.Gson;

import fi.bioklaani.klaanonbot.BotException;
import fi.bioklaani.klaanonbot.PostList;
import fi.bioklaani.klaanonbot.Utils;

/** Manages locally cached list of {@code Post}s. All public methods are synchronized
* to ensure correct behaviour while writing to and reading the cache.*/
public class PostCache {

	private final static Gson GSON = new Gson();

	private final static Path POST_CACHE = FileSystems.getDefault().getPath("post_cache.json");
	
	private static PostList CACHE = null;
	
	private static boolean initialized = false;
	
	/** Initializes all cached data if bot has never been ran before.
	* Fails with a {@code BotException} if called twice during program execution.*/
	public static synchronized void initialize() {
		if(initialized) { throw new BotException("Tried to initialize post cache twice"); }
		initialized = true;
		
		if(!Files.exists(POST_CACHE)) {
			try {
				Files.createFile(POST_CACHE);
			} catch(IOException e) {
				throw new BotException("Failed to create post cache");
			}
			
			writeCache(new PostList());
		}
		
		if(Files.isDirectory(POST_CACHE)) {
			throw new BotException("Post cache is a directory");
		}
		
		CACHE = readCache();
	}
	
	/** Updates cached data, returning a {@code PostList} consisting of newly added {@code Post}s.*/
	public static synchronized PostList update(PostList updatedPosts) {
		PostList newPosts = updatedPosts.relativeComplement(CACHE);
		
		if(!newPosts.isEmpty()) {
			writeCache(updatedPosts);
			CACHE = readCache();
		}
		
		return newPosts;
	}
	
	/** Returns the current cache.*/
	public static synchronized PostList cache() {
		return CACHE;
	}
	
	private static void writeCache(PostList posts) {
		try(BufferedWriter br = Files.newBufferedWriter(POST_CACHE,
				StandardCharsets.UTF_8)) {
			br.write(toJSON(posts));
		} catch(IOException e) {
			throw new BotException(e, "Failed to write post cache");
		} finally {}
	}
	
	private static PostList readCache() {
		try(BufferedReader br = Files.newBufferedReader(POST_CACHE,
				StandardCharsets.UTF_8)) {
			return ofJSON(Utils.readBufferedReader(br));
		} catch(IOException e) {
			throw new BotException(e, "Failed to read post cache");
		} finally {}
	}
	
	private static String toJSON(PostList posts) {
		return GSON.toJson(posts);
	}
	
	private static PostList ofJSON(String json) {
		return GSON.fromJson(json, PostList.class);
	}
}
