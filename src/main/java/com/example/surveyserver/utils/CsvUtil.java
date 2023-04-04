package com.example.surveyserver.utils;// CsvUtil.java
import com.example.surveyserver.model.QuestionReply;
import com.example.surveyserver.model.SurveyReply;

import java.io.PrintWriter;
import java.util.List;

public class CsvUtil {
    public static void surveyRepliesToCsv(List<SurveyReply> surveyReplies, PrintWriter writer) {
        writer.println("Reply ID,User ID,Question ID,Option ID,Answer Text,Created At");
        for (SurveyReply reply : surveyReplies) {
            for (QuestionReply questionReply : reply.getQuestionReply()) {
                writer.printf("%d,%d,%d,%d,\"%s\",%s%n",
                        reply.getId(),
                        reply.getUser().getId(),
                        questionReply.getId(),
                        questionReply.getOptionReplies(),
                        questionReply.getReplyText(),
                        reply.getCreatedAt());
            }
        }
    }
}
