package log;

import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
@Accessors(chain = true)
public class User {
    private String username;
    private String phone;
    private String password;
}
