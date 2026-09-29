package com.janseva.backend.service;

import com.janseva.backend.model.Category;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AIService {

    public String getSmartCategoryMatch(
            String query,
            List<Category> allCategories
    ) {

        if (
                query == null ||
                query.isBlank() ||
                allCategories == null ||
                allCategories.isEmpty()
        ) {
            return "Other";
        }

        String lower =
                query.toLowerCase();

        Map<String,Integer> scores =
                new HashMap<>();

        for (Category category : allCategories) {

            String categoryName =
                    category.getMainCategory();

            int score = 0;

            if (
                    lower.contains(
                            categoryName.toLowerCase()
                    )
            ) {
                score += 25;
            }

            if (
                    category.getSubCategories()
                            != null
            ) {

                for (
                        String sub :
                        category.getSubCategories()
                ) {

                    if (
                            lower.contains(
                                    sub.toLowerCase()
                            )
                    ) {
                        score += 12;
                    }
                }
            }

            score += keywordScore(
                    categoryName,
                    lower
            );

            scores.put(
                    categoryName,
                    score
            );
        }

        String bestCategory =
                "Other";

        int highestScore =
                0;

        for (
                Map.Entry<String,Integer> entry :
                scores.entrySet()
        ) {

            if (
                    entry.getValue()
                            > highestScore
            ) {

                highestScore =
                        entry.getValue();

                bestCategory =
                        entry.getKey();
            }
        }

        if (
                highestScore < 10
        ) {
            return "Other";
        }

        return bestCategory;
    }

    private int keywordScore(
            String category,
            String text
    ) {

        List<String> keywords =
                switch (
                        category.toLowerCase()
                ) {

                    case "water" -> Arrays.asList(

                            "water",
                            "pani",
                            "jal",
                            "pipeline",
                            "pipe",
                            "leakage",
                            "tap",
                            "nal",
                            "dirty water",
                            "water supply",
                            "no water"
                    );

                    case "electricity" -> Arrays.asList(

                            "electricity",
                            "bijli",
                            "light",
                            "power cut",
                            "current",
                            "transformer",
                            "meter",
                            "wire",
                            "spark",
                            "voltage"
                    );

                    case "roads" -> Arrays.asList(

                            "road",
                            "sadak",
                            "gadda",
                            "pothole",
                            "bridge",
                            "highway",
                            "street damage"
                    );

                    case "health" -> Arrays.asList(

                            "hospital",
                            "doctor",
                            "medicine",
                            "ambulance",
                            "health",
                            "injury",
                            "medical",
                            "treatment",
                            "fever"
                    );

                    case "police" -> Arrays.asList(

                            "police",
                            "crime",
                            "theft",
                            "fight",
                            "violence",
                            "murder",
                            "rape",
                            "attack",
                            "gun",
                            "harassment",
                            "missing"
                    );

                    case "garbage" -> Arrays.asList(

                            "garbage",
                            "kachra",
                            "dustbin",
                            "trash",
                            "waste",
                            "dirty area"
                    );

                    case "drainage" -> Arrays.asList(

                            "drain",
                            "drainage",
                            "sewage",
                            "naali",
                            "gutter",
                            "water logging"
                    );

                    case "internet" -> Arrays.asList(

                            "internet",
                            "wifi",
                            "network",
                            "tower",
                            "signal",
                            "broadband",
                            "slow internet"
                    );

                    case "education" -> Arrays.asList(

                            "school",
                            "teacher",
                            "student",
                            "college",
                            "exam",
                            "scholarship",
                            "education"
                    );

                    case "agriculture" -> Arrays.asList(

                            "farmer",
                            "crop",
                            "fertilizer",
                            "tractor",
                            "kisan",
                            "subsidy",
                            "farming"
                    );
                                        case "transport" -> Arrays.asList(

                            "bus",
                            "traffic",
                            "transport",
                            "auto",
                            "taxi",
                            "vehicle",
                            "jam",
                            "road accident"
                    );

                    case "housing" -> Arrays.asList(

                            "house",
                            "home",
                            "ghar",
                            "housing",
                            "pm awas",
                            "land dispute",
                            "building"
                    );

                    case "women" -> Arrays.asList(

                            "women",
                            "mahila",
                            "girl",
                            "women safety",
                            "harassment",
                            "domestic violence",
                            "lady problem"
                    );

                    case "child" -> Arrays.asList(

                            "child",
                            "kid",
                            "bacha",
                            "child labour",
                            "child abuse",
                            "missing child"
                    );

                    case "employment" -> Arrays.asList(

                            "job",
                            "employment",
                            "unemployment",
                            "work issue",
                            "mgnrega",
                            "salary"
                    );

                    case "sanitation" -> Arrays.asList(

                            "toilet",
                            "cleaning",
                            "sanitation",
                            "dirty area",
                            "public toilet"
                    );

                    case "tourism" -> Arrays.asList(

                            "tourism",
                            "tourist",
                            "guide",
                            "monument",
                            "travel place"
                    );

                    case "banking" -> Arrays.asList(

                            "bank",
                            "atm",
                            "account",
                            "loan",
                            "payment",
                            "transaction"
                    );

                    case "fire" -> Arrays.asList(

                            "fire",
                            "aag",
                            "blast",
                            "burning",
                            "short circuit"
                    );

                    case "pension" -> Arrays.asList(

                            "pension",
                            "retirement",
                            "old age",
                            "widow pension"
                    );

                    case "other" -> Arrays.asList(

        "problem",
        "issue",
        "complaint",
        "complain",
        "general",
        "other",
        "help",
        "support",
        "unknown",
        "misc",
        "general problem",
        "help me",
        "issue hai",
        "problem hai",
        "samasya",
        "shikayat"
);

                    default -> Collections.emptyList();
                };

        int score = 0;

        for (String keyword : keywords) {

            if (
                    text.contains(
                            keyword.toLowerCase()
                    )
            ) {
                score += 15;
            }
        }

        return score;
    }

    public Map<String,Object> analyzeComplaint(
            String text,
            String imageUrl
    ) {

        String lower =
                text == null
                        ? ""
                        : text.toLowerCase();

        boolean fake = false;
        boolean emergency = false;

        double confidence = 98.0;

        String analysis =
                "Complaint appears genuine";

        List<String> fakeWords =
                Arrays.asList(

                        "test complaint",
                        "demo",
                        "trial",
                        "random",
                        "fake",
                        "spam",
                        "just checking",
                        "testing"
                );

        for (String word : fakeWords) {

            if (lower.contains(word)) {

                fake = true;

                confidence = 90.0;

                analysis =
                        "Suspicious complaint detected";

                break;
            }
        }

        List<String> emergencyWords =
                Arrays.asList(

                        "fire",
                        "blast",
                        "murder",
                        "rape",
                        "attack",
                        "gun",
                        "blood",
                        "accident",
                        "ambulance",
                        "suicide",
                        "violence",
                        "emergency",
                        "death",
                        "terror"
                );

        for (String word : emergencyWords) {

            if (lower.contains(word)) {

                emergency = true;

                confidence = 99.5;

                analysis =
                        "Emergency complaint detected";

                break;
            }
        }

        Map<String,Object> result =
                new HashMap<>();

        result.put("is_fake", fake);
        result.put("emergency", emergency);
        result.put("confidence", confidence);
        result.put("analysis", analysis);

        return result;
    }
}