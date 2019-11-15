import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ExtInput implements IKwicInput, Runnable {
    private static BlockingQueue<List<String>> buffer;
    private static Scanner scan;

    ExtInput() {
        buffer = new LinkedBlockingQueue<>();
        scan = new Scanner(System.in);
    }

    private static void getUserInput() {
        List<String> userInput = new ArrayList<>();
        while (true) {
            String temp = scan.nextLine();
            if (temp.equals("end")) {
                try {
                    buffer.put(userInput);
                    userInput = new ArrayList<>();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else if (temp.equals("exit")) {
                System.exit(0);
            } else {
                userInput.add(temp);
            }
        }
    }

    @Override
    public List<String> inputFromStdin() {
        return buffer.poll();
    }

    @Override
    public void run() {
        getUserInput();
    }
}
