package com.example.demo;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class DocumentSearchEngine {

    private static final Set<String> COMMON_WORDS_EXCLUDE = Set.of("of", "the", "to", "and", "for");

    public static void main(String[] args) throws IOException {
        Scanner console = null; 
        if (args.length < 1) {
            System.out.println("Missing Args. Application Usage: java DocumentSearchEngine <argsword> ");
            console = new Scanner(System.in);
            System.out.println("Please Enter search word to  make a search in the listed documents: ");
            String input = console.nextLine();
            args = new String[]{input};
            //System.exit(1);
        }

        String searchWord = args[0].toLowerCase();
        if (COMMON_WORDS_EXCLUDE.contains(searchWord)) {
            System.out.println("The word '" + searchWord + "' is a common word and is ignored.");
            System.exit(2);
        }

        Map<String, String> documents = Map.of(
                "Declaration Of Independence", loadFile("DeclarationOfIndependence.txt"),
                "US Constitution", loadFile("Constitution.txt"),
                "Magna Carta", loadFile("MagnaCarta.txt")
        );

        Map<String, Integer> wordCounts = new HashMap<>();
        for (Map.Entry<String, String> entry : documents.entrySet()) {
            int count = wordOcurrencesCounter(entry.getValue(), searchWord);
            wordCounts.put(entry.getKey(), count);
        }
        System.out.println("Word gets counted for '" + searchWord + "':");
        String bestMatch = Collections.max(wordCounts.entrySet(), Map.Entry.comparingByValue()).getKey();
        int maxCount = wordCounts.get(bestMatch);

        if (maxCount > 0) {
            System.out.println("The word '" + searchWord + "' occurs " + maxCount + " times in " + bestMatch + ".");
            System.out.println("-because " + searchWord + " occurs in the " +bestMatch + " the most often."); 
        } else {
            System.out.println("The word '" + searchWord + "' was not found in any document.");
        }
    }

    private static String loadFile(String filename) throws IOException {
        return Files.readString(Paths.get("docs", filename)).toLowerCase().replaceAll("[^a-z\\s]", "");
    }

    private static int wordOcurrencesCounter(String content, String word) {
        List<String> words = Arrays.asList(content.split("\\s+"));
        return (int) words.stream().filter(w -> w.equals(word)).count();
    }
}
