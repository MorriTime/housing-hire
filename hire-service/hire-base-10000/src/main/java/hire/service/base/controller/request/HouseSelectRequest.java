package hire.service.base.controller.request;

import lombok.Data;

@Data
public class HouseSelectRequest {

    private String content;

    private Number priceBegin;

    private Number priceEnd;

    private String city;

    private String address;

    private Number spaceBegin;

    private Number spaceEnd;

    private Integer floorBegin;

    private Integer floorEnd;

    private Integer termBegin;

    private Integer termEnd;

    private Integer current;

    private Integer size;
}
