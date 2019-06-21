package go.pass.file_support;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import static go.pass.Constants.*;

/**
 * @author Anatolii Nosenko
 * @version 1.0 6/18/2019.
 */
public class PassWriter {
    /**
     * @param text Text to save in file.
     */
    public void write(final String text, int count) {
        final String encoding = "UTF-8";
        try (Writer writer =
                     new OutputStreamWriter(new FileOutputStream(RESULT_FOLDER + createFileName(count)), encoding)) {
            writer.write(text);

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private String createFileName(int count) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY-MM-dd HH-mm-ss");
        return count + " PASS " + simpleDateFormat.format(new Date()) + ".txt";
    }

}
