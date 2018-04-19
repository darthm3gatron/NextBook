package com.andrew.nextbook;

import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Andrew on 4/17/2018.
 * Model object responsible for encapsulating the attributes for each individual book
 */

public class Book implements Serializable {
    private ArrayList<Book> arrayList;
    private String openLibraryId;
    private String author;
    private String title;
    private String date;

    public String getOpenLibraryId() {
        return openLibraryId;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getDate() {
        return date;
    }

    public String getCoverUrl() {
        return "http://covers.openlibrary.org/b/olid/" + openLibraryId + "-M.jpg?default=false";
    }

    public String getLargeCoverUrl() {
        return "http://covers.openlibrary.org/b/olid/" + openLibraryId + "-L.jpg?default=false";
    }

    public static Book fromJson(JSONObject jsonObject) {
        Book book = new Book();
        try {
            if (jsonObject.has("cover_edition_key")) {
                book.openLibraryId = jsonObject.getString("cover_edition_key");
            } else if (jsonObject.has("edition_key")) {
                final JSONArray ids = jsonObject.getJSONArray("edition_key");
                book.openLibraryId = ids.getString(0);
            }
            book.title = jsonObject.has("title_suggest") ? jsonObject.getString("title_suggest") : "";
            book.author = getAuthor(jsonObject);
            book.date = jsonObject.has("first_publish_year") ? jsonObject.getString("first_publish_year") : "";
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return book;
    }

    // Return comma separated author list when there is more than one author
    private static String getAuthor(final JSONObject jsonObject) {
        try {
            final JSONArray authors = jsonObject.getJSONArray("author_name");
            int numAuthors = authors.length();
            final String[] authorStrings = new String[numAuthors];
            for(int i = 0; i < numAuthors; ++i) {
                authorStrings[i] = authors.getString(i);
            }
            return TextUtils.join(", ", authorStrings);
        } catch (JSONException e) {
            return "";
        }
    }

//    private static int getDate(final JSONObject jsonObject) {
//        try {
//            final JSONArray dates = jsonObject.getJSONArray("first_publish_year");
//            int numDates = dates.length();
//            final int[] dateInts = new int[numDates];
//            for (int i = 0; i < numDates; ++i) {
//                dateInts[i] = dates.getInt(i);
//            }
//            return ;
//        } catch (JSONException e) {
//            return 0;
//        }
//    }


    // Decodes array of book json results into business model objects
    public static ArrayList<Book> fromJson(JSONArray jsonArray) {
        ArrayList<Book> books = new ArrayList<Book>(jsonArray.length());
        // Process each result in json array, decode and convert to business
        // object
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject bookJson = null;
            try {
                bookJson = jsonArray.getJSONObject(i);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
            Book book = Book.fromJson(bookJson);
            if (book != null) {
                books.add(book);
            }
        }
        return books;
    }
}
