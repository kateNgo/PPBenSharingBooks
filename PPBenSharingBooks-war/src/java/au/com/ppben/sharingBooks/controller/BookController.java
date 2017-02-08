package au.com.ppben.sharingBooks.controller;

import au.com.ppben.sharingBooks.MyUtility;
import au.com.ppben.sharingBooks.domain.Book;
import au.com.ppben.sharingBooks.domain.SubType;
import au.com.ppben.sharingBooks.domain.Type;
import au.com.ppben.sharingBooks.remote.BookBeanRemote;
import au.com.ppben.sharingBooks.remote.SubTypeBeanRemote;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import org.jboss.logging.Logger;
import au.com.ppben.sharingBooks.remote.TypeBeanRemote;

/**
 *
 * @author Phuong Ngo
 * <p>
 * This is backing bean for book . It has session scope
 */
@Named
@SessionScoped
public class BookController implements Serializable {

    Logger log = Logger.getLogger(this.getClass().getName());
    /**
     * Creates a new instance of BookBean with empty value
     */
    
    
    private Book book = new Book();
    private Part uploadedFile;
    @EJB
    private BookBeanRemote bookBean ;
    @EJB 
    private SubTypeBeanRemote subTypeBean;
    
    private static final String PDF_MINE_TYPE = "application/pdf";
    private String searchTerm;
    private int selectedSubType;
    /**
     * This is to inject member controller to in order to ensure the users must
     * login before accessing other pages
     */
    @Inject
    private AccountController accountController;
    
    /**
     * The getter method is to inject the backing bean accountController into
     * bookController
     *
     * @param mc
     */
    public void setAccountController(AccountController mc) {
        this.accountController = mc;
    }

    /**
     * This is default constructor
     */
    public BookController() {
    }

    /**
     * This method is to get book object
     *
     * @return Book object
     */
    public Book getBook() {
        return book;
    }

    /**
     * This is to set book object
     *
     * @param book
     */
    public void setBook(Book book) {
        this.book = book;
    }

    /**
     * This method is to get searchTerm value
     * @return string value of the search term
     */
    public String getSearchTerm() {
        return searchTerm;
    }

    /**
     * This method is to set value for searchTerm 
     * @param searchTerm 
     */
    public void setSearchTerm(String searchTerm) {
        this.searchTerm = searchTerm;
    }

    public int getSelectedSubType() {
        return selectedSubType;
    }

    public void setSelectedSubType(int selectedSubType) {
        this.selectedSubType = selectedSubType;
    }

    
    /**
     * This method is to get uploaded file
     *
     * @return Part
     */
    public Part getUploadedFile() {
        return uploadedFile;
    }

    /**
     * this method is to set value of uploaded file
     *
     * @param uploadedFile
     */
    public void setUploadedFile(Part uploadedFile) {
        this.uploadedFile = uploadedFile;
    }

    /**
     * This method is called when the user wants to list all books
     *
     * @return List the list of books
     */
    public List<Book> allBooks() {
        return bookBean.allBooks();
    }

    /**
     * This method is called when user wants to search book
     *
     * @return arrayList of books are found
     *
     */
    
    public List<Book> searchBooksByTitle() {
        log.info("search book by title: " + accountController.getAccount().getEmail());
        return bookBean.searchBooksByTitle(searchTerm);
    }

    /**
     * This method is to handle the deleting a book that she/he owns or Administrator
     * When book is deleted by Administrator, sms will be sent to user.
     *
     * @param id bookId
     *
     */
    public void delete(long id) {
        //Need to be check it is fine with location/ process at here.
       
        bookBean.delete(id);
    }

    /**
     * this method is called when the user wants to edit the book that he/she
     * owns 
     * @param id
     * @return String
     */
    public String editBook(int id) {
        book = bookBean.getBookbyId(id);
        selectedSubType = book.getSubType().getSubTypeId().intValue();
        return "/secure/editBook?faces-redirect=true";
    }

    /**
     * This method is called when user wants to save a editing book
     *
     * @return string next page after updating or edit page if failures
     */
    public String updateBook() {
        try {
            if (this.uploadedFile != null) {
                String fileName = upload();
                if (!MyUtility.isEmptyString(fileName)) {
                    book.setBookFile(fileName);

                }
            }
            setSubTypeBeforeSaving();
            bookBean.update(book);
             resetBook();

        } catch (IOException e) {
            log.error(e.getMessage());
            MyUtility.showError(e.getMessage());
            return "";
        }
        return "searchBooks?faces-redirect=true";
    }

    private void setSubTypeBeforeSaving(){
        SubType subType = subTypeBean.getSubType(selectedSubType);
            book.setSubType(subType);
    }
    /**
     * This method is called when user wants to add a new book
     *
     * @return String is name of the next page
     *
     */
    public String addBook() {

        log.info("adding book: " + accountController.getAccount().getEmail());
        try {

            // get Type
            setSubTypeBeforeSaving();
            String fileName = upload();
            if (!MyUtility.isEmptyString(fileName)) {
                book.setBookFile(fileName);
                book.setAccount(accountController.getAccount());
                bookBean.addNew(book);
                resetBook();
                return "searchBooks?faces-redirect=true";
            }
            MyUtility.showError("Canot add this book ");
            return "";

        } catch (IOException e) {
            log.error(e.getMessage());
            MyUtility.showError(e.getMessage());
            return "";
        }

    }

    /**
     * this method is to upload a file from client to server
     * <p>
     * the file will be saved in web\\uploadedFiles directory on server
     *
     * @throws IOException
     */
    private String upload() throws IOException {
        if (uploadedFile.getContentType().equals(PDF_MINE_TYPE)) {
            String realPath = getRealPath();
            String fileNameOnServer;
            try (InputStream inputStream = uploadedFile.getInputStream()) {
                fileNameOnServer = realPath + getFilename(uploadedFile);
                while ((new File(fileNameOnServer)).exists()) {
                    fileNameOnServer = fileNameOnServer.substring(0, fileNameOnServer.length() - 4) + "1.pdf";
                }
                try (FileOutputStream outputStream = new FileOutputStream(fileNameOnServer)) {
                    log.info("uploading file: " + getFilename(uploadedFile));
                    byte[] buffer = new byte[4096];
                    int bytesRead = 0;
                    while (true) {
                        bytesRead = inputStream.read(buffer);
                        if (bytesRead > 0) {
                            outputStream.write(buffer, 0, bytesRead);
                        } else {
                            break;
                        }
                    }
                }
            }
            log.info("upload success: " + fileNameOnServer);
            return fileNameOnServer;
        } else {
            MyUtility.showError("File uploading must be PDF file");
        }
        return "";

    }

    private String getFilename(Part part) {
        for (String cd : part.getHeader("content-disposition").split(";")) {
            if (cd.trim().startsWith("filename")) {
                String filename = cd.substring(cd.indexOf('=') + 1).trim().replace("\"", "");
                String file = filename.substring(filename.lastIndexOf('/') + 1).substring(filename.lastIndexOf('\\') + 1);
                log.info("file name: " + file);
                return file;
            }
        }
        return null;
    }

    /**
     * This method reads PDF from the server and writes it back as a response.
     *
     * @param fileName a String
     * @throws IOException
     */
    public void downloadPdf(String fileName) throws IOException {
        // Get the FacesContext
        FacesContext facesContext = FacesContext.getCurrentInstance();

        // Get HTTP response
        HttpServletResponse response = (HttpServletResponse) facesContext.getExternalContext().getResponse();

        // Set response headers
        response.reset();   // Reset the response in the first place
        response.setHeader("Content-Type", "application/pdf");  // Set only the content type

        // Open response output stream
        OutputStream responseOutputStream = response.getOutputStream();

        // Read PDF contents
        //URL url = new URL(get fileName);
        File file = new File(fileName);
        InputStream pdfInputStream = new FileInputStream(file);

        // Read PDF contents and write them to the output
        byte[] bytesBuffer = new byte[2048];
        int bytesRead;
        while ((bytesRead = pdfInputStream.read(bytesBuffer)) > 0) {
            responseOutputStream.write(bytesBuffer, 0, bytesRead);
        }

        // Make sure that everything is out
        responseOutputStream.flush();

        // Close both streams
        pdfInputStream.close();
        responseOutputStream.close();

        // JSF doc: 
        // Signal the JavaServer Faces implementation that the HTTP response for this request has already been generated 
        // (such as an HTTP redirect), and that the request processing lifecycle should be terminated
        // as soon as the current phase is completed.
        facesContext.responseComplete();

    }

    
    /**
     * This is private method get the real path of uploadedFiles folder
     *
     * @return String
     */
    private String getRealPath() {
        ServletContext ctx = (ServletContext) FacesContext.getCurrentInstance()
                .getExternalContext().getContext();
        return ctx.getRealPath("/") + "uploadedFiles/";

    }

    /**
     * This book is to create a new instant of book
     *
     */
    public void resetBook() {
        book = new Book();
    }

}
