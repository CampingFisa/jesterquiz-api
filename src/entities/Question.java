package entities;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import map.ThemeMap;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Question {

    @JsonProperty("questionId")
    private int questionId;

    @JsonProperty("question")
    @JsonAlias("question")
    private String question;

    @JsonProperty("answerA")
    @JsonAlias("A")
    private String answerA;

    @JsonProperty("answerB")
    @JsonAlias("B")
    private String answerB;

    @JsonProperty("answerC")
    @JsonAlias("C")
    private String answerC;

    @JsonProperty("answerD")
    @JsonAlias("D")
    private String answerD;

    @JsonProperty("correctAnswer")
    @JsonAlias("correcte")
    private String correctAnswer;

    public static List<Question> convertStringIntoQuestions(String questionStr){
        ObjectMapper objectMapper = new ObjectMapper();
        List<Question> questions = new ArrayList<>();
        try {
            questions = objectMapper.readValue(questionStr, new TypeReference<>(){});

            return questions;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return questions;
    }
}
