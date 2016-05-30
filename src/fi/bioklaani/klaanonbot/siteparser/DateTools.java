package fi.bioklaani.klaanonbot.siteparser;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

/** Contains methods for converting various date formats into {@code Instant}s.*/
class DateTools {

	private final static DateTimeFormatter DATE_FORMATTER =
			DateTimeFormatter.ofPattern("d.M.y");
			
	static Instant toInstant(String date) {
		LocalDate localDate = LocalDate.parse(date, DATE_FORMATTER);
		return localDate.atStartOfDay().toInstant(ZoneOffset.of("+2"));
	}
}
