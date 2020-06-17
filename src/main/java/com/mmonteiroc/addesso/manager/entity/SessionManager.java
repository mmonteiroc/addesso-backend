package com.mmonteiroc.addesso.manager.entity;

import com.mmonteiroc.addesso.entity.Session;
import com.mmonteiroc.addesso.repository.SessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;

/**
 * Code created by: mmonteiroc
 * Email: miguelmonteiroclaveri@gmail.com
 * Github: https://github.com/mmonteiroc
 * LinkedIn: https://www.linkedin.com/in/mmonteiroc/?locale=en_US
 * Date of creation: 17/06/2020
 * Package:com.mmonteiroc.addesso.manager.entity
 * Project: addesso
 */
@Service
public class SessionManager {

    @Autowired
    private SessionRepository sessionRepository;

    public Session findById(Long id) {
        return this.sessionRepository.findByIdSession(id);
    }

    public void createOrUpdate(Session... sessions) {
        Iterable<Session> iterable = Arrays.asList(sessions);
        this.sessionRepository.saveAll(iterable);
    }

    public void delete(Session... sessions) {
        Iterable<Session> iterable = Arrays.asList(sessions);
        this.sessionRepository.deleteAll(iterable);
    }
}
