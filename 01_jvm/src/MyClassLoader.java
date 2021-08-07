import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MyClassLoader extends ClassLoader{
    public static void main(String[] args) {
        try {
            MyClassLoader classLoader = new MyClassLoader();
            Object hello = classLoader.findClass("Hello").newInstance();
            Class<?> helloClass = hello.getClass();
            Method method = helloClass.getMethod("hello");
            method.invoke(hello);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        byte[] bytes = getBytesFromFile("/Users/lizhenjiang/IdeaProjects/GeekBangCourseCode/01_jvm/src/resources/Hello.xlass");
        return defineClass(name, bytes, 0, bytes.length);
    }

    private byte[] getBytesFromFile(String filePath) {
        File source = new File(filePath);
        if (source.isFile()) {
            try {
                FileInputStream fileInputStream = new FileInputStream(source);
                int available = fileInputStream.available();
                byte[] bytes = new byte[available];
                if ((fileInputStream.read(bytes)) > 0) {
                    for (int i = 0; i < bytes.length; i++) {
                        bytes[i] = (byte) (255 ^ bytes[i]);
                    }
                }
                return bytes;
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("no file!");
        }
        return new byte[1024];
    }
}
