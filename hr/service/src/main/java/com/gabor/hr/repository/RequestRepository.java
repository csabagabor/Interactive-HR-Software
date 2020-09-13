package com.gabor.hr.repository;


import com.gabor.hr.model.Request;
import com.gabor.hr.model.Status;
import com.gabor.hr.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {
    @Modifying
    @Query("update Request r set r.status='ACCEPTED' where r.id = :id")
    void accept(@Param("id") Long id);

    @Modifying
    @Query("update Request r set r.status='REJECTED' where r.id = :id")
    void reject(@Param("id") Long id);

    List<Request> findAllByStatus(Status status);

    List<Request> findAllByUser(User user);
}
