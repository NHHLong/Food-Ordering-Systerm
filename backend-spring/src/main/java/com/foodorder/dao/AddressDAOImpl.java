package com.foodorder.dao;

import com.foodorder.model.Address;
import org.springframework.stereotype.Repository;

@Repository
public class AddressDAOImpl extends GenericDAOImpl<Address, Integer> implements IAddressDAO {
    public AddressDAOImpl() {
        super(Address.class);
    }
}
