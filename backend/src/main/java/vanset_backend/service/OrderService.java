package vanset_backend.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import vanset_backend.dto.OrderItemRequest;
import vanset_backend.dto.OrderItemResponse;
import vanset_backend.dto.OrderRequest;
import vanset_backend.dto.OrderResponse;
import vanset_backend.entity.Address;
import vanset_backend.entity.Order;
import vanset_backend.entity.OrderItem;
import vanset_backend.entity.OrderStatus;
import vanset_backend.entity.ProductVariant;
import vanset_backend.entity.User;
import vanset_backend.exception.AddressNotFoundException;
import vanset_backend.exception.InsufficientStockException;
import vanset_backend.exception.OrderNotFoundException;
import vanset_backend.exception.ProductVariantNotFoundException;
import vanset_backend.exception.UserNotFoundException;
import vanset_backend.repository.AddressRepository;
import vanset_backend.repository.OrderItemRepository;
import vanset_backend.repository.OrderRepository;
import vanset_backend.repository.ProductVariantRepository;
import vanset_backend.repository.UserRepository;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final UserRepository userRepository;
    private final ProductVariantRepository productVariantRepository;
    private final AddressRepository addressRepository;

    public OrderService(
            OrderRepository orderRepository,
            OrderItemRepository orderItemRepository,
            UserRepository userRepository,
            ProductVariantRepository productVariantRepository,
            AddressRepository addressRepository) {

        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.userRepository = userRepository;
        this.productVariantRepository = productVariantRepository;
        this.addressRepository = addressRepository;
    }

    @Transactional
    public OrderResponse createOrder(OrderRequest request) {

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() ->
                        new UserNotFoundException(request.getUserId()));

        Address address = addressRepository.findById(request.getAddressId())
                .orElseThrow(() ->
                        new AddressNotFoundException(request.getAddressId()));

        if (!address.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException(
                    "Address does not belong to this user");
        }

        Order order = new Order();

        order.setUser(user);
        order.setStatus(OrderStatus.PENDING);
        order.setCreatedAt(LocalDateTime.now());
        order.setTotalAmount(0);

        order.setDeliveryRecipientName(
                address.getRecipientName());

        order.setDeliveryPhone(
                address.getPhone());

        order.setDeliveryAddressLine(
                address.getAddressLine());

        order.setDeliveryCity(
                address.getCity());

        order.setDeliveryState(
                address.getState());

        order.setDeliveryPincode(
                address.getPincode());

        Order savedOrder = orderRepository.save(order);

        double totalAmount = 0;

        for (OrderItemRequest itemRequest : request.getItems()) {

            ProductVariant productVariant =
                    productVariantRepository
                            .findById(itemRequest.getProductVariantId())
                            .orElseThrow(() ->
                                    new ProductVariantNotFoundException(
                                            itemRequest.getProductVariantId()));

            int requestedQuantity = itemRequest.getQuantity();

            if (requestedQuantity > productVariant.getStockQuantity()) {
                throw new InsufficientStockException(
                        "Insufficient stock for variant: "
                                + productVariant.getId());
            }

            BigDecimal price =
                    BigDecimal.valueOf(productVariant.getPrice());

            OrderItem orderItem = new OrderItem();

            orderItem.setOrder(savedOrder);
            orderItem.setProductVariant(productVariant);
            orderItem.setQuantity(requestedQuantity);
            orderItem.setPrice(price);

            orderItemRepository.save(orderItem);

            productVariant.setStockQuantity(
                    productVariant.getStockQuantity()
                            - requestedQuantity
            );

            productVariantRepository.save(productVariant);

            totalAmount +=
                    productVariant.getPrice() * requestedQuantity;
        }

        savedOrder.setTotalAmount(totalAmount);

        Order finalOrder = orderRepository.save(savedOrder);

        return toResponse(finalOrder);
    }

    public OrderResponse getOrderById(Long id) {

        Order order = orderRepository.findById(id)
                .orElseThrow(() ->
                        new OrderNotFoundException(id));

        return toResponse(order);
    }

    private OrderResponse toResponse(Order order) {

        List<OrderItemResponse> items =
                orderItemRepository.findByOrderId(order.getId())
                        .stream()
                        .map(item -> new OrderItemResponse(
                                item.getId(),
                                item.getProductVariant().getId(),
                                item.getQuantity(),
                                item.getPrice()
                        ))
                        .toList();

        return new OrderResponse(
                order.getId(),
                order.getUser().getId(),
                order.getStatus(),
                order.getTotalAmount(),
                order.getCreatedAt(),
                order.getDeliveryRecipientName(),
                order.getDeliveryPhone(),
                order.getDeliveryAddressLine(),
                order.getDeliveryCity(),
                order.getDeliveryState(),
                order.getDeliveryPincode(),
                items
        );
    }
}