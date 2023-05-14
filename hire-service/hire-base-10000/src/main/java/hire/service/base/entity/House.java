package hire.service.base.entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;


@Data
@Builder
@Document(indexName = "house", type = "_doc", createIndex = false)
public class House {

    @Id
    private Long id;
    /**
     * 标题
     */
    private String title;
    /**
     * 简介
     */
    private String introduce;
    /**
     * 价格
     */
    private double price;
    /**
     * 价格
     */
    private String city;
    /**
     * 地址
     */
    private String address;
    /**
     * 图片数组
     */
    private String image;
    /**
     * 面积
     */
    private double space;
    /**
     * 楼层
     */
    private int floor;
    /**
     * 标签
     */
    private String tags;
    /**
     * 最小租期
     */
    private int term;
    /**
     * 户型
     */
    private String type;
    /**
     * 状态
     */
    private Boolean state;

    private Long userId;

    private String createTime;

    private String updateTime;
}
