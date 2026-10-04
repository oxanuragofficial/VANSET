package vanset_backend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import vanset_backend.dto.PaymentResponse;
import vanset_backend.service.PaymentService;
@RestController
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/api/orders/{orderId}/payment")
    public PaymentResponse createPayment(
            @PathVariable Long orderId) {

        return paymentService.createPayment(orderId);
    }
    @GetMapping("/api/orders/{orderId}/payment")
public PaymentResponse getPayment(
        @PathVariable Long orderId) {

    return paymentService.getPaymentByOrderId(orderId);
}
}