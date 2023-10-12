package com.luv2code.springbootlibrary.requestmodels;

import lombok.Data;

@Data
public class UpdateBookRequest {

    private String title;

    private String author;

    private String description;

    private String category;

    private int copies;

    private int copiesAvailable;


}
