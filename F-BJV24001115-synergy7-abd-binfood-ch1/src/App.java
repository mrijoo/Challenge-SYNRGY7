import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class App {
    private static void clearConsole() {
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                // new ProcessBuilder("clear").inheritIO().start().waitFor();

                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static String formatHarga(int harga) {
        return String.format("%,d", harga).replace(",", ".");
    }

    public static void main(String[] args) {
        try {
            Scanner scanner = new Scanner(System.in);
            int totalHarga = 0;
            int totalItem = 0;

            String[][] menuItems = {
                    { "Nasi Goreng", "15000" },
                    { "Mie Goreng", "13000" },
                    { "Nasi + Ayam", "18000" },
                    { "Es Teh Manis", "3000" },
                    { "Es Jeruk", "5000" }
            };

            int[] pesanan = new int[menuItems.length];

            int choice;
            do {
                clearConsole();
                System.out.println("==========================");
                System.out.println("Selamat datang di BinarFud");
                System.out.println("==========================");
                System.out.println("\nSilakan pilih makanan :");
                for (int i = 0; i < menuItems.length; i++) {
                    System.out.println(
                            (i + 1) + ". " + menuItems[i][0] + "\t| " + formatHarga(Integer.parseInt(menuItems[i][1])));
                }
                System.out.println("99. Pesan dan Bayar");
                System.out.println("0. Keluar");
                System.out.print("=> ");
                while (!scanner.hasNextInt()) {
                    System.out.println("Harap masukkan angka sesuai menu!");
                    System.out.print("=> ");
                    scanner.next();
                }
                choice = scanner.nextInt();

                if (choice >= 1 && choice <= menuItems.length) {
                    clearConsole();
                    System.out.println("==========================");
                    System.out.println("Berapa pesanan anda");
                    System.out.println("==========================");
                    System.out.print(
                            menuItems[choice - 1][0] + "\t| " + formatHarga(Integer.parseInt(menuItems[choice - 1][1]))
                                    + "\n(input 0 untuk kembali)\nqty => ");
                    while (!scanner.hasNextInt()) {
                        System.out.println("Harap masukkan jumlah pesanan yang valid!");
                        System.out.print("qty => ");
                        scanner.next();
                    }
                    int quantity = scanner.nextInt();
                    pesanan[choice - 1] += quantity;
                    totalHarga += quantity * Integer.parseInt(menuItems[choice - 1][1]);
                    totalItem += quantity;
                } else if (choice == 99) {
                    if (totalItem == 0) {
                        System.out.println("Anda belum memesan apapun!");
                        Thread.sleep(1000);
                        continue;
                    }

                    while (choice != 1 && choice != 2 && choice != 0) {
                        clearConsole();
                        System.out.println("==========================");
                        System.out.println("Konfirmasi & Pembayaran");
                        System.out.println("==========================\n");
                        System.out.println(templateStruk(menuItems, pesanan, totalItem, totalHarga, false));
                        System.out.println("\n1. Konfirmasi dan Bayar");
                        System.out.println("2. Kembali ke menu utama");
                        System.out.println("0. Keluar aplikasi");
                        System.out.print("=> ");
                        while (!scanner.hasNextInt()) {
                            System.out.println("Harap masukkan angka sesuai pilihan!");
                            System.out.print("=> ");
                            scanner.next();
                        }
                        choice = scanner.nextInt();

                        if (choice == 2 || choice == 0) {
                            break;
                        } else if (choice != 1 && choice != 2) {
                            clearConsole();
                            System.out.println("Pilihan tidak valid, silakan pilih sesuai pilihan yang tersedia!");
                            Thread.sleep(1000);
                        }
                    }
                    if (choice == 1) {
                        try {
                            LocalDateTime now = LocalDateTime.now();
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");

                            Path folder = Paths.get("struk");
                            if (!Files.exists(folder)) {
                                Files.createDirectory(folder);
                            }

                            FileWriter file = new FileWriter(
                                    folder.resolve("struk_" + now.format(formatter) + ".txt").toString());
                            file.write(templateStruk(menuItems, pesanan, totalItem, totalHarga, true));
                            file.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        clearConsole();
                        System.err.println(templateStruk(menuItems, pesanan, totalItem, totalHarga, true));
                        System.out.println("\nStruk telah disimpan sebagai bukti pembayaran!");
                        break;
                    }
                } else if (choice != 0) {
                    clearConsole();
                    System.out.println("Pilihan tidak valid, silakan pilih sesuai menu!");
                    Thread.sleep(1000);
                }
            } while (choice != 0);

            if (choice == 0) {
                System.out.println("Terima kasih telah berkunjung!");
            }
        } catch (Exception e) {
            clearConsole();
            System.out.println("Keluar dari aplikasi");
        }
    }

    private static String templateStruk(String[][] menuItems, int[] pesanan, int totalItem, int totalHarga,
            boolean isPrint) {
        StringBuilder struk = new StringBuilder();
        if (isPrint) {
            struk.append("==========================\n");
            struk.append("Binarfud\n");
            struk.append("==========================\n\n");
            struk.append("Terima kasih sudah memesan\n");
            struk.append("di BinarFud\n\n");
            struk.append("Dibawah ini adalah pesanan anda\n\n");
        }

        for (int i = 0; i < menuItems.length; i++) {
            if (pesanan[i] > 0) {
                struk.append(menuItems[i][0] + "\t" + pesanan[i] + "\t"
                        + formatHarga(pesanan[i] * Integer.parseInt(menuItems[i][1]))
                        + "\n");
            }
        }
        struk.append("-------------------------------+\n");
        struk.append("Total Harga\t" + totalItem + "\t" + formatHarga(totalHarga));
        if (isPrint) {
            struk.append("\nPembayaran : BinarCash\n\n");
            struk.append("==========================\n");
            struk.append("Simpan struk ini sebagai\n");
            struk.append("bukti pembayaran\n");
            struk.append("==========================\n");
        }

        return struk.toString();
    }
}
