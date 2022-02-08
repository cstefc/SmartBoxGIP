package eu.happyit.smartbox.api.web;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import eu.happyit.smartbox.api.domain.User;
import eu.happyit.smartbox.api.repositories.UserRepository;
import eu.happyit.smartbox.api.websocket.messageTemplates.LockDownMessage;
import eu.happyit.smartbox.api.websocket.messageTemplates.PasswordMessage;
import eu.happyit.smartbox.api.websocket.messageTemplates.PlayMessage;

@Controller
@RequestMapping(path = "/user")
public class UserController {

	@Autowired
	UserRepository userRepo;
	@Autowired
	SimpMessagingTemplate temp;

	ResponseEntity<?> badRequest = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	ResponseEntity<?> succesRequest = new ResponseEntity<>(HttpStatus.OK);
	PasswordEncoder passEncoder = new BCryptPasswordEncoder();

	// Create (see admin controller)

	// Read
	@RequestMapping(path = "/verify")
	ResponseEntity<?> verify(){
		return succesRequest;
	}

	@RequestMapping(path = "/showUser")
	public @ResponseBody User showUser(Authentication auth) {

		// Get the user information
		User user = userRepo.findByUsername(auth.getName());
		return user;
	}

	// Update
	@RequestMapping(path = "/setLockDown")
	ResponseEntity<?> changeLockDown(Authentication auth, @RequestParam boolean state) {

		String username = auth.getName();
		User user = userRepo.findByUsername(username);
		userRepo.changeLockDown(username, state);
		
		user.setLockDown(state);
		temp.convertAndSendToUser(username, "/queue/lockDown", new LockDownMessage(user));
		
		return succesRequest;
	}

	@RequestMapping(path = "/changeMusic")
	ResponseEntity<?> changeMusic(Authentication auth, Principal principal) {

		String username = auth.getName();
		User user = userRepo.findByUsername(username);
		boolean musicOn = user.isMusicOn();
		if (musicOn) {
			userRepo.changeMusicOn(username, false);
			user.setMusicOn(false);
			temp.convertAndSendToUser(username, "/queue/play", new PlayMessage(user));
			return succesRequest;
		}
		if (!musicOn) {
			userRepo.changeMusicOn(username, true);
			user.setMusicOn(true);
			temp.convertAndSendToUser(principal.getName(), "/queue/play", new PlayMessage(user));
			return succesRequest;
		} else {
			return badRequest;
		}
	}

	@RequestMapping(path = "/changePassword")
	ResponseEntity<?> changePassword(Authentication auth, @RequestParam String password,
			@RequestParam String newPassword) {

		String username = auth.getName();
		User user = userRepo.findByUsername(username);
		if (user != null) {
			if (passEncoder.matches(password, user.getPassword())) {
				userRepo.updatePassword(passEncoder.encode(newPassword), username);
				userRepo.flush();
				temp.convertAndSendToUser(username, "/queue/password", new PasswordMessage(newPassword));
				return succesRequest;
			}
		} else {
			return badRequest;
		}
		return badRequest;
	}

	// Delete (see admin controller)
}
