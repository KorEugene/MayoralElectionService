package net.thumbtack.school.elections.serializerimpl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.thumbtack.school.elections.serializer.DataBaseWriter;
import net.thumbtack.school.elections.server.DataBase;

import java.io.FileWriter;
import java.io.IOException;

public class DataBaseJsonWriter implements DataBaseWriter {

    private Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .create();

    @Override
    public void saveDataBase(String filePath) throws IOException {
        try (FileWriter fileWriter = new FileWriter(filePath)) {
            gson.toJson(DataBase.getInstance(), fileWriter);
        }
    }
}
