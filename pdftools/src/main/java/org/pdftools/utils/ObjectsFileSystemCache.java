package org.pdftools.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.commons.io.FileUtils;

public class ObjectsFileSystemCache<K extends Serializable> {

    private String diskLocation;
    
    public ObjectsFileSystemCache(String diskLocation) throws IOException {
        this.diskLocation = diskLocation;
        Files.createDirectories(Paths.get(diskLocation));
    }
    
    public void serializeToDisk(String serializedObjectName, K data) throws FileNotFoundException, IOException {
        try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(Paths.get(this.diskLocation,
                serializedObjectName).toFile()))) {
            oos.writeObject(data);
            oos.flush();
            oos.close();
        }
    }
    
    public K deSerializeFromDisk(String serializedObjectName) throws FileNotFoundException, IOException, ClassNotFoundException {
        Object data = null;
        try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(Paths.get(this.diskLocation,
                serializedObjectName).toFile()))) {
                data = ois.readObject();
        }
        return (K)data;
    }
    
    public void deleteFromDisk(String serializedObjectName) throws FileNotFoundException, IOException {
        Files.delete(Paths.get(this.diskLocation, serializedObjectName));
    }
    
    public boolean contains(String serializedObjectName) throws FileNotFoundException, IOException {
        return Files.exists(Paths.get(this.diskLocation, serializedObjectName));
    }
    
    public void delete() throws FileNotFoundException, IOException {
        FileUtils.deleteDirectory(Paths.get(diskLocation).toFile());
    }
}
