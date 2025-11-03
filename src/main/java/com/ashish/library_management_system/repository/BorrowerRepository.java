package com.ashish.library_management_system.repository;

import com.ashish.library_management_system.model.Borrower;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;


@Repository
public interface BorrowerRepository extends JpaRepository<Borrower,Long> , JpaSpecificationExecutor<Borrower> {


}
