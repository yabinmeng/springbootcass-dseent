package com.ymeng.springbootcass.dseent.repository;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.dse.DseSession;
import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;
import com.ymeng.springbootcass.dseent.model.BookRating;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.UUID;

@Repository
public class BookRatingRepository {

    private MappingManager mappingManager;
    private Mapper<BookRating> mapper;
    private DseSession dseSession;

    private static final String TBL_NAME = "bookrating";

    @Autowired
    public BookRatingRepository(MappingManager mappingManager) {
        this.mappingManager = mappingManager;
    }

    @PostConstruct
    public void init(){
        this.dseSession = (DseSession) mappingManager.getSession();
        this.mapper = mappingManager.mapper(BookRating.class);
    }

    public void saveRating(BookRating bookRatingEntity, Mapper.Option... mapperOptions ) {
        mapper.save(bookRatingEntity, mapperOptions);
    }

    public BookRating findRating(UUID book_id, UUID author_id, int rating_year) {
        return mapper.get(book_id, author_id, rating_year);
    }

    public List<BookRating> findRatingForBook(UUID bookId) {
        PreparedStatement preparedStatement =
            dseSession.prepare("select * from " + TBL_NAME + " where book_id = ? ALLOW FILTERING");

        BoundStatement boundStatement = preparedStatement.bind(bookId);

        ResultSet resultSet = dseSession.execute(boundStatement);

        return mapper.map(resultSet).all();

    }

    public void deleteRating(BookRating bookRatingEntity, Mapper.Option... mapperOptions) {
        mapper.delete(bookRatingEntity, mapperOptions);
    }

}
