package org.example;
import java.util.List;

public interface DataService {
    boolean exportData(List<Ticket> tickets, String fileName);
}