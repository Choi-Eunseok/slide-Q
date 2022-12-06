package slideq.com.slideq;

public class CategoryList {
    String categoryName;
    String exampleQuestion;

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getExampleQuestion() {
        return exampleQuestion;
    }

    public void setExampleQuestion(String exampleQuestion) {
        this.exampleQuestion = exampleQuestion;
    }

    public CategoryList(String categoryName, String exampleQuestion) {
        this.categoryName = categoryName;
        this.exampleQuestion = exampleQuestion;
    }
}
