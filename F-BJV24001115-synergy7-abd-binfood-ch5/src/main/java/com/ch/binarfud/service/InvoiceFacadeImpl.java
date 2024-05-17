package com.ch.binarfud.service;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.sql.DataSource;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import com.ch.binarfud.dto.order.response.OrderResponseDto;
import com.ch.binarfud.dto.order.response.OrderResponseDto.OrderDetails;
import com.ch.binarfud.model.OrderDetail;
import com.ch.binarfud.model.User;

import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;

@Service
public class InvoiceFacadeImpl implements InvoiceFacade {

    private final DataSource dataSource;

    private final OrderFacade orderFacade;

    private final OrderDetailService orderDetailService;

    public InvoiceFacadeImpl(DataSource dataSource, OrderFacade orderFacade, OrderDetailService orderDetailService) {
        this.dataSource = dataSource;
        this.orderFacade = orderFacade;
        this.orderDetailService = orderDetailService;
    }

    private Connection getConnection() {
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public JasperPrint generateInvoice(UUID orderId, User user) {
        try {
            InputStream fileReport = new ClassPathResource("report/InvoiceReport.jasper").getInputStream();
            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(fileReport);

            OrderResponseDto order = orderFacade.getOrderById(orderId, user);
            List<OrderDetail> orderDetails = orderDetailService.getOrderDetailsByOrderId(orderId);

            Collection<OrderDetails> responseOrderDetails = orderDetails.stream().map(orderDetail -> {
                OrderDetails responseOrderDetail = new OrderDetails();
                responseOrderDetail.setProductId(orderDetail.getProductId());
                responseOrderDetail.setProductName(orderDetail.getProductName());
                responseOrderDetail.setPrice(orderDetail.getPrice());
                responseOrderDetail.setQuantity(orderDetail.getQuantity());
                return responseOrderDetail;
            }).collect(Collectors.toList());
            order.setDetails((List<OrderDetails>) responseOrderDetails);

            double totalPrice = orderDetails.stream()
                    .mapToDouble(orderDetail -> orderDetail.getPrice() * orderDetail.getQuantity())
                    .sum();

            Map<String, Object> params = new HashMap<>();
            params.put("order_id", orderId);
            params.put("total_price", totalPrice);
            return JasperFillManager.fillReport(jasperReport, params, getConnection());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
