package AdminPortal;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;

import utils.DBUtil;

public class AdminTasks {

	WebDriver wd=null;
	
	@Before
	public void init() {
		System.setProperty("webdriver.chrome.driver", "C:\\Users\\tanuj\\OneDrive\\Desktop\\tanu\\full stack\\Capstone Projects\\chromedriver_win32\\chromedriver.exe");
	    wd=new ChromeDriver();
	}
	
	//Register the admin
	@Test
	public void testA() throws InterruptedException {

		
		wd.get("http://localhost:8080");
        wd.manage().window().maximize();
        
        
        String actualHomemsg=wd.findElement(By.tagName("h1")).getText();
        String expectedHomemsg="Welcome to My Movie Plan, An Online Movie ticket Booking Web Application";
        
        assertEquals(expectedHomemsg,actualHomemsg);
        
        wd.findElement(By.linkText("Register")).click();
        
        Thread.sleep(4000);
        
        String actualRegister=wd.findElement(By.tagName("h1")).getText();
  		String expectedRegister="Register";
        
  		assertEquals(expectedRegister,actualRegister);
  		
  		wd.findElement(By.name("name")).sendKeys("admin2");
		wd.findElement(By.name("pwd")).sendKeys("asdfg");
		WebElement userType=wd.findElement(By.name("userType"));
		Select u1=new Select(userType);
		
		u1.selectByVisibleText("Admin");
		
		Thread.sleep(6000);
		
		wd.findElement(By.xpath("//button[text()='Register']")).click();
		
		String actualRegister2=wd.findElement(By.xpath("/html/body/h2")).getText();
		String expectedRegister2="User 'admin2' registered successfully";
		
		assertEquals(expectedRegister2,actualRegister2);
		
		Thread.sleep(6000);
		
		
}//End of testA()


	
	
	//TestB() to add Genre
	@Test
	public void testB() throws InterruptedException, ClassNotFoundException, SQLException {
               
		wd.get("http://localhost:8080");
        wd.manage().window().maximize();
        
        
        String actualHomemsg=wd.findElement(By.tagName("h1")).getText();
        String expectedHomemsg="Welcome to My Movie Plan, An Online Movie ticket Booking Web Application";
        
        assertEquals(expectedHomemsg,actualHomemsg);
        
        wd.findElement(By.linkText("Sign In")).click();
        
        Thread.sleep(2000);
        
        String actualSignIn=wd.findElement(By.xpath("/html/body/h1")).getText();
  		String expectedSignIn="Sign In";
  		
  		assertEquals(expectedSignIn,actualSignIn);
  		
  		wd.findElement(By.name("uname")).sendKeys("admin2");
		wd.findElement(By.name("upwd")).sendKeys("asdfg");
		Thread.sleep(4000);
		wd.findElement(By.tagName("button")).click();
	
		
		//Check if in the admin Dashboard
		Thread.sleep(2000);
		String actualAdminDboard=wd.findElement(By.tagName("h1")).getText();
		String expectedAdminDboard="Welcome admin2 to Admin Portal";
		
		assertEquals(expectedAdminDboard,actualAdminDboard);
            
		//Click on add genre link
		wd.findElement(By.partialLinkText("Add")).click();
		
		String actualAdd=wd.findElement(By.tagName("h1")).getText();
		String expectedAdd="Add Genres";
		
		assertEquals(expectedAdd,actualAdd);
		
		//Check if genre 'Science fiction' already exists in database
		Connection con=DBUtil.getConnection();
    	String sql="select * from genre where genre_name like ?";
    	PreparedStatement ps=con.prepareStatement(sql,ResultSet.TYPE_SCROLL_INSENSITIVE);
    	ps.setString(1, "%Science Fiction%");
    	ResultSet rs=ps.executeQuery();
    	
    	String expectedGenreAdd="Genre 'Science Fiction' added successfully";
    	
    		
    	
    	
    		while(rs.next()) {
    		 
    		 expectedGenreAdd="Genre 'Science Fiction' already exists in database";
    		 System.out.println("Genre Name: "+ rs.getString("genre_name"));
    		}
    	
    	
    	
    	
    	
		wd.findElement(By.name("genrename")).sendKeys("Science Fiction");
		wd.findElement(By.xpath("//button[text()='Add Genre']")).click();
		
		String actualGenreAdd=wd.findElement(By.tagName("h2")).getText();
		
		
    	assertEquals(expectedGenreAdd,actualGenreAdd);
        
    	
    	//Check in the database if genre added successfully
    	if(actualGenreAdd.contains("added successfully")) {
    	Connection con2=DBUtil.getConnection();
    	String sql2="select * from genre where genre_name=?";
    	PreparedStatement ps2=con2.prepareStatement(sql2,ResultSet.TYPE_SCROLL_INSENSITIVE);
    	ps2.setString(1, "Science Fiction");
    	ResultSet rs2=ps2.executeQuery();
    	String genreName="";
    
         while(rs2.next()) {
        	 
        	  genreName=rs2.getString("genre_name");
        	 
        	
         }
         assertEquals("Science Fiction",genreName);
             
	}//End of if
    	
    	
    	
	}//End of testB()
	
	
	
      //Test to Edit movie Details
	@Test
	public void testC() throws InterruptedException, ClassNotFoundException, SQLException {
		
		wd.get("http://localhost:8080");
        wd.manage().window().maximize();
        
        
        String actualHomemsg=wd.findElement(By.tagName("h1")).getText();
        String expectedHomemsg="Welcome to My Movie Plan, An Online Movie ticket Booking Web Application";
        
        assertEquals(expectedHomemsg,actualHomemsg);
        
        wd.findElement(By.linkText("Sign In")).click();
        
        Thread.sleep(2000);
        
        String actualSignIn=wd.findElement(By.xpath("/html/body/h1")).getText();
  		String expectedSignIn="Sign In";
  		
  		assertEquals(expectedSignIn,actualSignIn);
  		
  		wd.findElement(By.name("uname")).sendKeys("admin2");
		wd.findElement(By.name("upwd")).sendKeys("asdfg");
		Thread.sleep(4000);
		wd.findElement(By.tagName("button")).click();
	
		
		//Check if in the admin Dashboard
		Thread.sleep(2000);
		String actualAdminDboard=wd.findElement(By.tagName("h1")).getText();
		String expectedAdminDboard="Welcome admin2 to Admin Portal";
		
		assertEquals(expectedAdminDboard,actualAdminDboard);
         
		//Click on edit Movie Details
		wd.findElement(By.partialLinkText("Edit")).click();
		
		String actualEdit=wd.findElement(By.tagName("h1")).getText();
		String expectedEdit="Edit Details of Movies";
		
		assertEquals(expectedEdit,actualEdit);
		
		wd.findElement(By.name("movieId")).sendKeys("1");
		wd.findElement(By.xpath("//button[text()='Search Movie']")).click();
		
		String actualId=wd.findElement(By.xpath("/html/body/form[2]/b[1]")).getText();
		
		String expectedId="1";
		
		assertEquals(expectedId,actualId);
		
		wd.findElement(By.name("mname")).sendKeys("Koi Mil Gaya 2");
		wd.findElement(By.name("language")).sendKeys("Marathi");
		wd.findElement(By.name("description")).sendKeys("ghj");
		wd.findElement(By.name("showDate")).sendKeys("29082023");
		wd.findElement(By.name("ShowTime")).sendKeys("1515");
		wd.findElement(By.name("price")).sendKeys("80.5");
		
		wd.findElement(By.xpath("//button[text()='Edit Movie Details']")).click();
		Thread.sleep(5000);
		
        String actualEditmsg=wd.findElement(By.tagName("h2")).getText();
        String expectedEditmsg="Movie with id 1 edited successfully";
        
        assertEquals(expectedEditmsg,actualEditmsg);
        
        //check in the database if movie is edited
        Connection con=DBUtil.getConnection();
    	String sql="select * from movie2 where movie_id=?";
    	PreparedStatement ps=con.prepareStatement(sql,ResultSet.TYPE_SCROLL_INSENSITIVE);
    	ps.setString(1, "1");
    	ResultSet rs=ps.executeQuery();
    	
    	while(rs.next()) {
                
    		String moviename=rs.getString("name");
    		String description=rs.getString("description");
    		String language=rs.getString("language");
    		Date show_date=rs.getDate("show_date");
    		Time show_time=rs.getTime("show_time");
    		String price=rs.getString("ticketprice");
    		
    		DateFormat dateformat=new SimpleDateFormat("yyyy-MM-dd");
    		System.out.println("Date :"+ show_date);
    		String strDate=dateformat.format(show_date);
    		System.out.println("StrDate :"+ strDate);
    		
    		DateFormat timeformat=new SimpleDateFormat("hh:mm");
    		System.out.println("Time: "+ show_time);
    		String strTime=timeformat.format(show_time);
    		System.out.println("StrTime: "+ strTime);
    		
    		assertEquals("Koi Mil Gaya 2",moviename);
    		assertEquals("ghj",description);
    		assertEquals("Marathi",language);
    		assertEquals("2023-08-29",strDate);
    		assertEquals("03:15",strTime);
    		assertEquals("80.5",price);
    		
    	}
    	
    
    	
    	} //End of testC()
	
	
	//Test for enable or disable a movie show
	@Test
	public void testD() throws InterruptedException, ClassNotFoundException, SQLException {
	
		wd.get("http://localhost:8080");
        wd.manage().window().maximize();
        
        
        String actualHomemsg=wd.findElement(By.tagName("h1")).getText();
        String expectedHomemsg="Welcome to My Movie Plan, An Online Movie ticket Booking Web Application";
        
        assertEquals(expectedHomemsg,actualHomemsg);
        
        wd.findElement(By.linkText("Sign In")).click();
        
        Thread.sleep(2000);
        
        String actualSignIn=wd.findElement(By.tagName("h1")).getText();
  		String expectedSignIn="Sign In";
  		
  		assertEquals(expectedSignIn,actualSignIn);
  		
  		wd.findElement(By.name("uname")).sendKeys("admin2");
		wd.findElement(By.name("upwd")).sendKeys("asdfg");
		Thread.sleep(4000);
		wd.findElement(By.tagName("button")).click();
	
		
		//Check if in the admin Dashboard
		Thread.sleep(2000);
		String actualAdminDboard=wd.findElement(By.tagName("h1")).getText();
		String expectedAdminDboard="Welcome admin2 to Admin Portal";
		
		assertEquals(expectedAdminDboard,actualAdminDboard);
		
          wd.findElement(By.partialLinkText("Enable")).click();
		
		String actualEnable=wd.findElement(By.tagName("h1")).getText();
		String expectedEnable="Enable or Disable Movie shows";
    
		
		assertEquals(expectedEnable,actualEnable);
		  
		wd.findElement(By.name("movieid")).sendKeys("1");
		  wd.findElement(By.xpath("//button[text()='Search Movie']")).click();
		  
		  String actualMoviename=wd.findElement(By.tagName("h3")).getText();
		
		  //Check the searched movie name from databsase
		  Connection con=DBUtil.getConnection();
	    	String sql="select * from movie2 where movie_id=?";
	    	PreparedStatement ps=con.prepareStatement(sql,ResultSet.TYPE_SCROLL_INSENSITIVE);
	    	ps.setString(1, "1");
	    	ResultSet rs=ps.executeQuery();
	    	
	    	String moviename="";
	    	while(rs.next()) {
	    		
	    		 moviename="Name of Movie: "+rs.getString("name");
	    		//assertEquals(moviename,actualMoviename);
	    	}
	    	
	    	assertEquals(moviename,actualMoviename);
		  
		
	    	//Disable the movie
	    	List<WebElement> enable=wd.findElements(By.name("enableMovies"));
	    			
	    			enable.get(1).click();
	    	
	    	
	    	//Click on Submit
	    	wd.findElement(By.xpath("//button[text()='Submit']")).click();
	    	Thread.sleep(5000);
	    	
	    	String actualEnablemsg=wd.findElement(By.tagName("h2")).getText();
	    	String expectedEnablemsg="Movie with id 1 and name as Koi Mil Gaya 2 is disabled";
	    	
		    assertEquals(expectedEnablemsg,actualEnablemsg);
		    
		    //Check in the database if the movie is disabled
		    
		    Connection con2=DBUtil.getConnection();
	    	String sql2="select * from movie2 where movie_id=?";
	    	PreparedStatement ps2=con2.prepareStatement(sql2,ResultSet.TYPE_SCROLL_INSENSITIVE);
	    	ps2.setString(1, "1");
	    	ResultSet rs2=ps2.executeQuery();
		    
	    	boolean enable2=true;
	    	
	    	while(rs2.next()) {
	    		
	    		 enable2=rs2.getBoolean("enable");
	    		
	    		
	    	}
	    	assertEquals(false,enable2);
		//assertEquals(false,enable2);
	}//End of testD()
	
	
	
}
