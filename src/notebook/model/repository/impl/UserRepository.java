package notebook.model.repository.impl;

//import notebook.model.dao.impl.FileOperation;
import notebook.util.DBConnector;
import notebook.util.mapper.impl.UserMapper;
import notebook.model.User;
import notebook.model.repository.GBRepository;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserRepository implements GBRepository {
    private final UserMapper mapper;


    public UserRepository() {
        this.mapper = new UserMapper();

        try (FileWriter writer = new FileWriter(DBConnector.DB_PATH, true)) {
                writer.flush();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }


    @Override
    public List<User> findAll() {
        List<String> lines = readAll();
        List<User> users = new ArrayList<>();
        for (String line : lines) {
            users.add(mapper.toOutput(line));
        }
        return users;
    }

    @Override
    public User create(User user) {
        List<User> users = findAll();
        long max = 0L;
        for (User u : users) {
            long id = u.getId();
            if (max < id) {
                max = id;
            }
        }
        long next = max + 1;
        user.setId(next);
        users.add(user);
        write(users);
        return user;
    }

    @Override
    public User findById(Long id) {
        List<User> users = findAll();
        User user = users.stream()
                .filter(u -> u.getId()
                        .equals(id))
                .findFirst().orElseThrow(() -> new RuntimeException("User not found"));
        return user;
    }

    @Override
    public Optional<User> update(Long userId, User update) {
        List<User> users = findAll();
        User editUser = users.stream()
                .filter(u -> u.getId()
                        .equals(userId))
                .findFirst().orElseThrow(() -> new RuntimeException("User not found"));
        editUser.setFirstName(update.getFirstName());
        editUser.setLastName(update.getLastName());
        editUser.setPhone(update.getPhone());
        write(users);
        return Optional.of(update);
    }

    @Override
    public boolean delete(Long id) {
        List<User> users = findAll();
        User editUser = users.stream()
                .filter(u -> u.getId()
                        .equals(id))
                .findFirst().orElseThrow(() -> new RuntimeException("User not found"));
        System.out.println("Контакт: \n" + editUser + "\nудалён!");
        users.remove(editUser);
        for (int i = 0; i < users.size(); i++) {
            users.get(i).setId(Long.parseLong(String.valueOf(i+1)));
            users.get(i).setFirstName(users.get(i).getFirstName());
            users.get(i).setLastName(users.get(i).getLastName());
            users.get(i).setPhone(users.get(i).getPhone());
        }
        write(users);
        return true;
    }
   @Override

   public boolean clear() {List<User> users = new ArrayList<>();
        System.out.println("Контакты удалены!");
        write(users);
        return true;
    }


    private void write(List<User> users) {
        List<String> lines = new ArrayList<>();
        for (User u : users) {
            lines.add(mapper.toInput(u));
        }
        saveAll(lines);
    }
    @Override
    public List<String> readAll() {
        List<String> lines = new ArrayList<>();
        try {
            File file = new File(DBConnector.DB_PATH);
            //создаем объект FileReader для объекта File
            FileReader fr = new FileReader(file);
            //создаем BufferedReader с существующего FileReader для построчного считывания
            BufferedReader reader = new BufferedReader(fr);
            // считаем сначала первую строку
            String line = reader.readLine();
            if (line != null) {
                lines.add(line);
            }
            while (line != null) {
                // считываем остальные строки в цикле
                line = reader.readLine();
                if (line != null) {
                    lines.add(line);
                }
            }
            fr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lines;
    }

    @Override
    public void saveAll(List<String> data) {
        try (FileWriter writer = new FileWriter(DBConnector.DB_PATH, false)) {
            for (String line : data) {
                // запись всей строки
                writer.write(line);
                // запись по символам
                writer.append('\n');
            }
            writer.flush();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }


}
