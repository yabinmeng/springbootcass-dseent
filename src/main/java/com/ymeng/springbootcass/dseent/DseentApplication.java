package com.ymeng.springbootcass.dseent;

import com.datastax.driver.mapping.Mapper;
import com.ymeng.springbootcass.dseent.model.BookRating;
import com.ymeng.springbootcass.dseent.repository.BookRatingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;
import java.util.UUID;

@SpringBootApplication
public class DseentApplication implements CommandLineRunner {

    @Autowired
    private BookRatingRepository bookRatingRepository;

    public static void main(String[] args) {
        SpringApplication.run(DseentApplication.class, args);
    }

    @Override
    public void run(String... args) {

        UUID bookId1 = UUID.randomUUID();
        UUID bookId2 = UUID.randomUUID();

        BookRating bookRating1 =
            new BookRating(bookId1, "book_a", 2017, 8.1f, "A very interesting book");
        bookRatingRepository.saveRating(bookRating1);

        BookRating bookRating2 =
            new BookRating(bookId1, "book_b", 2017);
        bookRating2.setRatingScore(6.4f);

        BookRating bookRating3 =
            new BookRating(bookId2, "book_c", 2018);


        bookRatingRepository.saveRating(bookRating1);
        bookRatingRepository.saveRating(bookRating2);
        bookRatingRepository.saveRating(bookRating3, Mapper.Option.saveNullFields(false));

        //UUID bookIdToQuery = UUID.fromString("65e30bfc-42e7-4d9a-91ab-e78a99d00a21");
        List<BookRating> bookRatings = bookRatingRepository.findRatingForBook(bookId1);

        for (BookRating bookRating : bookRatings) {
            System.out.println(bookRating.toString());
        }


        System.exit(0);
    }
}
