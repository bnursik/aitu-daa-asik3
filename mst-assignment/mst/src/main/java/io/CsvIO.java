package io;

import java.io.*;
import java.util.*;

public class CsvIO {
    public static void writeSummary(String path, List<String[]> rows) throws IOException {
        try (var w = new BufferedWriter(new FileWriter(path))) {
            for (String[] r : rows) {
                w.write(String.join(",", r));
                w.newLine();
            }
        }
    }
}
