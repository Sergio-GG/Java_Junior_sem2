import java.lang.reflect.AccessFlag;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayDeque;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

public class TestRunner {

    public static void run(Class<?> testClass) throws InvocationTargetException, IllegalAccessException {
        final Object testObj = initTestObj(testClass);
        Queue<Method> methodsBeforeAll = new ArrayDeque<>();
        Queue<Method> methodsAfterAll = new ArrayDeque<>();
        Queue<Method> methodsBeforeEach = new ArrayDeque<>();
        Queue<Method> methodsAfterEach= new ArrayDeque<>();
        Queue<Method> methodsTest = new PriorityQueue<>((m1,m2) -> Integer.compare(m1.getDeclaredAnnotation(Test.class).value(), m2.getDeclaredAnnotation(Test.class).value()));
        Method method;
        for (Method testMethod : testClass.getDeclaredMethods()) {
            if (testMethod.accessFlags().contains(AccessFlag.PRIVATE)) {
                continue;
            }
            if (testMethod.getAnnotation(BeforeAll.class) != null){
              methodsBeforeAll.add(testMethod);
            }
            if (testMethod.getAnnotation(BeforeEach.class) != null){
                methodsBeforeEach.add(testMethod);
            }
            if (testMethod.getAnnotation(Test.class) != null) {
                methodsTest.add(testMethod);
            }
            if (testMethod.getAnnotation(AfterEach.class) != null){
                methodsAfterEach.add(testMethod);
            }
            if (testMethod.getAnnotation(AfterAll.class) != null){
                methodsAfterAll.add(testMethod);
            }
        }

        while ((method = methodsBeforeAll.poll()) != null){
            method.invoke(testObj);
        }
        while ((method = methodsTest.poll()) != null){
            for (Method m: methodsBeforeEach) {
                m.invoke(testObj);
            }
            method.invoke(testObj);
            for (Method m: methodsAfterEach) {
                m.invoke(testObj);
            }
        }
        while ((method = methodsAfterAll.poll()) != null){
            method.invoke(testObj);
        }
    }

    private static Object initTestObj(Class<?> testClass) {
        try {
            Constructor<?> noArgsConstructor = testClass.getConstructor();
            return noArgsConstructor.newInstance();
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("Нет конструктора по умолчанию");
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException("Не удалось создать объект тест класса");
        }
    }

}

//   try {
//           testMethod.invoke(testObj);
//           } catch (IllegalAccessException | InvocationTargetException e) {
//           throw new RuntimeException(e);
//           }
