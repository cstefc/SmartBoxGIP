package eu.happyit.smartbox.api.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import eu.happyit.smartbox.api.repositories.UserRepository;

@Controller
public class WebsocketController {
	@Autowired
	SimpMessagingTemplate template;
	@Autowired
	UserRepository userRepo;
	
	Logger logger = LoggerFactory.getLogger(WebsocketController.class);

	@EventListener
	public void handleWebSocketConnectListener(SessionConnectedEvent event) throws InterruptedException {
		String username = event.getUser().getName();
		logger.info("New connection: username: " + username);

	}

	@EventListener
	public void handleSessionDisconnectEvent(SessionDisconnectEvent event) {
		String username = event.getUser().getName();;
		String sessionId = event.getSessionId();
		
		logger.info("Session disconnected, username: " + username + ", sessionId: " + sessionId);
	}
}
