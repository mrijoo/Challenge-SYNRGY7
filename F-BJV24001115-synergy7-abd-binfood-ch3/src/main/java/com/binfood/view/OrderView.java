package com.binfood.view;

import com.binfood.model.MenuItem;
import com.binfood.model.Order;
import com.binfood.utils.MessageUtils;
import com.binfood.utils.SystemUtils;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class OrderView {
    public void showMenu(List<MenuItem> menuItems) {
        System.out.println(MessageUtils.SEPARATOR);
        System.out.println("Selamat datang di BinarFud");
        System.out.println(MessageUtils.SEPARATOR + "\n");
        System.out.println("Silakan pilih makanan:");

        menuItems.forEach(item -> System.out.printf("%d. %s%n", menuItems.indexOf(item) + 1, item.getDetails()));

        System.out.println("99. Pesan dan Bayar");
        System.out.println("0. Keluar aplikasi\n");
        System.out.print("=> ");
    }

    public void showOrder(Order order) {
        System.out.println(MessageUtils.SEPARATOR + "");
        System.out.println("Konfirmasi & Pembayaran");
        System.out.println(MessageUtils.SEPARATOR);

        List<Map.Entry<MenuItem, Integer>> entries = new ArrayList<>(order.getItems().entrySet());
        Collections.reverse(entries); // Reverse the order of entries

        int totalItems = 0;
        int orderNumber = 1;
        for (Map.Entry<MenuItem, Integer> entry : entries) {
            MenuItem item = entry.getKey();
            Integer quantity = entry.getValue();
            System.out.printf("%d. %s\t%d\t%s%n", orderNumber++, item.getName(), quantity,
                    SystemUtils.formatPrice(item.getPrice() * quantity));
            totalItems += quantity;
        }
        System.out.println("-------------------------------+");
        System.out.printf("Total\t\t%d\t%s%n", totalItems, SystemUtils.formatPrice(order.getTotal()));
    }

    public void printReceipt(Order order) {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
        Path dir = Paths.get("receipts");

        try {
            if (!dir.toFile().exists()) {
                Files.createDirectory(dir);
            }

            Path filePath = dir.resolve("receipt_" + now.format(formatter) + ".txt");
            try (FileWriter receipt = new FileWriter(filePath.toString())) {
                receipt.write(MessageUtils.SEPARATOR + "\n");
                receipt.write("Binarfud\n");
                receipt.write(MessageUtils.SEPARATOR + "\n\n");
                receipt.write("Terima kasih sudah memesan\n");
                receipt.write("di BinarFud\n\n");
                receipt.write("Dibawah ini adalah pesanan anda\n\n");

                int totalItems = 0;
                for (Map.Entry<MenuItem, Integer> entry : order.getItems().entrySet()) {
                    MenuItem item = entry.getKey();
                    Integer quantity = entry.getValue();
                    receipt.write(String.format("%s\t%d\t%s%n", item.getName(), quantity,
                            SystemUtils.formatPrice(item.getPrice() * quantity)));
                    totalItems += quantity;
                }

                receipt.write("-------------------------------+\n");
                receipt.write(String.format("Total\t\t%d\t%s%n%n", totalItems,
                        SystemUtils.formatPrice(order.getTotal())));
                receipt.write("Pembayaran : BinarCash\n\n");
                receipt.write(MessageUtils.SEPARATOR + "\n");
                receipt.write("Simpan struk ini sebagai\n");
                receipt.write("bukti pembayaran\n");
                receipt.write(MessageUtils.SEPARATOR + "\n");
            }

            System.out.println("Struk pembayaran telah dicetak di " + filePath);
        } catch (IOException e) {
            SystemUtils.logError(e.getMessage());
            System.out.println("Error mencetak struk pembayaran. Silakan coba lagi");
        }
    }
}
