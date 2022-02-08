package eu.happyit.smartbox.api.web;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

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

import eu.happyit.smartbox.api.domain.Authorities;
import eu.happyit.smartbox.api.domain.User;
import eu.happyit.smartbox.api.repositories.AuthoritiesRepository;
import eu.happyit.smartbox.api.repositories.UserRepository;
import eu.happyit.smartbox.api.websocket.messageTemplates.PasswordMessage;

@Controller
@RequestMapping(path = "/admin")
public class UserAdminController {

	@Autowired
	UserRepository userRepo;
	@Autowired
	AuthoritiesRepository authorityRepo;
	@Autowired
	AuthoritiesRepository authRepo;
	@Autowired
	SimpMessagingTemplate temp;

	ResponseEntity<?> badRequest = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	ResponseEntity<?> succesRequest = new ResponseEntity<>(HttpStatus.OK);
	PasswordEncoder passEncoder = new BCryptPasswordEncoder();

	// Create functions

	@RequestMapping(path = "/addUser")
	ResponseEntity<?> addUser(@RequestParam String username, @RequestParam String password, @RequestParam String role) {

		if (!username.equals("") && !password.equals("") && (role.equals("ROLE_USER") || role.equals("ROLE_ADMIN"))) {
			// Checking if there's another user with this name
			if (userRepo.findByUsername(username) != null) {
				return badRequest;
			} else {
				User user = new User(username, passEncoder.encode(password), role);

				userRepo.save(user);
				authRepo.updateUser(user, role);

				return succesRequest;
			}
		} else {
			// Bad request
			return badRequest;
		}
	}

	@RequestMapping(path = "/addAuthority")
	ResponseEntity<?> addAuthority(@RequestParam String username, @RequestParam String role) {
		User user = userRepo.findByUsername(username);
		if (user != null && (role.equals("ROLE_USER") || role.equals("ROLE_ADMIN"))) {
			// Creating the new authority
			Authorities newAuthority = new Authorities();
			newAuthority.setUser(user);
			newAuthority.setAuthority(role);

			// Checking all authorities of the user
			Set<Authorities> allAuthorities = user.getAuthorities();
			Iterator<Authorities> authorities = allAuthorities.iterator();

			while (authorities.hasNext()) {
				Authorities authority = authorities.next();

				if (authority.getAuthority().equals(role)) {
					// If the User has already the authority, return 400
					return badRequest;
				}
			}

			// Adding the new object to the Set
			allAuthorities.add(newAuthority);

			// Setting the authorities to the user
			user.setAuthorities(allAuthorities);

			// Saving the user
			userRepo.save(user);
			return succesRequest;

		} else {
			return badRequest;
		}
	}

	// Read functions

	@RequestMapping(path = "/showUsers")
	public @ResponseBody List<User> showUser() {

		// Getting all users and returning them
		return userRepo.findAll();
	}

	@RequestMapping(path = "/showAuthorities")
	public @ResponseBody Set<Authorities> showAuthorities(@RequestParam String username) {
		// Getting all users and returning them
		User user = userRepo.findByUsername(username);
		if (user == null) {
			return null;
		}
		Set<Authorities> authorities = user.getAuthorities();
		return authorities;
	}

	// Update functions
	// The update functions aren't only for the administrators see UserController

	// Delete functions

	@RequestMapping(path = "/deleteUser")
	ResponseEntity<?> deleteUser(@RequestParam String username) {
		User user = userRepo.findByUsername(username);

		// Checking that user exist
		if (user == null) {
			return badRequest;
		} else {
			// deleting the user
			userRepo.delete(user);
			return succesRequest;
		}
	}

	@RequestMapping(path = "/deleteAuthority")
	ResponseEntity<?> deleteAuthority(@RequestParam String username, @RequestParam String role) {
		User user = userRepo.findByUsername(username);

		if (user == null || role.equals("ROLE_USER")) {
			return badRequest;
		}

		Set<Authorities> authorities = user.getAuthorities();
		Iterator<Authorities> authority = authorities.iterator();

		while (authority.hasNext()) {
			Authorities check = authority.next();
			if (check.getAuthority().equals(role)) {
				authorityRepo.delete(check);
				return succesRequest;
			}
		}
		return badRequest;
	}

	@RequestMapping(path = "/changePassword")
	ResponseEntity<?> changePassword(Authentication auth, @RequestParam String newPassword) {
		String username = auth.getName();
		User user = userRepo.findByUsername(username);
		if (user != null) {
			userRepo.updatePassword(passEncoder.encode(newPassword), username);
			userRepo.flush();
			temp.convertAndSendToUser(username, "/queue/password", new PasswordMessage(newPassword));
			return succesRequest;
		} else {
			return badRequest;
		}
	}

}
