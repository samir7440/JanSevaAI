package com.janseva.backend.service;

import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class PriorityService {


public String detectPriority(

        String category,

        String description

) {

    if (description == null) {
        description = "";
    }

    String lower =
            description.toLowerCase();

    List<String> emergencyWords =
            Arrays.asList(

                    "murder",
                    "rape",
                    "attack",
                    "gun",
                    "ambulance",
                    "blood",
                    "fire",
                    "blast",
                    "suicide",
                    "harassment",
                    "child abuse",
                    "women safety",
                    "violence",
                    "accident",
                    "emergency",
                    "death"
            );

    for (String word : emergencyWords) {

        if (lower.contains(word)) {

            return "CRITICAL";
        }
    }

    if (

            category != null

                    &&

                    (

                            category.equalsIgnoreCase("Police")

                                    ||

                                    category.equalsIgnoreCase("Health")

                                    ||

                                    category.equalsIgnoreCase("Women")
                    )
    ) {

        return "HIGH";
    }

    return "NORMAL";
}


}
