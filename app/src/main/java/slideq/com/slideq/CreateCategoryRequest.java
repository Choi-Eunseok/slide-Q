package slideq.com.slideq;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class CreateCategoryRequest extends StringRequest {

    final static private String URL = "http://ted12333.cafe24.com/CreateCategory.php";
    private Map<String, String> parameters;

    public CreateCategoryRequest(String categoryName, String exampleQuestion, String creatorID, Response.Listener<String> listener){
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("categoryName",categoryName);
        parameters.put("exampleQuestion",exampleQuestion);
        parameters.put("creatorID",creatorID);
    }

    @Override
    public Map<String, String> getParams(){
        return parameters;
    }

}
