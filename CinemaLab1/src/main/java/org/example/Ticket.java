package org.example;

import java.util.Objects;

public class Ticket {
    private int id;
    private double price;
    private boolean isSold;

    public Ticket(int id, double price) {
        if (id <= 0) throw new IllegalArgumentException("ID квитка має бути додатним");
        if (price <= 0) throw new IllegalArgumentException("Ціна має бути більшою за 0");
        this.id = id;
        this.price = price;
        this.isSold = false;
    }

    public int getId() { return id; }
    public double getPrice() { return price; }

    public void setPrice(double price) {
        if (price <= 0) throw new IllegalArgumentException("Ціна має бути більшою за 0");
        this.price = price;
    }

    public boolean isSold() { return isSold; }
    public void setSold(boolean sold) { isSold = sold; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ticket ticket = (Ticket) o;
        return id == ticket.id;
    }

    @Override
    public int hashCode() { return Objects.hash(id); }

    @Override
    public String toString() {
        return "Квиток [ID=" + id + ", Ціна=" + price + " грн, Проданий=" + (isSold ? "Так" : "Ні") + "]";
    }
}