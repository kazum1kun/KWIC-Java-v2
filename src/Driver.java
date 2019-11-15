public class Driver {
    public static void main(String[] args) {
        // Initialize components
        ExtInput ex = new ExtInput();
        OutputFormatter of = new OutputFormatter();
        Alphabetizer al = new Alphabetizer(of);
        CircularShifter cs = new CircularShifter(al);
        InputChecker ic = new InputChecker(cs, ex);

        Thread oft, alt, cst, ict, ext;
        oft = new Thread(of);
        alt = new Thread(al);
        cst = new Thread(cs);
        ict = new Thread(ic);
        ext = new Thread(ex);

        ext.start();
        oft.start();
        alt.start();
        cst.start();
        ict.start();
    }
}
