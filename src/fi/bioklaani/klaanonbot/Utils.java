package fi.bioklaani.klaanonbot;

import java.io.BufferedReader;
import java.io.IOException;

/** Contains useful utility methods.*/
public class Utils {

	/** Reads a {@code BufferedReader}, consuming it and returning the result.*/
	public static String readBufferedReader(BufferedReader reader) throws IOException {
		StringBuilder sb = new StringBuilder();
		
		String line = "";
		while((line = reader.readLine()) != null) {
			sb.append(line);
		}
		
		reader.close();
		return sb.toString();
	}
}
