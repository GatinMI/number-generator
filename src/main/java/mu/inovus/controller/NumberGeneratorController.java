package mu.inovus.controller;

import mu.inovus.entity.RegistrationNumber;
import mu.inovus.urls.NumberGeneratorUrls;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

/**
 * Created by maratgatin on 13/03/2019.
 */

@RestController
@RequestMapping(NumberGeneratorUrls.BASE)
public class NumberGeneratorController {

    private static final String SESSION_NUMBER = "currentNumber";

    @GetMapping(NumberGeneratorUrls.RANDOM)
    public ResponseEntity<String> random(HttpSession session) {
        RegistrationNumber registrationNumber = RegistrationNumber.generateNumber();
        session.setAttribute(SESSION_NUMBER, registrationNumber);
        return ResponseEntity.ok(registrationNumber.toString());
    }

    @GetMapping(NumberGeneratorUrls.NEXT)
    public ResponseEntity<String> next(HttpSession session) {
        Object o = session.getAttribute(SESSION_NUMBER);
        if (o == null) {
            return ResponseEntity.badRequest().build();
        } else if (o instanceof RegistrationNumber) {
            RegistrationNumber next = RegistrationNumber.getNext((RegistrationNumber) o);
            session.setAttribute(SESSION_NUMBER, next);
            return ResponseEntity.ok(next.toString());
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
