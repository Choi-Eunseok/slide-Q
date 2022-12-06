package slideq.com.slideq;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class CreateQuestionRequest extends StringRequest {

    final static private String URL = "http://ted12333.cafe24.com/CreateQuiz.php";
    private Map<String, String> parameters;

    public CreateQuestionRequest(String quizCategory, String quizQuiz, int Num, int quizCateNum, String correctAnswer, String wrongAnswer, Response.Listener<String> listener){
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("quizCategory",quizCategory);
        parameters.put("quizQuiz",quizQuiz);
        parameters.put("Num",Num + "");
        parameters.put("quizCateNum",quizCateNum + "");
        parameters.put("correctAnswer",correctAnswer);
        parameters.put("wrongAnswer",wrongAnswer);
    }

    @Override
    public Map<String, String> getParams(){
        return parameters;
    }

}
