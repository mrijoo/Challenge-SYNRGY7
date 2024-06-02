package com.ch.binarfud.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import com.ch.binarfud.dto.order.request.OrderDto;
import com.ch.binarfud.dto.order.response.AllOrderResponseDto;
import com.ch.binarfud.dto.order.response.OrderResponseDto;
import com.ch.binarfud.model.Order;
import com.ch.binarfud.model.OrderDetail;
import com.ch.binarfud.model.Product;
import com.ch.binarfud.model.User;

@Service
public class OrderFacadeImpl implements OrderFacade {
    private final ModelMapper modelMapper;

    private final UserService userService;

    private final OrderService orderService;

    private final OrderDetailService orderDetailService;

    private ProductService productService;

    public OrderFacadeImpl(ModelMapper modelMapper, UserService userService, OrderService orderService,
            ProductService productService, OrderDetailService orderDetailService) {
        this.modelMapper = modelMapper;
        this.userService = userService;
        this.orderService = orderService;
        this.productService = productService;
        this.orderDetailService = orderDetailService;
    }

    public Page<AllOrderResponseDto> getAllOrderByUser(User user, int page, int size) {
        Page<Order> ordersPage = orderService.getAllOrderByUser(user, page, size);

        return ordersPage.map(order -> {
            AllOrderResponseDto allOrderResponseDto = modelMapper.map(order, AllOrderResponseDto.class);
            List<OrderDetail> orderDetails = orderDetailService.getOrderDetailsByOrderId(order.getId());
            List<AllOrderResponseDto.OrderDetails> responseOrderDetails = orderDetails.stream().map(orderDetail -> {
                AllOrderResponseDto.OrderDetails responseOrderDetail = modelMapper.map(orderDetail,
                        AllOrderResponseDto.OrderDetails.class);
                responseOrderDetail.setProductId(orderDetail.getProductId());
                responseOrderDetail.setProductName(orderDetail.getProductName());
                responseOrderDetail.setPrice(orderDetail.getPrice());
                responseOrderDetail.setQuantity(orderDetail.getQuantity());
                return responseOrderDetail;
            }).collect(Collectors.toList());
            allOrderResponseDto.setDetails(responseOrderDetails);

            double totalPrice = orderDetails.stream()
                    .mapToDouble(orderDetail -> orderDetail.getPrice() * orderDetail.getQuantity())
                    .sum();
            allOrderResponseDto.setTotalPrice(totalPrice);

            return allOrderResponseDto;
        });
    }

    @Override
    public OrderResponseDto getOrderById(UUID id, User user) {
        Order order = orderService.getOrderById(id);

        if (!order.getUserId().equals(user.getId())) {
            throw new AccessDeniedException("You are not authorized to access this order");
        }

        OrderResponseDto orderResponseDto = modelMapper.map(order, OrderResponseDto.class);
        List<OrderDetail> orderDetails = orderDetailService.getOrderDetailsByOrderId(order.getId());
        List<OrderResponseDto.OrderDetails> responseOrderDetails = orderDetails.stream().map(orderDetail -> {
            OrderResponseDto.OrderDetails responseOrderDetail = modelMapper.map(orderDetail,
                    OrderResponseDto.OrderDetails.class);
            responseOrderDetail.setProductId(orderDetail.getProductId());
            responseOrderDetail.setProductName(orderDetail.getProductName());
            responseOrderDetail.setPrice(orderDetail.getPrice());
            responseOrderDetail.setQuantity(orderDetail.getQuantity());
            return responseOrderDetail;
        }).collect(Collectors.toList());

        orderResponseDto.setDetails(responseOrderDetails);

        double totalPrice = orderDetails.stream()
                .mapToDouble(orderDetail -> orderDetail.getPrice() * orderDetail.getQuantity())
                .sum();

        orderResponseDto.setTotalPrice(totalPrice);

        return orderResponseDto;
    }

    public OrderResponseDto addOrder(User user, OrderDto orderDto) {
        Map<UUID, Double> sellerTotalPriceMap = new HashMap<>();
        double totalOrderPrice = 0.0;

        for (OrderDto.DetailOrder detailOrder : orderDto.getDetails()) {
            Product product = productService.getProductById(detailOrder.getProductId());

            double productTotalPrice = product.getPrice() * detailOrder.getQuantity();
            totalOrderPrice += productTotalPrice;
            UUID sellerId = product.getMerchant() != null ? product.getMerchant().getUserId() : null;

            sellerTotalPriceMap.put(sellerId, sellerTotalPriceMap.getOrDefault(sellerId, 0.0) + productTotalPrice);
        }

        if ("BALANCE".equals(orderDto.getPaymentMethod().toString()) && user.getBalance() < totalOrderPrice) {
            throw new IllegalArgumentException("Insufficient balance");
        }

        for (Map.Entry<UUID, Double> entry : sellerTotalPriceMap.entrySet()) {
            UUID sellerId = entry.getKey();
            Double totalPrice = entry.getValue();

            if (sellerId != null) {
                userService.transferBalance(user.getId(), sellerId, totalPrice);
            }
        }

        Order newOrder = modelMapper.map(orderDto, Order.class);
        newOrder.setUserId(user.getId());
        newOrder = orderService.addOrder(newOrder);

        List<OrderResponseDto.OrderDetails> responseOrderDetails = new ArrayList<>();

        for (OrderDto.DetailOrder detailOrder : orderDto.getDetails()) {
            OrderDetail orderDetail = modelMapper.map(detailOrder, OrderDetail.class);
            orderDetail.setOrderId(newOrder.getId());

            Product product = productService.getProductById(detailOrder.getProductId());

            orderDetail.setProductName(product.getName());
            orderDetail.setPrice(product.getPrice());

            responseOrderDetails.add(modelMapper.map(orderDetail, OrderResponseDto.OrderDetails.class));

            orderDetailService.addOrderDetail(orderDetail);
        }

        OrderResponseDto orderResponseDto = modelMapper.map(newOrder, OrderResponseDto.class);
        orderResponseDto.setTotalPrice(
                responseOrderDetails.stream().mapToDouble(detail -> detail.getPrice() * detail.getQuantity()).sum());
        orderResponseDto.setDetails(responseOrderDetails);

        return orderResponseDto;
    }

}
