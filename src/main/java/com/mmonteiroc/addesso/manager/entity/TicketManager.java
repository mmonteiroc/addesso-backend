package com.mmonteiroc.addesso.manager.entity;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mmonteiroc.addesso.entity.Category;
import com.mmonteiroc.addesso.entity.Ticket;
import com.mmonteiroc.addesso.exceptions.petition.NotRecivedRequiredParamsException;
import com.mmonteiroc.addesso.repository.CategoryRepository;
import com.mmonteiroc.addesso.repository.TikcetRepository;
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
    private TikcetRepository tikcetRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private Gson gson;

    public Ticket findById(Long id) {
        return this.tikcetRepository.findByIdTicket(id);
    }

    public Set<Ticket> findByCategory(Category category) {
        return this.tikcetRepository.findByCategory(category);
    }

    public Set<Ticket> findAll() {
        return this.tikcetRepository.findAll();
    }

    public void createOrUpdate(Ticket... tickets) {
        Iterable<Ticket> iterable = Arrays.asList(tickets);
        this.tikcetRepository.saveAll(iterable);
    }

    public void delete(Ticket... tickets) {
        Iterable<Ticket> iterable = Arrays.asList(tickets);
        this.tikcetRepository.deleteAll(iterable);
    }

    /**
     * @param json          to get the params
     * @param requireParams If true checks that params musn't be missing
     * @return ticket with params recived
     * @throws NotRecivedRequiredParamsException if there was params required missing
     */
    public Ticket convertFromJson(String json, boolean requireParams) throws NotRecivedRequiredParamsException {
        JsonObject jsonObject = gson.fromJson(json, JsonObject.class);
        Ticket ticket = new Ticket();
        if (jsonObject.get("idTicket") != null) {
            ticket = this.findById(jsonObject.get("idTicket").getAsLong());
            if (ticket == null) ticket = new Ticket();
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
            ticket.setCategory(cat);
        } else if (requireParams) {
            throw new NotRecivedRequiredParamsException("Param category was required");
        }

        return ticket;
    }
}
