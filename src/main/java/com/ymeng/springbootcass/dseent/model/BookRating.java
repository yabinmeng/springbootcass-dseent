package com.ymeng.springbootcass.dseent.model;

import com.datastax.driver.mapping.annotations.ClusteringColumn;
import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;

import java.io.Serializable;
import java.util.UUID;

@Table(name = "bookrating")
public class BookRating implements Serializable {

    @PartitionKey(0)
    @Column(name = "book_id")
    private UUID bookId;

    @PartitionKey(1)
    @Column(name = "author_code", caseSensitive = false)
    private String authorCode;

    @ClusteringColumn(0)
    @Column(name = "rating_year")
    private int ratingYear;

    @Column(name = "rating_score")
    private float ratingScore;

    @Column(name = "rating_desc")
    private String ratingDesc;

    public BookRating() {
    }

    public BookRating(UUID bookId, String authorCode, int ratingYear) {
        this.bookId = bookId;
        this.authorCode = authorCode;
        this.ratingYear = ratingYear;
    }

    public BookRating(UUID bookId, String authorCode, int ratingYear, float ratingScore, String ratingDesc) {
        this.bookId = bookId;
        this.authorCode = authorCode;
        this.ratingYear = ratingYear;
        this.ratingScore = ratingScore;
        this.ratingDesc = ratingDesc;
    }

    public UUID getBookId() { return this.bookId; }
    public void setBookId(UUID bookId) { this.bookId = bookId; }

    public String getAuthorCode() { return this.authorCode; }
    public void setAuthorCode(String authorCode) { this.authorCode = authorCode; }

    public int getRatingYear() { return this.ratingYear; }
    public void setRatingYear(int ratingYear) { this.ratingYear = ratingYear; }

    public float getRatingScore() { return this.ratingScore; }
    public void setRatingScore(float ratingScore) { this.ratingScore = ratingScore; }

    public String getRatingDesc() { return this.ratingDesc; }
    public void setRatingDesc(String ratingDesc) { this.ratingDesc = ratingDesc; }

    public String toString() {
        return String.format(
            "{ bookId = %1$s, authorCode = %2$s, ratingYear = %3$s, ratingScore = %4$s, ratingDesc = %5$s }",
            getBookId(), getAuthorCode(), getRatingYear(), getRatingScore(), getRatingDesc());
    }
}

