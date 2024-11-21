package com.violin.base.act.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

/**
 * This utility extracts files and directories of a standard zip file to
 * a destination directory.
 */
public class UnzipUtility {
    /**
     * Size of the buffer to read/write data
     */
    private static final int BUFFER_SIZE = 4096;

    /**
     * Extracts a zip file specified by the zipFilePath to a directory specified by
     * destDirectory (will be created if does not exists)
     *
     * @param zippedFile zipped file
     * @param destDir    dest directory
     * @throws IOException write file exception
     */
    public static void unzip(File zippedFile, File destDir) throws IOException {

        if (destDir.exists() && !destDir.isDirectory()) {
            destDir.delete();
        }
        if (!destDir.exists()) {
            destDir.mkdirs();
        }

        String destDirectory = destDir.getAbsolutePath();

        ZipInputStream zipIn = new ZipInputStream(new FileInputStream(zippedFile), Charset.forName("UTF-8"));
        ZipEntry entry = zipIn.getNextEntry();
        // iterates over entries in the zip file
        while (entry != null) {
            String entryName = entry.getName();
            //
            if (entryName == null) {
                continue;
            }

            if (entryName.contains("../") || entryName.contains("..%2F")) {
                throw new IOException("Security Exception:" + entryName);
            }

            String filePath = destDirectory + File.separator + entryName;
            if (!entry.isDirectory()) {
                // if the entry is a file, extracts it
                extractFile(zipIn, filePath);
            } else {
                // if the entry is a directory, make the directory
                File dir = new File(filePath);
                dir.mkdirs();
            }
            zipIn.closeEntry();
            entry = zipIn.getNextEntry();
        }
        zipIn.close();
    }




    /**
     * Extracts a zip entry (file entry)
     *
     * @param zipIn    {@link ZipInputStream}
     * @param filePath output path
     * @throws IOException write file exception
     */
    private static void extractFile(ZipInputStream zipIn, String filePath) throws IOException {
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));
        byte[] bytesIn = new byte[BUFFER_SIZE];
        int read;
        while ((read = zipIn.read(bytesIn)) != -1) {
            bos.write(bytesIn, 0, read);
        }
        bos.close();
    }
}