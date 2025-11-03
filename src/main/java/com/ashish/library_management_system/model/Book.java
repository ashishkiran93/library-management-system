package com.ashish.library_management_system.model;

import jakarta.persistence.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@ToString
@EqualsAndHashCode
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String title;
    private String author;
    private String category;
    private boolean available;
    private int totalCopies;
    private int availableCopies;

    public boolean isAvailable() {
        return this.getAvailableCopies() > 0;
    }
}
