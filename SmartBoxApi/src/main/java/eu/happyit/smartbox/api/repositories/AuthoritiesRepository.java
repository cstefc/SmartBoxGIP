package eu.happyit.smartbox.api.repositories;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import eu.happyit.smartbox.api.domain.Authorities;
import eu.happyit.smartbox.api.domain.User;

public interface AuthoritiesRepository extends JpaRepository<Authorities, Long>{

	@Transactional
	@Modifying
	@Query("UPDATE Authorities a SET a.user = :user WHERE a.user = null AND a.authority = :role")
	void updateUser(User user, String role);

}
