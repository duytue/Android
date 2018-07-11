package com.example.duytue.miniproject1;

import android.content.Context;

import java.util.ArrayList;
import java.util.regex.Pattern;

/**
 * Created by duytue on 6/19/17.
 */

public class SearchManager {
    ArrayList<Place> tempList;
    Context context;

    public SearchManager(Context context, ArrayList<Place> list) {
        this.context = context;
        tempList = list;
    }

    public ArrayList<Place> searchByName(String name) {
       ArrayList<Place> result = new ArrayList<>();

        for (int i = 0; i < tempList.size(); ++i) {
            Place temp = tempList.get(i);
            if (isFound(name, temp.Name)){
                result.add(temp);
            } else {
                for (int j = 0; j < temp.alternateNames.length; ++j) {
                    if (isFound(name, temp.alternateNames[j])) {
                        result.add(temp);
                    }
                }
            }
        }

        return result;
    }

    public ArrayList<Place> searchByType(String name) {
        ArrayList<Place> result = new ArrayList<>();

        for (int i = 0; i < tempList.size(); ++i) {
            Place temp = tempList.get(i);
             for (int j = 0; j < temp.types.length; ++j) {
                    if (isFound(name, temp.types[j])) {
                        result.add(temp);
                    }
                }
            }

        return result;
    }

    public boolean isFound(String name, String sample) {
        return Pattern.compile(Pattern.quote(name), Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE).matcher(sample).find();
    }
}
