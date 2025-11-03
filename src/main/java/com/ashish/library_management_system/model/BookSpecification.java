package com.ashish.library_management_system.model;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class BookSpecification {
    public static Specification<Book> applyFilters(Map<String,String> paramList){
        return (root, query, cb) -> {
            List<Predicate> predicateList = new ArrayList<>();
            paramList.forEach((key,value)->{
                switch (key){
                    case "title":
                    case "author":
                    case "category": predicateList.add(cb.like(cb.lower(root.get(key)),"%"+value.toLowerCase()+"%"));
                                      break;
                    case "available": predicateList.add(cb.equal(root.get(key),Boolean.valueOf(value)));
                                      break;
                }
            });
            return cb.or(predicateList.toArray(new Predicate[0]));
        };
    }
}
