package link.therealdomm.heldix.lavasurvival.util.mysql;

import lombok.Data;

/**
 * a simple data holder for the mysql credentials
 *
 * @author TheRealDomm
 * @since 10.10.2021
 */
@Data
public class MySQLData {

    private String hostname = "localhost";
    private String database = "database";
    private String username = "username";
    private String password = "password";
    private int port = 3306;

}
