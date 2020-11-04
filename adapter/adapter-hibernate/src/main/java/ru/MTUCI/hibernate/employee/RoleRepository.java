package ru.MTUCI.hibernate.employee;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.MTUCI.domain.security.Role;
import ru.MTUCI.domain.security.RoleName;

import java.util.List;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleName name);

    List<Role> findAll();
}
