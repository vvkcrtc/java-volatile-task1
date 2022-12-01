import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;


public class Main {

    static String[] texts = new String[100_000];

    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }


    public static boolean isPalindrome(String word) {
        int i1 = 0;
        int i2 = word.length() - 1;
        while (i2 > i1) {
            if (word.charAt(i1) != word.charAt(i2)) {
                return false;
            }
            ++i1;
            --i2;
        }
        for (i1 = 0; i1 < word.length() / 2; i1++) {
            if (word.charAt(i1) > word.charAt(i1 + 1)) {
                return false;
            }
        }
        return true;
    }

    static class CountNicks implements Callable<Integer> {
        int size;
        AtomicInteger countNicks = new AtomicInteger(0);

        public CountNicks(int size) {
            this.size = size;
        }

        public Integer call() throws Exception {
            for (int i = 0; i < texts.length; i++) {
                if (texts[i].length() == size) {
                    if (isPalindrome(texts[i])) {
                        countNicks.addAndGet(1);
                    }
                }
            }
            //System.out.println("Thread :"+size);
            return countNicks.intValue();
        }
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        Random random = new Random();

        for (int i = 0; i < texts.length; i++) {
            texts[i] = generateText("abc", 3 + random.nextInt(3));
        }
        int countPal = 0;

        for (String str : texts) {
            if (isPalindrome(str) == true) {
                System.out.println(str);
                countPal++;
            }
        }

        List<Future> threads = new ArrayList<>();
        ExecutorService threadPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        for (int i = 0; i < 3; i++) {
            Callable callable = new CountNicks(i + 3);
            threads.add(threadPool.submit(callable));
        }

        for (int t = 0; t < threads.size(); t++) {
            Future task = threads.get(t);
            Integer result = (Integer) task.get();
            System.out.println("Красивых слов с длиной " + (t + 3) + " шт " + result.intValue());
        }
        threadPool.shutdown();


        System.out.println("Красивых слов всего " + countPal);


    }
}
