package vanset_backend.dto;

public class AddressResponse {

    private Long id;
    private Long userId;
    private String label;
    private String recipientName;
    private String phone;
    private String addressLine;
    private String city;
    private String state;
    private String pincode;

    public AddressResponse(
            Long id,
            Long userId,
            String label,
            String recipientName,
            String phone,
            String addressLine,
            String city,
            String state,
            String pincode) {

        this.id = id;
        this.userId = userId;
        this.label = label;
        this.recipientName = recipientName;
        this.phone = phone;
        this.addressLine = addressLine;
        this.city = city;
        this.state = state;
        this.pincode = pincode;
    }

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public String getLabel() {
        return label;
    }

    public String getRecipientName() {
        return recipientName;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddressLine() {
        return addressLine;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getPincode() {
        return pincode;
    }
}