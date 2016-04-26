package org.pdftools.utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.commons.io.FileUtils;

public class FileSystemCache {

    private String diskLocation;
    
    public FileSystemCache(String diskLocation) throws IOException {
        this.diskLocation = diskLocation;
        Files.createDirectories(Paths.get(diskLocation));
    }
    
    public void writeFileToDisk(String fileName, byte[] data) throws FileNotFoundException, IOException {
        Files.write(Paths.get(diskLocation , fileName), data);
    }
    
    public byte[] readFileFromDisk(String fileName) throws FileNotFoundException, IOException {        
        return Files.readAllBytes(Paths.get(diskLocation , fileName));
    }
    
    public void deleteFromDisk(String fileName) throws FileNotFoundException, IOException {
        Files.deleteIfExists(Paths.get(this.diskLocation, fileName));
    }
    
    public boolean contains(String fileName) throws FileNotFoundException, IOException {
        return Files.exists(Paths.get(this.diskLocation, fileName));
    }
    
    public boolean isReadable(String fileName) throws FileNotFoundException, IOException {
        return Files.isReadable(Paths.get(this.diskLocation, fileName));
    }
    
    public void delete() throws FileNotFoundException, IOException {
        FileUtils.deleteDirectory(Paths.get(diskLocation).toFile());
    }
}
