package org.example;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.util.Arrays;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
class CinemaTest {
    private Session session;
    private Cinema cinema;
    @BeforeEach
    void setUp() {
        cinema = new Cinema("Планета Кіно");
        session = new Session(1, "Дюна: Частина друга");
        session.addTicket(new Ticket(101, 150.0));
        session.addTicket(new Ticket(102, 200.0));
        cinema.addSession(session);
    }
    @Test
    @DisplayName("1. Успішна купівля: правильний розрахунок суми та зміна статусу")
    void testBuyTicketsSuccess() {
        double sum = session.buyTickets(Arrays.asList(101));
        assertEquals(150.0, sum, "Сума до сплати має бути 150.0");
        assertTrue(session.getTickets().get(0).isSold(), "Статус квитка має змінитися на 'проданий'");
    }
    @Test
    @DisplayName("2. Статистика: правильний підрахунок вільних та проданих місць")
    void testSessionStatistics() {
        session.buyTickets(Arrays.asList(102));
        assertEquals(1, session.getAvailableCount(), "Має залишитись 1 вільний квиток");
        assertEquals(1, session.getSoldTicketsCount(), "Має бути 1 проданий квиток");
    }
    @Test
    @DisplayName("3. Помилка бізнес-логіки: спроба купити вже проданий квиток")
    void testBuyAlreadySoldTicket() {
        session.buyTickets(Arrays.asList(101)); // Купуємо вперше
        Exception exception = assertThrows(IllegalStateException.class, () -> {
            session.buyTickets(Arrays.asList(101)); // Спроба купити вдруге
        });
        assertTrue(exception.getMessage().contains("вже продано"));
    }

    @Test
    @DisplayName("4. Валідація даних: неможливо створити квиток з від'ємною ціною")
    void testTicketNegativePrice() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Ticket(103, -50.0);
        });
        assertTrue(exception.getMessage().contains("більшою за 0"));
    }

    @Test
    @DisplayName("5. Захист даних: заборона додавання квитка з існуючим ID (дублікат)")
    void testAddDuplicateTicket() {
        Ticket duplicate = new Ticket(101, 300.0); // ID 101 вже існує в setUp
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            session.addTicket(duplicate);
        });
        assertTrue(exception.getMessage().contains("вже існує"));
    }

    @Test
    @DisplayName("6. CRUD Update: успішна зміна ціни квитка")
    void testUpdateTicketPrice() {
        session.updateTicketPrice(101, 180.0);
        Ticket updatedTicket = session.getTickets().stream()
                .filter(x -> x.getId() == 101)
                .findFirst()
                .orElse(null);
        assertNotNull(updatedTicket);
        assertEquals(180.0, updatedTicket.getPrice(), "Ціна має оновитися до 180.0");
    }

    @Test
    @DisplayName("7. CRUD Update (Помилка): неможливо змінити ціну вже проданого квитка")
    void testUpdateSoldTicketPrice() {
        session.buyTickets(Arrays.asList(101)); // Робимо квиток проданим
        assertThrows(IllegalStateException.class, () -> {
            session.updateTicketPrice(101, 200.0);
        });
    }

    @Test
    @DisplayName("8. CRUD Delete: успішне видалення сеансу з кінотеатру")
    void testRemoveSessionFromCinema() {
        assertNotNull(cinema.getSessionById(1), "Сеанс має існувати до видалення");
        cinema.removeSession(1);
        assertNull(cinema.getSessionById(1), "Сеанс має бути відсутнім після видалення");
        assertEquals(0, cinema.getSessions().size());
    }

    @Test
    @DisplayName("9. Mockito: перевірка виклику методу експорту даних (Імітація)")
    void testDataExportWithMock() {
        DataService mockService = mock(DataService.class);
        when(mockService.exportData(anyList(), anyString())).thenReturn(true);
        boolean result = mockService.exportData(session.getTickets(), "export_data.json");
        assertTrue(result, "Експорт має завершитися успішно");
        verify(mockService, times(1)).exportData(anyList(), eq("export_data.json"));
    }
}