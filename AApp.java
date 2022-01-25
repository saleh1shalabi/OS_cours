import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalTime;

// Code written by Group 30: Saleh Shalabi, Robin Svensson and Mattias Ståhlgren
public class App {
    public static void main(String[] args) throws IOException {
        runCommand("mkdir test-directory");
    }

    public static void runCommand(String command) throws IOException {
        Process p = new ProcessBuilder("/usr/local/bin/zsh", "-c", command).start();
        for (int i = 0; i < 500; i++) {
            try {
                String time = LocalTime.now().toString().substring(0, 12);
                File f = new File("./test-directory/" + time + ".txt");
                if (f.createNewFile()) {
                    
                    FileWriter fileWriter = new FileWriter(f);
                    for (int o = 0; o < 10000; o++) {
                        fileWriter.append(time);
                        fileWriter.append("\n");
                    }
                    fileWriter.close();
                    Thread.sleep(10);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        System.out.println("All files have been successfully created!");
    }

}