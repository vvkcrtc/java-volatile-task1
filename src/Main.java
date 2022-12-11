import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

enum Checks {CHECK_PALINDROME, CHECK_INC, ONE_CHAR}

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

    static boolean isOneChar(String word) {
        char firstChar = word.charAt(0);
        for (char c : word.toCharArray()) {
            if (c != firstChar) {
                return false;
            }
        }
        return true;
    }

    static boolean isIncChars(String word) {
        int strCount = word.length() - 1;
        int incCount = 0;

        if (isOneChar(word)) {
            return false;
        }

        for (int i = 0; i < strCount; i++) {
            if (word.charAt(i) <= word.charAt(i + 1)) {
                incCount++;
            }
        }
        if (incCount == strCount) {
            return true;
        } else {
            return false;
        }
    }

    static boolean isPalindrome(String word) {
        int i1 = 0;
        int i2 = word.length() - 1;

        if (isOneChar(word)) {
            return false;
        }

        while (i2 > i1) {
            if (word.charAt(i1) != word.charAt(i2)) {
                return false;
            }
            ++i1;
            --i2;
        }
        for (int i = 0; i < word.length() / 2; i++) {
            if (word.charAt(i) > word.charAt(i + 1)) {
                return false;
            }
        }
        return true;
    }

    public static boolean isBeautifulNick(String word) {
        if (isPalindrome(word) || isIncChars(word) || isOneChar(word)) {
            return true;
        }
        return false;
    }

    static AtomicInteger countNicks3 = new AtomicInteger(0);
    static AtomicInteger countNicks4 = new AtomicInteger(0);
    static AtomicInteger countNicks5 = new AtomicInteger(0);

    static class CountNicks implements Runnable {
        Checks checks;

        public CountNicks(Checks checks) {
            this.checks = checks;
        }

        public void run() {
            for (int i = 0; i < texts.length; i++) {
                if (checks == Checks.CHECK_PALINDROME && isPalindrome(texts[i]) ||
                        checks == Checks.CHECK_INC && isIncChars(texts[i]) ||
                        checks == Checks.ONE_CHAR && isOneChar(texts[i])) {

                    switch (texts[i].length()) {
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
    }




    public static void main(String[] args) throws ExecutionException, InterruptedException {

        Random random = new Random();

        for (int i = 0; i < texts.length; i++) {
            texts[i] = generateText("abc", 3 + random.nextInt(3));
        }
        int countPal = 0;

        for (String str : texts) {
            if (isBeautifulNick(str) == true) {
                System.out.println(str);
                countPal++;
            }
        }

        List<Thread> threads = new ArrayList<>();

        for (Checks ch : Checks.values()) {
            threads.add(new Thread(new CountNicks(ch)));
        }

        for(Thread tr : threads) {
            tr.start();
        }

        for(Thread tr : threads) {
            tr.join();
        }

        System.out.println("Красивых слов с длиной 3 шт " + countNicks3.intValue());
        System.out.println("Красивых слов с длиной 4 шт " + countNicks4.intValue());
        System.out.println("Красивых слов с длиной 5 шт " + countNicks5.intValue());
        System.out.println("Красивых слов всего " + countPal);
        System.out.println(" " + (countNicks3.intValue() + countNicks4.intValue() + countNicks5.intValue()));


    }
}
