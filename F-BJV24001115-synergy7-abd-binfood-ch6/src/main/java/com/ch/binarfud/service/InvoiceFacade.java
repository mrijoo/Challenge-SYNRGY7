package com.ch.binarfud.service;

import java.util.UUID;

import com.ch.binarfud.model.User;

import net.sf.jasperreports.engine.JasperPrint;

public interface InvoiceFacade {
    JasperPrint generateInvoice(UUID orderId, User userId);
}
