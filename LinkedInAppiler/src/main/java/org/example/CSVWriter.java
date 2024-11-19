package org.example;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class CSVWriter {
    private static boolean flag = true;
    private static final String CSV_FILE = "JobsToApply.csv";

    public static  void writeToCSV(String jobUrl) {
        File file = new File(CSV_FILE);
        boolean isFileEmpty = !file.exists() || file.length() == 0;

        if (file.exists() && flag) {
            clearFile();
            flag = false;
        }

        try (FileWriter writer = new FileWriter(CSV_FILE, true)) {

                writeReportData(writer,jobUrl);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void clearFile() {
        try (FileWriter writer = new FileWriter(CSV_FILE, false)) {
            writer.append("JobUrl,\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private static void writeReportData(FileWriter writer, String jobUrl) throws IOException {
        writer.append(jobUrl);
        writer.append("\n");

    }
}