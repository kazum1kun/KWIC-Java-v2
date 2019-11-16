import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class OutputFormatter implements IKwicOutput, Runnable {

    /**
     * A buffer for processed input that is yet to be outputted
     */
    private static BlockingQueue<List<String>> buffer;

    /**
     * Callback function that is called when additional processed input is available from Alphabetizer
     */
    void onAlphabetizerLineReady(Alphabetizer al) {
        List<String> alphaLines = al.getAlphabetizedLines();
        if (alphaLines != null) {
            try {
                buffer.put(alphaLines);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Output shifted and alphabetized lines to the stdout
     */
    public void outputToStdOut() {
        List<String> outputLines = buffer.poll();
        if (outputLines != null) {
            for (String s : outputLines) {
                System.out.println(s);
            }
        }

    }

    @Override
    public void run() {
        buffer = new LinkedBlockingQueue<>();
    }
}
