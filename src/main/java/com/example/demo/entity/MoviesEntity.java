package com.example.demo.entity;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "movies", schema = "public", catalog = "postgres")

public class MoviesEntity {
    @Basic
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private int id;
    @Basic
    @Column(name = "title")
    private String title;
    @Basic
    @Column(name = "availability")
    private boolean availability ;

    public MoviesEntity(String title, boolean availability) {
        this.title = title;
        this.availability  = availability;
    }

    public MoviesEntity() {
    }

    public boolean isAvailability() {
        return availability;
    }

    public void setAvailability (boolean availability) {
        this.availability = availability;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MoviesEntity that = (MoviesEntity) o;

        if (id != that.id) return false;
        if (title != null ? !title.equals(that.title) : that.title != null) return false;
        if (availability != that.availability) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (availability ? 1 : 0);
        return result;
    }
}