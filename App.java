import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

// Code written by Group 30: Saleh Shalabi, Robin Svensson and Mattias Ståhlgren
public class App {
    public static void main(String[] args) throws IOException {
        runCommand("id");
        runCommand("find /etc/ -name 'rc*'");
        runCommand("hostname freebsd-vm-group30-upd");
        runCommand("hostname");
    }

    public static void runCommand(String command) throws IOException {
        ArrayList<String> result = new ArrayList<>();
        Process p = new ProcessBuilder("/usr/local/bin/zsh", "-c", command).start();
        InputStream i = p.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(i));
        while (true) {
            String line = reader.readLine();
            if (line == null)
                break;
            result.add(line);
        }
        System.out.println("the Command used is " + command);
        if (result.isEmpty()) {
            System.out.println("No Result to show with this command " + command);
        } else {
            System.out.println("Result: ");
        }

        for (String cc : result) {
            System.out.println(cc);
        }
        System.out.println("Exit code: " + p.exitValue());
    }

}
