/**
 *
 * @author Audrey
 */
public class Book {
    
    private int id;
    private String title;
    private String name;
    private String genre;
    private String summary;
    private int numPages;
    
    
    public Book(int id, String title, String authorfname, String authorlname, String genre, String summary, int numPages){
        this.id = id;
        this.title = title;
        this.name = authorfname + " " + authorlname;
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
    
    public String getAuthor(){
        return this.name;
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
