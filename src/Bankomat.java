import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Bankomat {
    private Map<String, Integer> kasetka; // Przechowuje informacje o ilości banknotów w kasie bankomatu

    public Bankomat() {
        kasetka = new HashMap<>();
        // Inicjalizacja kasetki bankomatu
        kasetka.put("20 zł", 10);
        kasetka.put("50 zł", 10);
        kasetka.put("100 zł", 10);
        kasetka.put("200 zł", 10);
    }

    public void uruchom() {
        Scanner scanner = new Scanner(System.in);
        boolean koniec = false;

        while (!koniec) {
            wyswietlMenu();
            int wybor = scanner.nextInt();

            switch (wybor) {
                case 1:
                    trybUzytkownika(scanner);
                    break;
                case 2:
                    trybSerwisowy(scanner);
                    break;
                case 3:
                    koniec = true;
                    break;
                default:
                    System.out.println("Nieprawidłowy wybór. Spróbuj ponownie.");
                    break;
            }
        }
    }

    private void wyswietlMenu() {
        System.out.println("Menu:");
        System.out.println("1. Tryb użytkownika");
        System.out.println("2. Tryb serwisowy");
        System.out.println("3. Wyjście");
        System.out.print("Wybierz opcję: ");
    }

    private void trybUzytkownika(Scanner scanner) {
        System.out.print("Podaj kwotę do wypłaty: ");
        int kwota = scanner.nextInt();

        Map<String, Integer> wypłata = wypłaćPieniądze(kwota);
        if (wypłata.isEmpty()) {
            System.out.println("Brak odpowiedniej ilości banknotów. Spróbuj inną kwotę.");
        } else {
            System.out.println("Oto wypłacone banknoty:");
            for (Map.Entry<String, Integer> entry : wypłata.entrySet()) {
                System.out.println(entry.getKey() + " x" + entry.getValue());
            }
        }
    }

    private Map<String, Integer> wypłaćPieniądze(int kwota) {
        Map<String, Integer> wypłata = new HashMap<>();
        int pozostałaKwota = kwota;

        // Sprawdź, czy bankomat ma wystarczającą ilość pieniędzy
        int dostępnaKwota = obliczDostępnąKwotę();
        if (kwota > dostępnaKwota) {
            return wypłata; // Pusty zestaw w przypadku braku wystarczających środków
        }

        // Rozpocznij wypłatę
        for (Map.Entry<String, Integer> entry : kasetka.entrySet()) {
            String nominal = entry.getKey().replaceAll("\\D+", ""); // Pobierz wartość nominalną banknotu
            int ilość = entry.getValue(); // Pobierz ilość dostępnych banknotów danego nominalu

            if (ilość > 0 && Integer.parseInt(nominal) <= pozostałaKwota) {
                int liczbaBanknotów = Math.min(ilość, pozostałaKwota / Integer.parseInt(nominal)); // Oblicz liczbę banknotów, które można wypłacić
                wypłata.put(entry.getKey(), liczbaBanknotów); // Dodaj do wypłaty dane o banknocie i liczbie banknotów
                pozostałaKwota -= liczbaBanknotów * Integer.parseInt(nominal); // Odejmij wypłaconą wartość od pozostałej kwoty do wypłaty
            }

            if (pozostałaKwota == 0) {
                // Wypłata zakończona sukcesem
                odejmijPieniądzeZKasetki(wypłata); // Odejmij wypłacone banknoty z kasetki bankomatu
                break;
            }
        }

        return wypłata;
    }

    private int obliczDostępnąKwotę() {
        int dostępnaKwota = 0;
        for (Map.Entry<String, Integer> entry : kasetka.entrySet()) {
            dostępnaKwota += Integer.parseInt(entry.getKey().replaceAll("\\D+", "")) * entry.getValue(); // Oblicz dostępną kwotę na podstawie ilości banknotów w kasetce
        }
        return dostępnaKwota;
    }

    private void odejmijPieniądzeZKasetki(Map<String, Integer> wypłata) {
        for (Map.Entry<String, Integer> entry : wypłata.entrySet()) {
            String nominal = entry.getKey(); // Pobierz nominal banknotu
            int ilość = entry.getValue(); // Pobierz liczbę wypłaconych banknotów

            kasetka.put(nominal, kasetka.get(nominal) - ilość); // Odejmij wypłacone banknoty z kasetki bankomatu
        }
    }

    private void trybSerwisowy(Scanner scanner) {
        System.out.println("Tryb serwisowy:");
        System.out.println("1. Wypełnij kasetkę");
        System.out.println("2. Sprawdź stan pieniężny bankomatu");
        System.out.println("3. Powrót");

        int wybor = scanner.nextInt();

        switch (wybor) {
            case 1:
                wypelnijKasetke(scanner);
                break;
            case 2:
                sprawdzStanPienieznegoBankomatu();
                break;
            case 3:
                break;
            default:
                System.out.println("Nieprawidłowy wybór. Spróbuj ponownie.");
                break;
        }
    }

    private void wypelnijKasetke(Scanner scanner) {
        System.out.println("Podaj nominał banknotu:");
        String nominal = scanner.next();
        System.out.println("Podaj ilość banknotów:");
        int ilość = scanner.nextInt();

        if (kasetka.containsKey(nominal)) {
            kasetka.put(nominal, kasetka.get(nominal) + ilość); // Dodaj banknoty do kasetki bankomatu
        } else {
            kasetka.put(nominal, ilość); // Dodaj nowy nominał banknotu do kasetki bankomatu
        }

        System.out.println("Kasetka została uzupełniona.");
    }

    private void sprawdzStanPienieznegoBankomatu() {
        System.out.println("Stan pieniężny bankomatu:");
        for (Map.Entry<String, Integer> entry : kasetka.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue()); // Wyświetl informacje o ilości banknotów w kasetce bankomatu
        }
    }

    public static void main(String[] args) {
        Bankomat bankomat = new Bankomat();
        bankomat.uruchom();
    }
}