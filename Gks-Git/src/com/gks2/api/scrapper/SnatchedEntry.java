package com.gks2.api.scrapper;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.gks2.app.R;

public class SnatchedEntry {
	
	public String seeding;
	public String title;
	public String prezLocaion;
	public String dlLocation;
	public String ratio;
	public String seedTime;
	public DataSize qtDl;
	public DataSize qtUl;
	public DataSize qtRealDl;

	public Map<String, String> toMap() {
		Field[] searchEntryFields = SnatchedEntry.class.getDeclaredFields();
		HashMap<String, String> result = new HashMap<String, String>(searchEntryFields.length);
		String last ="";
		Object fieldValue = null;
		
		for (Field f : searchEntryFields) {
			try {
				last = f.getName();
				fieldValue = f.get(this);
				if (fieldValue != null) result.put(f.getName(), fieldValue.toString());
				
			} catch (Exception e) {
				System.out.println("Last= "+last+" title="+title);
				e.printStackTrace();
			} 
		}
		
		return result;
		}
}
