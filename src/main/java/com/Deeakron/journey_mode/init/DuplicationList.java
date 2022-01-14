package com.Deeakron.journey_mode.init;

import java.util.Arrays;
import java.util.HashMap;

public class DuplicationList {
    private HashMap<String, String[]> categories;

    public DuplicationList(ItemList list) {
        this.categories = new HashMap<String, String[]>();
        for (int i = 0; i < list.getItems().length; i++) {
            String newCat = list.getCategories()[i];
            if (this.categories.get(newCat) != null) {
                String[] oldArray = this.categories.get(newCat);
                oldArray = Arrays.copyOf(oldArray, oldArray.length + 1);
                oldArray[oldArray.length - 1] = list.getItems()[i];
                this.categories.remove(newCat);
                this.categories.put(newCat, oldArray);
            } else {
                String[] newArray = new String[1];
                newArray[0] = list.getItems()[i];
                this.categories.put(newCat, newArray);
            }
        }
    }

    public String getLeadingItem(String category) {
        if (this.categories.get(category) == null) {
            return "null";
        } else {
            return this.categories.get(category)[0];
        }
    }

    public String[] getCategoryItems(String category) {
        if (this.categories.get(category) == null) {
            return null;
        } else {
            return this.categories.get(category);
        }
    }
}
