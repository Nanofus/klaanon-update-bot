package fi.bioklaani.klaanonbot;

import java.util.Arrays;
import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;

/** A sorted, immutable list of {@code Post}s.*/
public class PostList {
	
	private final Set<Post> posts;
	
	public PostList(Collection<Post> collection) {
		if(collection instanceof TreeSet) {
			posts = (TreeSet<Post>) collection;
			return;
		}
		
		posts = new TreeSet<>();
		for(Post post : collection) {
			posts.add(post);
		}
	}
	
	public PostList(Post...array) {
		this(Arrays.asList(array));
	}
	
	public boolean isEmpty() { return posts.isEmpty(); }
	
	public boolean contains(Post post) { return posts.contains(post); }
	
	/** Returns a {@code PostList} that contains all {@code Post}s that are in
	* this {@code PostList} or the argument {@code PostList}.*/
	public PostList union(PostList other) {
		Set<Post> newSet = new TreeSet<>();
		newSet.addAll(posts);
		newSet.addAll(other.posts);
		return new PostList(newSet);
	}
	
	/** Returns a new {@code PostList} that contains all {@code Post}s that are
	* both in this {@code PostList} and the argument {@code PostList}.*/
	public PostList intersection(PostList other) {
		Set<Post> newSet = new TreeSet<>();
		for(Post post : posts) {
			if(other.posts.contains(post)) {
				newSet.add(post);
			}
		}
		return new PostList(newSet);
	}
	
	/** Returns a new {@code PostList} that contains all {@code Post}s that are
	* in this {@code PostList} and not in the argument {@code PostList}.*/
	public PostList relativeComplement(PostList other) {
		Set<Post> newSet = new TreeSet<>();
		for(Post post : posts) {
			if(!other.posts.contains(post)) {
				newSet.add(post);
			}
		}
		return new PostList(newSet);
	}
	
	@Override
	public String toString() { return posts.toString(); }
	
	@Override
	public boolean equals(Object o) {
		if(o == null || !(o instanceof PostList)) {
			return false;
		}
		
		return posts.equals(((PostList) o).posts);
	}
	
	@Override
	public int hashCode() {
		return posts.hashCode();
	}
}
