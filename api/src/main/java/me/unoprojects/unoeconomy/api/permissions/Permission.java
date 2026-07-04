package me.unoprojects.unoeconomy.api.permissions;

public enum Permission {

    COMMAND("command"),
    COMMAND_VIEW("command.view"),
    COMMAND_SET("command.set"),
    COMMAND_ADD("command.add"),
    COMMAND_REMOVE("command.remove"),
    ;

    private final String permission;

    Permission(String permission) {
        this.permission = "unoeconomy." + permission;
    }

    public String getPermission() {
        return permission;
    }
}
