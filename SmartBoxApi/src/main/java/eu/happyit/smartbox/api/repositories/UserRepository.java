package eu.happyit.smartbox.api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import eu.happyit.smartbox.api.domain.Users;

public interface UserRepository extends JpaRepository<Users, Long> {
	@Query("SELECT u FROM Users u LEFT JOIN FETCH u.authorities WHERE u.username = :username")
    Users findByUsername(String username);

	@Transactional
	@Modifying
	@Query("UPDATE Users u SET u.password = :newPassword WHERE u.username = :username")
	void updatePassword(String newPassword, String username);

	
	@Transactional
	@Modifying
	@Query("UPDATE Users u SET u.lockDown = :lockDown WHERE u.username = :username")
	void changeLockDown(String username, boolean lockDown);

	
	@Transactional
	@Modifying
	@Query("UPDATE Users u SET u.musicOn = :musicOn WHERE u.username = :username")
	void changeMusicOn(String username, boolean musicOn);

}
