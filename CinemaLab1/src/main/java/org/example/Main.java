package org.example;

import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        Cinema cinema = new Cinema("Планета Кіно (Київ)");
        Session s1 = new Session(1, "Дюна: Частина друга");
        for (int i = 1; i <= 10; i++) s1.addTicket(new Ticket(100 + i, 200.0));
        Session s2 = new Session(2, "Дедпул і Росомаха");
        for (int i = 1; i <= 5; i++) s2.addTicket(new Ticket(200 + i, 150.0));
        Session s3 = new Session(3, "Думками навиворіт 2");
        for (int i = 1; i <= 8; i++) s3.addTicket(new Ticket(300 + i, 180.0));

        cinema.addSession(s1);
        cinema.addSession(s2);
        cinema.addSession(s3);

        Scanner scanner = new Scanner(System.in);
        boolean isRunning = true;

        System.out.println("=========================================");
        System.out.println("  Вітаємо у кінотеатрі: " + cinema.getName());
        System.out.println("=========================================");

        while (isRunning) {
            System.out.println("\n--- ГОЛОВНЕ МЕНЮ ---");
            System.out.println("1. Вивести всі сеанси та їх статистику");
            System.out.println("2. Купити квитки (за кількістю)");
            System.out.println("--- CRUD Квитків та Сеансів ---");
            System.out.println("3. Додати новий сеанс (Create)");
            System.out.println("4. Видалити сеанс (Delete)");
            System.out.println("5. Додати новий квиток на сеанс (Create)");
            System.out.println("0. Вихід");
            System.out.print("Оберіть дію: ");

            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("❌ Помилка: введіть число!");
                continue;
            }

            try {
                switch (choice) {
                    case 1:
                        System.out.println("\n[АФІША ТА СТАТИСТИКА]");
                        for (Session ses : cinema.getSessions()) {
                            System.out.println("ID " + ses.getId() + " | Фільм: " + ses.getMovieTitle());
                            System.out.println("   Вільних місць: " + ses.getAvailableCount() + " | Продано: " + ses.getSoldTicketsCount());
                        }
                        break;

                    case 2:
                        System.out.println("\n[КУПІВЛЯ КВИТКІВ]");
                        System.out.print("Введіть ID сеансу (фільму): ");
                        int sessionId = Integer.parseInt(scanner.nextLine());
                        Session selectedSession = cinema.getSessionById(sessionId);

                        if (selectedSession == null) {
                            System.out.println("❌ Сеанс не знайдено!");
                            break;
                        }

                        System.out.print("Скільки квитків бажаєте придбати? ");
                        int quantity = Integer.parseInt(scanner.nextLine());

                        if (quantity <= 0) {
                            System.out.println("❌ Кількість має бути більшою за нуль!");
                            break;
                        }
                        List<Integer> idsToBuy = selectedSession.getTickets().stream()
                                .filter(t -> !t.isSold())
                                .limit(quantity)
                                .map(Ticket::getId)
                                .collect(Collectors.toList());

                        if (idsToBuy.size() < quantity) {
                            System.out.println("❌ Вибачте, на цей сеанс залишилося лише " + selectedSession.getAvailableCount() + " вільних місць.");
                        } else {
                            double sum = selectedSession.buyTickets(idsToBuy);
                            System.out.println("✅ Успішно! Ви придбали " + quantity + " квитків. До сплати: " + sum + " грн.");
                        }
                        break;

                    case 3:
                        System.out.print("Введіть ID нового сеансу: ");
                        int sId = Integer.parseInt(scanner.nextLine());
                        System.out.print("Введіть назву фільму: ");
                        String title = scanner.nextLine();
                        cinema.addSession(new Session(sId, title));
                        System.out.println("✅ Сеанс створено!");
                        break;

                    case 4:
                        System.out.print("Введіть ID сеансу для видалення: ");
                        int sdId = Integer.parseInt(scanner.nextLine());
                        cinema.removeSession(sdId);
                        System.out.println("✅ Сеанс видалено!");
                        break;

                    case 5:
                        System.out.print("Введіть ID сеансу, на який додаємо квиток: ");
                        int targetSessionId = Integer.parseInt(scanner.nextLine());
                        Session targetSession = cinema.getSessionById(targetSessionId);

                        if (targetSession != null) {
                            System.out.print("Введіть унікальний ID нового квитка: ");
                            int tId = Integer.parseInt(scanner.nextLine());
                            System.out.print("Введіть ціну: ");
                            double price = Double.parseDouble(scanner.nextLine());
                            targetSession.addTicket(new Ticket(tId, price));
                            System.out.println("✅ Квиток додано до сеансу!");
                        } else {
                            System.out.println("❌ Сеанс не знайдено.");
                        }
                        break;

                    case 0:
                        isRunning = false;
                        System.out.println("Вихід з програми. Гарного дня!");
                        break;

                    default:
                        System.out.println("❌ Невідома команда.");
                }
            } catch (NumberFormatException e) {
                System.out.println("❌ Помилка вводу: очікується число.");
            } catch (Exception e) {
                System.out.println("❌ Помилка операції: " + e.getMessage());
            }
        }
        scanner.close();
    }
}