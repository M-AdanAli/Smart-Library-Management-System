package com.adanali.library.repository;

import com.adanali.library.exceptions.EntityDuplicationException;
import com.adanali.library.model.Book;
import com.adanali.library.util.JsonStorageUtil;
import com.fasterxml.jackson.core.type.TypeReference;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class    BooksRepository implements RepositoryPattern<Book,String>{
    private Set<Book> bookSet;
    private final JsonStorageUtil<Book> bookJsonStorge;
    private static final String BOOKS_JSON_FILE_PATH = "src\\main\\java\\com\\adanali\\library\\persistence\\Books.json";

    public BooksRepository(){
        this.bookSet = new HashSet<>();
        bookJsonStorge = new JsonStorageUtil<>(BOOKS_JSON_FILE_PATH);
        loadBooks();
    }

    public void loadBooks(){
        try {
            bookSet = bookJsonStorge.loadData(new TypeReference<Set<Book>>() {
            });
        } catch (IOException e) {
            System.out.println(e.getMessage());
            if (e.getCause() != null) System.out.println(e.getCause().getMessage());
        }
    }

    public void saveBooks(){
        try {
            bookJsonStorge.saveData(bookSet);
        }catch (IOException e){
            System.out.println(e.getMessage());
        }
    }

    public enum SearchAttribute{
        TITLE, AUTHOR, GENRE, ALL
    }

    public List<Book> searchBooks(String query , SearchAttribute attribute){
        String queryLower = query.toLowerCase();

        return bookSet.stream()
                .filter(book -> switch (attribute) {
                    case AUTHOR -> book.getAuthor().toLowerCase().contains(queryLower);
                    case TITLE -> book.getTitle().toLowerCase().contains(queryLower);
                    case GENRE -> book.getGenre().toLowerCase().contains(queryLower);
                    case ALL -> book.getAuthor().toLowerCase().contains(queryLower) ||
                            book.getTitle().toLowerCase().contains(queryLower) ||
                            book.getGenre().toLowerCase().contains(queryLower);
                })
                .toList();
    }

    @Override
    public void add(Book book) throws EntityDuplicationException {
        if (getById(book.getIsbn()).isEmpty()){
            bookSet.add(book);
            saveBooks();
        }else throw new EntityDuplicationException(book.getClass(),"Book with ISBN ("+book.getIsbn()+") already exists!");
    }

    @Override
    public boolean remove(String isbn) {
        boolean isRemoved = bookSet.removeIf(book -> book.getIsbn().equals(isbn));
        if (isRemoved) saveBooks();
        return isRemoved;
    }

    @Override
    public Optional<Book> getById(String isbn) {
        return bookSet.stream().filter(book -> book.getIsbn().equals(isbn)).findFirst();
    }

    @Override
    public List<Book> getAll() {
        return List.copyOf(bookSet);
    }
}
