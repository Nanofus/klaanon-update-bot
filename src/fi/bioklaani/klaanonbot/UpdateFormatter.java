package fi.bioklaani.klaanonbot;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/** Formats a coherent message notifying that new posts have arrived.*/
public class UpdateFormatter {

	public static String format(PostList newPosts) {
		int amount = newPosts.size();
		Post oldest = newPosts.oldest();
		LocalDateTime now = LocalDateTime.now();
		
		long seconds = ChronoUnit.SECONDS.between(oldest.date, now);
		long minutes = ChronoUnit.MINUTES.between(oldest.date, now);
		long hours = ChronoUnit.HOURS.between(oldest.date, now);
		long days = ChronoUnit.DAYS.between(oldest.date, now);
		
		String timeString = timeString(seconds, minutes, hours, days);
		
		if(amount == 1) {
			return "Uusi viesti \"" + oldest.name + "\" kirjoittajalta "
				+ oldest.author + " " + timeString + " sitten:\n" + oldest.url; 
		} else {
			return amount + " uutta viestiä, vanhin \"" + oldest.name + "\" kirjoittajalta "
				+ oldest.author + " " + timeString + " sitten:\n" + oldest.url;
				
		}
	}
	
	private static String timeString(long seconds, long minutes, long hours, long days) {
		if(days != 0) { return days + " " + (days == 1 ? "päivä" : "päivää"); }
		if(hours != 0) { return hours + " " + (hours == 1 ? "tunti" : "tuntia"); }
		if(minutes != 0) { return minutes + " " + (days == 1 ? "minuutti" : "minuuttia"); }
		return seconds + " " + (seconds == 1 ? "sekunti" : "sekuntia");
	}
}
