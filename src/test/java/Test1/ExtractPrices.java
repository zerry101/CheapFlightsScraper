package Test1;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExtractPrices {
    public  List<String> extractPricesFromText(String filePath) throws IOException {

        List<String> fileLines = Files.readAllLines(Paths.get(filePath));

        List<String> priceListOfFlights = new ArrayList<>();

        String priceRegexPattern = "(CAD ?\\d+|\\$ ?\\d+|\\d+ ?CAD|\\d+ ?\\$|\\$? ?\\[\\d+\\] ?\\$?|CAD? ?\\[\\d+\\] ?CAD?)";
        Pattern priceRegexPatternComplied = Pattern.compile(priceRegexPattern);

        for (String fileLine : fileLines) {
            Matcher regexPriceMatcher = priceRegexPatternComplied.matcher(fileLine);
            while (regexPriceMatcher.find()) {
                String priceMatch = regexPriceMatcher.group();
                priceListOfFlights.add(priceMatch.replace(" ", ""));
            }
        }

        return priceListOfFlights;
    }
}
