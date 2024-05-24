package sigurnostbackend.project.configurations;

import java.io.File;

public class Config {
    public static final String USERS = "src/main/resources/tools" + File.separator + "users";
    public static final String CA = "src/main/resources/tools" + File.separator + "ca";
    public static final String SERIAL = "src/main/resources/tools" + File.separator + "serial";
    public static void config() {

        File users = new File(USERS);
        if (!users.exists()) {
            users.mkdirs();
        }
        File ca = new File(CA);
        if (!ca.exists()) {
            ca.mkdirs();
        }
        File serial = new File(SERIAL);
        if (!serial.exists()) {
            serial.mkdirs();
        }
    }

}
