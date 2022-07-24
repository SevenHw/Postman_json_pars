import lombok.Data;

import java.util.List;

@Data
public class ItemEntity {

    /**
     * 方法名称
     */
    private String name;

    /**
     * 请求方式
     */
    private String method;

    /**
     * 请求类型
     */
    private String requestType;

    /**
     * 请求路径
     */
    private String url;

    /**
     * body的json值
     */
    private String bodeJsonValue;

    /**
     * 子级
     */
    private List<ItemEntity> itemEntityList;

    /**
     * 参数集合
     */
    private List<ParamEntity> paramEntityList;
}
