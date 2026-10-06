package vanset_backend.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
import vanset_backend.exception.UnauthorizedResourceAccessException;
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
    private final PaymentService paymentService;

    public OrderService(
            OrderRepository orderRepository,
            OrderItemRepository orderItemRepository,
            UserRepository userRepository,
            ProductVariantRepository productVariantRepository,
            AddressRepository addressRepository,
            PaymentService paymentService) {

        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.userRepository = userRepository;
        this.productVariantRepository = productVariantRepository;
        this.addressRepository = addressRepository;
        this.paymentService = paymentService;
    }

    @Transactional
    public OrderResponse createOrder(OrderRequest request) {

        Long authenticatedUserId =
                getAuthenticatedUserId();

        User user =
                userRepository.findById(
                        authenticatedUserId
                ).orElseThrow(() ->
                        new UserNotFoundException(
                                authenticatedUserId
                        ));

        Address address =
                addressRepository
                        .findByIdAndUserId(
                                request.getAddressId(),
                                authenticatedUserId
                        )
                        .orElseThrow(() ->
                                new AddressNotFoundException(
                                        request.getAddressId()
                                ));

        Order order = new Order();

        order.setUser(user);
        order.setStatus(OrderStatus.PENDING);
        order.setCreatedAt(LocalDateTime.now());
        order.setTotalAmount(BigDecimal.ZERO);

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

        Order savedOrder =
                orderRepository.save(order);

        BigDecimal totalAmount =
                BigDecimal.ZERO;

        for (OrderItemRequest itemRequest :
                request.getItems()) {

            ProductVariant productVariant =
                    productVariantRepository
                            .findById(
                                    itemRequest
                                            .getProductVariantId()
                            )
                            .orElseThrow(() ->
                                    new ProductVariantNotFoundException(
                                            itemRequest
                                                    .getProductVariantId()
                                    ));

            int requestedQuantity =
                    itemRequest.getQuantity();

            if (requestedQuantity >
                    productVariant.getStockQuantity()) {

                throw new InsufficientStockException(
                        "Insufficient stock for variant: "
                                + productVariant.getId()
                );
            }

            BigDecimal price =
                    productVariant.getPrice();

            OrderItem orderItem =
                    new OrderItem();

            orderItem.setOrder(savedOrder);
            orderItem.setProductVariant(
                    productVariant
            );
            orderItem.setQuantity(
                    requestedQuantity
            );
            orderItem.setPrice(price);

            orderItemRepository.save(orderItem);

            productVariant.setStockQuantity(
                    productVariant.getStockQuantity()
                            - requestedQuantity
            );

            productVariantRepository.save(
                    productVariant
            );

            BigDecimal itemTotal =
                    price.multiply(
                            BigDecimal.valueOf(
                                    requestedQuantity
                            )
                    );

            totalAmount =
                    totalAmount.add(itemTotal);
        }

        savedOrder.setTotalAmount(
                totalAmount
        );

        Order finalOrder =
                orderRepository.save(savedOrder);

        return toResponse(finalOrder);
    }

    public OrderResponse getOrderById(Long id) {

        Long authenticatedUserId =
                getAuthenticatedUserId();

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        boolean isAdmin =
                authentication.getAuthorities()
                        .stream()
                        .anyMatch(authority ->
                                authority.getAuthority()
                                        .equals("ROLE_ADMIN")
                        );

        Order order;

        if (isAdmin) {

            order = orderRepository
                    .findById(id)
                    .orElseThrow(() ->
                            new OrderNotFoundException(id));

        } else {

            order = orderRepository
                    .findByIdAndUserId(
                            id,
                            authenticatedUserId
                    )
                    .orElseThrow(() ->
                            new UnauthorizedResourceAccessException(
                                    "You can only access your own orders"
                            ));
        }

        return toResponse(order);
    }

    @Transactional
    public OrderResponse updateOrderStatus(
            Long id,
            OrderStatus newStatus) {

        Order order =
                orderRepository.findById(id)
                        .orElseThrow(() ->
                                new OrderNotFoundException(id));

        OrderStatus currentStatus =
                order.getStatus();

        if (currentStatus == OrderStatus.PENDING
                && newStatus == OrderStatus.CONFIRMED
                && !paymentService.isAdvancePaid(id)) {

            throw new IllegalStateException(
                    "50% advance payment is required before order confirmation"
            );
        }

        if (!isValidTransition(
                currentStatus,
                newStatus)) {

            throw new IllegalArgumentException(
                    "Invalid order status transition: "
                            + currentStatus
                            + " -> "
                            + newStatus
            );
        }

        order.setStatus(newStatus);

        Order savedOrder =
                orderRepository.save(order);

        return toResponse(savedOrder);
    }

    private boolean isValidTransition(
            OrderStatus currentStatus,
            OrderStatus newStatus) {

        if (currentStatus == OrderStatus.PENDING) {

            return newStatus == OrderStatus.CONFIRMED
                    || newStatus == OrderStatus.CANCELLED;
        }

        if (currentStatus == OrderStatus.CONFIRMED) {

            return newStatus == OrderStatus.SHIPPED
                    || newStatus == OrderStatus.CANCELLED;
        }

        if (currentStatus == OrderStatus.SHIPPED) {

            return newStatus == OrderStatus.DELIVERED;
        }

        return false;
    }

    private Long getAuthenticatedUserId() {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        if (authentication == null ||
                !authentication.isAuthenticated()) {

            throw new IllegalStateException(
                    "User is not authenticated"
            );
        }

        try {

            return Long.parseLong(
                    authentication.getName()
            );

        } catch (NumberFormatException exception) {

            throw new IllegalStateException(
                    "Invalid authenticated user ID",
                    exception
            );
        }
    }

    private OrderResponse toResponse(
            Order order) {

        List<OrderItemResponse> items =
                orderItemRepository
                        .findByOrderId(
                                order.getId()
                        )
                        .stream()
                        .map(item ->
                                new OrderItemResponse(
                                        item.getId(),
                                        item.getProductVariant()
                                                .getId(),
                                        item.getQuantity(),
                                        item.getPrice()
                                )
                        )
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