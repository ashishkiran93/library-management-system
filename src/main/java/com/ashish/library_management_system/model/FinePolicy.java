package com.ashish.library_management_system.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FinePolicy {
    private long id;
    private String category;
    private float fineAmount;
}
