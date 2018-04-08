package com.jakubeeee.allegrointegrator.security.persistence.repositories;

import com.jakubeeee.allegrointegrator.security.persistence.entities.Role;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends CrudRepository<Role, Long> {

}
