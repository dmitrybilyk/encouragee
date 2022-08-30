package main;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class SteamsJoining {
    public static void main(String[] args) {
        // Creating a character list
        List<Character> ch = Arrays.asList(
                'G', 'e', 'e', 'k', 's', 'f', 'o', 'r', 'G',
                'e', 'e', 'k', 's');

        // Converting character list into string
        // using joining() method of Collectors class
        String chString
                = ch.stream()
                .map(String::valueOf)
                .collect(Collectors.joining());

        // Printing the concatenated string
        System.out.println(chString);
    }
}
