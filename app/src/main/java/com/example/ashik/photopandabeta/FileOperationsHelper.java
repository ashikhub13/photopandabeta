package com.example.ashik.photopandabeta;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * FileOperationsHelper :- Helper class for file operations.
 *
 * @author - Rahul Padmkumar
 * @since - 2016-07-01
 */

public class FileOperationsHelper {

    /**
     * To create a file in a specified folder in the specified directory.
     * @param dir - Name of Directory.
     * @param folderName - Name of Folder.
     * @param fileName - Name of file.
     * @return - Created file.
     */
    public static File createFile(File dir, @SuppressWarnings("SameParameterValue") String folderName, String fileName){

        File folder = new File(dir, File.separator.concat(folderName));
        createFile(folder);
        return new File(folder, fileName);
    }

    /**
     * Function to create the file if it does not exist.
     * @param file - File to be created.
     *
     */
    private static void createFile(File file){

        if(!file.exists()){
            if (!file.mkdirs()) {
                throw new RuntimeException("File Could not be created");
            }
        }
    }

    /**
     * Function to check whether the file is valid or not based on file size.
     * @param file - File to be checked.
     * @return - Boolean value indicating whether the file is  valid or not.
     */
    public static boolean isFileValid(File file) {

        return file.length() > 0;
    }

    /**
     * To create a subFolder within a folder
     * @param dir - Directory folder.
     * @param folderName - Parent folder name.
     * @param fileName - Name of sub folder.
     * @return created subFolder
     */
    public static File createSubFolder(File dir, String folderName, String fileName) {

        File newFolder = new File(dir, File.separator.concat(folderName));
        createFile(newFolder);
        File file = new File(newFolder, File.separator.concat(fileName));
        createFile(file);
        return file;
    }

    /**
     * To create a new file in a particular folder.
     * @param folder - Folder in which the file is to be created.
     * @param fileName - Name to be given to the file.
     * @return created file.
     */
    public static File createFile(File folder, String fileName){

        return new File(folder, fileName);
    }

    /**
     * Function to return output strean specifying file.
     * @param file - File whose output stream is to be obtained.
     * @return - FileOutputStream
     * @throws FileNotFoundException
     */
    public static FileOutputStream getFileOutputStream(File file) throws FileNotFoundException {
        return new FileOutputStream(file);
    }
}
