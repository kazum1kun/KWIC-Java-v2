import java.text.Collator;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

class Alphabetizer implements Runnable {

    /**
     * A buffer for processed input that is yet to be accepted by the output formatter
     */
    private static BlockingQueue<List<String>> buffer;
    private static OutputFormatter ofInstance;

    Alphabetizer(OutputFormatter of) {
        ofInstance = of;
    }

    /**
     * Callback function that is called when additional shifted input is available from CircularShifter
     */
    void onShiftedLinesReady(CircularShift csInstance) {
        List<String> shiftedLines = csInstance.getShiftedLines();
        if (shiftedLines != null) {
            alphabetize(shiftedLines);
        }

    }

    /**
     * Sort the lines in alphabetical order and store in the buffer
     */
    private void alphabetize(List<String> input) {
        input.sort(Collator.getInstance());
        try {
            buffer.put(input);
            notifyOutputFormatter();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void notifyOutputFormatter() {
        ofInstance.onAlphabetizerLineReady(this);
    }

    /**
     * Return the oldest alphabetized input from the buffer
     */
    List<String> getAlphabetizedLines() {
        return buffer.poll();
    }

    @Override
    public void run() {
        if (buffer == null) {
            buffer = new LinkedBlockingQueue<>();
        }
    }
}
