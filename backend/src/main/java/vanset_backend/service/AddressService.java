package vanset_backend.service;

import java.util.List;

import org.springframework.stereotype.Service;

import vanset_backend.dto.AddressRequest;
import vanset_backend.dto.AddressResponse;
import vanset_backend.entity.Address;
import vanset_backend.entity.User;
import vanset_backend.exception.AddressNotFoundException;
import vanset_backend.exception.UserNotFoundException;
import vanset_backend.repository.AddressRepository;
import vanset_backend.repository.UserRepository;

@Service
public class AddressService {

    private final AddressRepository addressRepository;
    private final UserRepository userRepository;

    public AddressService(
            AddressRepository addressRepository,
            UserRepository userRepository) {

        this.addressRepository = addressRepository;
        this.userRepository = userRepository;
    }

    public AddressResponse createAddress(AddressRequest request) {

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() ->
                        new UserNotFoundException(request.getUserId()));

        Address address = new Address();

        address.setLabel(request.getLabel());
        address.setRecipientName(request.getRecipientName());
        address.setPhone(request.getPhone());
        address.setAddressLine(request.getAddressLine());
        address.setCity(request.getCity());
        address.setState(request.getState());
        address.setPincode(request.getPincode());
        address.setUser(user);

        Address savedAddress = addressRepository.save(address);

        return toResponse(savedAddress);
    }

    public List<AddressResponse> getAddressesByUserId(Long userId) {

        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException(userId);
        }

        return addressRepository.findByUserId(userId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public AddressResponse getAddressById(Long id) {

        Address address = addressRepository.findById(id)
                .orElseThrow(() ->
                        new AddressNotFoundException(id));

        return toResponse(address);
    }

    public AddressResponse updateAddress(
            Long id,
            AddressRequest request) {

        Address existingAddress = addressRepository.findById(id)
                .orElseThrow(() ->
                        new AddressNotFoundException(id));

        existingAddress.setLabel(request.getLabel());
        existingAddress.setRecipientName(request.getRecipientName());
        existingAddress.setPhone(request.getPhone());
        existingAddress.setAddressLine(request.getAddressLine());
        existingAddress.setCity(request.getCity());
        existingAddress.setState(request.getState());
        existingAddress.setPincode(request.getPincode());

        Address savedAddress = addressRepository.save(existingAddress);

        return toResponse(savedAddress);
    }

    public void deleteAddress(Long id) {

        if (!addressRepository.existsById(id)) {
            throw new AddressNotFoundException(id);
        }

        addressRepository.deleteById(id);
    }

    private AddressResponse toResponse(Address address) {

        return new AddressResponse(
                address.getId(),
                address.getUser().getId(),
                address.getLabel(),
                address.getRecipientName(),
                address.getPhone(),
                address.getAddressLine(),
                address.getCity(),
                address.getState(),
                address.getPincode()
        );
    }
}