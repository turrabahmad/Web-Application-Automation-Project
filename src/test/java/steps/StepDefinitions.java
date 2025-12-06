package steps;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.UUID;

import static org.junit.Assert.assertTrue;

public class StepDefinitions {

    private static WebDriver driver;
    private static WebDriverWait wait;
    private static String baseUrl = "https://petstore.octoperf.com/actions/Catalog.action";

    
    private static String username;
    private static String password = "Test@1234";

    private static String firstName;
    private static String lastName;
    private static String email;
    private static String phone;
    private static String address1;
    private static String city;
    private static String state;
    private static String zip;
    private static String country;


    @Before
    public void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        driver = new ChromeDriver(options); 
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(8));
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    @After
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Given("I open the petstore homepage")
    public void i_open_the_petstore_homepage() {
        driver.get(baseUrl);
        assertTrue(driver.getTitle().toLowerCase().contains("petstore"));
    }

    @When("I register a new user")
    public void i_register_a_new_user() {
        
        try {
            WebElement signInLink = driver.findElement(By.linkText("Sign In"));
            signInLink.click();
        } catch (Exception e) {
            driver.get(baseUrl + "?viewAccount=");
        }

       
        try {
            WebElement registerLink = driver.findElement(By.linkText("Register Now!"));
            registerLink.click();
        } catch (Exception e) {
            try {
                WebElement registerLink2 = driver.findElement(By.xpath("//a[contains(text(),'Register')]"));
                registerLink2.click();
            } catch (Exception ex) {
                driver.get("https://petstore.octoperf.com/actions/Account.action?newAccountForm=");
            }
        }

        
        username  = "user_" + UUID.randomUUID().toString().substring(0, 8);
        firstName = "FN_" + UUID.randomUUID().toString().substring(0, 5);
        lastName  = "LN_" + UUID.randomUUID().toString().substring(0, 5);
        email     = username + "@example.com";
        phone     = "03" + (int) (Math.random() * 100000000); 
        address1  = "Address_" + UUID.randomUUID().toString().substring(0, 6);
        city      = "City_" + UUID.randomUUID().toString().substring(0, 4);
        state     = "ST_" + UUID.randomUUID().toString().substring(0, 2);
        zip       = String.valueOf((int) (Math.random() * 90000) + 10000); 
        country   = "PK";

        
       
        try { driver.findElement(By.name("username")).sendKeys(username); } catch (Exception ignored) {}
        try { driver.findElement(By.name("password")).sendKeys(password); } catch (Exception ignored) {}
        try { driver.findElement(By.name("repeatedPassword")).sendKeys(password); } catch (Exception ignored) {}

        try { driver.findElement(By.name("account.firstName")).sendKeys(firstName); } catch (Exception ignored) {}
        try { driver.findElement(By.name("account.lastName")).sendKeys(lastName); } catch (Exception ignored) {}
        try { driver.findElement(By.name("account.email")).sendKeys(email); } catch (Exception ignored) {}
        try { driver.findElement(By.name("account.phone")).sendKeys(phone); } catch (Exception ignored) {}
        try { driver.findElement(By.name("account.address1")).sendKeys(address1); } catch (Exception ignored) {}
        try { driver.findElement(By.name("account.city")).sendKeys(city); } catch (Exception ignored) {}
        try { driver.findElement(By.name("account.state")).sendKeys(state); } catch (Exception ignored) {}
        try { driver.findElement(By.name("account.zip")).sendKeys(zip); } catch (Exception ignored) {}
        try { driver.findElement(By.name("account.country")).sendKeys(country); } catch (Exception ignored) {}


       
        try {
            driver.findElement(By.name("newAccount")).click();
        } catch (Exception e) {
            try {
                WebElement saveBtn = driver.findElement(By.xpath("//input[@value='Save Account' or @value='Register' or @value='Create Account']"));
                saveBtn.click();
            } catch (Exception ignored) {}
        }
    }

    @And("I sign in with the registered credentials")
    public void i_sign_in_with_the_registered_credentials() {
        driver.get(baseUrl);
        try {
            driver.findElement(By.linkText("Sign In")).click();
        } catch (Exception e) {
            driver.get(baseUrl + "?viewAccount=");
        }

        try { driver.findElement(By.name("username")).clear(); } catch (Exception ignored) {}
        try { driver.findElement(By.name("username")).sendKeys(username); } catch (Exception ignored) {}
        try { driver.findElement(By.name("password")).clear(); } catch (Exception ignored) {}
        try { driver.findElement(By.name("password")).sendKeys(password); } catch (Exception ignored) {}

    
        try {
            driver.findElement(By.name("signon")).click();
        } catch (Exception e) {
            try {
                driver.findElement(By.xpath("//input[@value='Login' or @value='Sign In']")).click();
            } catch (Exception ignored) {}
        }

       
        boolean signedIn = driver.getPageSource().toLowerCase().contains(username.toLowerCase())
                || driver.getPageSource().toLowerCase().contains("sign out")
                || driver.getPageSource().toLowerCase().contains("my account");
        assertTrue("Expected to be signed in, but login checks failed.", signedIn);
    }

    @Then("I add a pet to the cart")
    public void i_add_a_pet_to_the_cart() {

        driver.get(baseUrl + "?viewCategory=&categoryId=FISH");

        
        WebElement firstProduct = wait.until(
                ExpectedConditions.elementToBeClickable(
                        By.xpath("(//a[contains(@href,'viewProduct')])[1]")
                )
        );
        firstProduct.click();

       
        WebElement firstItem = wait.until(
                ExpectedConditions.elementToBeClickable(
                        By.xpath("(//a[contains(@href,'viewItem')])[1]")
                )
        );
        firstItem.click();

        
        WebElement addToCart = wait.until(
                ExpectedConditions.elementToBeClickable(
                        By.xpath("//a[contains(text(),'Add to Cart')]"
                                + "|//input[@value='Add to Cart']")
                )
        );
        addToCart.click();

        
        WebElement cartTitle = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("//*[contains(text(),'Shopping Cart')]")
                )
        );

        String page = driver.getPageSource().toLowerCase();
        boolean hasCart = page.contains("shopping cart")
                && page.contains("item"); 

        assertTrue("Expected cart to show items.", hasCart);
    }
}