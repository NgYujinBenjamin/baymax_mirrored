package authentication.json;

import java.util.List;
import java.util.Map;

public class BaselineParam {
    public List<Map<String,Object>> baseline;
    public String staff_id;

    public BaselineParam(List<Map<String, Object>> baselines, String staff_id) {
        this.baseline = baselines;
        this.staff_id = staff_id;
    }
}
