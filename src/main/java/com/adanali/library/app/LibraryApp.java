package com.adanali.library.app;

import com.adanali.library.model.Librarian;
import com.adanali.library.model.Student;
import com.adanali.library.model.User;
import com.adanali.library.service.LibraryService;
import com.adanali.library.util.ConsoleUtil;

import java.time.LocalDate;

public class LibraryApp {
    private static final LibraryService library = new LibraryService();
    private static User currentUser;

    public static void main(String[] args){
        boolean reRun = true;
        do {
            ConsoleUtil.clearConsole();
            loginScreen();
            if (currentUser == null){
                ConsoleUtil.printNewLines(2);
                System.out.println("Wanna Retry?");
                System.out.println("Yes - Enter \"y\"");
                System.out.println("No - Enter any key");
                if (!ConsoleUtil.inputString("Enter").equals("y")) {
                    reRun = false;
                }
            }else {
                homeScreen(currentUser);
            }
        }while (reRun);
    }

    private static void loginScreen(){
        ConsoleUtil.printLibraryHeader();
        System.out.println("- Login -");
        ConsoleUtil.printNewLines(2);
        String email = ConsoleUtil.inputString("Enter E-mail");
        String password = ConsoleUtil.inputString("Enter Password");

        currentUser = library.login(email,password).orElse(null);
        ConsoleUtil.delay(800);
    }

    private static void homeScreen(User user){
        boolean exit = false;
        while (!exit){
            ConsoleUtil.printLibraryHeader();
            ConsoleUtil.printWelcomeUser(currentUser.getName());
            if (user instanceof Librarian){
                System.out.println("1 - My Profile");
                System.out.println("2 - Manage Users");
                System.out.println("3 - Manage Books");
                System.out.println("4 - Borrowing Service");
                System.out.println("5 - Manage Borrowing Records");
                System.out.println("6 - Logout");
                System.out.println("7 - Exit");

                String choice = ConsoleUtil.inputString("Enter your choice");
                switch (choice){
                    case "1" :
                        myProfile();
                        break;
                    case "2" :
                        manageUsers();
                        break;
                    case "3" :
                        manageBooks();
                        break;
                    case "4" :
                        borrowingService();
                        break;
                    case "5":
                        seeBorrowingRecords();
                        break;
                    case "6" :
                        currentUser = null;
                        exit = true;
                        break;
                    case "7":
                        currentUser = null;
                        System.exit(0);
                        break;
                    default:
                        System.out.println("Invalid choice!");
                        ConsoleUtil.delay(1000);
                }
            }else if (user instanceof Student){
                System.out.println("1 - My profile");
                System.out.println("2 - Search Books");
                System.out.println("3 - Manage My Borrowings");
                System.out.println("4 - Logout");
                System.out.println("5 - Exit");

                String choice = ConsoleUtil.inputString("Enter your choice");
                switch (choice){
                    case "1" :
                        myProfile();
                        break;
                    case "2" :
                        searchBooksOptions();
                        break;
                    case "3" :
                        library.printBorrowingsByUser(currentUser.getEmail());
                        ConsoleUtil.delay(2000);
                        break;
                    case "4" :
                        exit = true;
                        currentUser = null;
                        break;
                    case "5":
                        currentUser = null;
                        System.exit(0);
                        break;
                    default:
                        System.out.println("Invalid choice!");
                        ConsoleUtil.delay(1000);
                }

            }
        }
    }

    private static void myProfile(){
        boolean exit = false;
        while (!exit){
            ConsoleUtil.printLibraryHeader();
            ConsoleUtil.printWelcomeUser(currentUser.getName());
            System.out.println("Your Role : "+currentUser.getRole());
            ConsoleUtil.printNewLines(1);

            System.out.println("1 - Change User Name");
            System.out.println("2 - Change Password");
            System.out.println("3 - Go Back");

            String choice = ConsoleUtil.inputString("Enter your choice");
            switch (choice){
                case "1":
                    String newUserName = ConsoleUtil.inputString("Enter new Username");
                    library.updateUserName(currentUser.getEmail(), newUserName);
                    ConsoleUtil.delay(500);
                    break;
                case "2":
                    while (true){
                        String passwordToCheck = ConsoleUtil.inputString("Enter current password");
                        if (passwordToCheck.equals(currentUser.getPassword())){
                            String newPassword = ConsoleUtil.inputString("Enter new password");
                            if (newPassword.equals(passwordToCheck)){
                                System.out.println("Password is same!");
                            }else {
                                library.updateUserPassword(currentUser.getEmail(), newPassword);
                            }
                            break;
                        }else{
                            System.out.println("Password does not match! try again...");
                        }
                    }
                    ConsoleUtil.delay(500);
                    break;
                case "3":
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid choice!");
                    ConsoleUtil.delay(800);
            }
        }
    }

    private static void manageUsers(){
        boolean exit = false;
        while (!exit){
            ConsoleUtil.printLibraryHeader();
            ConsoleUtil.printWelcomeUser(currentUser.getName());

            System.out.println("1 - Add Student");
            System.out.println("2 - Remove Student");
            System.out.println("3 - List of Users");
            System.out.println("4 - Search a User");
            System.out.println("5 - Go Back");

            String choice = ConsoleUtil.inputString("Enter your choice");
            switch (choice){
                case "1":
                    addStudentPage();
                    break;
                case "2":
                    removeStudentPage();
                    break;
                case "3":
                    userListPage();
                    break;
                case "4":
                    userSearchPage();
                    break;
                case "5":
                    exit=true;
                    break;
                default:
                    System.err.println("Invalid Input! Try Again...");
            }
        }
    }

    private static void addStudentPage(){
           library.registerStudent(ConsoleUtil.inputString("Enter the name of student"),
                    ConsoleUtil.inputString("Enter the email of student"),
                    ConsoleUtil.inputString("Enter the password of student"),
                    ConsoleUtil.inputString("Enter the address of student"));
           ConsoleUtil.delay(500);
    }

    private static void removeStudentPage(){
        String emailOfTheStudentToRemove = ConsoleUtil.inputString("Enter the email of the Student");
        library.removeStudent(emailOfTheStudentToRemove);
        ConsoleUtil.delay(500);
    }

    private static void userListPage(){
        boolean reRun = true;
        while (reRun){
            ConsoleUtil.printLibraryHeader();
            System.out.println("1 - Librarians");
            System.out.println("2 - Students");
            System.out.println("3 - Go Back");
            String innerChoice = ConsoleUtil.inputString("Enter your choice");
            switch (innerChoice){
                case "1":
                    ConsoleUtil.printLibraryHeader();
                    library.printAllLibrarians();
                    ConsoleUtil.delay(2000);
                    break;
                case "2":
                    ConsoleUtil.printLibraryHeader();
                    library.printAllStudents();
                    ConsoleUtil.delay(2000);
                    break;
                case "3":
                    reRun = false;
                    break;
                default:
                    System.err.println("Invalid Choice! Retry...");
            }
        }
    }

    private static void userSearchPage(){
        String searchQuery = ConsoleUtil.inputString("Enter search query");
        ConsoleUtil.printLibraryHeader();
        library.searchForUser(searchQuery);
        ConsoleUtil.delay(2000);
    }

    private static void manageBooks(){
        boolean exit = false;
        while (!exit){
            ConsoleUtil.printLibraryHeader();
            ConsoleUtil.printWelcomeUser(currentUser.getName());

            System.out.println("1- Add a book");
            System.out.println("2- Remove a book");
            System.out.println("3- Update a book");
            System.out.println("4- List all books");
            System.out.println("5- Search a book");
            System.out.println("6- Go back");

            String userChoice = ConsoleUtil.inputString("Enter your choice");

            switch (userChoice) {
                case "1":
                    addBookPage();
                    break;
                case "2":
                    removeBookPage();
                    break;
                case "3":
                    updateBookPage();
                    break;
                case "4":
                    ConsoleUtil.printLibraryHeader();
                    library.printAllBooks();
                    ConsoleUtil.delay(2000);
                    break;
                case "5":
                    searchBooksOptions();
                    break;
                case "6":
                    exit = true;
                    break;
                default:
            }
        }
    }

    public static void addBookPage(){
        library.addNewBook(ConsoleUtil.inputString("Enter the ISBN"),
                ConsoleUtil.inputString("Enter the Title"),
                ConsoleUtil.inputString("Enter Author name"),
                ConsoleUtil.inputString("Enter Genre"),
                ConsoleUtil.inputDate("Enter the Publication Date"),
                ConsoleUtil.inputInteger("Enter the number of copies being added"));
        ConsoleUtil.delay(500);
    }

    public static void removeBookPage(){
        String isbnOfTheBookToRemove = ConsoleUtil.inputString("Enter the ISBN of the book to remove");
        library.removeABook(isbnOfTheBookToRemove);
        ConsoleUtil.delay(500);
    }

    public static void updateBookPage(){
        boolean reRun = true;
        while (reRun) {
            ConsoleUtil.printLibraryHeader();
            System.out.println("1 - Update Book's Title");
            System.out.println("2 - Update Book's Author");
            System.out.println("3 - Update Book's Genre");
            System.out.println("4 - Update Book's Publication Date");
            System.out.println("5 - Update Book's Quantity");
            System.out.println("6 - Go Back");

            String innerChoice = ConsoleUtil.inputString("Enter your choice");
            switch (innerChoice) {
                case "1":
                    updateBookTitlePage();
                    break;
                case "2":
                    updateBookAuthorPage();
                    break;
                case "3":
                    updateBookGenrePage();
                    break;
                case "4":
                    updateBookPublicationDatePage();
                    break;
                case "5":
                    updateBookQuantityPage();
                    break;
                case "6":
                    reRun = false;
                    break;
                default:
                    System.err.println("Invalid Input! Try Again...");
            }
        }
    }

    public static void updateBookTitlePage(){
        String isbnOfTheBookToUpdate = ConsoleUtil.inputString("Enter the isbn of book");
        String updatedTitle = ConsoleUtil.inputString("Enter the updated Title");
        library.updateBookTitle(isbnOfTheBookToUpdate, updatedTitle);
        ConsoleUtil.delay(500);
    }

    public static void updateBookAuthorPage(){
        String isbnOfTheBookToUpdate = ConsoleUtil.inputString("Enter the isbn of book");
        String updatedAuthor = ConsoleUtil.inputString("Enter the updated Author Name");
        library.updateBookAuthor(isbnOfTheBookToUpdate, updatedAuthor);
        ConsoleUtil.delay(500);
    }

    public static void updateBookGenrePage(){
        String isbnOfTheBookToUpdate = ConsoleUtil.inputString("Enter the isbn of book");
        String updatedGenre = ConsoleUtil.inputString("Enter the updated Genre");
        library.updateBookGenre(isbnOfTheBookToUpdate, updatedGenre);
        ConsoleUtil.delay(500);
    }

    public static void updateBookPublicationDatePage(){
        String isbnOfTheBookToUpdate = ConsoleUtil.inputString("Enter the isbn of book");
        LocalDate updatedPublicationDate = ConsoleUtil.inputDate("Enter the updated Publication Date");
        library.updateBookPublicationDate(isbnOfTheBookToUpdate, updatedPublicationDate);
        ConsoleUtil.delay(500);
    }

    public static void updateBookQuantityPage(){
        boolean exit = false;
        while (!exit){
            ConsoleUtil.printLibraryHeader();
            System.out.println("1 - Increment Book Quantity");
            System.out.println("2 - Decrement Book Quantity");
            System.out.println("3 - Go Back");

            String innerChoice = ConsoleUtil.inputString("Enter your choice");
            String isbnOfTheBookToUpdate;
            int variationValue;
            switch (innerChoice){
                case "1":
                    isbnOfTheBookToUpdate = ConsoleUtil.inputString("Enter the isbn of book");
                    variationValue = ConsoleUtil.inputInteger("Increment in Book's quantity");
                    library.incrementBookQuantity(isbnOfTheBookToUpdate,variationValue);
                    ConsoleUtil.delay(500);
                    break;
                case "2":
                    isbnOfTheBookToUpdate = ConsoleUtil.inputString("Enter the isbn of book");
                    variationValue = ConsoleUtil.inputInteger("Decrement in Book's quantity");
                    library.decrementBookQuantity(isbnOfTheBookToUpdate,variationValue);
                    ConsoleUtil.delay(500);
                    break;
                case "3":
                    exit = true;
                    break;
                default:
                    System.err.println("Invalid Input! Try Again...");
            }
        }
    }

    public static void searchBooksOptions(){
        boolean exit = false;
        while (!exit) {
            ConsoleUtil.printLibraryHeader();
            System.out.println("1 - Search Overall");
            System.out.println("2 - Search by title");
            System.out.println("3 - Search by Author Name");
            System.out.println("4 - Search by Genre");
            System.out.println("5 - Go Back");

            String innerChoice = ConsoleUtil.inputString("Enter your choice");
            String searchQuery;
            switch (innerChoice) {
                case "1":
                    searchQuery = ConsoleUtil.inputString("Enter search query");
                    ConsoleUtil.printLibraryHeader();
                    library.searchForBookOverall(searchQuery);
                    ConsoleUtil.delay(2000);
                    break;
                case "2":
                    searchQuery = ConsoleUtil.inputString("Enter search query");
                    ConsoleUtil.printLibraryHeader();
                    library.searchForBookByTitle(searchQuery);
                    ConsoleUtil.delay(2000);
                    break;
                case "3":
                    searchQuery = ConsoleUtil.inputString("Enter search query");
                    ConsoleUtil.printLibraryHeader();
                    library.searchForBookByAuthor(searchQuery);
                    ConsoleUtil.delay(2000);
                    break;
                case "4":
                    searchQuery = ConsoleUtil.inputString("Enter search query");
                    ConsoleUtil.printLibraryHeader();
                    library.searchForBookByGenre(searchQuery);
                    ConsoleUtil.delay(2000);
                    break;
                case "5":
                    exit = true;
                    break;
                default:
                    System.err.println("Invalid Input! Try Again...");
            }
        }
    }

    private static void borrowingService(){
        boolean exit = false;
        while (!exit){
            ConsoleUtil.printLibraryHeader();
            ConsoleUtil.printWelcomeUser(currentUser.getName());

            System.out.println("1 - Add a borrowed book");
            System.out.println("2 - Add a returned book");
            System.out.println("3 - Add paid fine");
            System.out.println("4 - Go Back");

            String choice = ConsoleUtil.inputString("Enter your choice");
            switch (choice){
                case "1":
                    String emailOfThePotentialBorrower = ConsoleUtil.inputString("Enter the email of borrower");
                    String isbnOfTheBookToBorrow = ConsoleUtil.inputString("Enter the isbn of book");
                    library.addBorrowedBook(emailOfThePotentialBorrower,isbnOfTheBookToBorrow);
                    ConsoleUtil.delay(500);
                    break;
                case "2":
                    String emailOfTheBorrower = ConsoleUtil.inputString("Enter the email of borrower");
                    String isbnOfBorrowedBook = ConsoleUtil.inputString("Enter the isbn of book");
                    library.addReturnedBook(emailOfTheBorrower,isbnOfBorrowedBook);
                    ConsoleUtil.delay(500);
                    break;
                case "3":
                    String emailOfPayer = ConsoleUtil.inputString("Enter the email of Paying borrower");
                    int paymentAmount = ConsoleUtil.inputInteger("Enter the amount paid");
                    library.addPaidFine(emailOfPayer,paymentAmount);
                    ConsoleUtil.delay(500);
                    break;
                case "4":
                    exit = true;
                    break;
                default:
                    System.err.println("Invalid choice! Try again...");
            }
        }
    }

    private static void seeBorrowingRecords(){
        boolean exit = false;
        while (!exit){
            ConsoleUtil.printLibraryHeader();
            ConsoleUtil.printWelcomeUser(currentUser.getName());

            System.out.println("1 - print Borrowing records by Status");
            System.out.println("2 - print Borrowing records by User");
            System.out.println("3 - print Borrowing records by Book");
            System.out.println("4 - print all Borrowing Records");
            System.out.println("5 - Go Back");

            String choice = ConsoleUtil.inputString("Enter your choice");
            switch (choice){
                case "1":
                    boolean reRun = true;
                    while (reRun){
                        ConsoleUtil.printLibraryHeader();
                        System.out.println("1 - Active Borrowings");
                        System.out.println("2 - Returned Borrowings");
                        System.out.println("3 - Overdue Borrowings");
                        System.out.println("4 - Go Back");

                        String innerChoice = ConsoleUtil.inputString("Enter your choice");
                        switch (innerChoice){
                            case "1":
                                ConsoleUtil.printLibraryHeader();
                                library.printActiveBorrowings();
                                ConsoleUtil.delay(2000);
                                break;
                            case"2":
                                ConsoleUtil.printLibraryHeader();
                                library.printReturnedBorrowings();
                                ConsoleUtil.delay(2000);
                                break;
                            case"3":
                                ConsoleUtil.printLibraryHeader();
                                library.printOverdueBorrowings();
                                ConsoleUtil.delay(2000);
                                break;
                            case"4":
                                reRun = false;
                                break;
                            default:
                                System.out.println("Invalid Choice! Try again...");
                        }
                    }
                    break;
                case "2":
                    String email = ConsoleUtil.inputString("Enter User's Email");
                    ConsoleUtil.printLibraryHeader();
                    library.printBorrowingsByUser(email);
                    ConsoleUtil.delay(2000);
                    break;
                case "3":
                    String isbn = ConsoleUtil.inputString("Enter ISBN of Book");
                    ConsoleUtil.printLibraryHeader();
                    library.printBorrowingsByBook(isbn);
                    ConsoleUtil.delay(2000);
                    break;
                case "4":
                    ConsoleUtil.printLibraryHeader();
                    library.printAllBorrowingRecords();
                    ConsoleUtil.delay(2000);
                    break;
                case "5":
                    exit = true;
                    break;
                default:
            }
        }
    }
}