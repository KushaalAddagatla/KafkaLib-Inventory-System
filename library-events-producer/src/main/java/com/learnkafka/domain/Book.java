package com.learnkafka.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record Book(
        @NotNull
        Integer bookId,
        @NotBlank
        String bookName,
        @NotBlank
        String bookAuthor) {
}


//package com.learnkafka.domain;
//
//public class Book {
//    private Integer bookId;
//    private String bookName;
//    private String bookAuthor;
//
//    // Constructors
//    public Book() {
//    }
//
//    public Book(Integer bookId, String bookName, String bookAuthor) {
//        this.bookId = bookId;
//        this.bookName = bookName;
//        this.bookAuthor = bookAuthor;
//    }
//
//    // Getters and Setters
//    public Integer getBookId() {
//        return bookId;
//    }
//
//    public void setBookId(Integer bookId) {
//        this.bookId = bookId;
//    }
//
//    public String getBookName() {
//        return bookName;
//    }
//
//    public void setBookName(String bookName) {
//        this.bookName = bookName;
//    }
//
//    public String getBookAuthor() {
//        return bookAuthor;
//    }
//
//    public void setBookAuthor(String bookAuthor) {
//        this.bookAuthor = bookAuthor;
//    }
//}
