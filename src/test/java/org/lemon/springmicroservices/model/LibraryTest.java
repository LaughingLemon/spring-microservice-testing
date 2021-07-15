package org.lemon.springmicroservices.model;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class LibraryTest {

    @Test
    public void test() {
        List<Book> library = new ArrayList<>();
        library.add(Book.builder().title("Book1")
                .extendedData(Collections.EMPTY_MAP)
                .build());
        library.add(Book.builder().title("Book2")
                .extendedData(Collections.singletonMap("warning", "this is a warning"))
                .build());
        library.add(Book.builder().title("Book3")
                .extendedData(Collections.EMPTY_MAP)
                .build());
        library.add(Book.builder().title("Book4")
                .extendedData(Collections.singletonMap("warning", "this is also a warning"))
                .build());
        library.add(Book.builder().title("Book5").build());

        library.stream()
                .filter(book -> Objects.nonNull(book.getExtendedData()))
                .map(book -> book.getExtendedData())
                .filter(ext -> ext.containsKey("warning"))
                .findFirst()
                .ifPresent(ext -> {
            log.info(ext.get("warning"));
        });

    }

}