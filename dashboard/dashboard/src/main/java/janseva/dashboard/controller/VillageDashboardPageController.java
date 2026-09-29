package janseva.dashboard.controller;

import janseva.dashboard.model.Complaint;

import janseva.dashboard.repository.ComplaintRepository;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class VillageDashboardPageController {

    @Autowired
    private ComplaintRepository repository;

    @GetMapping("/")
    public String dashboard(
            Model model
    ) {

        List<Complaint> complaints =

                repository.findByCurrentLevel(
                        "VILLAGE"
                );

        long pending =

                complaints.stream()

                        .filter(c ->

                                c.getStatus() != null &&

                                c.getStatus()
                                        .equalsIgnoreCase(
                                                "PENDING"
                                        )
                        )

                        .count();

        long resolved =

                complaints.stream()

                        .filter(c ->

                                c.getStatus() != null &&

                                c.getStatus()
                                        .equalsIgnoreCase(
                                                "RESOLVED"
                                        )
                        )

                        .count();

        model.addAttribute(
                "complaints",

                complaints
        );

        model.addAttribute(
                "total",

                complaints.size()
        );

        model.addAttribute(
                "pending",

                pending
        );

        model.addAttribute(
                "resolved",

                resolved
        );

        return "index";
    }
}