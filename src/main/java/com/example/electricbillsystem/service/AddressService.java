package com.example.electricbillsystem.service;

import com.example.electricbillsystem.CustomException;
import com.example.electricbillsystem.model.Address;
import com.example.electricbillsystem.repository.AddressRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class AddressService {

    private final AddressRepo addressRepo;

    @Autowired
    public AddressService(AddressRepo addressRepo) {
        this.addressRepo = addressRepo;
    }

    public List<Address> getAddresses() {
        return addressRepo.findAll();
    }

    public void addAddress(Address address) throws CustomException {
        List<Address> addresses = addressRepo.findAll();
        if (addresses.contains(address)) {
            throw new CustomException("This address has already been added, double-check if you made a mistake somewhere");
        }
        addressRepo.save(address);
    }

    public Address getAddress(Integer id) throws CustomException {
        Address address;
        try {
            address = addressRepo.findById(id).get();
        } catch (EmptyResultDataAccessException e) {
            throw new CustomException("There is no address with such id");
        }
        return address;
    }

    public void deleteAddress(Integer id) throws CustomException {
        Address address = getAddress(id);
        addressRepo.delete(address);
    }

}
