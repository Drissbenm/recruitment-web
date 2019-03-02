package fr.d2factory.libraryapp.proxy;

import fr.d2factory.libraryapp.exception.DefinedTownsvilleLibraryException;
import fr.d2factory.libraryapp.library.Library;
import fr.d2factory.libraryapp.member.Member;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class SecurityHandler implements InvocationHandler {

    private Library library;

    private SecurityHandler(Library library) {
        this.library = library;
    }

    public static Library getLibraryInstance(Library library) {
        return (Library) java.lang.reflect.Proxy.newProxyInstance(library.getClass().getClassLoader(), library
                .getClass().getInterfaces(), new SecurityHandler(library));
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        Object result;
        try {
            if (method.getName().contains("borrow")) {
                Member member = (Member) args[1];
                if (member.getWallet() <= 0) {
                    throw DefinedTownsvilleLibraryException.EMPTY_WALLOW_EXCEPTION;
                } else {
                    result = method.invoke(library, args);
                }
            } else {
                result = method.invoke(library, args);
            }
        } catch (InvocationTargetException e) {
            throw e.getTargetException();
        } catch (Exception e) {
            throw new RuntimeException("unexpected invocation exception: "
                    + e.getMessage());
        }
        return result;
    }
}
