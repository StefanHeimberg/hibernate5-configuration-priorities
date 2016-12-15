package org.example;

import java.net.URL;
import org.hibernate.cfg.Configuration;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Test;

public class PrioritiesTest {
    
    @Test
    public void assert_merge_hibernate_properties_and_hibernate_cfg_xml() {
        final Configuration config = new Configuration().configure();
        assertEquals("org.hibernate.dialect.MySQLDialect", config.getProperty("hibernate.dialect"));
        assertEquals("jta", config.getProperty("hibernate.current_session_context_class"));
        assertEquals("true", config.getProperty("hibernate.use_identifier_rollback"));
        assertNull(config.getProperty("hibernate.connection.autocommit"));
        
        final URL url = getClass().getClassLoader().getResource("hibernate.cfg.xml");
        final Configuration config2 = new Configuration().configure(url);
        assertEquals("org.hibernate.dialect.MySQLDialect", config2.getProperty("hibernate.dialect"));
        assertEquals("jta", config2.getProperty("hibernate.current_session_context_class"));
        assertEquals("true", config.getProperty("hibernate.use_identifier_rollback"));
        assertNull(config2.getProperty("hibernate.connection.autocommit"));
    }
    
    @Test
    public void assert_merge_hibernate_properties_and_oracle_hibernate_cfg_xml() {
        final URL url = getClass().getClassLoader().getResource("oracle-hibernate.cfg.xml");
        final Configuration config = new Configuration().configure(url);
        assertEquals("org.hibernate.dialect.Oracle9Dialect", config.getProperty("hibernate.dialect"));
        assertEquals("jta", config.getProperty("hibernate.current_session_context_class"));
        assertEquals("true", config.getProperty("hibernate.connection.autocommit"));
        assertNull(config.getProperty("hibernate.use_identifier_rollback"));
    }
    
    @Test
    public void assert_value_from_hibernate_properties_used() {
        final Configuration config = new Configuration();
        assertEquals("org.hibernate.dialect.H2Dialect", config.getProperty("hibernate.dialect"));
        assertEquals("jta", config.getProperty("hibernate.current_session_context_class"));
        assertNull(config.getProperty("hibernate.connection.autocommit"));
        assertNull(config.getProperty("hibernate.use_identifier_rollback"));
    }

    @Test
    public void assert_value_from_system_properties_used() {
        System.setProperty("hibernate.auto_quote_keyword", "true");
        System.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQL95Dialect");
        try {
            final Configuration config = new Configuration().setProperties(System.getProperties());
            assertEquals("true", config.getProperty("hibernate.auto_quote_keyword"));
            assertEquals("org.hibernate.dialect.PostgreSQL95Dialect", config.getProperty("hibernate.dialect"));
        } finally {
            System.clearProperty("hibernate.auto_quote_keyword");
            System.clearProperty("hibernate.dialect");
        }
    }

}
