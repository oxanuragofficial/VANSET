package vanset_backend.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import vanset_backend.dto.PaymentResponse;
import vanset_backend.entity.Order;
import vanset_backend.entity.Payment;
import vanset_backend.entity.PaymentStatus;
import vanset_backend.exception.OrderNotFoundException;
import vanset_backend.repository.OrderRepository;
import vanset_backend.repository.PaymentRepository;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;

    public PaymentService(
            PaymentRepository paymentRepository,
            OrderRepository orderRepository) {

        this.paymentRepository = paymentRepository;
        this.orderRepository = orderRepository;
    }

    @Transactional
    public PaymentResponse createPayment(Long orderId) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() ->
                        new OrderNotFoundException(orderId));

        if (paymentRepository.findByOrderId(orderId).isPresent()) {
            throw new IllegalStateException(
                    "Payment already exists for order: " + orderId);
        }

        BigDecimal totalAmount = order.getTotalAmount();

        BigDecimal advanceAmount = totalAmount
                .multiply(new BigDecimal("0.50"))
                .setScale(2, RoundingMode.HALF_UP);

        BigDecimal remainingAmount = totalAmount
                .subtract(advanceAmount)
                .setScale(2, RoundingMode.HALF_UP);

        Payment payment = new Payment();

        payment.setOrder(order);
        payment.setTotalAmount(totalAmount);
        payment.setAdvanceAmount(advanceAmount);
        payment.setRemainingAmount(remainingAmount);
        payment.setStatus(PaymentStatus.PENDING);

        LocalDateTime now = LocalDateTime.now();

        payment.setCreatedAt(now);
        payment.setUpdatedAt(now);

        Payment savedPayment = paymentRepository.save(payment);

        return toResponse(savedPayment);
    }

    @Transactional
    public PaymentResponse markAdvancePaid(Long orderId) {

        Payment payment = paymentRepository.findByOrderId(orderId)
                .orElseThrow(() ->
                        new IllegalStateException(
                                "Payment not found for order: " + orderId));

        if (payment.getStatus() == PaymentStatus.ADVANCE_PAID) {
            throw new IllegalStateException(
                    "Advance payment is already marked as paid");
        }

        if (payment.getStatus() == PaymentStatus.FULLY_PAID) {
            throw new IllegalStateException(
                    "Payment is already fully paid");
        }

        if (payment.getStatus() != PaymentStatus.PENDING) {
            throw new IllegalStateException(
                    "Advance payment cannot be recorded from status: "
                            + payment.getStatus());
        }

        payment.setStatus(PaymentStatus.ADVANCE_PAID);
        payment.setUpdatedAt(LocalDateTime.now());

        Payment savedPayment = paymentRepository.save(payment);

        return toResponse(savedPayment);
    }

    @Transactional(readOnly = true)
    public PaymentResponse getPaymentByOrderId(Long orderId) {

        Payment payment = paymentRepository.findByOrderId(orderId)
                .orElseThrow(() ->
                        new IllegalStateException(
                                "Payment not found for order: " + orderId));

        return toResponse(payment);
    }

    @Transactional(readOnly = true)
    public boolean isAdvancePaid(Long orderId) {

        Payment payment = paymentRepository.findByOrderId(orderId)
                .orElse(null);

        if (payment == null) {
            return false;
        }

        return payment.getStatus() == PaymentStatus.ADVANCE_PAID
                || payment.getStatus() == PaymentStatus.FULLY_PAID;
    }

    private PaymentResponse toResponse(Payment payment) {

        PaymentResponse response = new PaymentResponse();

        response.setId(payment.getId());
        response.setOrderId(payment.getOrder().getId());
        response.setTotalAmount(payment.getTotalAmount());
        response.setAdvanceAmount(payment.getAdvanceAmount());
        response.setRemainingAmount(payment.getRemainingAmount());
        response.setStatus(payment.getStatus());
        response.setCreatedAt(payment.getCreatedAt());
        response.setUpdatedAt(payment.getUpdatedAt());

        return response;
    }
}