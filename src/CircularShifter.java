import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class CircularShifter implements CircularShift, Runnable {
    /**
     * A buffer for processed input that is yet to be processed by Alphabetizer
     */
    private static BlockingQueue<List<String>> buffer;
    private static Alphabetizer alInstance;

    CircularShifter(Alphabetizer al) {
        alInstance = al;
    }

    /**
     * Callback function that is called when additional input is available from InputChecker
     */
    void onInputReady(InputChecker icInstance) {
        List<String> input = icInstance.getCheckedInput();
        if (input != null) {
            circularShift(input);
        }
    }

    /**
     * Circular shift the lines and store the result into the buffer
     */
    private void circularShift(List<String> lines) {
        List<String> shifted = new ArrayList<>();

        // Iterate through the original lines
        for (String line : lines) {
            // Separate the line into an array
            String[] words = line.split(" ");
            List<String> currentShiftedLine = new LinkedList<>(Arrays.asList(words));

            // Shift the line from 0 to n-1 times
            for (int i = 0; i < currentShiftedLine.size(); i++) {
                shifted.add(String.join(" ", currentShiftedLine));
                // Remove a word from the beginning and append it to the end
                currentShiftedLine.add(currentShiftedLine.remove(0));
            }
        }

        try {
            buffer.put(shifted);
            notifyAlphabetizer();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Notify the alphabetizer that additional shifted line is available
     */
    private void notifyAlphabetizer() {
        alInstance.onShiftedLinesReady(this);
    }

    /**
     * Return the oldest shifted input from the buffer
     */
    public List<String> getShiftedLines() {
        return buffer.poll();
    }

    @Override
    public void run() {
        if (buffer == null) {
            buffer = new LinkedBlockingQueue<>();
        }
    }
}
