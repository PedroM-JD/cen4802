
package com.pedro.cen4802;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
public class StudySessionController {

    private static final Logger logger =
            LoggerFactory.getLogger(StudySessionController.class);

    private final List<StudySession> sessions = new ArrayList<>();

    @GetMapping("/")
    public String showHomePage(Model model) {
        logger.info("Home page opened. Total sessions: {}", sessions.size());
        logger.debug("Calculating total study minutes");

        int totalMinutes = 0;

        for (StudySession studySession : sessions) {
            totalMinutes += studySession.getMinutes();
        }

        model.addAttribute("sessions", sessions);
        model.addAttribute("totalMinutes", totalMinutes);
        model.addAttribute("totalSessions", sessions.size());

        logger.debug("Total study minutes: {}", totalMinutes);

        return "index";
    }

    @PostMapping("/sessions")
    public String addSession(@RequestParam("courseName") String courseName,
                             @RequestParam("minutes") int minutes) {

        if (!courseName.isBlank() && minutes > 0) {
            sessions.add(new StudySession(courseName.trim(), minutes));

            logger.info("Study session added. Course: {}, Minutes: {}",
                    courseName.trim(), minutes);
        } else {
            logger.warn("Invalid study session. Course: {}, Minutes: {}",
                    courseName, minutes);
        }

        return "redirect:/";
    }

    @PostMapping("/sessions/delete")
    public String deleteSession(@RequestParam("index") int index) {

        if (index >= 0 && index < sessions.size()) {
            StudySession removedSession = sessions.remove(index);

            logger.info("Study session deleted. Course: {}, Minutes: {}",
                    removedSession.getCourseName(), removedSession.getMinutes());
        } else {
            logger.error("Cannot delete session. Invalid index: {}, Total sessions: {}",
                    index, sessions.size());
        }

        return "redirect:/";
    }
}
