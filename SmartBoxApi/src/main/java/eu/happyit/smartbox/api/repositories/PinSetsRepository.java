package eu.happyit.smartbox.api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import eu.happyit.smartbox.api.domain.PinSets;
import eu.happyit.smartbox.api.domain.Templates;

public interface PinSetsRepository extends JpaRepository<PinSets, Long>{

	@Transactional
	@Modifying
	@Query ("UPDATE PinSets p SET p.pinSetName =:newPinSetName WHERE p.id = :id")
	void renamePinSet(String newPinSetName, long id);
	
	@Transactional
	@Modifying
	@Query ("UPDATE PinSets p SET p.template = :template WHERE p.pinSetName = :pinSetName")
	void changeTemplate (Templates template, String pinSetName);
	
	
	@Transactional
	@Modifying
	@Query ("UPDATE PinSets p SET p.active = :active WHERE p.pinSetName = :pinSetName")
	void changeActive (boolean active, String pinSetName);
	
	@Transactional
	@Modifying
	@Query("UPDATE PinSets p SET p.template = null WHERE p.pinSetName = :pinSetName")
	void deleteRelation(String pinSetName);

	@Transactional
	@Modifying
	@Query("DELETE FROM PinSets p WHERE p.id =:id")
	void deleteByPinSetId(long id);
}
