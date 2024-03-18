import java.lang.reflect.InvocationTargetException;

public class TestRunnerDemo {

    public static void main(String[] args) throws InvocationTargetException, IllegalAccessException {
        TestRunner.run(TestRunnerDemo.class);
    }

    @BeforeAll
    static void beforeAll(){
        System.out.println("Before all is running");
    }

    @BeforeEach
    public void beforeEach(){
        System.out.println("Before each is running");
    }

    @AfterEach
    public void afterEach(){
        System.out.println("After each is running");
    }

    @AfterAll
    static void afterAll(){
        System.out.println("After all is running");
    }

    @Test(value = 1)
    void test1() {
        System.out.println("test1");
    }

    @Test(value = 2)
    void test2() {
        System.out.println("test2");
    }

    @Test(value = 3)
    void test3() {
        System.out.println("test3");
    }

}