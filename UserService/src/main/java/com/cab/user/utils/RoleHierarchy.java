package com.cab.user.utils;

import com.cab.user.enums.Role;

import java.util.*;

public final class RoleHierarchy {

    private static final List<Role> RIDER_ROLES = new ArrayList<>(List.of(Role.RIDER));
    private static final List<Role> DRIVER_ROLES = new ArrayList<>(List.of(Role.DRIVER));
    private static final List<Role> ADMIN_ROLES = new ArrayList<>(List.of(Role.ADMIN));

    private final static Map<Role, List<Role>> ROLE_MAP = new HashMap<>();

    static {
        // Assign hierarchical roles
        DRIVER_ROLES.addAll(RIDER_ROLES); // Driver inherits Rider permissions
        ADMIN_ROLES.addAll(DRIVER_ROLES); // Admin inherits Driver & Rider permissions

        // Map roles
        ROLE_MAP.put(Role.ADMIN, ADMIN_ROLES);
        ROLE_MAP.put(Role.DRIVER, DRIVER_ROLES);
        ROLE_MAP.put(Role.RIDER, RIDER_ROLES);
    }

    public static List<Role> getRoleList(Role role) {
        return ROLE_MAP.getOrDefault(role, Collections.emptyList());
    }
}