package src;

import java.io.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CounterIpAddr {
    public static void main(String[] args) {
        Set<String> setIp = new HashSet<>();
        File file = Path.of("resources", "testFile.txt").toFile();

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String s;
            long begin = System.nanoTime();
            while ((s = reader.readLine()) != null) {
                setIp.add(s);
            }

            System.out.println(setIp.size());
            long time = System.nanoTime() - begin;
            System.out.printf("Took %.3f seconds to write", time/ 1e9);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
