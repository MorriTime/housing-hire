package hire.service.user.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class House implements Serializable {

    @TableId(type = IdType.AUTO)
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

    @TableField(fill = FieldFill.INSERT)
    private String createTime;

    @TableField(fill = FieldFill.UPDATE)
    private String updateTime;
}
