package com.ashish.library_management_system.dto;

import lombok.*;

import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class BorrowerBooksDTO {
    private long borrowerId;
    private String borrowerName;
    private String borrowerEmail;

    private List<BookBowworedDTO> bookdBorrowed;


}
