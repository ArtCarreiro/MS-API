package com.amc.api.Repositories;

import com.amc.api.Entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, String> {

    Category findByUuid(String userUuid);
}
