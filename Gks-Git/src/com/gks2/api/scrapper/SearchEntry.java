package com.gks2.api.scrapper;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import android.R.integer;

import com.gks2.app.R;





public class SearchEntry {
	public int id;
	public String type;
	public String title;
	public String prezLocaion;
	public String dlLocation;
	public int qtComments;
	public int qtSeeders;
	public int qtLeechers;
	public Collection<SearchOption> terms;
	public DataSize postSize;
	public int qtCompleteDownload;
	public Boolean FreeLeech = false;
	
	public Map<String, String> toMap() {
		Field[] searchEntryFields = SearchEntry.class.getDeclaredFields();
		HashMap<String, String> result = new HashMap<String, String>(searchEntryFields.length);
		String last ="";
		Object fieldValue = null;
		
		for (Field f : searchEntryFields) {
			try {
				last = f.getName();
				fieldValue = f.get(this);
				if (fieldValue != null) result.put(f.getName(), fieldValue.toString());
				if(FreeLeech)  
					result.put("icon",  String.valueOf(R.drawable.ic_torrent_freeleech));
				else
					result.put("icon",  String.valueOf(R.drawable.ic_torrent_default));
				if(getImgCat() != -1)
				{
					result.put("typeIcon",  String.valueOf(getImgCat()));
				}else
					result.put("typeIcon",  String.valueOf(R.drawable.ic_torrent_default));
				
			} catch (Exception e) {
				System.out.println("Last= "+last+" title="+title);
				e.printStackTrace();
			} 
		}
		
		return result;
	}
	
	@Override
	public String toString() {
		return String
				.format("SearchEntry\n\tid = %s,\n\ttype = %s,\n\ttitle = %s,\n\tprezLocaion = %s",
						id, type, title, prezLocaion
						);
	}
	
	private int getImgCat(){
		int type = Integer.parseInt(this.type);
		
		switch (type) 
		{ 
		case 3: return R.drawable.cat_win ; 
		case 4: return  R.drawable.cat_apple ;
		case 5: return R.drawable.cat_dvd_r ; 
		case 6: return  R.drawable.cat_dvd_r_e ;
		case 7: return R.drawable.cat_emissions ; 
		case 8: return  R.drawable.cat_doc ;
		case 9: return R.drawable.cat_doc_hd ; 
		case 10: return  R.drawable.cat_pack_tv ;
		case 11: return R.drawable.cat_tv_e ; 
		case 12: return  R.drawable.cat_tv ;
		case 13: return R.drawable.cat_tv_hd_e ; 
		case 14: return  R.drawable.cat_tv_hd ;
		case 15: return R.drawable.cat_720p ; 
		case 16: return  R.drawable.cat_1080p ;
		case 17: return R.drawable.cat_b_r ; 
		case 18: return  R.drawable.cat_divers ;
		case 19: return  R.drawable.cat_dvd_r_films ;
		case 20: return R.drawable.cat_dvd_r_series ; 
		case 21: return  R.drawable.cat_anim ;
		case 22: return R.drawable.cat_tv_vo ; 
		case 23: return  R.drawable.cat_concerts ;
		case 24: return R.drawable.cat_ebook ; 
		case 28: return  R.drawable.cat_sport ;
		case 29: return R.drawable.cat_game_pc ; 
		case 30: return  R.drawable.cat_ds ;
		case 31: return  R.drawable.cat_wii ;
		case 32: return  R.drawable.cat_xbox_360 ;
		case 34: return  R.drawable.cat_psp ;
		case 38: return  R.drawable.cat_ps3 ;
		case 39: return  R.drawable.cat_flac ;
		default:
			return R.drawable.ic_torrent_default;
		
		}
	
	}
	
}
