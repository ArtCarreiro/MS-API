package com.amc.api.Repositories;

import com.amc.api.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    User findByUuid(String userUuid);

    User findByEmail(String email);

    @Modifying
    @Query(
        value = """
            UPDATE customers 
            SET deleted = true, active = false
            WHERE uuid = :uuid
        """, nativeQuery = true)
    void deleteUserByUuid(@Param("uuid") String uuid);
}
