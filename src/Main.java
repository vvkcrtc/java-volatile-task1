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

    static AtomicInteger countNicks3 = new AtomicInteger(0);
    static AtomicInteger countNicks4 = new AtomicInteger(0);
    static AtomicInteger countNicks5 = new AtomicInteger(0);

    static class CountNicks implements Callable<Integer> {
        int size;

        public CountNicks(int size) {
            this.size = size;
        }

        public Integer call() throws Exception {
            for (int i = 0; i < texts.length; i++) {
                if (texts[i].length() == size) {
                    if (isPalindrome(texts[i])) {
                        switch (size) {
                            case 3:
                                countNicks3.addAndGet(1);
                                break;
                            case 4:
                                countNicks4.addAndGet(1);
                                break;
                            case 5:
                                countNicks5.addAndGet(1);
                                break;
                            default:
                                break;
                        }
                    }
                }
            }
            return size;
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
            task.get();
        }

        threadPool.shutdown();

        System.out.println("Красивых слов с длиной 3 шт " + countNicks3.intValue());
        System.out.println("Красивых слов с длиной 4 шт " + countNicks4.intValue());
        System.out.println("Красивых слов с длиной 5 шт " + countNicks5.intValue());
        System.out.println("Красивых слов всего " + countPal);


    }
}
