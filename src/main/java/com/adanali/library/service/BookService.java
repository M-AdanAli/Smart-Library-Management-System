package com.adanali.library.service;

import com.adanali.library.exceptions.EntityDuplicationException;
import com.adanali.library.exceptions.EntityNotFoundException;
import com.adanali.library.model.Book;
import com.adanali.library.repository.BooksRepository;
import com.adanali.library.util.StringUtil;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class BookService {
    private final BooksRepository booksRepository;

    public BookService(){
        booksRepository = new BooksRepository();
    }

    public void addBook(String isbn,String title,String authorName,String genre,LocalDate publicationDate,int quantity) throws EntityDuplicationException {
        if (StringUtil.isValidIsbn(isbn)){
            StringUtil.validateNotNullOrBlank(title,"Title");
            StringUtil.validateNotNullOrBlank(authorName,"Author's name");
            StringUtil.validateNotNullOrBlank(genre,"Genre");
            if (quantity>0){
                booksRepository.add(new Book(isbn, title, authorName, genre, publicationDate, quantity));
            }else {
                throw new IllegalArgumentException("Quantity should be greater than 0");
            }
        }
    }

    public void removeBook(String isbn){
        if (StringUtil.isValidIsbn(isbn)){
            booksRepository.remove(isbn);
        }
    }

    public Optional<Book> getBookByIsbn(String isbn){
        if (StringUtil.isValidIsbn(isbn)){
            return booksRepository.getById(isbn);
        }return Optional.empty(); // Unreachable
    }

    public List<Book> listAllBooks(){
        return booksRepository.getAll();
    }

    public void updateBookTitle(String isbn, String newTitle) throws EntityNotFoundException {
        Book book = getBookByIsbn(isbn).orElseThrow(()-> new EntityNotFoundException("Book not found with ISBN : "+isbn));
        book.setTitle(newTitle);
        booksRepository.saveBooks();
    }

    public void updateBookAuthor(String isbn, String author) throws EntityNotFoundException {
        Book book = getBookByIsbn(isbn).orElseThrow(()->new EntityNotFoundException("Book not found with ISBN : "+isbn));
        book.setAuthor(author);
        booksRepository.saveBooks();
    }

    public void updateBookGenre(String isbn, String genre) throws EntityNotFoundException {
        Book book = getBookByIsbn(isbn).orElseThrow(()->new EntityNotFoundException("Book not found with ISBN : "+isbn));
        book.setGenre(genre);
        booksRepository.saveBooks();
    }

    public void updateBookPublicationDate(String isbn, LocalDate publicationDate) throws EntityNotFoundException {
        Book book = getBookByIsbn(isbn).orElseThrow(()->new EntityNotFoundException("Book not found with ISBN : "+isbn));
        book.setPublicationDate(publicationDate);
        booksRepository.saveBooks();
    }

    public void increaseBookQuantity(String isbn, int value) throws EntityNotFoundException {
        Book book = getBookByIsbn(isbn).orElseThrow(()->new EntityNotFoundException("Book not found with ISBN : "+isbn));
        if (value>0) {
            book.increaseQuantity(value);
            booksRepository.saveBooks();
        }else throw new IllegalArgumentException("Invalid increment value");
    }

    public void decreaseBookQuantity(String isbn, int value) throws EntityNotFoundException {
        Book book = getBookByIsbn(isbn).orElseThrow(()->new EntityNotFoundException("Book not found with ISBN : "+isbn));
        if (value>0) {
            book.decreaseQuantity(value);
            booksRepository.saveBooks();
        }else throw new IllegalArgumentException("Invalid decrement value");
    }

    public List<Book> searchBooks(String query , BooksRepository.SearchAttribute searchAttribute){
        StringUtil.validateNotNullOrBlank(query,"Search Query");
        return booksRepository.searchBooks(query,searchAttribute);
    }

    public BooksRepository getBooksRepository() {
        return booksRepository;
    }
}
