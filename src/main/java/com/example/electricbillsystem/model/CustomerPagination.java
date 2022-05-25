package com.example.electricbillsystem.model;

import lombok.Data;

import java.util.List;

@Data
public class CustomerPagination {

    private List<Customer> content;
    private Integer pageNumber;
    private Integer pageSize;
    private Integer totalBooks;
    private Integer totalPages;
    private boolean last;

}
