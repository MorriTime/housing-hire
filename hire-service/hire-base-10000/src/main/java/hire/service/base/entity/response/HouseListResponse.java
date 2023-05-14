package hire.service.base.entity.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HouseListResponse {

    private Long id;

    private String title;

    private String introduce;

    private double price;

    private String city;

    private String address;

    private String cover;

    private List<String> image;

    private List<String> tags;

    private double space;

    private int floor;

    private int term;

    private String type;

    private String createTime;
}
