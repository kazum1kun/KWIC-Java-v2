import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadLocalRandom;

class InputChecker implements Runnable {

    /**
     * A buffer for processed input that is yet to be processed by CircularShifter
     */
    private static BlockingQueue<List<String>> buffer;
    private static CircularShifter csInstance;
    private static IKwicInput inputSource;

    InputChecker(CircularShifter cs, IKwicInput input) {
        csInstance = cs;
        inputSource = input;
    }

    /**
     * Get user input from the external interface IKwicInput
     */
    private void getInput() {
        while (true) {
            List<String> input = inputSource.inputFromStdin();
            if (input != null) {
                checkInput(input);
            }

            try {
                Thread.sleep(ThreadLocalRandom.current().nextInt(1000, 2000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Check if input is semantically correct. Bad ones will be rejected
     */
    private boolean checkInput(List<String> in) {
        // For now everything will be accepted
        try {
            buffer.put(in);
            notifyCircularShifter();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return true;
    }

    /**
     * Notify the circular shifter that additional input is available
     */
    private void notifyCircularShifter() {
        csInstance.onInputReady(this);
    }

    /**
     * Return the oldest checked input from the buffer
     */
    List<String> getCheckedInput() {
        return buffer.poll();
    }

    @Override
    public void run() {
        buffer = new LinkedBlockingQueue<>();
        getInput();
    }
}
