package ru.skillbox.aop;

import ru.skillbox.model.Account;

public class CurrentUserContext {
    private static final ThreadLocal<Account> currentUser = new ThreadLocal<>();

    public static void setCurrentUser(Account account) {
        currentUser.set(account);
    }

    public static Account getCurrentUser() {
        return currentUser.get();
    }

    public static void clear() {
        currentUser.remove();
    }
}

