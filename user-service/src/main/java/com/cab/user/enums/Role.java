package com.cab.user.enums;

public enum Role {
    RIDER("ROLE_RIDER"),
    DRIVER("ROLE_DRIVER"),
    ADMIN("ROLE_ADMIN");

    /**
     * Get the role enum corresponding to the given display name.
     *
     * @param displayName The display name of the role.
     * @return The role enum corresponding to the given display name.
     * @throws IllegalArgumentException If no matching role is found.
     */
    public static Role fromDisplayName(final String displayName) {
        for (final Role role : Role.values()) {
            if (role.displayName.equalsIgnoreCase(displayName)) {
                return role;
            }
        }
        throw new IllegalArgumentException("No role with display name: " + displayName);
    }

    private final String displayName;

    Role(final String displayName) {
        this.displayName = displayName;
    }

    /**
     * Get the display name of the role.
     *
     * @return The display name of the role.
     */
    public String getDisplayName() {
        return displayName;
    }
}
