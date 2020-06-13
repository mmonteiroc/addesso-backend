package com.mmonteiroc.addesso.controller.entity;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mmonteiroc.addesso.entity.*;
import com.mmonteiroc.addesso.exceptions.entity.CategoryNotFoundException;
import com.mmonteiroc.addesso.exceptions.entity.StatusNotFoundException;
import com.mmonteiroc.addesso.exceptions.entity.TicketNotFoundException;
import com.mmonteiroc.addesso.exceptions.entity.UploadedFileNotFoundException;
import com.mmonteiroc.addesso.exceptions.petition.NotRecivedRequiredParamsException;
import com.mmonteiroc.addesso.exceptions.token.TokenInvalidException;
import com.mmonteiroc.addesso.exceptions.token.TokenOverdatedException;
import com.mmonteiroc.addesso.manager.administration.FileStorageManager;
import com.mmonteiroc.addesso.manager.entity.*;
import com.mmonteiroc.addesso.manager.security.TokenManager;
import com.mmonteiroc.addesso.util.Resources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import javax.websocket.server.PathParam;
import java.io.File;
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
    private Environment environment;

    @Autowired
    private TicketManager ticketManager;

    @Autowired
    private CategoryManager categoryManager;

    @Autowired
    private UserManager userManager;

    @Autowired
    private TokenManager tokenManager;

    @Autowired
    private TicketHistoryManager ticketHistoryManager;

    @Autowired
    private Gson gson;

    @Autowired
    private UploadedFileManager uploadedFileManager;

    @Autowired
    private FileStorageManager fileStorageManager;

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

    @GetMapping("/tickets/{id}/history")
    public Set<TicketHistory> getTicketHistory(@PathParam("id") Long id, HttpServletResponse response) throws IOException {

        try {
            Ticket ticket = this.ticketManager.findById(id);
            return this.ticketHistoryManager.findAllByTicket(ticket);
        } catch (TicketNotFoundException e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
            return null;
        }
    }

    @GetMapping("/tickets/not/solved")
    public Set<Ticket> getTicketsNotSolved() {
        return null;//this.ticketManager.findAllNotSolved();
    }

    @GetMapping("/tickets/solved")
    public Set<Ticket> getTicketsSolved() {
        return null;//this.ticketManager.findAllSolved();
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

    @GetMapping("ticket/{id}/file")
    public Set<UploadedFile> getFilesOfTicket(@PathVariable("id") Long id, HttpServletResponse response) throws IOException {
        try {
            Ticket ticket = this.ticketManager.findById(id);
            return ticket.getAtachedFiles();
        } catch (TicketNotFoundException e) {
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
     * @param json     json where all the params are recived. We expect a JSON with the next structure:
     *                 {
     *                 title: 'HERE TITLE OF TICKET',
     *                 description: 'DESCRIPTION',
     *                 idCategory: 2 // NUMBER OF CATEGORY - HAS TO EXIST IN DATABASE
     *                 }
     * @param response 200 OK, 401 Your token is invalid or overdated, 403 NOT PERMISION TO THIS ACTION
     * @param request
     * @return
     * @throws IOException
     */
    @PostMapping("/tickets")
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
             * We set the owner
             * */
            String token = request.getHeader("Authorization");
            token = token.replace("Bearer ", "");
            User owner = this.tokenManager.getUsuariFromToken(token);
            ticket.setUserOwner(owner);

            this.ticketManager.createOrUpdate(ticket);
            return new ResponseEntity<>("Ticket saved correctly", HttpStatus.OK);
        } catch (NotRecivedRequiredParamsException | TokenInvalidException | CategoryNotFoundException | StatusNotFoundException | TokenOverdatedException e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/ticket/{id}/file")
    public ResponseEntity<String> attachFiles(@PathVariable("id") Long id, @RequestPart(value = "file") final MultipartFile fileToUpload) {

        try {
            Ticket ticket = this.ticketManager.findById(id);
            UploadedFile file = new UploadedFile();


            String uriSaved = fileStorageManager.saveMultiparFiles(fileToUpload);

            file.setContentType(fileToUpload.getContentType());
            file.setFiletype(uriSaved.substring(uriSaved.lastIndexOf('.') + 1));
            file.setName(uriSaved);
            file.setTicket(ticket);


            ticket.getAtachedFiles().add(file);
            this.ticketManager.createOrUpdate(ticket);
            return ResponseEntity.ok("file uploaded correctly");
        } catch (TicketNotFoundException | IOException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/ticket/comment")
    public ResponseEntity<String> addComment(@RequestBody String json, HttpServletRequest request) {
        try {
            Ticket ticket = this.ticketManager.convertFromJson(json);
            /*
             * In case they send us a id, we dont wanna update cause
             * this endpoint is only to create tickets.
             * So we erase this id
             * */
            if (ticket.getIdTicket() == null)
                return new ResponseEntity<>("WE NEED ID_TICKET HERE", HttpStatus.BAD_REQUEST);

            /*
             * We set the owner
             * */
            String token = request.getHeader("Authorization");
            token = token.replace("Bearer ", "");
            User commenter = this.tokenManager.getUsuariFromToken(token);

            JsonObject jsonObject = gson.fromJson(json, JsonObject.class);
            if (jsonObject.get("text") != null && !jsonObject.get("text").getAsString().equals(""))
                ticket.addComment(commenter, jsonObject.get("text").getAsString());
            else return new ResponseEntity<>("NO TEXT SENT", HttpStatus.BAD_REQUEST);

            this.ticketManager.createOrUpdate(ticket);
            return new ResponseEntity<>("Ticket saved correctly", HttpStatus.OK);
        } catch (TokenInvalidException | TokenOverdatedException e) {
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
     *             idTicket:1,
     *             title:'HERE TITLE MODIFIED',
     *             description:'DESC MODIFIED',
     *             idCategory: 2 // REQUIRED - HAS TO EXIST IN DDBB
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
        if (ticket.getUserAssigned() != null && ticket.getUserAssigned().equals(worker))
            return new ResponseEntity<>("USER WAS ALREADY ASIGNED TO THIS TICKET", HttpStatus.BAD_REQUEST);
        if (!worker.isTechnician())
            return new ResponseEntity<>("The worker to assign has to be a technician", HttpStatus.BAD_REQUEST);

        ticket.setUserAssigned(worker);
        this.ticketManager.createOrUpdate(ticket);
        return new ResponseEntity<>("USER ASINGED CORRECTLY", HttpStatus.OK);
    }


    @PutMapping("/tickets/worker/me")
    @Transactional
    public ResponseEntity<String> assignMyself(@RequestBody String json, HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        token = token.replace("Bearer ", "");
        User worker = null;
        try {
            worker = this.tokenManager.getUsuariFromToken(token);
        } catch (TokenInvalidException | TokenOverdatedException e) {
            e.printStackTrace();
        }

        Ticket ticket = this.ticketManager.convertFromJson(json);
        if (worker.getIduser() == null)
            return new ResponseEntity<>("ID USER NOT CORRECT", HttpStatus.BAD_REQUEST);
        if (ticket.getIdTicket() == null)
            return new ResponseEntity<>("ID TICKET NOT CORRECT", HttpStatus.BAD_REQUEST);
        if (ticket.getUserAssigned() != null && ticket.getUserAssigned().equals(worker))
            return new ResponseEntity<>("USER WAS ALREADY ASIGNED TO THIS TICKET", HttpStatus.BAD_REQUEST);
        if (!worker.isTechnician())
            return new ResponseEntity<>("The worker to assign has to be a technician", HttpStatus.BAD_REQUEST);

        ticket.setUserAssigned(worker);
        this.ticketManager.createOrUpdate(ticket);
        return new ResponseEntity<>("USER ASINGED CORRECTLY", HttpStatus.OK);
    }



    /*
     * DELETE MAPPING
     * */

    /*
     * ID REFERS TO THE ID OF THE UPLOADED FILE
     * */
    @DeleteMapping("/ticket/file/{id}")
    public ResponseEntity<String> deleteFile(@PathVariable("id") Long id) {
        try {
            UploadedFile file = this.uploadedFileManager.findById(id);


            File sysFile = new File(environment.getProperty("UPLOADS_DIRECTORY") + Resources.getUploadDir(file.getUploadDate()) + file.getName());

            boolean result = sysFile.delete();

            if (!result) return ResponseEntity.badRequest().body("NO POSIBLE DELETE FILE");
            this.uploadedFileManager.delete(file);

            return ResponseEntity.ok("file deleted correctly");
        } catch (UploadedFileNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /*
     * ID REFERS TO THE ID OF THE TICKET ID
     * */
    @DeleteMapping("/ticket/{id}/file/")
    public ResponseEntity<String> deleteFiles(@PathVariable("id") Long id) {


        Ticket ticket = new Ticket();
        ticket.setIdTicket(id);

        Set<UploadedFile> files = this.uploadedFileManager.findByTicket(ticket);

        /*
         * los borramos del sistema
         * */
        for (UploadedFile file : files) {
            File sysFile = new File(environment.getProperty("UPLOADS_DIRECTORY") + Resources.getUploadDir(file.getUploadDate()) + file.getName());

            boolean result = sysFile.delete();

            if (!result) return ResponseEntity.badRequest().body("NO POSIBLE DELETE FILE");
        }


        for (UploadedFile file : files) {
            this.uploadedFileManager.delete(file);
        }


        return ResponseEntity.ok("file deleted correctly");

    }

    @DeleteMapping("/tickets/status")
    public ResponseEntity<String> removeStatus(@RequestBody String json) {
        JsonObject obj = this.gson.fromJson(json, JsonObject.class);
        Long id = obj.get("idHistory") != null ? obj.get("idHistory").getAsLong() : null;
        if (id == null) return new ResponseEntity<>("NO ID RECIVED", HttpStatus.BAD_REQUEST);

        TicketHistory statusChange = this.ticketHistoryManager.findById(id);
        this.ticketHistoryManager.delete(statusChange);
        return new ResponseEntity<>("This change has been erased from the history", HttpStatus.OK);
    }

}