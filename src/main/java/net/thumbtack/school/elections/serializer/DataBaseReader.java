package net.thumbtack.school.elections.serializer;

import java.io.IOException;

public interface DataBaseReader {

    void loadDataBase(String filePath) throws IOException;
}
