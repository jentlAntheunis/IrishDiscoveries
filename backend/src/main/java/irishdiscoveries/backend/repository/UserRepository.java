package irishdiscoveries.backend.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import irishdiscoveries.backend.domain.User;

public interface UserRepository extends JpaRepository<User, UUID> {

}
