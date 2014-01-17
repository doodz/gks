package com.gks2.api.scrapper;

public enum SortTypeAjax {
	
	booktorrent("booktorrent"),
	autoget("autoget"),
	delbookmark("delbookmark"),
	delforumbookmark("delforum"),
	delwikiobookmark("delwiki"),
	delrequestbookmark("delrequest")
    ;
    /**
     * @param text
     */
    private SortTypeAjax(final String text) {
        this.text = text;
    }

    private final String text;

    /* (non-Javadoc)
     * @see java.lang.Enum#toString()
     */
    @Override
    public String toString() {
        return text;
    }
}
