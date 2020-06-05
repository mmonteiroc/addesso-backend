package com.mmonteiroc.addesso.controller.entity;

import com.mmonteiroc.addesso.entity.Category;
import com.mmonteiroc.addesso.entity.Ticket;
import com.mmonteiroc.addesso.entity.User;
import com.mmonteiroc.addesso.entity.enums.TicketStatus;
import com.mmonteiroc.addesso.exceptions.entity.CategoryNotFound;
import com.mmonteiroc.addesso.exceptions.petition.NotRecivedRequiredParamsException;
import com.mmonteiroc.addesso.exceptions.token.TokenInvalidException;
import com.mmonteiroc.addesso.exceptions.token.TokenOverdatedException;
import com.mmonteiroc.addesso.manager.entity.CategoryManager;
import com.mmonteiroc.addesso.manager.entity.TicketManager;
import com.mmonteiroc.addesso.manager.entity.UserManager;
import com.mmonteiroc.addesso.manager.security.TokenManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

/**
 * Code created by: mmonteiroc
 * Email: miguelmonteiroclaveri@gmail.com
 * Github: https://github.com/mmonteiroc
 * LinkedIn: https://www.linkedin.com/in/mmonteiroc/?locale=en_US
 * Date of creation: 04/06/2020
 * Package: com.mmonteiroc.addesso.controller.entity
 * Project: addesso
 */
@RestController
public class TicketController {

    @Autowired
    private TicketManager ticketManager;

    @Autowired
    private CategoryManager categoryManager;

    @Autowired
    private UserManager userManager;

    @Autowired
    private TokenManager tokenManager;

    @GetMapping("/tickets")
    public Set<Ticket> getAllTickets() {
        return this.ticketManager.findAll();
    }

    @GetMapping("/tickets/{id}")
    public Ticket getSpecificTicket(@PathVariable(name = "id") Long idTicket) {
        return this.ticketManager.findById(idTicket);
    }

    @GetMapping("/tickets/category/{id}")
    public Set<Ticket> getAllTicketsByCategory(@PathVariable(name = "id") Long idCategory, HttpServletResponse response) throws IOException {
        try {
            Category cat = this.categoryManager.findById(idCategory);
            return this.ticketManager.findByCategory(cat);
        } catch (CategoryNotFound e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
            return null;
        }
    }

    @PostMapping("/tickets")
    public ResponseEntity<String> addTicket(@RequestBody String json, HttpServletResponse response, HttpServletRequest request) throws IOException {
        try {
            Ticket ticket = this.ticketManager.convertFromJson(json, true);
            /*
             * In case they send us a id, we dont wanna update cause
             * this endpoint is only to create tickets.
             * So we erase this id
             * */
            ticket.setIdTicket(null);

            /*
             * We set default status when created
             * */
            ticket.setStatus(TicketStatus.CREATED);


            /*
             * We set the owner
             * */
            String token = request.getHeader("Authorization");
            token = token.replace("Bearer ", "");
            User owner = this.tokenManager.getUsuariFromToken(token);
            ticket.setUserOwner(owner);


            this.ticketManager.createOrUpdate(ticket);
            return new ResponseEntity<>("Ticket saved correctly", HttpStatus.OK);
        } catch (NotRecivedRequiredParamsException | TokenInvalidException | TokenOverdatedException e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/tickets")
    public ResponseEntity<String> modifyTicket(@RequestBody String json) {

        return new ResponseEntity<>("OK", HttpStatus.OK);
    }

    @DeleteMapping("/tickets")
    public ResponseEntity<String> deleteTicket(@RequestBody String json) {

        return new ResponseEntity<>("OK", HttpStatus.OK);
    }


}
