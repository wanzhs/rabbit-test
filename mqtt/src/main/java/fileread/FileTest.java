package fileread;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

@Slf4j
public class FileTest {
    public static void main(String[] args) throws IOException {
        FileInputStream inputStream = new FileInputStream("C:\\Users\\John\\Documents\\Tencent Files\\2249428556\\FileRecv\\info.log");
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF8");
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        String line = "";
        while ((line = bufferedReader.readLine()) != null) {
            if (line.indexOf(")- ") >= 0) {
                String rs = line.split("\\)- ")[1];
//                log.info(rs);
                OpRabbitMessage<String> str=new ObjectMapper().readValue(rs,OpRabbitMessage.class);
                System.out.println(str);
                break;
            }
        }
    }
}
