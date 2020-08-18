package me.lightlord323dev.cursedvaults.util.file;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.*;

/**
 * Created by Luda on 7/17/2020.
 */
public class GsonUtil {

    private static Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static void saveObject(Object object, File file) {
        try {
            Writer writer = new FileWriter(file);
            writer.write(gson.toJson(object));
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static <T> T loadObject(TypeToken<T> token, File file) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            return gson.fromJson(br, token.getType());
        } catch (IOException e) {
            return null;
        }
    }
}
