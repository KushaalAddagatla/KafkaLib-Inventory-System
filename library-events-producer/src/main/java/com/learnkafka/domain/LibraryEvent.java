package com.learnkafka.domain;

import com.learnkafka.domain.Book;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record LibraryEvent(
        Integer libraryEventId,

        LibraryEventType libraryEventType,

        @NotNull
        @Valid
        Book book) {
    }


//package com.learnkafka.domain;
//
//import jakarta.validation.Valid;
//import jakarta.validation.constraints.NotNull;
//
//public class LibraryEvent {
//    private Integer libraryEventId;
//    private LibraryEventType libraryEventType;
//
//    @NotNull
//    @Valid
//    private Book book;
//
//    // Constructors
//    public LibraryEvent() {
//    }
//
//    public LibraryEvent(Integer libraryEventId, LibraryEventType libraryEventType, Book book) {
//        this.libraryEventId = libraryEventId;
//        this.libraryEventType = libraryEventType;
//        this.book = book;
//    }
//
//    // Getters and Setters
//    public Integer getLibraryEventId() {
//        return libraryEventId;
//    }
//
//    public void setLibraryEventId(Integer libraryEventId) {
//        this.libraryEventId = libraryEventId;
//    }
//
//    public LibraryEventType getLibraryEventType() {
//        return libraryEventType;
//    }
//
//    public void setLibraryEventType(LibraryEventType libraryEventType) {
//        this.libraryEventType = libraryEventType;
//    }
//
//    public Book getBook() {
//        return book;
//    }
//
//    public void setBook(Book book) {
//        this.book = book;
//    }
//}
