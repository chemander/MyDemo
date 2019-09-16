package com.chemander.mydemo.search;

import android.content.SearchRecentSuggestionsProvider;

public class StorySuggestionProvider extends SearchRecentSuggestionsProvider {
    public final static String AUTHORITY = "com.chemander.mydemo.search.StorySuggestionProvider";
    public final static int MODE = DATABASE_MODE_QUERIES;

    public StorySuggestionProvider(){
        setupSuggestions(AUTHORITY, MODE);
    }
}
