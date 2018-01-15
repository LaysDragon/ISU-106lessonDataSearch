package com.lays.indexer;

import com.google.common.collect.Maps;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.Part;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class IndexManager {
    private static IndexManager ourInstance = new IndexManager();

    public static IndexManager getInstance() {
        return ourInstance;
    }

    private IndexManager() {
    }

    List<Document> Docs = new ArrayList<>();

    public TreeMap<String, Term> IndexDataMap = new TreeMap<>();
    public TreeMap<String, List<Term>> Gram2IndexDataMap = new TreeMap<>();
    public TreeMap<String, Term> BiwordIndexDataMap = new TreeMap<>();
    public TreeMap<String, Term> PermutermIndex = new TreeMap<>();
    public String SplitChar = "\\n\\r\\t;=/.\"{}()\\-<>:!,'&~…?|“–”®©•\\[\\]";
    public boolean andOpSkipMode = true;
    public int andOpLoopCounter = 0;


    public void saveDataBase() {
        FileOutputStream fout = null;
        try {
            fout = new FileOutputStream("d:/dataset/IndexDataMap");
            ObjectOutputStream oos = new ObjectOutputStream(fout);
            oos.writeObject(IndexDataMap);

            fout = new FileOutputStream("d:/dataset/Gram2IndexDataMap");
            oos = new ObjectOutputStream(fout);
            oos.writeObject(Gram2IndexDataMap);


            fout = new FileOutputStream("d:/dataset/BiwordIndexDataMap");
            oos = new ObjectOutputStream(fout);
            oos.writeObject(BiwordIndexDataMap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addFileParts(Collection<Part> fileparts) throws Exception {
        List<Document> docs = fileparts.stream().map(Document::getDocFromPart).collect(Collectors.toList());
        addDocuments(docs);


    }


    public void addDocuments(Collection<Document> docs) {
        for (Document doc : docs) {
            addDocument(doc);
        }
    }

    public synchronized void addDocument(Document doc) {
        this.Docs.add(doc);
        List<TokenPosition> terms = new ArrayList<>();
        Matcher matcher = Pattern.compile("[a-zA-Z0-9]+").matcher(doc.textContent.toLowerCase());
        int index = 0;
        while (matcher.find()) {
            terms.add(new TokenPosition(index++, matcher.start(), matcher.end(), matcher.group(0)));
        }
        TokenPosition token1 = null;
        TokenPosition token2 = null;

        for (TokenPosition token : terms) {
            if (this.IndexDataMap.containsKey(token.keywords)) {
                List<IndexRecord> indexRecords = this.IndexDataMap.get(token.keywords).indexRecords;
                Optional<IndexRecord> dataOptional = indexRecords.stream().filter(indexRecord -> indexRecord.document.id == doc.id).findFirst();
                if (dataOptional.isPresent()) {
                    dataOptional.get().frequency++;
                    dataOptional.get().position.add(token);
                } else {
                    IndexRecord indexRecord = new IndexRecord(doc, 1);
                    indexRecord.position.add(token);
                    indexRecords.add(indexRecord);
                }
            } else {

                Term term_data = new Term(token.keywords);
                this.IndexDataMap.put(token.keywords, term_data);
                IndexRecord indexRecord = new IndexRecord(doc, 1);
                indexRecord.position.add(token);
                term_data.indexRecords.add(indexRecord);
            }
            //Biword index

            if (token1 == null) {
                token1 = token;
                continue;
            } else if (token2 == null) {
                token2 = token;
            } else {
                token1 = token2;
                token2 = token;
            }
            Term term = new Term(token1.keywords + " " + token2.keywords);
            if (!BiwordIndexDataMap.containsKey(term.keyword)) {
                BiwordIndexDataMap.put(term.keyword, term);
            }

            term = BiwordIndexDataMap.get(term.keyword);
            Optional<IndexRecord> indexDataOptional = term.indexRecords.stream().filter(indexRecord -> indexRecord.document.id == doc.id).findFirst();
            if (indexDataOptional.isPresent()) {
                indexDataOptional.get().position.add(token1);
                indexDataOptional.get().position.add(token2);
                indexDataOptional.get().frequency++;
            } else {
                IndexRecord indexRecord = new IndexRecord(doc, 1);
                indexRecord.position.add(token1);
                indexRecord.position.add(token2);
                term.indexRecords.add(indexRecord);
            }

//            term.indexRecords = searchSentance(token1+" "+token2,1);
//
//            if(term.indexRecords.size()>0) {
//                if(BiwordIndexDataMap.containsKey(term.keyword)) {
//                    Term termOrignal =  BiwordIndexDataMap.get(term.keyword);
//                    termOrignal.indexRecords = orOperation(termOrignal.indexRecords,term.indexRecords);
//                }else{
//                    BiwordIndexDataMap.put(term.keyword, term);
//
//                }
//            }
        }
        for (Term term : IndexDataMap.values()) {
            //Gram2IndexDataMap
            String[] keywords = term.getKeyword().split("");
            for (int i = -1; i < keywords.length; i++) {
                String key = "";
                if (i == -1) {
                    key = "$" + keywords[i + 1];
                } else if (i == term.getKeyword().length() - 1) {
                    key = keywords[i] + "$";
                } else {
                    key = keywords[i] + keywords[i + 1];
                }
                if (!Gram2IndexDataMap.containsKey(key))
                    Gram2IndexDataMap.put(key, new ArrayList<>());
                if (!Gram2IndexDataMap.get(key).contains(term))
                    Gram2IndexDataMap.get(key).add(term);
            }
            //Permuterm index

            for (int i = 0; i < term.getKeyword().length(); i++) {
                String pindex = term.getKeyword() + '$';
                pindex = pindex.substring(i, pindex.length()) + pindex.substring(0, i);
                PermutermIndex.put(pindex, term);
            }
        }


//        for (Term term1 :IndexDataMap.values()) {
//            for (Term term2 :IndexDataMap.values()) {
//                if(term1 == term2)continue;
//                Term term = new Term(term1.keyword+" "+term2.keyword);
//                term.indexRecords = searchSentance(term1.keyword+" "+term2.keyword,1);
//
//                if(term.indexRecords.size()>0) {
//                    if(BiwordIndexDataMap.containsKey(term.keyword)) {
//                        Term termOrignal =  BiwordIndexDataMap.get(term.keyword);
//                        termOrignal.indexRecords = orOperation(termOrignal.indexRecords,term.indexRecords);
//                    }else{
//                        BiwordIndexDataMap.put(term.keyword, term);
//
//                    }
//                }


//            }
//        }
    }

    public Document getDoc(int id) {
        Optional<Document> documentOptional = this.Docs.stream().filter(document -> document.id == id).findFirst();
        return documentOptional.orElse(null);
    }

    public SkipLinkedList<IndexRecord> getIndexData(String keyword) {
        return getIndexData(keyword, true);
    }


    public SkipLinkedList<IndexRecord> getIndexData(String keyword, boolean preMarkedAllPosition) {

        Term term = this.IndexDataMap.get(keyword);
        if (term != null)
            return term.indexRecords.stream().map(indexRecord -> {
                IndexRecord tmp = new IndexRecord(indexRecord.document, indexRecord.frequency);
                tmp.position.addAll(indexRecord.position);
                if (preMarkedAllPosition)
                    tmp.markedPosition.addAll(indexRecord.position);
                return tmp;
            }).collect(Collectors.toCollection(SkipLinkedList::new));
        return new SkipLinkedList<>();
//
    }

    public List<SkipLinkedList<IndexRecord>> getGran2IndexData(String keyword) {
        return getGran2IndexData(keyword, true);
    }


    public List<SkipLinkedList<IndexRecord>> getGran2IndexData(String keyword, boolean preMarkedAllPosition) {

        List<Term> terms = this.Gram2IndexDataMap.get(keyword);

        if (terms != null)
            return terms.stream().map(term -> term.indexRecords.stream().map(indexRecord -> {
                IndexRecord tmp = new IndexRecord(indexRecord.document, indexRecord.frequency);
                tmp.position.addAll(indexRecord.position);
                if (preMarkedAllPosition)
                    tmp.markedPosition.addAll(indexRecord.position);
                return tmp;
            }).collect(Collectors.toCollection(SkipLinkedList::new))).collect(Collectors.toList());

        return new ArrayList<>();
//
    }

    public SkipLinkedList<IndexRecord> getBiwordIndexData(String keyword, String secondKeyword) {


        Term term = this.BiwordIndexDataMap.get(keyword + " " + secondKeyword);
        if (term != null)
            return term.indexRecords.stream().map(indexRecord -> {
                IndexRecord tmp = new IndexRecord(indexRecord.document, indexRecord.frequency);
                tmp.position.addAll(indexRecord.position);
                tmp.markedPosition.addAll(indexRecord.position);
                return tmp;
            }).collect(Collectors.toCollection(SkipLinkedList::new));
        return new SkipLinkedList<>();
//
    }

    public List<Document> toDocs(SkipLinkedList<IndexRecord> indexRecordSkipLinkedList) {

        return indexRecordSkipLinkedList
                .stream().map(indexRecord -> indexRecord.document)
                .collect(Collectors.toList());
    }

    public String queryCorrection(String query){
        StringBuilder result = new StringBuilder();
        for(String token:this.parseToken(query)){
            if(!token.contains("\""))
                result.append(this.wordCorrection(token)).append(" ");
            else
                result.append(token).append(" ");
        }
        return result.toString();
    }

    public String wordCorrection(String word) {

        return this.IndexDataMap
                .entrySet()
                .stream()
//                .filter(e -> Math.abs(word.length() - e.getKey().length()) <= 5)
                .collect(Collectors.toMap(Map.Entry::getKey, s -> this.compareWordDistance(s.getKey(), word)))
                .entrySet()
                .stream()
                .min(Comparator.comparing(Map.Entry::getValue))
                .get().getKey();
    }

//    public int compareWordDistance(String word1, String word2) {
//        return compareWordDistance(word1, word2, word1.length(), word2.length());
//    }
//
//
//    public int compareWordDistance(String word1, String word2, int row, int col) {
//        if (row == 0)
//            return col;
//        else if (col == 0)
//            return row;
//        int up = this.compareWordDistance(word1, word2, row - 1, col) + 1;
//        int left = this.compareWordDistance(word1, word2, row, col - 1) + 1;
//        int corner = this.compareWordDistance(word1, word2, row - 1, col - 1);
//        if (word1.toCharArray()[row - 1] == word2.toCharArray()[col - 1])
//            corner += 0;
//        else {
//            corner += 1;
//        }
//        return Arrays.stream(new int[]{up, left, corner}).min().getAsInt();
//    }

    public int compareWordDistance(String word1, String word2) {
        int[][] matrix = new int[word1.length() + 1][word2.length() + 1];

        for (int row = 0; row <= word1.length(); row++) {
            for (int col = 0; col <= word2.length(); col++) {
                if (col == 0)
                    matrix[row][col] = row;
                else if (row == 0)
                    matrix[row][col] = col;
            }
        }
        for (int row = 1; row <= word1.length(); row++) {
            for (int col = 1; col <= word2.length(); col++) {
                int up = matrix[row - 1][col] + 1;
                int left = matrix[row][col - 1] + 1;
                int corner = matrix[row - 1][col - 1];
                if(word1.toCharArray()[row-1] != word2.toCharArray()[col-1])
                    corner +=1;
                matrix[row][col] = Arrays.stream(new int[]{up, left, corner}).min().getAsInt();
            }
        }
        return matrix[word1.length()][word2.length()];
    }

    public  List<String> parseToken(String query){
        String regex = "(@\"[^\"]*\")|(\"[^\"]*\")|(\\S+)";

        Matcher m = Pattern.compile(regex).matcher(query.toLowerCase());
        List<String> tokenList = new ArrayList<>();
        while (m.find()) {
            if (m.group(1) != null) {
                tokenList.add(m.group(1));
            } else if (m.group(2) != null) {
                tokenList.add(m.group(2));
            } else {
                tokenList.add(m.group(3));
            }
        }
        return tokenList;
    }

    public List<IndexRecord> search(String query, boolean wildcardModeSwitch, boolean skipMode) {
        this.andOpSkipMode = skipMode;
        this.andOpLoopCounter = 0;
        SkipLinkedList<IndexRecord> result = new SkipLinkedList<>();
//        StringTokenizer keyword = new StringTokenizer(query.toLowerCase());


        Iterator<String> tokenIterator = this.parseToken(query).iterator();


        if (!tokenIterator.hasNext()) {
            return result;
        }

        String initToken = tokenIterator.next().trim();
        if (initToken.indexOf("\"") == 0) {
            result.addAll(searchSentance(initToken.substring(1, initToken.length() - 1)));
        } else if (initToken.indexOf("@\"") == 0) {
            result.addAll(searchSentanceBiword(initToken.substring(2, initToken.length() - 1)));
        } else if (initToken.indexOf('*') > -1) {
            result = this.orOperation(result, searchWildCard(initToken, wildcardModeSwitch));
        } else {
            result.addAll(this.getIndexData(initToken));
        }
        Maps.EntryTransformer<SkipLinkedList<IndexRecord>, SkipLinkedList<IndexRecord>, SkipLinkedList<IndexRecord>> operation = null;
        while (tokenIterator.hasNext()) {


            String keywordToken = tokenIterator.next();
            String nextToken = keywordToken;


            switch (keywordToken) {

                case "or":
                    operation = this::orOperation;
                    nextToken = tokenIterator.next();

                    break;
                case "not":
                    operation = this::notOperation;
                    nextToken = tokenIterator.next();
                    break;
                case "and":
                    nextToken = tokenIterator.next();
                default:
                    operation = this::andOperation;
                    break;
            }
            nextToken = nextToken.trim();
            if (nextToken.indexOf("\"") == 0) {

                result = operation.transformEntry(result, searchSentance(nextToken.substring(1, nextToken.length() - 1)));
            } else if (nextToken.indexOf("@\"") == 0) {

                result = operation.transformEntry(result, searchSentanceBiword(nextToken.substring(2, nextToken.length() - 1)));
            } else if (nextToken.indexOf('*') > -1) {
                result = operation.transformEntry(result, searchWildCard(nextToken, wildcardModeSwitch));
            } else {
                result = operation.transformEntry(result, this.getIndexData(nextToken));
            }
        }
        return result;


    }


    public SkipLinkedList<IndexRecord> searchSentanceBiword(String sentenceToken) {
        List<String> token = Arrays.asList(sentenceToken.trim().split("[^a-zA-Z0-9]")).stream().filter(s -> !s.isEmpty()).collect(Collectors.toList());
        ;
        if (token.size() == 1) {
            return getIndexData(token.get(0));
        }

        Iterator<String> tokenIterator = token.iterator();

        SkipLinkedList<IndexRecord> result = new SkipLinkedList<>();

        String firstToken = tokenIterator.next();
        String SecondToken = tokenIterator.next();
        result.addAll(getBiwordIndexData(firstToken, SecondToken));
        while (tokenIterator.hasNext()) {
            firstToken = SecondToken;
            SecondToken = tokenIterator.next();
            result = andOperation(result, getBiwordIndexData(firstToken, SecondToken));

        }
        return result;
    }

    public SkipLinkedList<IndexRecord> searchSentance(String sentenceToken) {
        return searchSentance(sentenceToken, 2);
    }


    public SkipLinkedList<IndexRecord> searchSentance(String sentenceToken, int default_token_interval) {
        List<String> token = Arrays.asList(sentenceToken.trim().split("[^a-zA-Z0-9:]")).stream().filter(s -> !s.isEmpty()).collect(Collectors.toList());
        if (token.size() == 1) {
            return getIndexData(token.get(0));
        }

        Iterator<String> tokenIterator = token.iterator();
        SkipLinkedList<IndexRecord> sentenceResult = getIndexData(tokenIterator.next(), false);
        while (tokenIterator.hasNext()) {
            String next = tokenIterator.next();
            int token_interval = default_token_interval;
            if (next.split(":").length > 1) {
                token_interval = Integer.parseInt(next.split(":")[0]);
                next = next.split(":")[1];
            }
            sentenceResult = andOperation(sentenceResult, getIndexData(next, false), token_interval);
        }
        return sentenceResult;
    }

    public SkipLinkedList<IndexRecord> searchWildCard(String wildCardToken, boolean modeSwitch) {
        if (modeSwitch) {
            return this.searchNGramWildCard(wildCardToken);
        } else {
            return this.searchPermutermQueryWildCard(wildCardToken);
        }
    }

    public List<String> parsePermutermQueryWildCardQuerry(String wildCardToken) {
        int wildcardNums = StringUtils.countMatches(wildCardToken, "*");
        List<String> queryList = new ArrayList<>();
        switch (wildcardNums) {
            case 0:
                queryList.add(wildCardToken + '$');
                break;
            case 1:
                if (wildCardToken.endsWith("*")) {
                    queryList.add(wildCardToken + "$");
                } else {
                    String query = wildCardToken + "$";
                    String[] split = query.split("\\*");
                    queryList.add(split[1] + split[0] + "*");
                }
                break;
            case 2:
                String[] split = wildCardToken.split("\\*");
                if (wildCardToken.startsWith("*") && wildCardToken.endsWith("*")) {
                    queryList.add(split[1] + "*");
                } else {
                    List<String> splitList = new ArrayList<>(Arrays.asList(split));
                    if (splitList.size() == 2)
                        splitList.add("");
                    split = splitList.toArray(new String[]{});
                    queryList.addAll(this.parsePermutermQueryWildCardQuerry("*" + split[1] + "*"));
                    queryList.addAll(this.parsePermutermQueryWildCardQuerry(split[0] + "*" + split[2]));
                }
                break;
            default:
        }
        return queryList;
    }

    public SkipLinkedList<IndexRecord> searchPermutermQueryWildCard(String wildCardToken) {
        List<String> queryList = this.parsePermutermQueryWildCardQuerry(wildCardToken);
        if (queryList.size() == 0)
            return new SkipLinkedList<>();
        SkipLinkedList<IndexRecord> result = PermutermIndex.entrySet().stream()
                .collect(Collectors.groupingBy(e -> e.getValue().keyword, Collectors.mapping(Map.Entry::getKey, Collectors.toList())))
                .entrySet().stream()
                .filter(entry -> {

                    for (String query : queryList) {

                        String[] split = query.split("\\*");
                        if (query.endsWith("*")) { //*X=>X$* X*Y=>Y$X*
                            if (entry.getValue().stream().noneMatch(s -> s.startsWith(split[0])))
                                return false;
                        } else {//X* => X*$
                            if (entry.getValue().stream().noneMatch(s -> s.startsWith(split[0]) && s.endsWith(split[1])))
                                return false;
                        }
                    }
                    return true;

                })
                .flatMap(entry -> this.getIndexData(entry.getKey()).stream())
                .collect(Collectors.toCollection(SkipLinkedList::new));
        return result;

    }

    public SkipLinkedList<IndexRecord> searchNGramWildCard(String wildCardToken) {
        String[] wildCardTokens = wildCardToken.split("");
        List<String> gram2tokens = new ArrayList<>();
        for (int i = -1; i < wildCardTokens.length; i++) {
            String key = "";
            if (i == -1) {
                key = "$" + wildCardTokens[i + 1];
            } else if (i == wildCardTokens.length - 1) {
                key = wildCardTokens[i] + "$";
            } else {
                key = wildCardTokens[i] + wildCardTokens[i + 1];
            }
            gram2tokens.add(key);
        }
        gram2tokens = gram2tokens.stream().filter(s -> !s.contains("*")).collect(Collectors.toList());
        if (gram2tokens.stream().anyMatch(key -> Gram2IndexDataMap.get(key) == null))
            return new SkipLinkedList<>();
        List<Set<String>> termLists = gram2tokens
                .stream()
                .map(key -> Gram2IndexDataMap
                        .get(key)
                        .stream()
                        .map(term -> term.getKeyword())
                        .collect(Collectors.toSet()))
                .collect(Collectors.toList());
        Set<String> finalTerms = termLists.remove(0);
        for (Set<String> terms : termLists) {
            finalTerms.retainAll(terms);
        }
        SkipLinkedList<IndexRecord> result = new SkipLinkedList<>();
        for (String term : finalTerms) {
            result = orOperation(result, getIndexData(term));
        }
//        SkipLinkedList<IndexRecord> result = new SkipLinkedList<>();
//        for(String gram2key:gram2tokens) {
//            List<SkipLinkedList<IndexRecord>> list = getGran2IndexData(gram2key);
//            SkipLinkedList<IndexRecord> gramOrList = new SkipLinkedList<>();
//            for (SkipLinkedList<IndexRecord> indexData : list) {
//                gramOrList = orOperation(gramOrList,indexData);
//            }
//            if(result.size()==0)
//                result.addAll(gramOrList);
//            else
//                result = andOperation(result,gramOrList);
//        }
        return result;
    }

    public SkipLinkedList<IndexRecord> andOperation(SkipLinkedList<IndexRecord> d1, SkipLinkedList<IndexRecord> d2) {
        return andOperation(d1, d2, -1);
    }

    public SkipLinkedList<IndexRecord> andOperation(SkipLinkedList<IndexRecord> d1, SkipLinkedList<IndexRecord> d2, int token_interval) {
        SkipLinkedList<IndexRecord> result = new SkipLinkedList<>();
        if (d1 == null || d1.isEmpty() || d2 == null || d2.isEmpty()) {
            return result;
        }

        SkipListIterator<IndexRecord> d1t = d1.listIterator();
        SkipListIterator<IndexRecord> d2t = d2.listIterator();

        IndexRecord indexRecord1 = null;
        IndexRecord indexRecord2 = null;

        do {
            this.andOpLoopCounter++;
            if ((indexRecord1 == null || indexRecord1.document.id == indexRecord2.document.id) && d1t.hasNext() && d2t.hasNext()) {

                indexRecord1 = d1t.next();
                indexRecord2 = d2t.next();
            } else if (indexRecord1.document.id > indexRecord2.document.id && d2t.hasNext()) {
                if (d2t.hasNextSkip() && (indexRecord1.document.id > d2t.getNextSkip().document.id) && this.andOpSkipMode) {
                    indexRecord2 = d2t.nextSkip();
                } else {
                    indexRecord2 = d2t.next();
                }


            } else if (indexRecord2.document.id > indexRecord1.document.id && d1t.hasNext()) {
                if (d1t.hasNextSkip() && (indexRecord2.document.id > d1t.getNextSkip().document.id) && this.andOpSkipMode) {
                    indexRecord1 = d1t.nextSkip();
                } else {
                    indexRecord1 = d1t.next();
                }
            } else {
                break;
            }
            if (indexRecord1.document.id == indexRecord2.document.id) {

                if (token_interval > 0) {
                    IndexRecord reusltIndexRecord = andPositionCheck(indexRecord1, indexRecord2, token_interval);
                    if (reusltIndexRecord != null)
                        result.add(reusltIndexRecord);

                } else {
                    IndexRecord finalIndexRecord = indexRecord1;
                    indexRecord1.markedPosition.addAll(indexRecord2.markedPosition.stream().filter(tokenPosition -> !finalIndexRecord.markedPosition.contains(tokenPosition)).collect(Collectors.toList()));
                    result.add(indexRecord1);
                }
            }
        } while (true);
        return result;


    }

    public IndexRecord andPositionCheck(IndexRecord indexRecord1, IndexRecord indexRecord2, int token_interval) {
        IndexRecord resultIndexRecord = new IndexRecord(indexRecord1.document, indexRecord1.frequency);
        Iterator<TokenPosition> posIter1 = indexRecord1.position.iterator();
        Iterator<TokenPosition> posIter2 = indexRecord2.position.iterator();
        TokenPosition pos1 = posIter1.next();
        TokenPosition pos2 = posIter2.next();
        try {
            resultIndexRecord.markedPosition.addAll(indexRecord1.markedPosition);
            do {

                if (pos1.index > pos2.index) {
                    pos2 = posIter2.next();
                } else if (pos1.index < pos2.index) {

                    if ((0 < pos2.index - pos1.index) && (pos2.index - pos1.index <= token_interval)) {
                        if (!resultIndexRecord.markedPosition.contains(pos1))
                            resultIndexRecord.markedPosition.add(pos1);
                        resultIndexRecord.markedPosition.add(pos2);
//                        resultIndexRecord.position.add(pos1);
                        resultIndexRecord.position.add(pos2);
                        pos1 = posIter1.next();
                        pos2 = posIter2.next();
                    } else {
                        pos1 = posIter1.next();
                    }

                } else if (pos1.index == pos2.index) {
                    pos2 = posIter2.next();
                }

            } while (true);
        } catch (NoSuchElementException e) {
//                        e.printStackTrace();
        }
        if (resultIndexRecord.position.size() > 0)
            return resultIndexRecord;
        return null;
    }

    public SkipLinkedList<IndexRecord> orOperation(SkipLinkedList<IndexRecord> d1, SkipLinkedList<IndexRecord> d2) {

        SkipLinkedList<IndexRecord> result = new SkipLinkedList<>();
        result.addAll(d1);
        for (IndexRecord indexRecord : d2) {
            Optional<IndexRecord> data = result.stream().filter(indexRecord1 -> indexRecord1.document.id == indexRecord.document.id).findFirst();
            if (data.isPresent()) {
                data.get().position.addAll(indexRecord.position.stream().filter(tokenPosition -> !data.get().position.contains(tokenPosition)).collect(Collectors.toList()));
                data.get().markedPosition.addAll(indexRecord.markedPosition.stream().filter(tokenPosition -> !data.get().markedPosition.contains(tokenPosition)).collect(Collectors.toList()));
                continue;
            }
            result.add(indexRecord);
        }
        return result;

    }

    public SkipLinkedList<IndexRecord> notOperation(SkipLinkedList<IndexRecord> d1, SkipLinkedList<IndexRecord> d2) {
        SkipLinkedList<IndexRecord> result = new SkipLinkedList<>();
        result.addAll(d1);
        List<Integer> d2Ids = d2.stream().map(indexRecord -> indexRecord.document.id).collect(Collectors.toList());
        result = result.stream().filter(indexRecord -> !d2Ids.contains(indexRecord.document.id)).collect(Collectors.toCollection(SkipLinkedList::new));

        return result;
    }

}

