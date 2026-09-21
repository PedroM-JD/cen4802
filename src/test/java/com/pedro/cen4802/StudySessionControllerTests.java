package com.pedro.cen4802;

import org.junit.jupiter.api.Test;
import org.springframework.ui.ExtendedModelMap;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class StudySessionControllerTests {

    @Test
    void addsValidStudySession() {
        StudySessionController controller = new StudySessionController();

        controller.addSession("Java", 60);

        ExtendedModelMap model = new ExtendedModelMap();
        controller.showHomePage(model);

        List<StudySession> sessions =
                (List<StudySession>) model.getAttribute("sessions");

        assertEquals(1, sessions.size());
        assertEquals("Java", sessions.get(0).getCourseName());
        assertEquals(60, sessions.get(0).getMinutes());
    }

    @Test
    void rejectsBlankCourseName() {
        StudySessionController controller = new StudySessionController();

        controller.addSession(" ", 30);

        ExtendedModelMap model = new ExtendedModelMap();
        controller.showHomePage(model);

        List<StudySession> sessions =
                (List<StudySession>) model.getAttribute("sessions");

        assertTrue(sessions.isEmpty());
    }

    @Test
    void rejectsZeroMinutes() {
        StudySessionController controller = new StudySessionController();

        controller.addSession("Python", 0);

        ExtendedModelMap model = new ExtendedModelMap();
        controller.showHomePage(model);

        List<StudySession> sessions =
                (List<StudySession>) model.getAttribute("sessions");

        assertTrue(sessions.isEmpty());
    }

    @Test
    void calculatesTotalMinutes() {
        StudySessionController controller = new StudySessionController();

        controller.addSession("Java", 30);
        controller.addSession("Python", 45);

        ExtendedModelMap model = new ExtendedModelMap();
        controller.showHomePage(model);

        assertEquals(75, model.getAttribute("totalMinutes"));
    }
}