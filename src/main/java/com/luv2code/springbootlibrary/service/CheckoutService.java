package com.luv2code.springbootlibrary.service;


import com.luv2code.springbootlibrary.dao.BookRepository;
import com.luv2code.springbootlibrary.dao.CheckoutRepository;
import com.luv2code.springbootlibrary.entity.Book;
import com.luv2code.springbootlibrary.entity.Checkout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
@Transactional
@Service
public class CheckoutService {

    @Autowired
    private CheckoutRepository checkoutRepository;

    // @Autowired
    // private UserService userService;

    @Autowired
    private BookRepository bookRepository;

    public List<Checkout> getCurrentLoans(String userEmail) {
        return checkoutRepository.findByUserEmail(userEmail);
    }

    public Boolean isBookCheckedOutByUser(String userEmail, Long bookId) {
        return checkoutRepository.existsByUserEmailAndBookId(userEmail, bookId);
    }

    public Checkout checkoutBook(String userEmail, Long bookId) throws Exception {
        Optional<Book> book = bookRepository.findById(bookId);

        // Check if the book exists and is available
        if (!book.isPresent() || book.get().getCopiesAvailable() <= 0) {
            throw new Exception("Book doesn't exist or not available");
        }

        // Check if the user has already checked out the book
        if (isBookCheckedOutByUser(userEmail, bookId)) {
            throw new Exception("Book already checked out by user");
        }

        // Create a new checkout record
        Checkout checkout = new Checkout(userEmail, LocalDate.now().toString(), LocalDate.now().plusDays(7).toString(), bookId);
        checkoutRepository.save(checkout);

        // Update book copies available
        book.get().setCopiesAvailable(book.get().getCopiesAvailable() - 1);
        bookRepository.save(book.get());

        return checkout;
    }



    public List<Checkout> getUserByEmail(String userEmail) {
        return checkoutRepository.findByUserEmail(userEmail);
    }
}
