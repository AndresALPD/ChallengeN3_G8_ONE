package com.andres.literalura.menu;

import com.andres.literalura.client.GutendexClient;
import com.andres.literalura.domain.Author;
import com.andres.literalura.domain.Book;
import com.andres.literalura.dto.AuthorDTO;
import com.andres.literalura.dto.BookDTO;
import com.andres.literalura.dto.GutendexResponseDTO;
import com.andres.literalura.service.AuthorService;
import com.andres.literalura.service.BookService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

@Component
public class ConsoleMenu implements CommandLineRunner {

    private final GutendexClient gutendexClient;
    private final BookService bookService;
    private final AuthorService authorService;

    public ConsoleMenu(GutendexClient gutendexClient, BookService bookService, AuthorService authorService) {
        this.gutendexClient = gutendexClient;
        this.bookService = bookService;
        this.authorService = authorService;
    }

    @Override
    public void run(String... args) {
        Scanner scanner = new Scanner(System.in);
        int opcion;

        do {
            System.out.println("\n=== Literalura - Menú ===");
            System.out.println("1) Buscar libro por título (API) y guardar");
            System.out.println("2) Listar libros guardados");
            System.out.println("3) Listar autores guardados");
            System.out.println("4) Listar autores vivos en un año");
            System.out.println("5) Listar libros por idioma");
            System.out.println("6) Top 10 por descargas");
            System.out.println("0) Salir");
            System.out.print("Elige una opción: ");

            while (!scanner.hasNextInt()) {
                System.out.print("Ingresa un número válido: ");
                scanner.next();
            }
            opcion = scanner.nextInt();
            scanner.nextLine(); // limpiar buffer

            switch (opcion) {
                case 1 -> buscarGuardar(scanner);
                case 2 -> listarLibros();
                case 3 -> listarAutores();
                case 4 -> autoresVivosEnAnio(scanner);
                case 5 -> librosPorIdioma(scanner);
                case 6 -> top10();
                case 0 -> System.out.println("¡Hasta luego!");
                default -> System.out.println("Opción inválida.");
            }
        } while (opcion != 0);
    }

    private void buscarGuardar(Scanner scanner) {
        System.out.print("Ingresa el título a buscar: ");
        String titulo = scanner.nextLine().trim();
        if (titulo.isEmpty()) {
            System.out.println("El título no puede estar vacío.");
            return;
        }

        try {
            GutendexResponseDTO response = gutendexClient.searchBooksByTitle(titulo);
            if (response == null || response.getResults() == null || response.getResults().isEmpty()) {
                System.out.println("No se encontraron resultados para: " + titulo);
                return;
            }

            List<BookDTO> results = response.getResults();
            System.out.println("\nResultados encontrados (" + results.size() + "):");
            for (int i = 0; i < results.size(); i++) {
                BookDTO b = results.get(i);
                String autores = (b.getAuthors() == null || b.getAuthors().isEmpty())
                        ? "(sin autores)"
                        : b.getAuthors().stream().map(AuthorDTO::getName).collect(Collectors.joining(", "));
                String idiomas = (b.getLanguages() == null || b.getLanguages().isEmpty())
                        ? "(sin idiomas)"
                        : String.join(", ", b.getLanguages());
                Integer descargas = b.getDownload_count() == null ? 0 : b.getDownload_count();

                System.out.printf("%d) [%d] %s | Autores: %s | Idiomas: %s | Descargas: %d%n",
                        i + 1, b.getId(), b.getTitle(), autores, idiomas, descargas);
            }

            System.out.print("\nElige el número a guardar (0 para cancelar): ");
            int idx = readIndex(scanner);
            if (idx == 0) {
                System.out.println("Operación cancelada.");
                return;
            }
            if (idx < 1 || idx > results.size()) {
                System.out.println("Índice inválido.");
                return;
            }

            BookDTO elegido = results.get(idx - 1);
            Book persisted = bookService.saveFromDto(elegido);
            System.out.printf("Guardado: [%d] %s (id BD=%d)%n",
                    persisted.getGutendexId(), persisted.getTitle(), persisted.getId());

        } catch (Exception e) {
            System.out.println("Ocurrió un error consultando/guardando: " + e.getMessage());
        }
    }

    private void listarLibros() {
        List<Book> books = bookService.findAllDetailed();
        if (books.isEmpty()) {
            System.out.println("No hay libros guardados.");
            return;
        }
        System.out.println("\nLibros guardados:");
        books.forEach(b -> {
            String idiomas = b.getLanguages() == null || b.getLanguages().isEmpty()
                    ? "(sin idiomas)" : String.join(", ", b.getLanguages());
            String autores = b.getAuthors().isEmpty()
                    ? "(sin autores)" : b.getAuthors().stream().map(Author::getName).collect(Collectors.joining(", "));
            System.out.printf("- [%d] %s | Idiomas: %s | Descargas: %d | Autores: %s%n",
                    b.getGutendexId(), b.getTitle(), idiomas,
                    b.getDownloadCount() == null ? 0 : b.getDownloadCount(), autores);
        });
    }

    private void listarAutores() {
        List<Author> authors = authorService.findAll();
        if (authors.isEmpty()) {
            System.out.println("No hay autores guardados.");
            return;
        }
        System.out.println("\nAutores guardados:");
        authors.forEach(a -> System.out.printf("- %s (nac: %s, fallec: %s)%n",
                a.getName(),
                a.getBirthYear() == null ? "?" : a.getBirthYear(),
                a.getDeathYear() == null ? "?" : a.getDeathYear()));
    }

    private void autoresVivosEnAnio(Scanner scanner) {
        System.out.print("Ingresa el año (ej. 1950): ");
        while (!scanner.hasNextInt()) {
            System.out.print("Ingresa un año válido: ");
            scanner.next();
        }
        int year = scanner.nextInt();
        scanner.nextLine();

        List<Author> vivos = authorService.findAliveInYear(year);
        if (vivos.isEmpty()) {
            System.out.println("No hay autores vivos en " + year + " registrados en BD.");
            return;
        }
        System.out.println("\nAutores vivos en " + year + ":");
        vivos.forEach(a -> System.out.printf("- %s (nac: %s, fallec: %s)%n",
                a.getName(),
                a.getBirthYear() == null ? "?" : a.getBirthYear(),
                a.getDeathYear() == null ? "?" : a.getDeathYear()));
    }

    private void librosPorIdioma(Scanner scanner) {
        System.out.print("Ingresa el código de idioma (ej. en, es, fr, pt): ");
        String lang = scanner.nextLine().trim();
        if (lang.isEmpty()) {
            System.out.println("Idioma no puede estar vacío.");
            return;
        }
        List<Book> books = bookService.findByLanguageDetailed(lang);
        if (books.isEmpty()) {
            System.out.println("No hay libros guardados para idioma: " + lang);
            return;
        }
        System.out.println("\nLibros en idioma " + lang + ":");
        books.forEach(b -> System.out.printf("- [%d] %s%n", b.getGutendexId(), b.getTitle()));
    }

    private void top10() {
        List<Book> top = bookService.top10ByDownloadsDetailed();
        if (top.isEmpty()) {
            System.out.println("No hay libros guardados.");
            return;
        }
        System.out.println("\nTop 10 por descargas:");
        int i = 1;
        for (Book b : top) {
            System.out.printf("%d) %s (descargas: %d)%n", i++, b.getTitle(),
                    b.getDownloadCount() == null ? 0 : b.getDownloadCount());
        }
    }

    private int readIndex(Scanner scanner) {
        while (!scanner.hasNextInt()) {
            System.out.print("Ingresa un número válido: ");
            scanner.next();
        }
        int idx = scanner.nextInt();
        scanner.nextLine(); // limpiar buffer
        return idx;
    }
}
