package vanset_backend.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import vanset_backend.dto.AddressRequest;
import vanset_backend.dto.AddressResponse;
import vanset_backend.service.AddressService;

@RestController
public class AddressController {

    private final AddressService addressService;

    public AddressController(
            AddressService addressService) {

        this.addressService = addressService;
    }

    @PostMapping("/api/addresses")
    public AddressResponse createAddress(
            @Valid @RequestBody AddressRequest request) {

        return addressService.createAddress(request);
    }

    @GetMapping("/api/users/{userId}/addresses")
    public List<AddressResponse> getAddressesByUserId(
            @PathVariable Long userId) {

        return addressService.getAddressesByUserId(
                userId
        );
    }

    @GetMapping("/api/addresses/{id}")
    public AddressResponse getAddressById(
            @PathVariable Long id) {

        return addressService.getAddressById(id);
    }

    @PutMapping("/api/addresses/{id}")
    public AddressResponse updateAddress(
            @PathVariable Long id,
            @Valid @RequestBody AddressRequest request) {

        return addressService.updateAddress(
                id,
                request
        );
    }

    @DeleteMapping("/api/addresses/{id}")
    public void deleteAddress(
            @PathVariable Long id) {

        addressService.deleteAddress(id);
    }
}