package com.gks2.api.scrapper;

public enum SortActionAjax {
    Add("add"),
    Dell("del")
    ;
    /**
     * @param text
     */
    private SortActionAjax(final String text) {
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
