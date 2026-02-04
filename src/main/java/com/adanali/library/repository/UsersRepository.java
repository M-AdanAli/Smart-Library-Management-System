package com.adanali.library.repository;

import com.adanali.library.exceptions.EntityDuplicationException;
import com.adanali.library.model.User;
import com.adanali.library.util.JsonStorageUtil;
import com.fasterxml.jackson.core.type.TypeReference;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class UsersRepository implements RepositoryPattern<User,String>{
    private Set<User> userSet ;
    private final JsonStorageUtil<User> userJsonStorge;
    private static final String USERS_JSON_FILE_PATH = "src\\main\\java\\com\\adanali\\library\\persistence\\Users.json";

    public UsersRepository(){
        this.userSet = new HashSet<>();
        userJsonStorge = new JsonStorageUtil<>(USERS_JSON_FILE_PATH);
        loadUsers();
    }

    public void loadUsers(){
        try {
            userSet = userJsonStorge.loadData(new TypeReference<Set<User>>() {});
        } catch (IOException e) {
            System.out.println(e.getMessage());
            if (e.getCause() != null) System.out.println(e.getCause().getMessage());
        }
    }

    public void saveUsers(){
        try {
            userJsonStorge.saveData(userSet);
        }catch (IOException e){
            System.out.println(e.getMessage());
            System.out.println(e.getCause().getMessage());
        }
    }

    public List<User> search(String query){
        String finalQuery = query.toLowerCase();
        return userSet.stream()
                .filter(user -> user.getName().toLowerCase().contains(finalQuery) || user.getEmail().toLowerCase().contains(finalQuery))
                .toList();
    }

    @Override
    public void add(User user) throws EntityDuplicationException {
        if (getById(user.getEmail()).isEmpty()){
            userSet.add(user);
            saveUsers();
        }else throw new EntityDuplicationException(user.getClass(),"User with email "+user.getEmail()+" already exists!");
    }

    @Override
    public boolean remove(String email) {
        boolean isRemoved = userSet.removeIf(user -> user.getEmail().equals(email));
        if (isRemoved) saveUsers();
        return isRemoved;
    }

    @Override
    public Optional<User> getById(String email) {
        return userSet.stream().filter(user -> user.getEmail().equals(email)).findFirst();
    }

    @Override
    public List<User> getAll() {
        return List.copyOf(userSet);
    }
}
