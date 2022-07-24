import lombok.Data;

@Data
public class ParamEntity {
    /**
     * 参数名称
     */
    private String key;
    /*
    参数值
     */
    private String value;
    /*
    参数类型
     */
    private String type;
    /*
    参数描述
     */
    private String description;
}
