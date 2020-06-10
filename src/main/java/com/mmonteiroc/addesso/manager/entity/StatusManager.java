package com.mmonteiroc.addesso.manager.entity;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mmonteiroc.addesso.entity.Status;
import com.mmonteiroc.addesso.exceptions.entity.StatusNotFoundException;
import com.mmonteiroc.addesso.exceptions.petition.NotRecivedRequiredParamsException;
import com.mmonteiroc.addesso.repository.StatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Set;

/**
 * Code created by: mmonteiroc
 * Email: miguelmonteiroclaveri@gmail.com
 * Github: https://github.com/mmonteiroc
 * LinkedIn: https://www.linkedin.com/in/mmonteiroc/?locale=en_US
 * Date of creation: 08/06/2020
 * Package:com.mmonteiroc.addesso.manager.entity
 * Project: addesso
 */
@Service
public class StatusManager {
    @Autowired
    private StatusRepository statusRepository;

    @Autowired
    private Gson gson;


    public Set<Status> findAll() {
        return this.statusRepository.findAll();
    }

    public Status findById(Long id) throws StatusNotFoundException {
        Status status = this.statusRepository.findByIdStatus(id);
        if (status == null) throw new StatusNotFoundException("Status with id [" + id + "] was not found");
        return status;
    }

    public Status convertFromJson(String json, boolean requireParams) throws NotRecivedRequiredParamsException {

        JsonObject jsonObject = gson.fromJson(json, JsonObject.class);
        Status status = new Status();
        if (jsonObject.get("idStatus") != null) {
            try {
                status = this.findById(jsonObject.get("idStatus").getAsLong());
            } catch (StatusNotFoundException e) {
                e.printStackTrace();
            }
        }
        if (jsonObject.get("shortName") != null) {
            status.setShortName(jsonObject.get("shortName").getAsString());
            if (status.getShortName().equals(""))
                throw new NotRecivedRequiredParamsException("Param shortName was required");
        } else if (requireParams) {
            throw new NotRecivedRequiredParamsException("Param shortName was required");
        }
        if (jsonObject.get("description") != null) {
            status.setDescription(jsonObject.get("description").getAsString());
            if (status.getDescription().equals(""))
                throw new NotRecivedRequiredParamsException("Param description was required");
        } else if (requireParams) {
            throw new NotRecivedRequiredParamsException("Param description was required");
        }

        return status;
    }

    public void createOrUpdate(Status... statuses) {
        Iterable<Status> iterable = Arrays.asList(statuses);
        this.statusRepository.saveAll(iterable);
    }
}
