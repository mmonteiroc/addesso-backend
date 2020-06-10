package com.mmonteiroc.addesso.manager.entity;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mmonteiroc.addesso.entity.Category;
import com.mmonteiroc.addesso.entity.Status;
import com.mmonteiroc.addesso.entity.Ticket;
import com.mmonteiroc.addesso.exceptions.entity.CategoryNotFoundException;
import com.mmonteiroc.addesso.exceptions.entity.StatusNotFoundException;
import com.mmonteiroc.addesso.exceptions.entity.TicketNotFoundException;
import com.mmonteiroc.addesso.exceptions.petition.NotRecivedRequiredParamsException;
import com.mmonteiroc.addesso.repository.CategoryRepository;
import com.mmonteiroc.addesso.repository.StatusRepository;
import com.mmonteiroc.addesso.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Set;

/**
 * Code created by: mmonteiroc
 * Email: miguelmonteiroclaveri@gmail.com
 * Github: https://github.com/mmonteiroc
 * LinkedIn: https://www.linkedin.com/in/mmonteiroc/?locale=en_US
 * Date of creation: 04/06/2020
 * Package: com.mmonteiroc.addesso.manager.entity
 * Project: addesso
 */
@Service
public class TicketManager {

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private StatusRepository statusRepository;

    @Autowired
    private Gson gson;

    public Ticket findById(Long id) throws TicketNotFoundException {
        Ticket ticket = this.ticketRepository.findByIdTicket(id);
        if (ticket == null) throw new TicketNotFoundException("Ticket with id [" + id + "] not found");
        return this.ticketRepository.findByIdTicket(id);
    }

    public Set<Ticket> findByCategory(Category category) {
        return this.ticketRepository.findAllByCategory(category);
    }

    public Set<Ticket> findAll() {
        return this.ticketRepository.findAll();
    }

    public void createOrUpdate(Ticket... tickets) {
        Iterable<Ticket> iterable = Arrays.asList(tickets);
        this.ticketRepository.saveAll(iterable);
    }

    public void delete(Ticket... tickets) {
        Iterable<Ticket> iterable = Arrays.asList(tickets);
        this.ticketRepository.deleteAll(iterable);
    }

    public Ticket convertFromJson(String json) {
        try {
            return this.convertFromJson(json, false);
        } catch (NotRecivedRequiredParamsException | CategoryNotFoundException | StatusNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * @param json          to get the params
     * @param requireParams If true checks that params musn't be missing
     * @return ticket with params recived
     * @throws NotRecivedRequiredParamsException if there was params required missing
     */
    public Ticket convertFromJson(String json, boolean requireParams) throws NotRecivedRequiredParamsException, CategoryNotFoundException, StatusNotFoundException {
        JsonObject jsonObject = gson.fromJson(json, JsonObject.class);
        Ticket ticket = new Ticket();
        if (jsonObject.get("idTicket") != null) {
            try {
                ticket = this.findById(jsonObject.get("idTicket").getAsLong());
            } catch (TicketNotFoundException e) {
                e.printStackTrace();
            }
        }

        if (jsonObject.get("title") != null) {
            ticket.setTitle(jsonObject.get("title").getAsString());
        } else if (requireParams) {
            throw new NotRecivedRequiredParamsException("Param title was required");
        }

        if (jsonObject.get("description") != null) {
            ticket.setDescription(jsonObject.get("description").getAsString());
        } else if (requireParams) {
            throw new NotRecivedRequiredParamsException("Param description was required");
        }
        if (jsonObject.get("idCategory") != null) {
            Category cat = this.categoryRepository.findByIdCategory(jsonObject.get("idCategory").getAsLong());
            if (cat == null)
                throw new CategoryNotFoundException("Category with id [" + jsonObject.get("idCategory").getAsLong() + "] not found");
            ticket.setCategory(cat);
        } else if (requireParams) {
            throw new NotRecivedRequiredParamsException("Param category was required");
        }

        if (jsonObject.get("idStatus") != null) {
            Long stat = jsonObject.get("idStatus").getAsLong();

            Status status = this.statusRepository.findByIdStatus(stat);
            if (status == null) throw new StatusNotFoundException("Status with id [" + stat + "] was not found");
            ticket.addStatus(status);
        } else if (requireParams) {
            throw new NotRecivedRequiredParamsException("Param idStatus was required");
        }

        return ticket;
    }

    public Set<Ticket> findByStatus(Status toBe) {
        return null;
    }

    public Set<Ticket> findByNotStatus(Status notToBe) {
        return null;
    }

}
