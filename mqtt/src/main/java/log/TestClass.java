package log;

public class TestClass {
    private static ThreadLocal<User> threadLocal=new ThreadLocal<>();
    public static void main(String [] args){
        User user=new User().setUsername("wanzhs").setPhone("13526856325").setPassword("123456");
        User user1=new User().setUsername("wanzhs1").setPhone("13526856325").setPassword("123456");
        threadLocal.set(user);
        threadLocal.set(user1);
        User user2=threadLocal.get();

        User user3=threadLocal.get();
    }
}
