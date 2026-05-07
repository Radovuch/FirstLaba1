package org.example;

import java.util.ArrayList;
import java.util.List;

public class Session {
    private int id;
    private String movieTitle;
    private List<Ticket> tickets = new ArrayList<>();

    public Session(int id, String movieTitle) {
        if (id <= 0) throw new IllegalArgumentException("ID сеансу має бути додатним");
        this.id = id;
        this.movieTitle = movieTitle;
    }

    public void addTicket(Ticket ticket) {
        if (ticket == null) throw new IllegalArgumentException("Квиток не може бути null");
        if (tickets.stream().anyMatch(t -> t.getId() == ticket.getId())) {
            throw new IllegalArgumentException("Квиток з ID " + ticket.getId() + " вже існує");
        }
        tickets.add(ticket);
    }
    public List<Ticket> getTickets() { return tickets; }

    public void updateTicketPrice(int ticketId, double newPrice) {
        Ticket t = tickets.stream().filter(x -> x.getId() == ticketId).findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Квиток не знайдено"));
        if (t.isSold()) throw new IllegalStateException("Неможливо змінити ціну проданого квитка");
        t.setPrice(newPrice);
    }

    public void removeTicket(int ticketId) {
        boolean removed = tickets.removeIf(t -> t.getId() == ticketId && !t.isSold());
        if (!removed) throw new IllegalArgumentException("Квиток не знайдено або він вже проданий");
    }

    public double buyTickets(List<Integer> ticketIds) {
        double sum = 0;
        for (int tid : ticketIds) {
            Ticket t = tickets.stream().filter(x -> x.getId() == tid).findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Квиток не знайдено: " + tid));
            if (t.isSold()) throw new IllegalStateException("Квиток вже продано: " + tid);
            t.setSold(true);
            sum += t.getPrice();
        }
        return sum;
    }
    public long getAvailableCount() { return tickets.stream().filter(t -> !t.isSold()).count(); }
    public long getSoldTicketsCount() { return tickets.stream().filter(Ticket::isSold).count(); }

    public int getId() { return id; }
    public String getMovieTitle() { return movieTitle; }
    public void setMovieTitle(String title) { this.movieTitle = title; } // Для Update сеансу

    @Override
    public String toString() {
        return "Сеанс [ID=" + id + ", Фільм='" + movieTitle + "']";
    }
}