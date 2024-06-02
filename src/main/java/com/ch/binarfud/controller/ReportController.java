package com.ch.binarfud.controller;

import java.util.UUID;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.ch.binarfud.model.User;
import com.ch.binarfud.service.InvoiceFacade;

import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperPrint;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@RestController
@RequestMapping("/api/v1/reports")
public class ReportController {
    private final InvoiceFacade invoiceFacade;

    public ReportController(InvoiceFacade invoiceFacade) {
        this.invoiceFacade = invoiceFacade;
    }

    @GetMapping("order/{orderId}")
    public void generateInvoice(@PathVariable UUID orderId, HttpServletResponse response,
            @AuthenticationPrincipal User user) {
        try {
            JasperPrint jasperPrint = invoiceFacade.generateInvoice(orderId, user);

            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment; filename=invoice.pdf");

            JasperExportManager.exportReportToPdfStream(jasperPrint, response.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Error generating invoice", e);
        }
    }
}
