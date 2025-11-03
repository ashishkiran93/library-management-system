package com.ashish.library_management_system.model;

import org.springframework.data.jpa.domain.Specification;

public class BorrowerSpecification {

    public static Specification<Borrower> isBorrowerExist(long id) {
        return (root, query, cb) -> cb.equal(root.get("id"), id);
    }
}
