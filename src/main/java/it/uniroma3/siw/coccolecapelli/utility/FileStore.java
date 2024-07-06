package it.uniroma3.siw.coccolecapelli.utility;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.web.multipart.MultipartFile;

public class FileStore {
	
	public static final String uploadDirectory = System.getProperty("user.dir")+"/src/main/resources/static/images/";
	//public static final String uploadDirectory = System.getProperty("user.dir")+"/static/images/";
	
	//per salvare
	public static String store(MultipartFile file, String folderName) {
        new File(uploadDirectory+folderName).mkdir();
        Path fileNameAndPath  = Paths.get(uploadDirectory+folderName, file.getOriginalFilename());
        try {
            Files.write(fileNameAndPath, file.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileNameAndPath.getFileName().toString();
    }
	
	//per cancellare
	public static void removeImg(String folderName, String name) {
        Path fileNameAndPath  = Paths.get(uploadDirectory+folderName,name);
        try {
            Files.delete(fileNameAndPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
