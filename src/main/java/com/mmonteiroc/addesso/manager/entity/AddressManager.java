package com.mmonteiroc.addesso.manager.entity;

import com.mmonteiroc.addesso.entity.Address;
import com.mmonteiroc.addesso.repository.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Set;

/**
 * Code created by: mmonteiroc
 * Email: miguelmonteiroclaveri@gmail.com
 * Github: https://github.com/mmonteiroc
 * LinkedIn: https://www.linkedin.com/in/mmonteiroc/?locale=en_US
 * Date of creation: 15/06/2020
 * Package:com.mmonteiroc.addesso.manager.entity
 * Project: addesso
 */
@Service
public class AddressManager {
    @Autowired
    private AddressRepository addressRepository;

    public void createOrUpdate(Address... addresses) {
        Iterable<Address> iterable = Arrays.asList(addresses);
        this.addressRepository.saveAll(iterable);
    }

    public void delete(Address... addresses) {
        Iterable<Address> iterable = Arrays.asList(addresses);
        this.addressRepository.deleteAll(iterable);
    }

    public Set<Address> findAll() {
        return this.addressRepository.findAll();
    }

    public Address findById(Long id) {
        return this.addressRepository.findByIdAddress(id);
    }
}
