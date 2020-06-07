package com.mmonteiroc.addesso.controller.entity;

import com.google.gson.Gson;
import com.mmonteiroc.addesso.entity.Category;
import com.mmonteiroc.addesso.entity.Ticket;
import com.mmonteiroc.addesso.entity.User;
import com.mmonteiroc.addesso.entity.enums.TicketStatus;
import com.mmonteiroc.addesso.exceptions.entity.CategoryNotFoundException;
import com.mmonteiroc.addesso.exceptions.entity.TicketNotFoundException;
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
import javax.transaction.Transactional;
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

    @Autowired
    private Gson gson;

    /*
    * --------------
    *
    *  GET MAPPINGS
    *
    * --------------
    * */

    @GetMapping("/tickets")
    public Set<Ticket> getAllTickets() {
        return this.ticketManager.findAll();
    }

    @GetMapping("/tickets/not/solved")
    public Set<Ticket> getTicketsNotSolved() {
        return this.ticketManager.findAllNotSolved();
    }

    @GetMapping("/tickets/solved")
    public Set<Ticket> getTicketsSolved() {
        return this.ticketManager.findAllSolved();
    }

    @GetMapping("/tickets/{id}")
    public Ticket getSpecificTicket(@PathVariable(name = "id") Long idTicket, HttpServletResponse response) throws IOException {
        try {
            return this.ticketManager.findById(idTicket);
        } catch (TicketNotFoundException e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
            return null;
        }
    }

    @GetMapping("/tickets/category/{id}")
    public Set<Ticket> getAllTicketsByCategory(@PathVariable(name = "id") Long idCategory, HttpServletResponse response) throws IOException {
        try {
            Category cat = this.categoryManager.findById(idCategory);
            return this.ticketManager.findByCategory(cat);
        } catch (CategoryNotFoundException e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
            return null;
        }
    }

    /*
     * ---------------
     *
     *  POST MAPPINGS
     *
     * ---------------
     * */


    /**
     * @param json json where all the params are recived. We expect a JSON with the next structure:
     *             {
     * 	                title: 'HERE TITLE OF TICKET',
     * 	                description: 'DESCRIPTION',
     * 	                idCategory: 2 // NUMBER OF CATEGORY - HAS TO EXIST IN DATABASE
     *             }
     *
     * @param response 200 OK, 401 Your token is invalid or overdated, 403 NOT PERMISION TO THIS ACTION
     * @param request
     * @return
     * @throws IOException
     */
    @PostMapping("/tickets")
    @Transactional
    public ResponseEntity<String> addTicket(@RequestBody String json, HttpServletResponse response, HttpServletRequest request) throws IOException {
        try {
            Ticket ticket = this.ticketManager.convertFromJson(json, true);
            /*
             * In case they send us a id, we dont wanna update cause
             * this endpoint is only to create tickets.
             * So we erase this id
             * */
            if (ticket.getIdTicket() != null)
                return new ResponseEntity<>("WE DONT ACCEPT ID_TICKET HERE", HttpStatus.BAD_REQUEST);

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
        } catch (NotRecivedRequiredParamsException | TokenInvalidException | TokenOverdatedException | CategoryNotFoundException e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /*
     * --------------
     *
     *  PUT MAPPINGS
     *
     * --------------
     * */


    /**
     * @param json data to modify the ticket, expected to be like:
     *             {
     * 	            idTicket:1,
     * 	            title:'HERE TITLE MODIFIED',
     * 	            description:'DESC MODIFIED',
     * 	            idCategory: 2 // REQUIRED - HAS TO EXIST IN DDBB
     *             }
     * @return
     */
    @PutMapping("/tickets")
    @Transactional
    public ResponseEntity<String> modifyTicket(@RequestBody String json) {

        Ticket ticket = this.ticketManager.convertFromJson(json);
        if (ticket.getIdTicket() == null) return new ResponseEntity<>("ID NOT VALID", HttpStatus.BAD_REQUEST);

        this.ticketManager.createOrUpdate(ticket);
        return new ResponseEntity<>("Ticket modified correctly", HttpStatus.OK);
    }

    /**
     * @param json needs to recive
     *             {
     *             idUser: HERE_GOES_ID,
     *             idTicket: HERE_GOES_ID
     *             }
     *             of worker to asign to the
     *             ticket and the ticket itself
     * @return response
     */
    @PutMapping("/tickets/worker")
    @Transactional
    public ResponseEntity<String> asignWorkerTicket(@RequestBody String json) {
        User worker = this.userManager.convertFromJson(json);
        Ticket ticket = this.ticketManager.convertFromJson(json);
        if (worker.getIduser() == null)
            return new ResponseEntity<>("ID USER NOT CORRECT", HttpStatus.BAD_REQUEST);
        if (ticket.getIdTicket() == null)
            return new ResponseEntity<>("ID TICKET NOT CORRECT", HttpStatus.BAD_REQUEST);
        if (ticket.getUserAsigned() != null && ticket.getUserAsigned().equals(worker))
            return new ResponseEntity<>("USER WAS ALREADY ASIGNED TO THIS TICKET", HttpStatus.BAD_REQUEST);
        if (!worker.isTechnician())
            return new ResponseEntity<>("The worker to assign has to be a technician", HttpStatus.BAD_REQUEST);

        ticket.setUserAsigned(worker);
        this.ticketManager.createOrUpdate(ticket);
        return new ResponseEntity<>("USER ASINGED CORRECTLY", HttpStatus.OK);
    }

}
