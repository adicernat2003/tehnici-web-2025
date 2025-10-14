import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Main {
    public static void main(String[] args) {
//        var context = new AnnotationConfigApplicationContext(ContextConfiguration.class);
//
//        var databaseConnectionService = context.getBean(DatabaseConnectionService.class);
//
//        databaseConnectionService.connect();

        //1. Given a book as a String array, count the number of times a word appears in the book.
        String[] word = {"Ana", "are", "mere", "are", "mere", "are"};

        Map<String, Long> counts = Arrays.stream(word)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        System.out.println(counts);
        List<Integer> list = IntStream.of(1, 2, 3, 4, 5)
                .boxed()
                .toList();

        // 2. What would you do differently if the operation needs to be repeatable and the book has a few million words?
        System.out.println(containsText("4191996"));

    }

    private static boolean containsText(CharSequence str) {
        int strLen = str.length();
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(str.charAt(i))) {
                return true;
            }
        }
        return false;
    }
}
