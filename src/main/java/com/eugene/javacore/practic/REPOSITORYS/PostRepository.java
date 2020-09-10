package com.eugene.javacore.practic.REPOSITORYS;

import com.eugene.javacore.practic.ESSENCES.Post;
import com.eugene.javacore.practic.REPOSIMPLS.PostRepositoryImpl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class PostRepository implements PostRepositoryImpl {
    private Path postsFile = Paths.get("C:\\javaFiles\\post.txt");
    @Override
    public Post getById(Long id) {
        try{
            List<String> listRegFile = Files.readAllLines(postsFile);
            if (!listRegFile.isEmpty()) {
                for (String str : listRegFile) {
                    String strArray[] = str.split(",");
                    if (strArray[0].equals(String.valueOf(id))) {
                        return new Post(String.valueOf(id), strArray[1]);
                    }
                }
            }
            //System.out.println("id не найден");
            return null;
        } catch (IOException e) {
            System.out.println(e);
            return null;
        }
    }

    @Override
    public List<Post> getAll() {
        try{
            List<String> listRegFile = Files.readAllLines(postsFile);
            List<Post> listRegionObj = new ArrayList<>();

            if (!listRegFile.isEmpty()) {
                for (String str : listRegFile) {
                    String strArray[] = str.split(",",2);
                    listRegionObj.add(new Post(strArray[0], strArray[1]));
                }
                return listRegionObj;
            }
            //System.out.println("Файл не содержит данных");
            return null;
        } catch (IOException e) {
            System.out.println(e);
            return null;
        }
    }

    @Override
    public Post save(Post post) {
        int flag = 0;
        try{
            if(!Files.exists(postsFile)){
                Files.createFile(postsFile);
            }
            List<String> listReg = Files.readAllLines(postsFile);
            ArrayList<String> listId = new ArrayList<>();
            int maxValue = 1;
            for (String str : listReg) {
                String strArray[] = str.split(",");
                listId.add(strArray[0]);
                if (maxValue < Integer.parseInt(strArray[0])) {
                    maxValue = Integer.parseInt(strArray[0]);
                }
            }
            switch (post.getId()) {
                case "": {
                    for (String str : listReg) {
                        if (str.contains(post.getContent())) {
                            String strArray[] = str.split(",");
                            post.setId(strArray[0]);
                            flag = 1;
                            break;
                        }
                    }
                    if (flag != 1) {
                        post.setId(String.valueOf(maxValue++));
                    }
                }
                default:
                    for (String str : listReg) {
                        if (str.contains(post.getContent())) {
                            String strArray[] = str.split(",");
                            post.setId(strArray[0]);
                            flag = 1;
                            break;
                        }
                        if (flag != 1) {
                            if (listId.contains(post.getId()))
                                post.setId(String.valueOf(++maxValue));
                        }
                    }
            }
            if (flag != 1) {
                try {
                    Files.writeString(postsFile, post.getId() + "," + post.getContent() + "\n", StandardOpenOption.APPEND);
                    // System.out.println("В файл записаны: id = " + region.getId() + " Регион = " + region.getCharRegName());

                } catch (IOException e) {
                    System.out.println("ошибка записи в файл3");
                }
            }
            return post;
        } catch (IOException e) {
            System.out.println(e);
            return null;
        }
    }

    @Override
    public Post update(Post post) {
        try {
            List<String> listRegFile = Files.readAllLines(postsFile);

            List<String> listId = new ArrayList<>();
            List<String> listRegionCharName = new ArrayList<>();
            if (!listRegFile.isEmpty()) {
                for (String str : listRegFile) {
                    String strArray[] = str.split(",");
                    listId.add(strArray[0]);
                    listRegionCharName.add(strArray[1]);
                }
                if (listId.contains(post.getId())) {
                    if (listRegionCharName.contains(post.getContent())) {
                        System.out.println("введеный Регион уже есть в файле");
                        return null;
                    }
                    listRegionCharName.add(listId.indexOf(post.getId()), post.getContent());
                    Files.delete(postsFile);
                    Files.createFile(postsFile);
                    for (int i = 0; i < listId.size(); i++) {
                        try {
                            Files.writeString(postsFile, listId.get(i) + "," + listRegionCharName.get(i) + "\n", StandardOpenOption.APPEND);
                            //   System.out.println("В файл записаны: id = " + listId.get(i) + " Регион = " + listRegionCharName.get(i));
                        } catch (IOException e) {
                            System.out.println("ошибка записи в файл3");
                        }
                    }
                }
                return post;
            }
            System.out.println("файл пуст");
            return null;
        } catch (IOException e) {
            System.out.println(e);
            return null;
        }
    }

    @Override
    public void deleteById(Long id) {
        try{
            List<String> listReg = Files.readAllLines(postsFile);
            ArrayList<Long> listId = new ArrayList<>();
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            String strPovtorVvod = "notnull";
            for (String str : listReg) {
                String strArray[] = str.split(",");
                listId.add(Long.parseLong(strArray[0]));
            }
            if (listId.contains(id)) {
                listReg.remove(listId.indexOf(id));
                Files.delete(postsFile);
                Files.createFile(postsFile);
                for (String str : listReg) {
                    try {
                        System.out.println(str);

                        Files.writeString(postsFile, str + "\n", StandardOpenOption.APPEND);
                        System.out.println("В файл записаны: id = " + str);
                    } catch (IOException e) {
                        System.out.println("ошибка записи в файл3");
                    }
                }
            } else {
                System.out.println("id не найден");
            }
        } catch (IOException e) {
            System.out.println(e);

        }
    }
}
