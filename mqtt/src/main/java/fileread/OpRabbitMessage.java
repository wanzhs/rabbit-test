package fileread;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class OpRabbitMessage<T> {

    private Type type;

    private ChildType childType;

    private String deviceCtrlAddress;

    private T content;


    public enum Type {
        JX(1, "玖行"),
        TLD(2, "特来电");

        private int value;
        private String desc;

        Type(int value, String desc) {
            this.value = value;
            this.desc = desc;
        }

        @JsonValue
        public Integer getValue() {
            return this.value;
        }

        public String getDesc() {
            return this.desc;
        }
    }


    public enum ChildType {
        Telecommunicating(1, "遥信"),
        Telemetering(2, "遥测"),
        OfflineWarning(3, "离线预警");

        private int value;
        private String desc;

        ChildType(int value, String desc) {
            this.value = value;
            this.desc = desc;
        }

        @JsonValue
        public Integer getValue() {
            return this.value;
        }

        public String getDesc() {
            return this.desc;
        }
    }


}
