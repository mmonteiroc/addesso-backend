package com.mmonteiroc.addesso.controller.entity;

import com.mmonteiroc.addesso.entity.Ticket;
import com.mmonteiroc.addesso.manager.entity.TicketManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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


    @GetMapping("/tickets")
    public Set<Ticket> getAllTickets() {
        return this.ticketManager.findAll();
    }

    @GetMapping("/tickets/{id}")
    public Ticket getSpecificTicket(@PathVariable(name = "id") Long idTicket) {
        return this.ticketManager.findById(idTicket);
    }

    @GetMapping("/tickets/category/{id}")
    public Set<Ticket> getAllTicketsByCategory(@PathVariable(name = "id") Long idCategory) {

        return null;
    }

    @PostMapping("/tickets")
    public ResponseEntity<String> addTicket(@RequestBody String json) {

        return new ResponseEntity<>("OK", HttpStatus.OK);
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
