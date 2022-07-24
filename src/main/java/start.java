
import com.alibaba.fastjson.JSONObject;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class start {
    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入JSON文件地址");
//        String originalFile = scanner.next();//需要转换的json文件
//        System.out.println("请输入输出地址");
//        String outputAddress = scanner.next();//需要转换的json文件

//        String json = readFile(originalFile);
        String json = readFile("C:\\Users\\sevenlikey\\Desktop\\postman请求格式.json");

        System.out.println("开始读取文件------------------------------------------------------");
        long time1 = System.currentTimeMillis();
        Map jsonMap = JSONObject.parseObject(json, Map.class);
        long time2 = System.currentTimeMillis();

        System.out.println("开始解析json-----------------------------------------------------");
        List item = (List) jsonMap.get("item");
        List<ItemEntity> list = new ArrayList<>();
        processData(item, list);
        long time3 = System.currentTimeMillis();

//
        System.out.println("读取文件耗时:"+(time2-time1));
        System.out.println("解析json耗时:"+(time3-time2));
    }

    /**
     * 数据处理
     *
     * @param item
     * @param list
     */
    private static void processData(List item, List<ItemEntity> list) {
        if (item == null || item.size() == 0) {
            return;
        }
        item.forEach(s -> {
            Map jsonMap = JSONObject.parseObject(String.valueOf(s), Map.class);
            ItemEntity itemEntity = new ItemEntity();


            List itemList = (List) jsonMap.get("item");
            List<ItemEntity> itemEntities = new ArrayList<>();
            processData(itemList, itemEntities);
            itemEntity.setParamEntityList(itemList);//子级

            itemEntity.setName((String) jsonMap.get("name"));//接口名称
            Map requestMap = (Map) jsonMap.get("request");
            itemEntity.setMethod((String) requestMap.get("method"));//请求方式


            if (requestMap.get("body") == null) {
                itemEntity.setRequestType("Params");//类型
                String url = requestMap.get("url").toString();
                if (url.contains("\"host\":")) {
                    Map urlMap = (Map) requestMap.get("url");
                    String raw = (String) urlMap.get("raw");
                    itemEntity.setUrl(raw.substring(0, raw.indexOf("?")));//url
                    itemEntity.setParamEntityList((List) urlMap.get("query"));//参数集合
                } else {
                    itemEntity.setUrl(url);//url
                }
            } else {
                itemEntity.setUrl((String) requestMap.get("url"));//url
                Map body = (Map) requestMap.get("body");
                String mode = (String) body.get("mode");
                if (mode != null && mode.equals("urlencoded")) {
                    itemEntity.setRequestType("x-www-form-urlencoded");//类型
                    itemEntity.setParamEntityList((List) body.get("urlencoded"));//参数集合
                } else if (mode != null && mode.equals("formdata")) {
                    itemEntity.setRequestType("formdata");//类型
                    itemEntity.setParamEntityList((List) body.get("formdata"));//参数集合
                } else if (mode != null && mode.equals("raw")) {
                    itemEntity.setRequestType("json");//类型
                    itemEntity.setParamEntityList((List) body.get("formdata"));//参数集合
                    itemEntity.setBodeJsonValue(body.get("raw").toString());
                }
            }


            list.add(itemEntity);
        });
    }

    /**
     * 读取文件
     *
     * @param originalFile
     * @return
     * @throws IOException
     */
    private static String readFile(String originalFile) throws IOException {
        //读取文件
        InputStream inputStream = new FileInputStream(originalFile);
        byte[] bytes = new byte[inputStream.available()];//一次性读取全部
        inputStream.read(bytes);
        String json = new String(bytes);//读取的json
        inputStream.close();
        return json;
    }
}
