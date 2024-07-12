package com.vaadin.tutorial.crm.backend.repository;

import com.vaadin.tutorial.crm.backend.entity.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ContactRepository extends JpaRepository<Contact, Long> {
    @Query(
            "select c from Contact c " +
                    "where lower(c.firstName) like lower(concat('%', :filterText, '%')) or " +
                    "lower(c.lastName) like lower(concat('%', :filterText, '%'))"
    )
    List<Contact> filterContactsByFirstNameOrLastName(@Param("filterText") String filterText);
}