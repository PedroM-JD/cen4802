package com.pedro.cen4802;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
public class StudySessionController {
    private final List<StudySession> sessions = new ArrayList<>();

    @GetMapping("/")
    public String showHomePage(Model model) {
        int totalMinutes = 0;

        for (StudySession session : sessions) {
            totalMinutes += session.getMinutes();
        }

        model.addAttribute("sessions", sessions);
        model.addAttribute("totalMinutes", totalMinutes);

        return "index";
    }

    @PostMapping("/sessions")
    public String addSession(@RequestParam("courseName") String courseName,
                             @RequestParam("minutes") int minutes) {
        if (!courseName.isBlank() && minutes > 0) {
            sessions.add(new StudySession(courseName.trim(), minutes));
        }

        return "redirect:/";
    }
}