package src;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.sql.Time;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


public class CreateFileWithIpAddr {
    public static void main(String[] args) {
        Path path = Path.of ("resources", "testFile.txt");
        long begin = System.nanoTime();
        String str = prepareDataWithThreadPool(10_000_000);
        writeIpAddrToFile(path, str);
        long time = System.nanoTime() - begin;
        System.out.printf("Took %.3f seconds to write", time/ 1e9);
    }

    public static String prepareData(int lineCount) {
        var data = new StringBuffer();
        Random rand = new Random();
        for (int k = 0; k < lineCount; k++) {
            String str = "%d.%d.%d.%d".formatted(rand.nextInt(256),rand.nextInt(256),rand.nextInt(256),rand.nextInt(256));
            data.append(str).append(System.lineSeparator());
        }
        return data.toString();
    }

    public static String prepareDataWithThreadPool(int lineCount) {
        var data = new StringBuffer();
        Random rand = new Random();
        ExecutorService executor = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 10 ; i++) {
            executor.execute(() -> {
                for (int k = 0; k < lineCount/10; k++) {
                    String str = "%d.%d.%d.%d".formatted(rand.nextInt(256),rand.nextInt(256),rand.nextInt(256),rand.nextInt(256));
                    data.append(str+System.lineSeparator());
                }
            });
        }


        try {
            executor.shutdown();
            executor.awaitTermination(120, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            System.err.println("task interrupted");
        } finally {
            if (!executor.isTerminated()) {
                System.out.println("cancel non-finished tasks");
            }
            executor.shutdownNow();
            System.out.println("Threadpool shutdown");
        }

        return data.toString();
    }

    public static void writeIpAddrToFile(Path path, String str) {
        try {
            Files.writeString(path, str, StandardOpenOption.CREATE);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
