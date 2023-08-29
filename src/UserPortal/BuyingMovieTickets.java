package UserPortal;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;

import utils.DBUtil;

public class BuyingMovieTickets {
	
	WebDriver wd=null;
	
	@Before
	public void init() {
		
		System.setProperty("webdriver.chrome.driver","C:\\Users\\tanuj\\OneDrive\\Desktop\\tanu\\full stack\\Capstone Projects\\chromedriver_win32\\chromedriver.exe");
		wd=new ChromeDriver();
		
		wd.get("http://localhost:8080");
		wd.manage().window().maximize();
		
	}
      
	//TestA() for registering a user
      	@Test
	   public void testA() throws InterruptedException {
      		
      		
      		String actualHome=wd.findElement(By.tagName("h1")).getText();
      	    String expectedHome="Welcome to My Movie Plan, An Online Movie ticket Booking Web Application";
      	    
      	    Thread.sleep(4000);
      	    assertEquals(expectedHome,actualHome);
      	    
            wd.findElement(By.linkText("Register")).click();
            
            String actualRegister=wd.findElement(By.tagName("h1")).getText();
            String expectedRegister="Register";
            
            assertEquals(expectedRegister,actualRegister);
            
            wd.findElement(By.name("name")).sendKeys("Esha Gupta");
            wd.findElement(By.name("pwd")).sendKeys("jklmn");
            wd.findElement(By.name("email")).sendKeys("esha@gmail.com");
            wd.findElement(By.name("phone")).sendKeys("88888");
            WebElement uType=wd.findElement(By.name("userType"));
            
            Select u1=new Select(uType);
            
            u1.selectByVisibleText("Customer");
            
            Thread.sleep(5000);
            
            wd.findElement(By.xpath("//button[text()='Register']")).click();
            
            Thread.sleep(4000);
            
            String actualRegmsg=wd.findElement(By.tagName("h2")).getText();
            String expectedRegmsg="User 'Esha Gupta' registered successfully";
            
            assertEquals(expectedRegmsg,actualRegmsg);
            
            
      	}
      	
      	//TestB() for Buying and ordering movie tickets
      	@Test
      	public void testB() throws InterruptedException, ClassNotFoundException, SQLException {
                  
      		
      		    //Sign in 
      		String actualHome=wd.findElement(By.tagName("h1")).getText();
      	    String expectedHome="Welcome to My Movie Plan, An Online Movie ticket Booking Web Application";
      	    
      	    assertEquals(expectedHome,actualHome);
      	    
            wd.findElement(By.linkText("Sign In")).click();
            
            Thread.sleep(3000);
            
            String actualSignin=wd.findElement(By.tagName("h1")).getText();
            String expectedSignin="Sign In";
            
            assertEquals(expectedSignin,actualSignin);
            
            wd.findElement(By.name("uname")).sendKeys("Esha Gupta");
            wd.findElement(By.name("upwd")).sendKeys("jklmn");
            Thread.sleep(5000);
            wd.findElement(By.xpath("//button[text()='Log in']")).click();
            
            
            //Customer Dashboard is displayed
            
            String actualCustDboard=wd.findElement(By.xpath("/html/body/h2[1]")).getText();
            String expectedCustDboard="Welcome Esha Gupta to Customer Dashboard";
            
            
            assertEquals(expectedCustDboard,actualCustDboard);
            //Enter a search keyword to search for movies
            wd.findElement(By.name("keyword")).sendKeys("Hindi");
            Thread.sleep(5000);
            wd.findElement(By.xpath("//button[text()='Search']")).click();
            
            
            List<WebElement> results=wd.findElements(By.className("table-danger"));
            
            //Verify the search results from database
            
            Connection con=DBUtil.getConnection();
	    	String sql="select * from movie2 where CONCAT(name,'',language,'',description,'',show_date,'',show_time,'',ticketprice,'') like ? OR genre_id in(select genre_id from genre where genre_name like ?) ";
	    	PreparedStatement ps=con.prepareStatement(sql,ResultSet.TYPE_SCROLL_INSENSITIVE);
	    	ps.setString(1, "%Hindi%");
	    	ps.setString(2, "%Hindi%");
	    	ResultSet rs=ps.executeQuery();
      		
	    	int count=0;
	    	while(rs.next()) {
	    		count++;
	    	}
	    	
	    	boolean pass=true;
	    	
	    	if(count!=results.size()) {
	    		
	    		System.out.println("Number of records in database and frontend are not same .Count:"+count);
	    		pass=false;
	    	}
	    	
	    	for(int i=0;i<results.size();i++) {
	    		
	    		if(!results.get(i).getText().contains("Hindi")) {
	    			
	    			System.out.println("Incorrect search results");
	    			pass=false;
	    		}
	    		
	    	}//End of for
      	
              assertEquals(true,pass);
              
              
              //Test add To Cart functionality
      	        
              List<String> movieNameinSearchRes=new ArrayList<String>();
               for(int i=2;i<=results.size();i++) {
            	   
                  // String movies[]=results.get(i).getText().split(" ");
            	   String movie=wd.findElement(By.xpath("/html/body/table/tbody/tr["+i+"]/th")).getText();
                   movieNameinSearchRes.add(movie);
            	   
               }
               
               
               //Clicking on AddToCart
               
               for(int i=0; i<results.size()-2; i++) {
            	   
            	   for(int j=0;j<2;j++) {
            	   
            	   results.get(i).findElement(By.name("button1")).click();
            	   
            	   results=wd.findElements(By.className("table-danger"));
            	   
            	   Thread.sleep(3000);
            	   String actualAddCart=wd.findElement(By.xpath("/html/body/h4[2]")).getText();
            	   String expectedAddCart="Added movie '"+ movieNameinSearchRes.get(i) + "' to cart";
            	   
            	   assertEquals(expectedAddCart,actualAddCart);
            	   } //End of inner for Click Add To Cart 2 times for a movie
               
               }  //End of outer for
               
               
               //Click on View Cart button
               wd.findElement(By.xpath("//button[text()='View Cart']")).click();
               
               Thread.sleep(2000);
               
               String actualViewCart=wd.findElement(By.tagName("h1")).getText();
               String expectedViewCart="Cart Details of user Esha Gupta";
               
               assertEquals(expectedViewCart,actualViewCart);
               
                Thread.sleep(5000);
                
                //Check if all the selected movies are in cart
                
                List<WebElement> moviesinCart=wd.findElements(By.className("table-danger"));
                
                List<String> movieNameinCart=new ArrayList<String>();
                
                for(int i=2;i<moviesinCart.size()+2;i++) {
                	
                	//String movies[]=moviesinCart.get(i).getText().split(" ");
                	String movie=wd.findElement(By.xpath("/html/body/table/tbody/tr["+i+"]/td[1]")).getText();
                	movieNameinCart.add(movie);
                }
                
                for(int i=0; i<movieNameinSearchRes.size()-2;i++) {
                	
                	assertEquals(movieNameinSearchRes.get(i),movieNameinCart.get(i));
                	//String qty[]=moviesinCart.get(i).getText().split(" ");
                	//assertEquals("2",qty[8]);
                	
                }
      	
               //Testing Remove from Cart
                
                String movieNameRem=wd.findElement(By.xpath("/html/body/table/tbody/tr[3]/td[1]")).getText();
                
                wd.findElement(By.xpath("/html/body/table/tbody/tr[3]/td[9]/form/button")).click();
                
                
                String remfromCartActual= wd.findElement(By.tagName("h2")).getText();
                
                String remfromCartExpected="Removed movie '"+ movieNameRem+ "' from cart";
                
                assertEquals(remfromCartExpected,remfromCartActual);
                
                Thread.sleep(5000);
                
                
                
                //List of movies in Cart Final
                
                List<WebElement> moviesinCartFinal=wd.findElements(By.className("table-danger"));
                
                List<String> movieNameinCartFinal=new ArrayList<String>();
                
                for(int i=2; i<moviesinCartFinal.size()+2;i++) {
                	
                	//String movies2[]=moviesinCartFinal.get(i).getText().split(" ");
                	String movie2=wd.findElement(By.xpath("/html/body/table/tbody/tr["+i+"]/td[1]")).getText();
                	movieNameinCartFinal.add(movie2);
                	
                }
                
                int noOfMoviesinCart=moviesinCartFinal.size();
         
                
                //Testing Purchase Tickets button
                
                wd.findElement(By.xpath("//button[text()='Purchase Tickets ']")).click();
                 Thread.sleep(5000);       
                String purchaseTicketsactual=wd.findElement(By.tagName("h1")).getText();
                String expectedpurchaseTickets="Summary of Tickets to be bought";
                
                assertEquals(expectedpurchaseTickets,purchaseTicketsactual);
                
                
                List<WebElement> moviesinBuyTickets=wd.findElements(By.className("table-warning"));
                
                assertEquals(noOfMoviesinCart,moviesinBuyTickets.size());
                
                List<String> movieinBuyTickets=new ArrayList<String>();
                
                for(int i=2; i< noOfMoviesinCart+2 ;i++) {
                
                	
                	//String movie3[]=moviesinBuyTickets.get(i).getText().split(" ");
                	String movie2=wd.findElement(By.xpath("/html/body/table/tbody/tr["+i+"]/td[1]")).getText();
                	movieinBuyTickets.add(movie2);
                	//assertEquals(movieNameinCartFinal.get(i),movieinBuyTickets);
                	
                }
                
                //Check if movies in Buy Tickets page is same as in cart
                
                for(int i=0;i<noOfMoviesinCart;i++) {
                	
                	assertEquals(movieNameinCartFinal.get(i),movieinBuyTickets.get(i));
                }
                
                
                //Testing Credit card details page
                wd.findElement(By.xpath("//button[text()='Pay Amount']")).click();
   	         Thread.sleep(2000);
   	         
   	         String expectedpaymentGateway="Payment Gateway";
   	         String actualpaymentGateway=wd.findElement(By.tagName("h1")).getText();
   	         
   	         assertEquals(expectedpaymentGateway,actualpaymentGateway);
   	         
   	         //Enter the card details
   	         wd.findElement(By.name("first")).sendKeys("7777");
   	         wd.findElement(By.name("second")).sendKeys("8888");
   	         wd.findElement(By.name("third")).sendKeys("3333");
   	         wd.findElement(By.name("fourth")).sendKeys("4444");
   	         
   	         WebElement validMonth=wd.findElement(By.name("validmonth"));
   	         Select m1=new Select(validMonth);
   	         
   	         m1.selectByVisibleText("5");
   	         
   	         WebElement validYear=wd.findElement(By.name("validyear"));
   	         Select y1=new Select(validYear);
   	         y1.selectByVisibleText("26");
   	         
   	         
   	         wd.findElement(By.name("cvv")).sendKeys("765");
   	         
   	         WebElement cardType=wd.findElement(By.name("cardType"));
   	         Select c1=new Select(cardType);
   	         c1.selectByVisibleText("Citigroup");
   	         
   	         Thread.sleep(3000);
   	         
   	         wd.findElement(By.xpath("//button[text()='Submit']")).click();
   	         Thread.sleep(9000);
                
                
                
                
                //Testing Pay Amount button
                
                //wd.findElement(By.xpath("//button[text()='Pay Amount']")).click();
                
               // Thread.sleep(7000);
                
                String actualPayAmt=wd.findElement(By.tagName("h2")).getText();
                
                String expectedPayAmt="Thanks for booking tickets Esha Gupta. Your tickets are confirmed";
                
                assertEquals(expectedPayAmt,actualPayAmt);
                
                
                List<WebElement> moviesinOrderpage=wd.findElements(By.className("table-info"));
                
                assertEquals(noOfMoviesinCart,moviesinOrderpage.size());
                
                boolean pass2=true;
                
                //Checking if all movies in Cart are in Order Summary page
                
                for(int i=2;i<noOfMoviesinCart+2;i++) {
                	
                	// String movies4[]=moviesinOrderpage.get(i).getText().split(" ");
                	String movieinPayAmt=wd.findElement(By.xpath("/html/body/table[2]/tbody/tr["+i+"]/td[1]")).getText();
                     //assertEquals(movieNameinCartFinal.get(i),movies4[1]);
                     
                     if(movieNameinCartFinal.contains(movieinPayAmt)) {}
                     else {pass2=false;}
                     
                } //End of for
                
                assertEquals(true,pass2);
                     
                	 
      	}//End of testB()
                
                
}
      	


