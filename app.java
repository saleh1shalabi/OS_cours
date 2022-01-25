import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalTime;
import java.util.ArrayList;

// Code written by Group 30: Saleh Shalabi, Robin Svensson and Mattias Ståhlgren

public class app {
    public static void main(String[] args) throws IOException {
        runCommand("/usr/local/bin/zsh", "cat test-named-pipe");
    }


    public static void runCommand(String c ,  String command) throws IOException {
        String step;
        ArrayList<String> result = new ArrayList<>();
        
        step = "Process Starts.";
        System.out.println("<PID: " + ProcessHandle.current().pid() + "> | <Time: " + LocalTime.now().toString().substring(0, 11) + "> | " + step);
        
        while (true) {
             
            Process p = new ProcessBuilder(c, "-c", command).start();
            step = "open pipe";
            System.out.println("<PID: " + p.pid() + "> | <Time: " + LocalTime.now().toString().substring(0, 11) + "> | " + step);
            BufferedReader reader = new BufferedReader (new InputStreamReader (p.getInputStream()));
            while (true) {        
                String line = reader.readLine();
                try {
                    if (line.isEmpty()) {
                    
                        for (String s : result) {
                            System.out.println(s);
                        }
                        result.clear();
                        break;
                    } else {
                        String x = "<PID: " + p.pid() + "> | <Time: " + LocalTime.now().toString().substring(0, 11) + "> | " + line;
                        result.add(x);
                        try {
                            Thread.sleep(3000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        
                    }
                } catch (NullPointerException e) {
                    for (String s : result) {
                        System.out.println(s);
                    }
                    result.clear();
                    break;
                } 
            }



            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            step = "closed pipe";
            
            System.out.println("<PID: " + p.pid() + "> | <Time: " + LocalTime.now().toString().substring(0, 11) + "> | " + step);
            p.destroy();
            
        }
            
    }

}




