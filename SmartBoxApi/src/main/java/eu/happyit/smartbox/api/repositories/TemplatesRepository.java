package eu.happyit.smartbox.api.repositories;

import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import eu.happyit.smartbox.api.domain.PinSets;
import eu.happyit.smartbox.api.domain.Templates;
import eu.happyit.smartbox.api.domain.Users;

public interface TemplatesRepository extends JpaRepository<Templates, Long> {

	@Query("SELECT t FROM Templates t WHERE t.templateName = :templateName AND t.user = :user")
	Templates findByTemplateName(String templateName, Users user);

	
	@Transactional
	@Modifying
	@Query("UPDATE Templates t SET t.pinSets = :pinSets WHERE t.templateName = :templateName")
	void updatePinSets(Set<PinSets> pinSets, String templateName);


	@Transactional
	@Modifying
	@Query("UPDATE Templates t SET t.templateName = :newTemplateName WHERE t.templateName = :templateName AND t.user = :user")
	void updateName(Users user, String templateName, String newTemplateName);
}
