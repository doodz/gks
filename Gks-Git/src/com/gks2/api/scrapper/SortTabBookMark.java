package com.gks2.api.scrapper;

public enum SortTabBookMark {

	Torrents("torrent"),
	Forums("forum"),
	Wiki("wiki"),
	Requests("requests")
    ;
    /**
     * @param text
     */
    private SortTabBookMark(final String text) {
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
