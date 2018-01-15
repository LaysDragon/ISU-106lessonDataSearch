package com.lays.bean;

import com.lays.indexer.IndexRecord;
import com.lays.indexer.TokenPosition;

import java.util.*;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringEscapeUtils.escapeHtml4;

public class SearchHeighlightBean {
    String HtmlContent = "";
    public void setIndexRecord(IndexRecord indexRecord){
        HtmlContent = "";
        //消除重錄標記並重新排序標記
        List<TokenPosition> tokens = new HashSet<>(indexRecord.markedPosition)
                .stream()
                .sorted(Comparator.comparingInt(TokenPosition::getIndex))
                .collect(Collectors.toList());
        int lastStartIndex = 0;
        for(TokenPosition token : tokens){
            String content = escapeHtml4(indexRecord.document.getTextContent().substring(lastStartIndex, token.getStartPosition()));
            if(content.length()>100)
                content =content.substring(0,20)+ "…"+content.substring(content.length()-20-1,content.length());
            HtmlContent+= content;
            HtmlContent+= "<mark>" +escapeHtml4(indexRecord.document.getTextContent().substring(token.getStartPosition(),token.getEndPosition()))+"</mark>";
            lastStartIndex = token.getEndPosition();
        }
        if(lastStartIndex < indexRecord.document.getTextContent().length()-1) {
            String content = escapeHtml4(indexRecord.document.getTextContent().substring(lastStartIndex));
            if(content.length()>100)
                content =content.substring(0,20)+ "…"+content.substring(content.length()-20-1,content.length());
            HtmlContent += content;
        }

        HtmlContent = String.join(" ",HtmlContent.split("</mark> <mark>"));

        String[] split = HtmlContent.split("\r\n");
        split = Arrays.stream(split).filter(s -> !s.trim().isEmpty() && !(s.trim().length()==1)).toArray(String[]::new);
        HtmlContent = String.join("<br>",split);

        split = HtmlContent.split("\n");
        split = Arrays.stream(split).filter(s -> !s.trim().isEmpty() && !(s.trim().length()==1)).toArray(String[]::new);
        HtmlContent = String.join("<br>",split);
//
//        String regex = "(<mark>.+(?!</mark>)</mark>)|((?!<mark>).+(?!</mark>))";
//
//        Matcher m = Pattern.compile(regex).matcher(HtmlContent);
//        List<String> tokenList = new ArrayList<>();
//        while (m.find()) {
//            if (m.group(1) != null) {
//                tokenList.add(m.group(1));
//            }else if(m.group(2)!= null){
//                tokenList.add(m.group(2));
//            } else {
//                tokenList.add(m.group(3));
//            }
//        }
//        return;
    }

    public String getHtmlContent() {
        return HtmlContent;
    }

}
