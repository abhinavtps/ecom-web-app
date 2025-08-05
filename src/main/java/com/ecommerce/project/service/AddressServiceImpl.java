package com.ecommerce.project.service;

import com.ecommerce.project.exceptions.ResourseNotFoundException;
import com.ecommerce.project.model.Address;
import com.ecommerce.project.model.User;
import com.ecommerce.project.payload.AddressDTO;
import com.ecommerce.project.repositories.AddressRepository;
import com.ecommerce.project.repositories.UserRepository;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    ModelMapper modelmapper;

    @Autowired
    AddressRepository addressRepository;

    @Autowired
    UserRepository userRepository;

    @Override
    public AddressDTO createAddress(AddressDTO addressDTO, User user) {
        Address address = modelmapper.map(addressDTO, Address.class);

        List<Address> addressList = user.getAddresses();
        addressList.add(address);
        user.setAddresses(addressList);

        address.setUser(user);

        Address savedAddress = addressRepository.save(address);

        return modelmapper.map(savedAddress, AddressDTO.class);

    }

    @Override
    public List<AddressDTO> getAddresses() {
        List<Address> addresses = addressRepository.findAll();
        return addresses.stream()
                .map(a -> modelmapper.map(a, AddressDTO.class))
                .toList();
    }

    @Override
    public AddressDTO getAddressById(Long addressId) {
        Address address = addressRepository.findById(addressId)
                .orElseThrow( () -> new ResourseNotFoundException("Address", "addressId", addressId));
        return modelmapper.map(address, AddressDTO.class);
    }

    @Override
    public List<AddressDTO> getUserAddresses(User user) {
        List<Address> addresses = user.getAddresses();
        return addresses.stream()
                .map(a -> modelmapper.map(a, AddressDTO.class))
                .toList();
    }

    @Override
    public AddressDTO updateAddress(Long addressId, AddressDTO addressDTO) {
       Address addressFromDatabase = addressRepository.findById(addressId)
               .orElseThrow( () -> new ResourseNotFoundException("Address", "addressId", addressId));

       addressFromDatabase.setCity(addressDTO.getCity());
       addressFromDatabase.setCountry(addressDTO.getCountry());
       addressFromDatabase.setStreet(addressDTO.getStreet());
       addressFromDatabase.setPincode(addressDTO.getPincode());
       addressFromDatabase.setState(addressDTO.getState());
       addressFromDatabase.setBuildingName(addressDTO.getBuildingName());

       Address updatedAddress = addressRepository.save(addressFromDatabase);
       User user = addressFromDatabase.getUser();
       user.getAddresses().removeIf(address -> address.getAddressId().equals(addressId));
       user.getAddresses().add(updatedAddress);

       userRepository.save(user);

        return modelmapper.map(updatedAddress, AddressDTO.class);
    }

    @Override
    public String deleteAddress(Long addressId) {
        Address addressFromDatabase = addressRepository.findById(addressId)
                .orElseThrow( () -> new ResourseNotFoundException("Address", "addressId", addressId));

        User user = addressFromDatabase.getUser();
        user.getAddresses().removeIf(address -> address.getAddressId().equals(addressId));
        userRepository.save(user);

        addressRepository.delete(addressFromDatabase);
        return "Address has been deleted successfully: " + addressId ;
    }
}
