package com.alliance.radish.repository;

import com.alliance.radish.domain.Resource;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResourceRepository extends CrudRepository<Resource,Long> {

    @Query("select t from Resource t")
    List<Resource> findAll();
}
