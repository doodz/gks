package com.gks2.api.scrapper;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class DataSize implements Comparable<DataSize>{

	public static final Pattern byteSizePattern = Pattern.compile(
            "([0-9]+([\\.,][0-9]+)?) (|K|M|G|T)o",
            Pattern.CASE_INSENSITIVE);
            
	protected final long byteSize;
	
	public DataSize(long s) { this.byteSize = s; }
	public DataSize(double s, SizeUnit u) {
		this(Math.round(s * u.multiplicator));
	}
	
	@Override
	public int compareTo(DataSize o) {
		return safeLongToInt(this.byteSize - o.byteSize);
	}

	// Pour compareTo
	public static int safeLongToInt(long l) {
		return (int) Math.min(Math.max(Integer.MIN_VALUE, l), Integer.MAX_VALUE);
	}
	
	// Le compilo va optimiser toussa
	public long byteSize() { return byteSize; }
	public double kilobyteSize() { return byteSize/SizeUnit.KB.multiplicator; }
	public double megabyteSize() { return byteSize/SizeUnit.MB.multiplicator; }
	public double gigabyteSize() { return byteSize/SizeUnit.GB.multiplicator; }
	public double terabyteSize() { return byteSize/SizeUnit.TB.multiplicator; }
	
	/**Fourni la taille reprŽsentŽe dans une chaine de caract�re
	 * @param s
	 * @return
	 */
	public static DataSize parseSize(String s) {
		final Matcher m = byteSizePattern.matcher(s);
		
		if (m.find()) {
			final double v = Double.parseDouble(m.group(1).replace(',', '.'));
			final SizeUnit su = SizeUnit.parse(m.group(3));
			return new DataSize(v, su);
		}
		throw new Error(String.format("Impossible d'extraire une taille de '%s'", s));
	}
	
	@Override
	public String toString() {
		SizeUnit u = 
			(byteSize <= SizeUnit.KB.multiplicator)?SizeUnit.B:
				(byteSize <= SizeUnit.MB.multiplicator)?SizeUnit.KB:
					(byteSize <= SizeUnit.GB.multiplicator)?SizeUnit.MB:
						(byteSize <= SizeUnit.TB.multiplicator)?SizeUnit.GB:SizeUnit.TB;
		if (u == SizeUnit.B) return String.format("%d B", this.byteSize);
		else {
			return String.format("%.2f %sB", new Double(this.byteSize)/u.multiplicator, u.getUnit());
		}
	}
	
}
