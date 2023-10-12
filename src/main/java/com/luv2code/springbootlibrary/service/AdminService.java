package com.luv2code.springbootlibrary.service;

import com.luv2code.springbootlibrary.dao.BookRepository;
import com.luv2code.springbootlibrary.dao.CheckoutRepository;
import com.luv2code.springbootlibrary.dao.ReviewRepository;
import com.luv2code.springbootlibrary.entity.Book;
import com.luv2code.springbootlibrary.entity.Checkout;
import com.luv2code.springbootlibrary.requestmodels.AddBookRequest;
import com.luv2code.springbootlibrary.requestmodels.UpdateBookRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AdminService {

    private BookRepository bookRepository;
    private ReviewRepository reviewRepository;
    private  CheckoutRepository checkoutRepository;

    @Autowired
    public AdminService (BookRepository bookRepository,
                         ReviewRepository reviewRepository,
                         CheckoutRepository checkoutRepository) {
        this.bookRepository = bookRepository;
        this.reviewRepository = reviewRepository;
        this.checkoutRepository = checkoutRepository;
    }

    public void increaseBookQuantity(Long bookId) throws Exception {

        Optional<Book> book = bookRepository.findById(bookId);

        if (!book.isPresent()) {
            throw new Exception("Book not found");
        }

        book.get().setCopiesAvailable(book.get().getCopiesAvailable() + 1);
        book.get().setCopies(book.get().getCopies() + 1);

        bookRepository.save(book.get());
    }

    public void decreaseBookQuantity(Long bookId) throws Exception {

        Optional<Book> book = bookRepository.findById(bookId);

        if (!book.isPresent() || book.get().getCopiesAvailable() <= 0 || book.get().getCopies() <= 0) {
            throw new Exception("Book not found or quantity locked");
        }

        book.get().setCopiesAvailable(book.get().getCopiesAvailable() - 1);
        book.get().setCopies(book.get().getCopies() - 1);

        bookRepository.save(book.get());
    }

    /*public void postBook(AddBookRequest addBookRequest) {
        Book book = new Book();
        book.setTitle(addBookRequest.getTitle());
        book.setAuthor(addBookRequest.getAuthor());
        book.setDescription(addBookRequest.getDescription());
        book.setCopies(addBookRequest.getCopies());
        book.setCopiesAvailable(addBookRequest.getCopies());
        book.setCategory(addBookRequest.getCategory());
        //book.setImg(addBookRequest.getImg());

        bookRepository.save(book);
    }*/

    public Book postBook(AddBookRequest addBookRequest) {
        Book book = new Book();
        book.setTitle(addBookRequest.getTitle());
        book.setAuthor(addBookRequest.getAuthor());
        book.setDescription(addBookRequest.getDescription());
        book.setCopies(addBookRequest.getCopies());
        book.setCopiesAvailable(addBookRequest.getCopies());
        book.setCategory(addBookRequest.getCategory());
        book.setImg(addBookRequest.getImg());

        return bookRepository.save(book);  // Save and then return the saved book.
    }


    public void deleteBook(Long bookId) throws Exception {

        Optional<Book> book = bookRepository.findById(bookId);

        if (!book.isPresent()) {
            throw new Exception("Book not found");
        }

        bookRepository.delete(book.get());
        checkoutRepository.deleteAllByBookId(bookId);
        reviewRepository.deleteAllByBookId(bookId);
    }




    public Book updateBook(Long bookId, UpdateBookRequest updateBookRequest) {
        // Find the existing book by ID
        Optional<Book> optionalBook = bookRepository.findById(bookId);
        if (optionalBook.isPresent()) {
            Book existingBook = optionalBook.get();

            // Update the book's fields with the data from the update request
            existingBook.setTitle(updateBookRequest.getTitle());
            existingBook.setAuthor(updateBookRequest.getAuthor());
            existingBook.setDescription(updateBookRequest.getDescription());
            existingBook.setCategory(updateBookRequest.getCategory());
            // Update other fields as needed

            return bookRepository.save(existingBook);  // Save and return the updated book.
        } else {
            throw new IllegalArgumentException("Book with ID " + bookId + " not found");
        }
    }


    public boolean doesBookExist(String title, String author) {
        Optional<Book> existingBook = bookRepository.findByTitleAndAuthor(title, author);
        return existingBook.isPresent();
    }



   public void updateBookCount(Long bookId, int countChange) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book not found"));

        // Update the book's count based on the countChange
        int updatedCount = book.getCopies() + countChange;
        book.setCopies(updatedCount);

        // Save the updated book entity back to the database
        bookRepository.save(book);
    }

    public Book saveBook(Book book) {
        return bookRepository.save(book);
    }

}
