package eu.happyit.smartbox.api.web;

import java.util.Iterator;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import eu.happyit.smartbox.api.domain.MusicTitles;
import eu.happyit.smartbox.api.domain.User;
import eu.happyit.smartbox.api.repositories.MusicTitlesRepository;
import eu.happyit.smartbox.api.repositories.UserRepository;
import eu.happyit.smartbox.api.websocket.messageTemplates.MusicMessage;

@Controller
@RequestMapping(path = "/music")
public class MusicTitleController {
	//Class has been checked
	@Autowired
	SimpMessagingTemplate template;
	@Autowired
	MusicTitlesRepository titleRepo;
	@Autowired
	UserRepository userRepo;
	
	String path = "/queue/music";
	ResponseEntity<?> badRequest = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	ResponseEntity<?> succesRequest = new ResponseEntity<>(HttpStatus.OK);

	// Create

	@RequestMapping(path = "/addMusicTitle")
	ResponseEntity<?> addMusicTitle(@RequestParam String title, @RequestParam int duration,
			Authentication auth) {
		// Getting the user information
		String username = auth.getName();
		User user = userRepo.findByUsername(username);
		if (user != null) {

			// Checking if the title doesn't already exist
			Iterator<MusicTitles> titles = titleRepo.findAll().iterator();
			while (titles.hasNext()) {
				MusicTitles musicTitle = titles.next();
				if (musicTitle.getSongTitle().equals(title)) {

					// Returning Bad Request if the title already exist
					return badRequest;
				}
			}
			// Making the new title and saving it
			MusicTitles newTitle = new MusicTitles(title, duration, user);
			titleRepo.save(newTitle);
			template.convertAndSendToUser(username, path, new MusicMessage(newTitle, "ADD"));
			return succesRequest;
		} else {
			return badRequest;
		}
	}

	// Read

	@RequestMapping(path = "/showMusicTitle")
	public @ResponseBody Set<MusicTitles> showMusicTitles(Authentication auth) {
		// Getting all the musicTitles of the user and returning them
		User user = userRepo.findByUsername(auth.getName());
		if (user != null) {
			Set<MusicTitles> musicTitles = user.getMusicTitles();
			return musicTitles;
		} else {
			return null;
		}

	}

	// Update

	@RequestMapping(path = "/changeMusicTitle")
	ResponseEntity<?> changeMusicTitle(@RequestParam String oldTitle, @RequestParam String newTitle, Authentication auth) {

		// Getting all the titles of the user
		User user = userRepo.findByUsername(auth.getName());
		if (user == null) {
			return badRequest;
		}
		
		// Searching for the title
		Iterator<MusicTitles> titles = user.getMusicTitles().iterator();
		while (titles.hasNext()) {
			MusicTitles title = titles.next();
			// Updating the name
			if (title.getSongTitle().equals(oldTitle)) {
				titleRepo.delete(title);
				title.setSongTitle(newTitle);
				titleRepo.save(title);

				// Return OK
				return succesRequest;
			}
		}
		// Bad Request
		return badRequest;
	}

	// Delete
	
	@RequestMapping(path = "/deleteMusicTitle")
	ResponseEntity<?> deleteMusicTitle(@RequestParam String title, Authentication auth) {

		// Getting the user information
		User user = userRepo.findByUsername(auth.getName());
		if (user != null) {
			// Getting all the tiles and searching for the title
			Iterator<MusicTitles> titles = user.getMusicTitles().iterator();
			while (titles.hasNext()) {
				MusicTitles oldTitle = titles.next();

				// Deliting the title
				if (oldTitle.getSongTitle().equals(title)) {
					titleRepo.delete(oldTitle);
					// Return OK code
					template.convertAndSendToUser(auth.getName(), path, new MusicMessage(oldTitle, "DELETE"));
					return succesRequest;
				}
			}
			// The title isn't found, returning Bad Request
			return badRequest;
		} else {
			// The user isn't found, returning Bad Request
			return badRequest;
		}
	}

}
