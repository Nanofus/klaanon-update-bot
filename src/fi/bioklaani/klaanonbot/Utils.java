package fi.bioklaani.klaanonbot;

import java.io.BufferedReader;
import java.io.IOException;

import com.google.gson.Gson;

/** Contains useful utility methods.*/
public class Utils {

	private final static Gson GSON = new Gson();
	
	/** Converts an object to JSON.*/
	public static String toJSON(Object obj) {
		return GSON.toJson(obj);
	}
	
	/** Reads an object from JSON.*/
	public static <T> T ofJSON(String json, Class<T> cls) {
		return GSON.fromJson(json, cls);
	}

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
