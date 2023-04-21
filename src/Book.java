/**
 *
 * @author Audrey
 */
public class Book {
    
    private int id;
    private String title;
    private int author_id;
    private String genre;
    private String summary;
    private int numPages;
    
    
    public Book(int id, String title, int author_id, String genre, String summary, int numPages){
        this.id = id;
        this.title = title;
        this.author_id = author_id;
        this.genre = genre;
        this.summary = summary;
        this.numPages = numPages;
    }
    
    public int getID(){
        return this.id;
    }
    
    public String getTitle(){
        return this.title;
    }
    
    public int getAuthorID(){
        return this.author_id;
    }
    
    public String getGenre(){
        return this.genre;
    }
    
    public String getSummary(){
        return this.summary;
    }
    
    public int getNumPages(){
        return this.numPages;
    }
    
    
}
