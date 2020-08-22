public class Start {
    private static final int workers = 5;

    public static void main(String[] args) {
        for (int i = 0; i < workers; i++) {
            new Thread(new Worker()).start();
        }
        new Thread(new Tasker()).start();
    }
}
