package eu.happyit.smartbox.api.web;

import java.util.HashSet;
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

import eu.happyit.smartbox.api.domain.PinSets;
import eu.happyit.smartbox.api.domain.Templates;
import eu.happyit.smartbox.api.domain.Users;
import eu.happyit.smartbox.api.repositories.PinSetsRepository;
import eu.happyit.smartbox.api.repositories.TemplatesRepository;
import eu.happyit.smartbox.api.repositories.UserRepository;
import eu.happyit.smartbox.api.websocket.messageTemplates.LightMessage;

@Controller
@RequestMapping(path = "/light")
public class TemplateController {
	@Autowired
	UserRepository userRepo;
	@Autowired
	TemplatesRepository templateRepo;
	@Autowired
	PinSetsRepository pinSetRepo;
	@Autowired
	SimpMessagingTemplate temp;

	private String destination = "/queue/light";
	private ResponseEntity<?> badRequest = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	private ResponseEntity<?> succesRequest = new ResponseEntity<>(HttpStatus.OK);

	// Create function

	@RequestMapping(path = "/addTemplate")
	ResponseEntity<?> addTemplate(@RequestParam int R, @RequestParam int G, @RequestParam int B,
			@RequestParam String templateName, Authentication auth) {

		String username = auth.getName();
		Users user = userRepo.findByUsername(username);

		Iterator<Templates> templates = user.getTemplates().iterator();
		while (templates.hasNext()) {
			Templates template = templates.next();
			if (template.equals("") || template.getTemplateName().equals(templateName) || user == null || R > 255
					|| R < 0 || G > 255 || G < 0 || B > 255 || B < 0 || templateName.equals("")) {
				return badRequest;
			}
		}

		Templates newTemplate = new Templates(R, G, B, templateName, user);
		templateRepo.save(newTemplate);

		return succesRequest;
	}

	@RequestMapping(path = "/addPinSet")
	ResponseEntity<?> addPinSet(@RequestParam int pinRed, @RequestParam int pinGreen, @RequestParam int pinBlue,
			@RequestParam String pinSetName, @RequestParam String templateName, Authentication auth) {

		// Getting all the user information
		String username = auth.getName();
		Users user = userRepo.findByUsername(username);

		if (user == null || pinSetName.equals("") || templateName.equals("")) {
			return badRequest;
		}

		Templates template = templateRepo.findByTemplateName(templateName, user);

		// Testing if the request params are correct
		for (Iterator<PinSets> pinSets = pinSetRepo.findAll().iterator(); pinSets.hasNext();) {
			PinSets testPinSetName = pinSets.next();
			if (testPinSetName.getPinSetName().equals(pinSetName) || template == null) {
				return badRequest;
			}

			// Adding the new PinSet to the template
			PinSets pinSet = new PinSets(pinRed, pinGreen, pinBlue, pinSetName, template);
			pinSetRepo.save(pinSet);
			temp.convertAndSendToUser(username, destination, new LightMessage(pinSet));
			return succesRequest;
		}

		return badRequest;
	}

	// Read function
	@RequestMapping(path = "/showPinSet")
	public @ResponseBody Set<PinSets> showPinSet(Authentication auth) {
		// Getting the users templates

		String username = auth.getName();
		Users user = userRepo.findByUsername(username);

		Set<PinSets> returnPinSets = new HashSet<PinSets>();

		// Checking which pinSets are with the templates
		for (Iterator<Templates> templates = user.getTemplates().iterator(); templates.hasNext();) {
			Templates template = templates.next();
			Set<PinSets> pinSets = template.getPinSets();
			for (Iterator<PinSets> it = pinSets.iterator(); it.hasNext();) {
				PinSets pinSet = it.next();
				returnPinSets.add(pinSet);
			}
		}
		// Returning the list
		return returnPinSets;
	}

	@RequestMapping(path = "/showTemplates")
	public @ResponseBody Set<Templates> showTemplates(Authentication auth) {

		String username = auth.getName();
		Users user = userRepo.findByUsername(username);
		Set<Templates> templates = user.getTemplates();
		return templates;
	}

// Update function

	@RequestMapping(path = "/changePinSet")

	ResponseEntity<?> changePinSet(@RequestParam String pinSetName, @RequestParam String newTemplateName,
			Authentication auth) {

		String username = auth.getName();
		Users user = userRepo.findByUsername(username);

		if (templateRepo.findByTemplateName(newTemplateName, user) == null && findPinSet(user, pinSetName) == null) {
			return badRequest;
		} else {
			Templates template = templateRepo.findByTemplateName(newTemplateName, user);
			pinSetRepo.changeTemplate(template, pinSetName);

			temp.convertAndSendToUser(username, destination, new LightMessage(findPinSet(user, pinSetName)));
			return succesRequest;
		}
	}

	@RequestMapping(path = "/renameTemplate")
	ResponseEntity<?> renameTemplate(@RequestParam String templateName, @RequestParam String newTemplateName,
			Authentication auth) {

		String username = auth.getName();
		Users user = userRepo.findByUsername(username);

		if (user != null && templateName != null) {
			templateRepo.updateName(user, templateName, newTemplateName);
			return succesRequest;
		} else {
			return badRequest;
		}
	}

	@RequestMapping(path = "/renamePinSet")
	ResponseEntity<?> renamePinSet(@RequestParam String oldPinSetName, @RequestParam String newPinSetName,
			Authentication auth) {

		// Getting all the user information
		String username = auth.getName();
		Users user = userRepo.findByUsername(username);

		// Searching for the PinSet
		PinSets pinSet = findPinSet(user, oldPinSetName);
		if (pinSet == null || newPinSetName.equals("")) {
			return badRequest;
		} else {
			pinSetRepo.renamePinSet(newPinSetName, pinSet.getId());
			return succesRequest;
		}
	}

	@RequestMapping(path = "/setLight")
	ResponseEntity<?> changeActive(@RequestParam String pinSetName, Authentication auth, @RequestParam boolean state) {

		String username = auth.getName();
		Users user = userRepo.findByUsername(username);

		PinSets pinSet = findPinSet(user, pinSetName);
		if (pinSet == null || pinSetName.equals("")) {
			return badRequest;
		}
		pinSetRepo.changeActive(state, pinSetName);
		pinSet.setActive(state);
		temp.convertAndSendToUser(username, destination, new LightMessage(pinSet));
		return succesRequest;
	}

	// Delete function

	@RequestMapping(path = "/deleteTemplate")
	ResponseEntity<?> deleteTemplate(@RequestParam String templateName, Authentication auth) {

		String username = auth.getName();
		Users user = userRepo.findByUsername(username);

		Templates template = templateRepo.findByTemplateName(templateName, user);

		if (user == null || templateName.equals("")) {
			return badRequest;
		} else if (template.getPinSets().isEmpty()) {
			templateRepo.delete(template);
			return succesRequest;
		} else {
			return badRequest;
		}

	}

	@RequestMapping(path = "/deletePinSet")
	ResponseEntity<?> deletePinSet(@RequestParam String pinSetName, Authentication auth) {

		String username = auth.getName();
		Users user = userRepo.findByUsername(username);

		if (user == null || pinSetName.equals("")) {
			return badRequest;
		} else {
			PinSets pinSet = findPinSet(user, pinSetName);
			if (pinSet == null) {
				return badRequest;
			} else {
				pinSetRepo.deleteByPinSetId(pinSet.getId());
				pinSet.setActive(false);
				temp.convertAndSendToUser(username, destination, new LightMessage(pinSet));
				return succesRequest;
			}
		}

	}

	public PinSets findPinSet(Users user, String pinSetName) {
		Set<Templates> templates = user.getTemplates();
		for (Iterator<Templates> templatesIt = templates.iterator(); templatesIt.hasNext();) {
			Set<PinSets> pinSets = templatesIt.next().getPinSets();
			for (Iterator<PinSets> pinSetIt = pinSets.iterator(); pinSetIt.hasNext();) {
				PinSets pinSet = pinSetIt.next();
				if (pinSet.getPinSetName().equals(pinSetName)) {
					return pinSet;
				}
			}
		}

		return null;
	}

}