package com.chemander.story.search;

import android.content.SearchRecentSuggestionsProvider;

public class StorySuggestionProvider extends SearchRecentSuggestionsProvider {
    public final static String AUTHORITY = "com.chemander.story.search.StorySuggestionProvider";
    public final static int MODE = DATABASE_MODE_QUERIES;

    public StorySuggestionProvider(){
        setupSuggestions(AUTHORITY, MODE);
    }
}
