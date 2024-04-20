package com.binfood;

import com.binfood.controller.OrderController;
import com.binfood.model.MenuItem;
import com.binfood.service.OrderService;
import com.binfood.utils.MessageUtils;
import com.binfood.utils.SystemUtils;
import com.binfood.view.OrderView;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Main {
    private static OrderService orderService = new OrderService();
    private static OrderView view = new OrderView();
    private static OrderController controller = new OrderController(orderService, view);
    private static Scanner scanner = new Scanner(System.in);
    private static boolean isNext = false;

    public static void main(String[] args) {
        while (!isNext) {
            try {
                SystemUtils.clearConsole();
                controller.displayMenu();

                int choice = scanner.nextInt();
                switch (choice) {
                    case 99:
                        handleConfirmation();
                        break;
                    case 0:
                        System.out.println("Terima kasih telah berkunjung!");
                        isNext = true;
                        break;
                    default:
                        handleMenuItemSelection(choice);
                }
            } catch (Exception e) {
                System.out.println(MessageUtils.INVALID_CHOICE_MESSAGE);
                System.out.println(MessageUtils.ENTER_TO_CONTINUE_MESSAGE);
                scanner.nextLine();
                scanner.nextLine();
            }
        }
        scanner.close();
    }

    private static void handleConfirmation() {
        SystemUtils.clearConsole();

        if (orderService.getOrder().getItems().isEmpty()) {
            System.out.println(MessageUtils.NOT_ORDERED);
            System.out.println(MessageUtils.ENTER_TO_CONTINUE_MESSAGE);
            scanner.nextLine();
            scanner.nextLine();
        } else {
            controller.displayOrder();
            System.out.println();
            System.out.println("""
                    1. Konfirmasi dan Bayar
                    2. Edit Pesanan
                    3. Kembali ke menu utama
                    """);
            System.out.print("=> ");

            int confirmChoice = scanner.nextInt();
            if (confirmChoice == 1) {
                controller.confirmAndPay();
                isNext = true;
            } else if (confirmChoice == 2) {
                controller.editOrder(false, null, null);
            } else {
                System.out.println(MessageUtils.INVALID_CHOICE_MESSAGE);
            }
        }
    }

    private static void handleMenuItemSelection(int choice) {
        SystemUtils.clearConsole();
        List<MenuItem> menuItems = orderService.getMenuItems();

        Optional<MenuItem> selectedMenuItemOptional = menuItems.stream()
                .filter(item -> menuItems.indexOf(item) + 1 == choice)
                .findFirst();

        selectedMenuItemOptional.ifPresent(selectedMenuItem -> {
            System.out.println(MessageUtils.FILL_QUANTITY);
            System.out.print("qty => ");
            int quantity = scanner.nextInt();

            if (quantity > 0) {
                controller.addOrderItem(choice, quantity);
            }
        });

        if (selectedMenuItemOptional.isEmpty()) {
            System.out.println(MessageUtils.INVALID_CHOICE_MESSAGE);
            System.out.println(MessageUtils.ENTER_TO_CONTINUE_MESSAGE);
            scanner.nextLine();
            scanner.nextLine();
        }
    }

}