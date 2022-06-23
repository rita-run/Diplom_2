package model;

public class DeleteUserPojo {
    private final String email;
    private final String password;
    private final String name;

    public DeleteUserPojo(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }
}
