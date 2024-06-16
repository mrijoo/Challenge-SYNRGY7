package com.ch.binarfud.service;

import java.util.UUID;

import net.sf.jasperreports.engine.JasperPrint;

public interface InvoiceFacade {
    JasperPrint generateInvoice(UUID orderId, UUID userId);
}
