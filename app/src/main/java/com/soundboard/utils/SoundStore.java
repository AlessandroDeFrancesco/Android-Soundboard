package com.soundboard.utils;

import com.soundboard.R;
import com.soundboard.models.Categories;
import com.soundboard.models.Sound;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

public class SoundStore {

    private Dictionary<Categories,ObservableList<Sound>> soundsDictionary;
    private List<Sound> soundsList;

    private static SoundStore instance = null;

    private SoundStore(){
        initialize();
    }

    public static synchronized SoundStore getInstance(){
        if(instance == null)
            instance = new SoundStore();

        return instance;
    }

    /* Retrieve the sounds from the raw folder, categorizing them on the first part of the name that ends with an underscore
     * Ex: funny_sneeze.ogg would be categorized in the funny category and given the name Sneeze
     */
    private void initialize(){
        try {
            // initialize lists
            soundsDictionary = new Hashtable<>(Categories.values().length);

            for (Categories category : Categories.values()) {
                soundsDictionary.put(category, new ObservableList<Sound>());
            }

            Field[] rawFields = R.raw.class.getFields();
            soundsList = new ArrayList<>(rawFields.length);
            // get all sounds files and populate the dictionary
            for (Field rawField : rawFields) {

                String fileName = rawField.getName();
                if (!fileName.contains("_"))
                    continue;

                int resourceID = rawField.getInt(rawField);

                String[] split = fileName.split("_");
                String stringCategory = Utils.capitalizeString(split[0]);
                Categories category = Categories.valueOf(stringCategory);

                StringBuilder builder = new StringBuilder();
                for (int i = 1; i < split.length; i++) {
                    builder.append(split[i]).append(" ");
                }
                String audioName = Utils.capitalizeString(builder.toString());

                Sound newSound = new Sound(audioName, resourceID, searchDrawableIDFromCategory(category.name()));
                soundsDictionary.get(category).add(newSound);
                soundsList.add(newSound);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int searchDrawableIDFromCategory(String category) throws IllegalAccessException {
        Field[] drawableFields = R.drawable.class.getFields();
        for (Field drawableField : drawableFields) {
            if (drawableField.getName().equalsIgnoreCase(category))
                return drawableField.getInt(drawableField);
        }

        return -1;
    }

    public Dictionary<Categories,ObservableList<Sound>> getSoundsDictionary() {
        return soundsDictionary;
    }

    public List<Sound> getSoundsList(){
        return soundsList;
    }

    public List<Sound> searchSounds(String query) {
        List<Sound> found = new ArrayList<>();
        for (Sound sound: getSoundsList()) {
            if(sound.getName().toLowerCase().contains(query))
                found.add(sound);
        }

        return found;
    }
}
