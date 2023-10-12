package com.luv2code.springbootlibrary.DTO;

import lombok.Data;

import java.util.List;

@Data
public class BookResponse {
    private String message;
    private int status;
    private BookData data;
    private  List<BookData> books;

    @Data
    public static class BookData {
        private Long id;
        private String title;
        private String author;
        private String description;
        private int copies;
        private int copiesAvailable;
        private String category;
    }
        // Create a setter method for the books field
        public void setBooks(List<BookData> books) {
            this.books = books;
        }
        /*private Links _links;



        public static class Links {
            private Link self;
            private Link book;

            // Getter, Setter, and Constructors ...

            public static class Link {
                private String href;

                // Getter, Setter, and Constructors ...
            }
        }*/

}

