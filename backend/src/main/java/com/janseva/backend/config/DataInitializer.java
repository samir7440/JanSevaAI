package com.janseva.backend.config;

import com.janseva.backend.model.Category;
import com.janseva.backend.model.Officer;
import com.janseva.backend.model.SystemSettings;
import com.janseva.backend.model.User;

import com.janseva.backend.repository.CategoryRepository;
import com.janseva.backend.repository.OfficerRepository;
import com.janseva.backend.repository.SystemSettingsRepository;
import com.janseva.backend.repository.UserRepository;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;
import java.util.*;

@Configuration
public class DataInitializer {


@Bean
CommandLineRunner initDatabase(

        CategoryRepository categoryRepository,

        OfficerRepository officerRepository,

        UserRepository userRepository,

        SystemSettingsRepository settingsRepository,

        BCryptPasswordEncoder passwordEncoder

) {

    return args -> {

        categoryRepository.deleteAll();

        officerRepository.deleteAll();

        userRepository.deleteAll();

        settingsRepository.deleteAll();

        List<Category> categories = Arrays.asList(

                createCategory(
                        "Water",
                        Arrays.asList(
                                "Pipeline Leakage",
                                "No Supply",
                                "Dirty Water"
                        )
                ),

                createCategory(
                        "Electricity",
                        Arrays.asList(
                                "Power Cut",
                                "Meter Issue",
                                "Wire Sparking"
                        )
                ),

                createCategory(
                        "Roads",
                        Arrays.asList(
                                "Potholes",
                                "Street Damage",
                                "Bridge Damage"
                        )
                ),

                createCategory(
                        "Health",
                        Arrays.asList(
                                "Hospital Problem",
                                "Medicine Shortage",
                                "Doctor Issue"
                        )
                ),

                createCategory(
                        "Police",
                        Arrays.asList(
                                "Theft",
                                "Violence",
                                "Harassment"
                        )
                ),

                createCategory(
                        "Garbage",
                        Arrays.asList(
                                "Garbage Collection",
                                "Dirty Area",
                                "Dustbin Missing"
                        )
                ),

                createCategory(
                        "Drainage",
                        Arrays.asList(
                                "Water Logging",
                                "Drain Blockage",
                                "Sewage"
                        )
                ),

                createCategory(
                        "Internet",
                        Arrays.asList(
                                "No Internet",
                                "Slow Speed",
                                "Tower Problem"
                        )
                ),

                createCategory(
                        "Education",
                        Arrays.asList(
                                "Teacher Complaint",
                                "School Issue",
                                "Scholarship"
                        )
                ),

                createCategory(
                        "Agriculture",
                        Arrays.asList(
                                "Crop Damage",
                                "Subsidy Issue",
                                "Fertilizer Problem"
                        )
                ),

                createCategory(
                        "Transport",
                        Arrays.asList(
                                "Traffic Jam",
                                "Bus Delay",
                                "Road Accident"
                        )
                ),

                createCategory(
                        "Housing",
                        Arrays.asList(
                                "House Damage",
                                "PM Awas Issue",
                                "Land Dispute"
                        )
                ),

                createCategory(
                        "Sanitation",
                        Arrays.asList(
                                "Public Toilet",
                                "Cleaning Issue",
                                "Dirty Area"
                        )
                ),

                createCategory(
                        "Women",
                        Arrays.asList(
                                "Women Safety",
                                "Harassment",
                                "Emergency Help"
                        )
                ),

                createCategory(
                        "Child",
                        Arrays.asList(
                                "Child Labour",
                                "Child Abuse",
                                "Missing Child"
                        )
                ),

                createCategory(
                        "Employment",
                        Arrays.asList(
                                "Job Issue",
                                "MGNREGA",
                                "Unemployment"
                        )
                ),

                createCategory(
                        "Other",
                        Arrays.asList(
                                "General Complaint"
                        )
                ),
                createCategory(
        "Tourism",
        Arrays.asList(
                "Tourist Issue",
                "Guide Problem",
                "Monument Damage"
        )
),

createCategory(
        "Banking",
        Arrays.asList(
                "ATM Issue",
                "Loan Problem",
                "Transaction Failed"
        )
),

createCategory(
        "Fire",
        Arrays.asList(
                "Fire Emergency",
                "Short Circuit",
                "Building Fire"
        )
),

createCategory(
        "Pension",
        Arrays.asList(
                "Old Age Pension",
                "Widow Pension",
                "Retirement Benefit"
        )
          )
        );

        categoryRepository.saveAll(
                categories
        );

        List<Officer> officers = Arrays.asList(

                createOfficer(
                        "Rajesh Verma",
                        "Water",
                        "VILLAGE",
                        "9876501001"
                ),

                createOfficer(
                        "Sanjay Patel",
                        "Water",
                        "DISTRICT",
                        "9876501002"
                ),

                createOfficer(
                        "Amit Tiwari",
                        "Water",
                        "STATE",
                        "9876501003"
                ),

                createOfficer(
                        "Vikas Sharma",
                        "Electricity",
                        "VILLAGE",
                        "9876502001"
                ),

                createOfficer(
                        "Rohit Singh",
                        "Electricity",
                        "DISTRICT",
                        "9876502002"
                ),

                createOfficer(
                        "Anil Dubey",
                        "Electricity",
                        "STATE",
                        "9876502003"
                ),

                createOfficer(
                        "Rakesh Yadav",
                        "Roads",
                        "VILLAGE",
                        "9876503001"
                ),

                createOfficer(
                        "Mukesh Rao",
                        "Roads",
                        "DISTRICT",
                        "9876503002"
                ),

                createOfficer(
                        "Kunal Mishra",
                        "Roads",
                        "STATE",
                        "9876503003"
                ),

                createOfficer(
                        "Dr Amit Sharma",
                        "Health",
                        "VILLAGE",
                        "9876504001"
                ),

                createOfficer(
                        "Dr Neha Jain",
                        "Health",
                        "DISTRICT",
                        "9876504002"
                ),

                createOfficer(
                        "Dr Vikram Joshi",
                        "Health",
                        "STATE",
                        "9876504003"
                ),

                createOfficer(
                        "Inspector Rana",
                        "Police",
                        "VILLAGE",
                        "9876505001"
                ),

                createOfficer(
                        "DSP Harshit",
                        "Police",
                        "DISTRICT",
                        "9349609286"
                ),

                createOfficer(
                        "Aditya Choudhary",
                        "Police",
                        "STATE",
                        "7089083424"
                ),
createOfficer(
        "Water Block Officer",
        "Water",
        "BLOCK",
        "9876511001"
),

createOfficer(
        "Water Zone Officer",
        "Water",
        "ZONE",
        "9876511002"
),

createOfficer(
        "Water Central Officer",
        "Water",
        "CENTRAL",
        "9876511003"
),
createOfficer(
        "Electricity Block Officer",
        "Electricity",
        "BLOCK",
        "9876521001"
),

createOfficer(
        "Electricity Zone Officer",
        "Electricity",
        "ZONE",
        "9876521002"
),

createOfficer(
        "Electricity Central Officer",
        "Electricity",
        "CENTRAL",
        "9876521003"
),
createOfficer(
        "Road Block Officer",
        "Roads",
        "BLOCK",
        "9876531001"
),

createOfficer(
        "Road Zone Officer",
        "Roads",
        "ZONE",
        "9876531002"
),

createOfficer(
        "Road Central Officer",
        "Roads",
        "CENTRAL",
        "9876531003"
),
createOfficer(
        "Health Block Officer",
        "Health",
        "BLOCK",
        "9876541001"
),

createOfficer(
        "Health Zone Officer",
        "Health",
        "ZONE",
        "9876541002"
),

createOfficer(
        "Health Central Officer",
        "Health",
        "CENTRAL",
        "9876541003"
),
createOfficer(
        "Police Block Officer",
        "Police",
        "BLOCK",
        "9876551001"
),

createOfficer(
        "Police Zone Officer",
        "Police",
        "ZONE",
        "9876551002"
),

createOfficer(
        "Police Central Officer",
        "Police",
        "CENTRAL",
        "9876551003"
),
createOfficer(
        "General Block Officer",
        "Other",
        "BLOCK",
        "9876561001"
),

createOfficer(
        "General Zone Officer",
        "Other",
        "ZONE",
        "9876561002"
),

createOfficer(
        "General Central Officer",
        "Other",
        "CENTRAL",
        "9876561003"
),
                createOfficer(
                        "General Officer",
                        "Other",
                        "VILLAGE",
                        "7440521040"
                )
        );

        officerRepository.saveAll(
                officers
        );

        List<User> users = Arrays.asList(

                createUser(
                        "Samir Sulakhe",
                        "9999999991",
                        "samir123",
                        "CITIZEN",
                        passwordEncoder
                ),

                createUser(
                        "Rahul Sharma",
                        "9999999992",
                        "rahul123",
                        "CITIZEN",
                        passwordEncoder
                ),

                createUser(
                        "Anjali Verma",
                        "9999999993",
                        "anjali123",
                        "CITIZEN",
                        passwordEncoder
                ),

                createUser(
                        "Admin User",
                        "9999990000",
                        "admin123",
                        "ADMIN",
                        passwordEncoder
                ),

                createUser(
                        "Water Officer",
                        "9876501001",
                        "water123",
                        "OFFICER",
                        passwordEncoder
                ),

                createUser(
        "Block Officer",
        "9876511001",
        "block123",
        "OFFICER",
        passwordEncoder
),

createUser(
        "Zone Officer",
        "9876511002",
        "zone123",
        "OFFICER",
        passwordEncoder
),

createUser(
        "Central Officer",
        "9876511003",
        "central123",
        "OFFICER",
        passwordEncoder
),
                createUser(
                        "Police Officer",
                        "9349609286",
                        "police123",
                        "OFFICER",
                        passwordEncoder
                )
        );

        userRepository.saveAll(
                users
        );

        SystemSettings settings =
                new SystemSettings();

        settings.setComplaintSubmissionEnabled(
                true
        );

        settings.setAiModerationEnabled(
                true
        );

        settings.setEscalationEnabled(
                true
        );

        settings.setLiveTrackingEnabled(
                true
        );

        settingsRepository.save(
                settings
        );

        System.out.println(
                "REAL GOVERNANCE SYSTEM INITIALIZED SUCCESSFULLY"
        );
    };
}

private Category createCategory(

        String name,

        List<String> subCategories

) {

    Category category =
            new Category();

    category.setMainCategory(name);

    category.setNames(

            Map.of(
                    "en", name,
                    "hi", name
            )
    );

    List<String> finalSubs =
            new ArrayList<>(subCategories);

    if (

            !finalSubs.contains(
                    "Other Problem"
            )
    ) {

        finalSubs.add(
                "Other Problem"
        );
    }

    category.setSubCategories(
            finalSubs
    );

    return category;
}

private Officer createOfficer(

        String name,

        String department,

        String level,

        String mobile

) {

    return new Officer(

            null,

            name,

            department,

            level,

            mobile,

            true
    );
}

private User createUser(

        String fullName,

        String mobile,

        String password,

        String role,

        BCryptPasswordEncoder passwordEncoder

) {

    User user = new User();

    user.setFullName(
            fullName
    );

    user.setMobile(
            mobile
    );

    user.setPassword(

            passwordEncoder.encode(
                    password
            )
    );

    user.setRole(
            role
    );

    user.setActive(
            true
    );

    user.setCreatedAt(
            LocalDateTime.now()
    );

    return user;
}


}
