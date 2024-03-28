package com.binfood.controller;

import com.binfood.model.MenuItem;
import com.binfood.model.Order;
import com.binfood.service.OrderService;
import com.binfood.utils.MessageUtils;
import com.binfood.utils.SystemUtils;
import com.binfood.view.OrderView;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class OrderController {
    private OrderService orderService;
    private OrderView view;
    private Scanner scanner;

    public OrderController(OrderService orderService, OrderView view) {
        this.orderService = orderService;
        this.view = view;
        this.scanner = new Scanner(System.in);
    }

    public void displayMenu() {
        List<MenuItem> menuItems = orderService.getMenuItems();
        view.showMenu(menuItems);
    }

    public void addOrderItem(int index, int quantity) {
        List<MenuItem> menuItems = orderService.getMenuItems();
        if (index >= 1 && index <= menuItems.size()) {
            MenuItem item = menuItems.get(index - 1);
            orderService.addOrderItem(item, quantity);
        } else {
            System.out.println(MessageUtils.INVALID_CHOICE_MESSAGE);
        }
    }

    public void displayOrder() {
        Order order = orderService.getOrder();
        view.showOrder(order);
    }

    public void editOrder() {
        while (true) {
            SystemUtils.clearConsole();
            displayOrder();
            System.out.println("Pilih item untuk diedit (0 untuk kembali):");
            int itemChoice = scanner.nextInt();
            if (itemChoice == 0) {
                return;
            }

            List<String> itemNames = new ArrayList<>(orderService.getOrder().getItemMap().keySet());
            if (itemChoice > 0 && itemChoice <= itemNames.size()) {
                String itemName = itemNames.get(itemChoice - 1);

                System.out.println("""
                        1. Ubah jumlah
                        2. Hapus item
                        """);

                int editChoice = scanner.nextInt();
                if (editChoice == 1) {
                    System.out.println("Masukkan jumlah baru:");
                    int newQuantity = scanner.nextInt();
                    orderService.getOrder().getQuantityMap().put(itemName, newQuantity);
                } else if (editChoice == 2) {
                    orderService.getOrder().getItemMap().remove(itemName);
                    orderService.getOrder().getQuantityMap().remove(itemName);
                } else {
                    System.out.println(MessageUtils.INVALID_CHOICE_MESSAGE);
                }
            } else {
                System.out.println(MessageUtils.INVALID_CHOICE_MESSAGE);
                scanner.nextLine();
                scanner.nextLine();
            }
        }
    }

    public void confirmAndPay() {
        Order order = orderService.getOrder();
        view.printReceipt(order);
    }
}