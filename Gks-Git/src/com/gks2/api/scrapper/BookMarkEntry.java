package com.gks2.api.scrapper;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import com.gks2.app.R;

public class BookMarkEntry {

	public Integer IdBookMark = -42;
	public Integer Id = -42;
	public String Name = "";
	public String DateAdded = "";
	public DataSize Size;
	public int qtSeeders;
	public int qtLeechers;
	
	
	public Map<String, String> toMap() {
		Field[] BookMarkEntryFields = BookMarkEntry.class.getDeclaredFields();
		HashMap<String, String> result = new HashMap<String, String>(BookMarkEntryFields.length);
		String last ="";
		Object fieldValue = null;
		
		for (Field f : BookMarkEntryFields) {
			try {
				last = f.getName();
				fieldValue = f.get(this);
				if (fieldValue != null) result.put(f.getName(), fieldValue.toString());
				
			} catch (Exception e) {
				System.out.println("Last= "+last+" name="+Name);
				e.printStackTrace();
			} 
		}
		
		return result;
	}
}
