package com.luv2code.springbootlibrary.controller;
import com.luv2code.springbootlibrary.DTO.BookResponse;
import com.luv2code.springbootlibrary.DTO.BookSummaryDTO;
import com.luv2code.springbootlibrary.dao.BookRepository;
import com.luv2code.springbootlibrary.entity.Checkout;
import com.luv2code.springbootlibrary.responsemodels.ShelfCurrentLoansResponse;
import com.luv2code.springbootlibrary.service.AdminService;
import org.jose4j.json.internal.json_simple.JSONObject;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;


import com.luv2code.springbootlibrary.entity.Book;
import com.luv2code.springbootlibrary.service.BookService;
import com.luv2code.springbootlibrary.utils.ExtractJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

import static com.luv2code.springbootlibrary.utils.ExtractJWT.validateToken;


@CrossOrigin("https://localhost:3000")
@RestController
@RequestMapping("/api/books")
public class BookController {

    private BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }


    @GetMapping("/secure/currentloans")
    public List<ShelfCurrentLoansResponse> currentLoans(@RequestHeader(value = "Authorization") String token)
            throws Exception {
        //String userEmail ="testuser@email.com";
        String userEmail = ExtractJWT.payloadJWTExtraction(token, "\"sub\"");
        return bookService.currentLoans(userEmail);

    }

    @GetMapping("/secure/currentloanacount")
    public ResponseEntity<Map<String, Object>> currentLoanacount(@RequestHeader(value = "Authorization") String token)
            throws Exception {
        String userEmail = ExtractJWT.payloadJWTExtraction(token, "\"sub\"");
        List<ShelfCurrentLoansResponse> currentLoans = bookService.currentLoans(userEmail);
        int loanCount = currentLoans.size();

        List<Map<String, Object>> simplifiedBooks = new ArrayList<>();
        for (ShelfCurrentLoansResponse loan : currentLoans) {
            Map<String, Object> simplifiedBook = new HashMap<>();
            simplifiedBook.put("id", loan.getBook().getId());
            simplifiedBook.put("title", loan.getBook().getTitle());
            simplifiedBook.put("author", loan.getBook().getAuthor());
            simplifiedBook.put("daysLeftToReturn", 7);
            simplifiedBook.put("userEmail", "testuser@email.com");
            simplifiedBook.put("checkoutDate", "31.08.2023");
            simplifiedBooks.add(simplifiedBook);
        }

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("books", simplifiedBooks);
        responseBody.put("count", loanCount);

        Map<String, Object> data = new HashMap<>();
        data.put("msg", "Current loans retrieved successfully");
        data.put("data", responseBody);
        data.put("status", 200);

        Map<String, Object> response = new HashMap<>();
        response.put("data", data);

        return ResponseEntity.status(200).body(response);
    }

    @GetMapping("/secure/currentloans/count")
    public int currentLoansCount(@RequestHeader(value = "Authorization") String token) {
        String userEmail = ExtractJWT.payloadJWTExtraction(token, "\"sub\"");
        // String userEmail ="testuser@email.com";
        return bookService.currentLoansCount(userEmail);
    }


    @GetMapping("/secure/ischeckedout/byuser")
    public Boolean checkoutBookByUser(@RequestHeader(value = "Authorization") String token,
                                      @RequestParam Long bookId) {
        String userEmail = ExtractJWT.payloadJWTExtraction(token, "\"sub\"");
        //String userEmail ="testuser@email.com";
        return bookService.checkoutBookByUser(userEmail, bookId);
    }

    @PutMapping("/secure/checkout")
    public Book checkoutBook(@RequestHeader(value = "Authorization") String token,
                             @RequestParam Long bookId) throws Exception {
        String userEmail = ExtractJWT.payloadJWTExtraction(token, "\"sub\"");
        //String userEmail ="testuser@email.com";
        return bookService.checkoutBook(userEmail, bookId);
    }


    @PutMapping("/secure/return")
    public void returnBook(@RequestHeader(value = "Authorization") String token,
                           @RequestParam Long bookId) throws Exception {
        String userEmail = ExtractJWT.payloadJWTExtraction(token, "\"sub\"");
        // String userEmail ="testuser@email.com";
        bookService.returnBook(userEmail, bookId);
    }


    @PutMapping("/secure/renew/loan")
    public void renewLoan(@RequestHeader(value = "Authorization") String token,
                          @RequestParam Long bookId) throws Exception {
        String userEmail = ExtractJWT.payloadJWTExtraction(token, "\"sub\"");
        //String userEmail ="testuser@email.com";
        bookService.renewLoan(userEmail, bookId);
    }



   /* @GetMapping("/{bookId}")
    public ResponseEntity<BookResponse> getBookById(@PathVariable Long bookId) {
        try {
            Book book = bookService.findBookById(bookId);
            BookResponse response = new BookResponse();

            if (book != null) {
                BookResponse.BookData data = new BookResponse.BookData();
                data.setId(book.getId());
                data.setTitle(book.getTitle());
                data.setAuthor(book.getAuthor());
                data.setDescription(book.getDescription());
                data.setCopies(book.getCopies());
                data.setCopiesAvailable(book.getCopiesAvailable());
                data.setCategory(book.getCategory());

                response.setMessage("Book details retrieved successfully.");
                response.setStatus(200);
                response.setData(data);

                return ResponseEntity.ok(response);
            } else {
                response.setMessage("Book not found");
                response.setStatus(404);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (Exception e) {
            BookResponse errorResponse = new BookResponse();
            errorResponse.setMessage("Error retrieving book");
            errorResponse.setStatus(500);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
*/

   /* @GetMapping("")
    public ResponseEntity<List<BookSummaryDTO>> getAllBooks() {
        try {
            List<Book> books = bookService.getAllBooks();

            if (!books.isEmpty()) {
                List<BookSummaryDTO> bookSummaries = new ArrayList<>();
                for (Book book : books) {
                    BookSummaryDTO bookSummary = new BookSummaryDTO();
                    bookSummary.setId(book.getId());
                    bookSummary.setTitle(book.getTitle());
                    bookSummary.setAuthor(book.getAuthor());
                    bookSummaries.add(bookSummary);
                }

                return ResponseEntity.ok(bookSummaries);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }*/


     @GetMapping("/validate-token")
     public ResponseEntity<String> validateToken(@RequestParam String token) {
        try {

            String issuer = "https://dev-86703467.okta.com/oauth2/default";
            String jwksUrl = "https://dev-86703467.okta.com/oauth2/default/v1/keys";

            JwtClaims jwtClaims = ExtractJWT.validateToken(token, issuer, jwksUrl);

            return ResponseEntity.ok("Token is valid. Claims: " + jwtClaims.toJson());
        } catch (InvalidJwtException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid token: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server errorrrrrrrrr");
        }
    }

}



















