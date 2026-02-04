package com.adanali.library.repository;

import com.adanali.library.dto.BorrowingRecordDTO;
import com.adanali.library.exceptions.EntityDuplicationException;
import com.adanali.library.model.Book;
import com.adanali.library.model.Borrower;
import com.adanali.library.model.BorrowingRecord;
import com.adanali.library.model.User;
import com.adanali.library.util.JsonStorageUtil;
import com.fasterxml.jackson.core.type.TypeReference;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class BorrowingRecordsRepository implements RepositoryPattern<BorrowingRecord, String>{
    private Set<BorrowingRecord> borrowingRecordSet;
    private final JsonStorageUtil<BorrowingRecordDTO> borrowingRecordsDTOJsonStorage;
    private final String BORROWING_RECORDS_JSON_FILE_PATH = "src\\main\\java\\com\\adanali\\library\\persistence\\BorrowingRecords.json";
    private final UsersRepository usersRepository;
    private final BooksRepository booksRepository;

    public BorrowingRecordsRepository(UsersRepository usersRepository, BooksRepository booksRepository){
        borrowingRecordSet = new HashSet<>();
        borrowingRecordsDTOJsonStorage = new JsonStorageUtil<>(BORROWING_RECORDS_JSON_FILE_PATH);
        this.usersRepository = usersRepository;
        this.booksRepository = booksRepository;
        loadBorrowingRecords();
    }

    public void loadBorrowingRecords(){
        try {
            Set<BorrowingRecordDTO> borrowingRecordsDTOSet = borrowingRecordsDTOJsonStorage.loadData(new TypeReference<Set<BorrowingRecordDTO>>() {});
            borrowingRecordSet = borrowingRecordsDTOSet.stream().map(this::fromDto).collect(Collectors.toSet());
        } catch (IOException e) {
            System.out.println(e.getMessage());
            if (e.getCause() != null) System.out.println(e.getCause().getMessage());
        }
    }

    public void saveBorrowingRecords(){
        try {
            Set<BorrowingRecordDTO> borrowingRecordsDTOSet = borrowingRecordSet.stream().map(this::toDto).collect(Collectors.toSet());
            borrowingRecordsDTOJsonStorage.saveData(borrowingRecordsDTOSet);
        }catch (IOException e){
            System.out.println(e.getMessage());
            if (e.getCause() != null) System.out.println(e.getCause().getMessage());
        }
    }

    private BorrowingRecordDTO toDto(BorrowingRecord borrowingRecord){
        if (borrowingRecord.getBorrower() instanceof User user){
            return new BorrowingRecordDTO(borrowingRecord.getRecordId(),
                    borrowingRecord.getBorrowedBook().getIsbn(),
                    user.getEmail(),
                    borrowingRecord.getBorrowDate(),
                    borrowingRecord.getDueDate(),
                    borrowingRecord.getReturnDate(),
                    borrowingRecord.getFine(),
                    borrowingRecord.getStatus());
        }else throw new IllegalStateException("Borrower is not a User"); // Unreachable
    }

    private BorrowingRecord fromDto(BorrowingRecordDTO dto) {
        Book book = booksRepository.getById(dto.getBookIsbn())
                .orElseThrow(() -> new IllegalStateException("Referenced book not found for ISBN: " + dto.getBookIsbn()));
        User user = usersRepository.getById(dto.getBorrowerEmail())
                .orElseThrow(() -> new IllegalStateException("Referenced user not found for E-mail: " + dto.getBorrowerEmail()));
        if (user instanceof Borrower borrower){
            return new BorrowingRecord(dto.getRecordId(), book, borrower, dto.getBorrowDate(), dto.getDueDate(),dto.getReturnDate(), dto.getFine());
        }else throw new IllegalStateException("Referenced user is not a Borrower: " + dto.getBorrowerEmail());
    }

    @Override
    public void add(BorrowingRecord borrowingRecord) throws EntityDuplicationException {
        if (getById(borrowingRecord.getRecordId()).isEmpty()){
            borrowingRecordSet.add(borrowingRecord);
            saveBorrowingRecords();
        }else throw new EntityDuplicationException(borrowingRecord.getClass(),"Borrowing record with Id ("+borrowingRecord.getRecordId()+") already exists!");
    }

    // May add functionality later to remove a record. I may delete the related records when removing a User permanently.
    @Override
    public boolean remove(String recordId) {
        boolean isRemoved = borrowingRecordSet.removeIf(borrowingRecord -> borrowingRecord.getRecordId().equals(recordId));
        if (isRemoved) saveBorrowingRecords();
        return isRemoved;
    }

    @Override
    public Optional<BorrowingRecord> getById(String recordId) {
        return borrowingRecordSet.stream().filter(borrowingRecord -> borrowingRecord.getRecordId().equals(recordId)).findFirst();
    }

    @Override
    public List<BorrowingRecord> getAll() {
        return List.copyOf(borrowingRecordSet);
    }
}
