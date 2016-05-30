package fi.bioklaani.klaanonbot;

import java.lang.Comparable;
import java.net.URL;
import java.time.Instant;

/** Describes a post, composed of the name, the author, the date and the
* URL. Implements {@link Comparable} and sorts based only on the date.
* Note that this is inconsistent with the notion of equality of this class.*/
public class Post implements Comparable<Post> {

	public final String name, author, url;
	public final Instant date;

	public Post(String name, String author, Instant date, String url) {
		this.name = name;
		this.author = author;
		this.date = date;
		this.url = url;
	}
	
	@Override
	public String toString() {
		return "[" + name + ", " + author + " " + date + "]";
	}
	
	@Override
	public boolean equals(Object o) {
		if(o == null) { return false; }
		if(!(o instanceof Post)) { return false; }
		
		Post p = (Post) o;
		
		return name.equals(p.name) && author.equals(p.author)
				&& url.equals(p.url) && date.equals(p.date);
	}
	
	@Override
	public int hashCode() {
		return (name.hashCode() << 24) ^ (author.hashCode() << 16) ^ (url.hashCode() << 8) ^ (date.hashCode());
	}
	
	@Override
	public int compareTo(Post other) {
		return date.compareTo(other.date);
	}
}