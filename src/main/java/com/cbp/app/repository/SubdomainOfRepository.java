package com.cbp.app.repository;

import com.cbp.app.model.db.SubdomainOf;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubdomainOfRepository extends JpaRepository<SubdomainOf, Integer> {
    List<SubdomainOf> findAllByWebsiteIdParent(int websiteIdParent);

    List<SubdomainOf> findAllByWebsiteIdChild(int websiteIdChild);
}
