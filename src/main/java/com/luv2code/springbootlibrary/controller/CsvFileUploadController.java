package com.luv2code.springbootlibrary.controller;

import com.luv2code.springbootlibrary.dao.BookRepository;
import com.luv2code.springbootlibrary.entity.Book;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

@CrossOrigin(origins = "https://localhost:3000")
@RestController
@RequestMapping("/api/books")
public class CsvFileUploadController {

    private final BookRepository bookRepository;

    @Autowired
    public CsvFileUploadController(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @PostMapping("/secure/upload")
    public ResponseEntity<String> uploadCsvFile(@RequestParam("file") MultipartFile file) {
        // Check if the uploaded file is empty
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("Please select a CSV file to upload.");
        }

        // Check if the uploaded file has a .csv extension
        if (!StringUtils.getFilenameExtension(file.getOriginalFilename()).equals("csv")) {
            return ResponseEntity.badRequest().body("Invalid file format. Please upload a CSV file.");
        }

        try (CSVParser csvParser = CSVFormat.DEFAULT.parse(new InputStreamReader(file.getInputStream()))) {
            List<CSVRecord> records = csvParser.getRecords();

            // Process CSV data and save it to the book table
            for (CSVRecord record : records) {
                String title = record.get(0);
                String author = record.get(1);
                String description = record.get(2);
                int copies = Integer.parseInt(record.get(3));
                int copiesAvailable = Integer.parseInt(record.get(4));
                String category = record.get(5);
                String img = record.get(6);


                // Create a Book entity and save it to the database
                Book book = new Book();
                book.setTitle(title);
                book.setAuthor(author);
                book.setDescription(description);
                book.setCopies(copies);
                book.setCopiesAvailable(copiesAvailable);
                book.setCategory(category);
                book.setImg(img);


                bookRepository.save(book);
            }

            return ResponseEntity.ok("File uploaded, and data saved to the book table.");
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("An error occurred while processing the file.");
        }
    }
}
