package com.baitap.profile;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.baitap.model.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.lang.reflect.Field;
import org.junit.jupiter.api.Test;

class UserJpaMappingTest {
    @Test
    void userEntityMapsToSqlServerUserTable() {
        Entity entity = User.class.getAnnotation(Entity.class);
        Table table = User.class.getAnnotation(Table.class);

        assertNotNull(entity);
        assertEquals("AppUser", entity.name());
        assertNotNull(table);
        assertEquals("[User]", table.name());
        assertEquals("dbo", table.schema());
    }

    @Test
    void userEntityMapsProfileColumns() throws Exception {
        assertNotNull(field("id").getAnnotation(Id.class));
        assertNotNull(field("id").getAnnotation(GeneratedValue.class));
        assertColumn("id", "id");
        assertColumn("userName", "username");
        assertColumn("fullName", "fullname");
        assertColumn("phone", "phone");
        assertColumn("avatar", "avatar");
        assertColumn("emailVerified", "email_verified");
    }

    private void assertColumn(String fieldName, String columnName) throws Exception {
        Column column = field(fieldName).getAnnotation(Column.class);
        assertNotNull(column);
        assertEquals(columnName, column.name());
    }

    private Field field(String name) throws Exception {
        return User.class.getDeclaredField(name);
    }
}
