package com.luv2code.springbootlibrary.controller;

import com.luv2code.springbootlibrary.entity.Book;
import com.luv2code.springbootlibrary.requestmodels.AddBookRequest;
import com.luv2code.springbootlibrary.requestmodels.UpdateBookRequest;
import com.luv2code.springbootlibrary.service.AdminService;
import com.luv2code.springbootlibrary.service.BookService;
import com.luv2code.springbootlibrary.utils.ExtractJWT;
import org.jose4j.jwt.JwtClaims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@CrossOrigin("https://localhost:3000")
@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private AdminService adminService;
    @Autowired
    private BookService bookService;
    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping("/secure/add/book")
    public void postBook(@RequestHeader(value="Authorization") String token,
                         @RequestBody AddBookRequest addBookRequest) throws Exception {

        String admin = ExtractJWT.payloadJWTExtraction(token, "\"userType\"");
        if (admin == null || !admin.equals("admin")) {
            throw new Exception("Administration page only");
        }
        adminService.postBook(addBookRequest);
    }




  /*  @DeleteMapping("/secure/delete/book")
    public ResponseEntity<Map<String, Object>> deleteBook(@RequestHeader(value = "Authorization") String token,
                                                          @RequestParam Long bookId) {
        Map<String, Object> response = new HashMap<>();

        try {
            String admin = ExtractJWT.payloadJWTExtraction(token, "\"userType\"");
            if (admin == null || !admin.equals("admin")) {
                response.put("status", HttpStatus.FORBIDDEN.value());
                response.put("message", "Administration page only");
                return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
            }

            adminService.deleteBook(bookId);

            response.put("status", HttpStatus.OK.value());
            response.put("message", "Book deleted successfully.");
            response.put("data", Collections.singletonMap("deletedBookId", bookId)); // Include deleted book ID
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.put("message", "An error occurred: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }*/


    @DeleteMapping("/secure/delete/book")
    public void deleteBook(@RequestHeader(value="Authorization") String token,
                           @RequestParam Long bookId) throws Exception {
        String admin = ExtractJWT.payloadJWTExtraction(token, "\"userType\"");
        if (admin == null || !admin.equals("admin")) {
            throw new Exception("Administration page only");
        }
        adminService.deleteBook(bookId);
    }


   /* @DeleteMapping("/secure/delete/book")
    public void deleteBook(@RequestHeader(value = "Authorization") String token,
                           @RequestParam Long bookId) throws Exception {
        // Validate the JWT token
        JwtClaims validatedToken = ExtractJWT.validateToken(token, "https://dev-86703467.okta.com/oauth2/default", "https://dev-86703467.okta.com/oauth2/default/v1/keys");

        if (validatedToken == null) {
            throw new Exception("Invalid token");
        }

        String admin = ExtractJWT.payloadJWTExtraction(token, "\"userType\"");
        if (admin == null || !admin.equals("admin")) {
            throw new Exception("Administration page only");
        }
        adminService.deleteBook(bookId);
    }*/

    @PutMapping("/secure/update/book/{bookId}")
    public ResponseEntity<Map<String, Object>> updateBook(
            @RequestHeader(value = "Authorization") String token,
            @PathVariable Long bookId,
            @RequestBody UpdateBookRequest updatebookRequest
    ) throws Exception {
        String admin = ExtractJWT.payloadJWTExtraction(token, "\"userType\"");
        if (admin == null || !admin.equals("admin")) {
            throw new Exception("Administration page only");
        }

        // Call the service to update the book details
        Book updatedBook = adminService.updateBook(bookId, updatebookRequest);

        // Extract the copies value from the request
        int updatedCopies = updatebookRequest.getCopies();

        // Update the book copies if needed
        if (updatedCopies > 0) {
            updatedBook.setCopies(updatedCopies); // Set the updated copies value

            // Update copiesAvailable as well
            int updatedCopiesAvailable = Math.max(0, updatedCopies);
            updatedBook.setCopiesAvailable(updatedCopiesAvailable);
        }

        // Save the updated book entity
        updatedBook = adminService.saveBook(updatedBook);

        // Create a response map containing the updated book details
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Book updated successfully.");
        response.put("status", 200);
        response.put("data", updatedBook); // Include the updated book details

        return ResponseEntity.ok(response);
    }

    @PutMapping("/secure/update/book/quantity")
    public ResponseEntity<Map<String, Object>> updateBookQuantity(
            @RequestHeader(value = "Authorization") String token,
            @RequestParam Long bookId,
            @RequestParam int quantityChange) throws Exception {

        Map<String, Object> response = new HashMap<>();

        try {
            String admin = ExtractJWT.payloadJWTExtraction(token, "\"userType\"");

            if (admin == null || !admin.equals("admin")) {
                response.put("status", HttpStatus.FORBIDDEN.value());
                response.put("message", "Administration page only");
                return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
            }

            if (quantityChange > 0) {
                adminService.increaseBookQuantity(bookId);
            } else if (quantityChange < 0) {
                adminService.decreaseBookQuantity(bookId);
            } else {
                // No change requested
                response.put("status", HttpStatus.BAD_REQUEST.value());
                response.put("message", "Invalid quantity change requested.");
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }

            int currentQuantity = bookService.getCurrentQuantity(bookId); // You need to implement this method in your service

            response.put("status", HttpStatus.OK.value());
            response.put("message", "Book quantity updated successfully.");
            response.put("data", Collections.singletonMap("currentQuantity", currentQuantity));

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.put("message", "An error occurred: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}










