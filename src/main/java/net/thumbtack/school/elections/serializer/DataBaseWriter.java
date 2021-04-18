package net.thumbtack.school.elections.serializer;

import java.io.IOException;

public interface DataBaseWriter {

    void saveDataBase(String filePath) throws IOException;
}
