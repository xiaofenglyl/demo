package com.example.service;

import org.apache.commons.lang.CharUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import sun.reflect.generics.tree.Tree;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by asus-Iabx on 2017/8/3.
 */
@Service
public class SensitiveService implements InitializingBean {
    @Override
    public void afterPropertiesSet() throws Exception {

        try{
            InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("Sensitive.txt");
            InputStreamReader reader = new InputStreamReader(is);
            BufferedReader bufferedReader = new BufferedReader(reader);
            String lineTxt;

            while((lineTxt = bufferedReader.readLine()) != null)
            {
                addWord(lineTxt.trim());
                System.out.println(lineTxt);
            }
            reader.close();
        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    private void addWord(String lineTxt)
    {
        TreeNode tempNode = rootNode;
        for(int i=0;i<lineTxt.length();++i)
        {
            Character c = lineTxt.charAt(i);
            if(isSymbol(c))
            {
                continue;
            }
            TreeNode node = rootNode.getSubNode(c);

            if(node==null)
            {
                node = new TreeNode();
                tempNode.addSubNode(c,node);
            }

            tempNode = node;

            if(i == lineTxt.length()-1)
            {
                tempNode.setKeywordEnd(true);
            }
        }

    }
    private class TreeNode
    {
        private boolean end = false;

        private Map<Character,TreeNode> SubNode = new HashMap<Character,TreeNode>();

        public void addSubNode(Character key, TreeNode treeNode)
        {
            SubNode.put(key,treeNode);
        }
        public TreeNode getSubNode(Character key){
            return SubNode.get(key);
        }

        boolean isKeywordEnd(){ return end; }
        void setKeywordEnd(boolean end){ this.end = end; }
    }

    private TreeNode rootNode = new TreeNode();

    String filter(String txt) {
        if (org.apache.commons.lang.StringUtils.isBlank(txt)) {
            return txt;
        }

        String replacement = "***";
        TreeNode tempNode = rootNode;
        int begin = 0;
        int position = 0;
        StringBuilder result = new StringBuilder();
        while (position < txt.length()) {
            char c = txt.charAt(position);

            if(isSymbol(c))
            {
                if(tempNode==rootNode)
                {
                    result.append(c);
                    ++begin;
                }
                ++position;
                continue;
            }
            tempNode = tempNode.getSubNode(c);

            if (tempNode == null) {
                result.append(txt.charAt(begin));
                position = begin + 1;
                begin = position;
                tempNode = rootNode;
            } else if (tempNode.isKeywordEnd()) {
                result.append(replacement);
                position = position + 1;
                begin = position;
                tempNode = rootNode;
            } else {
                ++position;
            }
        }

        result.append(txt.substring(begin));
        return result.toString();
    }

    private boolean isSymbol(char c)
    {
        int ic = (int) c;
        return !CharUtils.isAsciiAlphanumeric(c) && (ic < 0x2E80 || ic > 0x9FFF);
    }



}
