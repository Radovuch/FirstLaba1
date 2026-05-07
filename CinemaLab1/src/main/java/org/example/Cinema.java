package org.example;

import java.util.ArrayList;
import java.util.List;

public class Cinema {
    private String name;
    private List<Session> sessions;

    public Cinema(String name) {
        this.name = name;
        this.sessions = new ArrayList<>();
    }

    public void addSession(Session session) {
        if (session == null) throw new IllegalArgumentException("Сеанс не може бути null");
        if (sessions.stream().anyMatch(s -> s.getId() == session.getId())) {
            throw new IllegalArgumentException("Сеанс з таким ID вже існує");
        }
        sessions.add(session);
    }

    public List<Session> getSessions() { return sessions; }
    public Session getSessionById(int sessionId) {
        return sessions.stream().filter(s -> s.getId() == sessionId).findFirst().orElse(null);
    }

    public void updateSessionTitle(int sessionId, String newTitle) {
        Session s = getSessionById(sessionId);
        if (s == null) throw new IllegalArgumentException("Сеанс не знайдено");
        s.setMovieTitle(newTitle);
    }

    public void removeSession(int sessionId) {
        sessions.removeIf(s -> s.getId() == sessionId);
    }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}