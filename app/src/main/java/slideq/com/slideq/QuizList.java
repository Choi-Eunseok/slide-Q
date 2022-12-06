package slideq.com.slideq;

public class QuizList {

    String qCategory, qQuiz, correctAnswer, wrongAnswer, createID, solvedID;

    public String getCreateID() {
        return createID;
    }

    public void setCreateID(String createID) {
        this.createID = createID;
    }

    public String getSolvedID() {
        return solvedID;
    }

    public void setSolvedID(String solvedID) {
        this.solvedID = solvedID;
    }

    public String getqCategory() {
        return qCategory;
    }

    public void setqCategory(String qCategory) {
        this.qCategory = qCategory;
    }

    public String getqQuiz() {
        return qQuiz;
    }

    public void setqQuiz(String qQuiz) {
        this.qQuiz = qQuiz;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public String getWrongAnswer() {
        return wrongAnswer;
    }

    public void setWrongAnswer(String wrongAnswer) {
        this.wrongAnswer = wrongAnswer;
    }

    public QuizList(String qCategory, String qQuiz, String correctAnswer, String wrongAnswer, String createID, String solvedID){

        this.qCategory = qCategory;
        this.qQuiz = qQuiz;
        this.correctAnswer = correctAnswer;
        this.wrongAnswer = wrongAnswer;
        this.createID = createID;
        this.solvedID = solvedID;

    }

}
