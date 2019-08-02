package com.ushareit.lucene.common;

import org.junit.Test;

public class DocsUtil {
    /**
     * 如果字符串中有一个中文就认为是中文
     * @param str
     * @return
     */
    public static boolean isChineseCharacters(String str){
        boolean result = false;
        char [] chars = str.toCharArray();
        for (char c : chars){

            if(c>128)return true;
        }
        return false;
    }

    @Test
    public void test(){
        isChineseCharacters("asdfgrdf");
    }
}
