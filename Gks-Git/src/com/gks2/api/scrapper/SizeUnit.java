package com.gks2.api.scrapper;



public enum SizeUnit {
	B("",1L), 
	KB("K",1024L), 
	MB("M",1024L*1024L), 
	GB("G",1024L*1024L*1024L), 
	TB("T",1024L*1024L*1024L*1024L);

	public final long multiplicator;
	private final String unit;
	private SizeUnit(String u, long m) { this.unit = u; this.multiplicator=m; }
	public String getUnit() { return unit; }
	
	
	
	public static SizeUnit parse(String s) {
		for (SizeUnit su : SizeUnit.values()) {
			if (su.unit.equalsIgnoreCase(s)) return su;
		}
		throw new Error("Impossible de parser "+s);
	}
}
