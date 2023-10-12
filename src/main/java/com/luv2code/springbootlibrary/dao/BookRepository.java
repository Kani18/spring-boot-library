package com.luv2code.springbootlibrary.dao;

import com.luv2code.springbootlibrary.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.RequestParam;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {  //<entity classname,primary key datatype>

    //Page<Book> is a container for a page of books that also includes pagination information. It typically includes the number of total pages, the number of elements in the current page, and the current page number. It's typically used in situations where you want to retrieve a specific page of data from a larger dataset and you want to include pagination information in the response.
    //Page<Book> findByTitleContaining(@RequestParam("title") String title, Pageable pageable);
    Page<Book> findByTitleContainingIgnoreCase(@RequestParam("title") String title, Pageable pageable);
    Page<Book> findByCategory(@RequestParam("category") String category, Pageable pageable);
    Optional<Book> findByTitleAndAuthor(String title, String author);

    @Query("SELECT b FROM Book b WHERE LOWER(b.title) LIKE %:title% AND (:category IS NULL OR b.category = :category)")
    Page<Book> findByTitleContainingIgnoreCaseAndCategory(
            @Param("title") String title,
            @Param("category") String category,
            Pageable pageable
    );

    @Query("select o from Book o where id in :book_ids")
    List<Book> findBooksByBookIds (@Param("book_ids") List<Long> bookId);
}
