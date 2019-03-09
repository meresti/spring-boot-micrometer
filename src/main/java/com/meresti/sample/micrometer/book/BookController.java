package com.meresti.sample.micrometer.book;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("books")
public class BookController {

    private final BookRepository bookRepository;

    @Autowired
    public BookController(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @GetMapping
    public ResponseEntity<List<Book>> getAll() {
        return ResponseEntity.ok().body(bookRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> getOne(@PathVariable Long id) {
        final Optional<Book> book = bookRepository.findById(id);
        if (book.isPresent()) {
            return ResponseEntity.ok().body(book.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<?> saveOne(@Valid @RequestBody Book book) {
        final Book savedBook = bookRepository.save(book);

        final URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(savedBook.getId()).toUri();

        return ResponseEntity.created(location).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Book> updateOne(@PathVariable Long id, @Valid @RequestBody Book book) {
        final Optional<Book> bookToUpdate = bookRepository.findById(id);
        if (bookToUpdate.isPresent()) {
            book.setId(id);
            final Book updatedBook = bookRepository.save(book);
            return ResponseEntity.ok().body(updatedBook);
        }

        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOne(@PathVariable Long id) {
        bookRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

}
