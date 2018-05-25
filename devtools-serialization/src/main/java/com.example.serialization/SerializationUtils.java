package com.example.serialization;

import java.io.*;

public class SerializationUtils {

    private static final String fileName = "E:\\objectFile";

    public static void serialize(Serializable object) throws IOException {
        File file = new File(fileName);
        if (file.exists()) {
            file.delete();
        }
        file.createNewFile();
        try (FileOutputStream fos = new FileOutputStream(file);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(object);
        }
    }

    public static Object deserialize() throws IOException, ClassNotFoundException {
        File file = new File(fileName);
        if(!file.exists()){
            throw new IllegalStateException();
        }
        try (FileInputStream fis = new FileInputStream(file);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            return ois.readObject();
        }
    }
}
