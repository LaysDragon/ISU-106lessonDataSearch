package com.lays.test;

import com.lays.indexer.SkipLinkedList;
import com.lays.indexer.SkipListIterator;

import java.util.Arrays;

public class SkipLinkedListTest {
    public static void main(String[] args) {
        SkipLinkedList<Integer> list = new SkipLinkedList<>(4);
        for (int i :
                Arrays.asList(0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26)) {
            list.add(i);
        }

//        list.refreshSkipLinked();
        SkipListIterator<Integer> skipListIterator = list.listIterator();
        while(skipListIterator.hasNext()){

            int a ;
//            if(skipListItr.hasNextSkip())
//                a = skipListItr.nextSkip();
//            else
//                a = skipListItr.next();
            a = skipListIterator.next();
            int index = skipListIterator.nextIndex();
            System.out.println("index "+(index-1)+":"+String.valueOf(a)+
                    (skipListIterator.hasNextSkip()? " skip:" + String.valueOf(skipListIterator.getNextSkip()):"")+
                    (skipListIterator.hasPreviousSkip()? " pre-skip:" + String.valueOf(skipListIterator.getPreviousSkip()):""));
        }
        skipListIterator = list.listIterator();
        while(skipListIterator.hasNext()){

            int a ;
            if(skipListIterator.hasNextSkip())
                a = skipListIterator.nextSkip();
            else
                a = skipListIterator.next();
//            a = skipListIterator.next();
            int index = skipListIterator.nextIndex();
            System.out.println("index "+(index-1)+":"+String.valueOf(a)+
                    (skipListIterator.hasNextSkip()? " skip:" + String.valueOf(skipListIterator.getNextSkip()):"")+
                    (skipListIterator.hasPreviousSkip()? " pre-skip:" + String.valueOf(skipListIterator.getPreviousSkip()):""));
        }

        list = new SkipLinkedList<>(4);
        list.addAll(Arrays.asList(0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26));


//        list.refreshSkipLinked();
        skipListIterator = list.listIterator();
        while(skipListIterator.hasNext()){

            int a ;
//            if(skipListItr.hasNextSkip())
//                a = skipListItr.nextSkip();
//            else
//                a = skipListItr.next();
            a = skipListIterator.next();
            int index = skipListIterator.nextIndex();
            System.out.println("index "+(index-1)+":"+String.valueOf(a)+
                    (skipListIterator.hasNextSkip()? " skip:" + String.valueOf(skipListIterator.getNextSkip()):"")+
                    (skipListIterator.hasPreviousSkip()? " pre-skip:" + String.valueOf(skipListIterator.getPreviousSkip()):""));
        }
        skipListIterator = list.listIterator();
        while(skipListIterator.hasNext()){

            int a ;
            if(skipListIterator.hasNextSkip())
                a = skipListIterator.nextSkip();
            else
                a = skipListIterator.next();
//            a = skipListIterator.next();
            int index = skipListIterator.nextIndex();
            System.out.println("index "+(index-1)+":"+String.valueOf(a)+
                    (skipListIterator.hasNextSkip()? " skip:" + String.valueOf(skipListIterator.getNextSkip()):"")+
                    (skipListIterator.hasPreviousSkip()? " pre-skip:" + String.valueOf(skipListIterator.getPreviousSkip()):""));
        }

    }
}
