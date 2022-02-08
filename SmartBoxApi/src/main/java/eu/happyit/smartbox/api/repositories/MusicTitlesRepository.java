package eu.happyit.smartbox.api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import eu.happyit.smartbox.api.domain.MusicTitles;

public interface MusicTitlesRepository extends JpaRepository <MusicTitles, Long>{

}
