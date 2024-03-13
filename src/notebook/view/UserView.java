package notebook.view;

import notebook.controller.UserController;
import notebook.model.User;
import notebook.util.Commands;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class UserView {
    private final UserController userController;

    public UserView(UserController userController) {
        this.userController = userController;
    }

    public void run() {
        Commands com;

        while (true) {

            System.out.println("Список доступных команд:");
            System.out.println(Arrays.toString(Commands.values()));
            String command = prompt("Введите команду: ");
            com = Commands.valueOf(command.toUpperCase());
            if (com == Commands.EXIT) return;
            switch (com) {
                case CREATE:
                    User u = createUser();
                    userController.saveUser(u);
                    break;
                case READ:
                    String id = prompt("Идентификатор пользователя: ");
                    try {
                        User user = userController.readUser(Long.parseLong(id));
                        System.out.println(user);
                        System.out.println();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    break;
                case UPDATE:
                    String userId = prompt("Enter user id: ");
                    userController.updateUser(userId, createUser());
                    break;
                case DELETE:
                    String idForDelete = prompt("Введите ID контакта для удаления:");
                    userController.deleteUser(Long.parseLong(idForDelete));
                    break;
                case READALL:
                    System.out.println(userController.allUsers());
                    break;
                case FINDBYID:
                    String idForFinding = prompt("Введите ID контакта");
                    userController.findById(Long.parseLong(idForFinding));
                    break;
                case LIST:
                    System.out.println("Список доступных команд:");
                    System.out.println(Arrays.toString(Commands.values()));
                    break;
//                case CLEAR:
//                    userController.clear();
                default:
                    System.out.println("Введена неверная команда.");
                    break;
            }
        }
    }

    private String prompt(String message) {
        Scanner in = new Scanner(System.in);
        System.out.print(message);
        return in.nextLine();
    }
    public String checkLine(String str) {
        str = str.trim().replace(" ", "");
        if (!str.isEmpty()) {
            return str;
        } else {
            System.out.println("Значение не может быть пустым.\n");
            str = prompt("Введите корректные данные: ");
            return checkLine(str);
        }
    }
    private User createUser() {
        String firstName = checkLine(prompt("Имя: "));
        String lastName = checkLine(prompt("Фамилия: "));
        String phone = checkLine(prompt("Номер телефона: "));
        return new User(firstName, lastName, phone);
    }
}
