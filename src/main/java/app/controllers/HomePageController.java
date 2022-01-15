package app.controllers;

import app.Data;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.*;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Controller
public class HomePageController {
    public static List<Map<String, Integer>> results = new ArrayList<>();
    public static boolean first = true;
    public static int tasksCount = 0;
    public static Map<String, String> tasks;

    Boolean error = false;

    @GetMapping("/")
    public ModelAndView homePageDisplay(ModelAndView modelAndView) throws FileNotFoundException {
        tasks = new LinkedHashMap<>();

        if (first) {
            Scanner countScanner = new Scanner(new File("tasks/all.txt"));
            tasksCount = Integer.parseInt(countScanner.nextLine());
            for (int i = 0; i < tasksCount; i++) {
                results.add(new LinkedHashMap<>());
            }
            first = false;
        }

        modelAndView.setViewName("index.html");
        modelAndView.addObject("results", results);
        modelAndView.addObject("error", error);

        for(int i=1; i<=tasksCount; i++){
            File taskFile = new File("tasks/"+i+"/task.txt");
            Scanner scanner = new Scanner(taskFile);
            String content = "";
            while(scanner.hasNextLine()){
                content += scanner.nextLine();
                content += "\n";
            }

            System.out.println(content);

            tasks.put("Задача "+i,content);
        }

        modelAndView.addObject("tasks", tasks);

        return modelAndView;
    }

    public void doStuff(Data data) throws IOException, InterruptedException {
        try {
            String name = data.getName();
            name += " - Task " + data.getTask();

            MultipartFile myFile = data.getFile();
            InputStream fileContent = myFile.getInputStream();

            String content;

            BufferedReader br = new BufferedReader(new InputStreamReader(fileContent));
            StringBuilder sb = new StringBuilder();

            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line + System.lineSeparator());
            }

            content = sb.toString();
            int c = 0;
            while (content.contains(".nextLine()")) {
                content = content.replaceFirst("\\w+.nextLine[(][)]", "args[" + c + "]");
                c++;
            }
            content = content.replaceAll("class \\w+", "class Main");

            System.out.println(content);

            File file = new File("Main.java");
            file.createNewFile();
            FileWriter writer = new FileWriter(file);
            writer.write(content);
            writer.close();

            int task = Integer.parseInt(data.getTask());
            Scanner countScan = new Scanner(new File("tasks/" + task + "/count.txt"));
            int count = Integer.parseInt(countScan.nextLine());

            int correctCount = 0;


            for (int i = 1; i <= count; i++) {
                File fileI = new File("tasks/" + task + "/" + i + ".txt");
                Scanner scanner = new Scanner(fileI);
                String input = scanner.nextLine();

                File fileO = new File("tasks/" + task + "/" + i + "a.txt");
                Scanner correct = new Scanner(fileO);
                String answer = correct.nextLine();

                Runtime.getRuntime().exec("javac Main.java");
                Process process = Runtime.getRuntime().exec("java Main " + input);

                BufferedReader stdInput = new BufferedReader(new
                        InputStreamReader(process.getInputStream()));

                String out = stdInput.readLine();

                System.out.println(out);
                System.out.println("-------------------------");

                if (out.equals(answer)) {
                    correctCount += 1;
                }
            }

            int rez = (int) (correctCount * 1.0 / count * 100);

            if (results.get(Integer.parseInt(data.getTask()) - 1).containsKey(name)) {
                if (results.get(Integer.parseInt(data.getTask()) - 1).get(name) < rez) {
                    results.get(Integer.parseInt(data.getTask()) - 1).put(name, rez);
                }
            } else {
                results.get(Integer.parseInt(data.getTask()) - 1).put(name, rez);
            }
        }catch (Exception exception){
            System.out.println(exception.getMessage());
        }
    }

    @PostMapping("/upl")
    public String importParse(Data data) throws IOException {
        try {
            doStuff(data);
            TimeUnit.SECONDS.sleep(3);
            doStuff(data);
        } catch (Exception exception) {
            error = true;
            System.out.println(exception.getMessage());
        }

        return "redirect:/";
    }

    @PostMapping("/ok")
    public String onErr(){
        error = false;
        return "redirect:/";
    }
}