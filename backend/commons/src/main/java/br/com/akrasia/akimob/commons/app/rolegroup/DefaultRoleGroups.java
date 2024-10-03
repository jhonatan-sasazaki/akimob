package br.com.akrasia.akimob.commons.app.rolegroup;

import java.util.HashMap;
import java.util.HashSet;

public final class DefaultRoleGroups {

    public static final HashMap<String, HashSet<String>> ROLE_GROUPS = new HashMap<String, HashSet<String>>() {
        {
            put("admin", new HashSet<String>() {
                {
                    add("role_group_add");
                    add("role_group_read");
                    add("role_group_edit");
                    add("role_group_delete");
                    add("address_read");
                    add("address_add");
                    add("address_edit");
                    add("address_delete");
                    add("person_add");
                    add("person_read");
                    add("person_edit");
                    add("person_delete");
                }
            });
            put("user", new HashSet<String>() {
                {
                    add("address_read");
                    add("address_add");
                    add("address_edit");
                    add("address_delete");
                    add("person_add");
                    add("person_read");
                    add("person_edit");
                    add("person_delete");
                }
            });
        }
    };

}
