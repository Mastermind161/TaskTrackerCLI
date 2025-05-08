package org.example;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class Main {
    static Scanner scanner = new Scanner(System.in);
    final static String name = "***task-cli*** ";


    public static int commCreateTask(String task) throws IOException {
        Json json = new Json();
        int counter = 0;
        while (json.parseJson(json.createFilePath(String.valueOf(counter)))!=1){
            counter++;
        }
        ArrayList<HashMap<String,String>> data = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            data.add(new HashMap<String,String>());
        }
        data.get(0).put(Enum.ID.getName(), String.valueOf(counter));
        data.get(1).put(Enum.TASK.getName(), task);
        data.get(2).put(Enum.STATUS.getName(), "to do");
        data.get(3).put(Enum.CREATEDAT.getName(), new Date().toString());
        data.get(4).put(Enum.UPDATEAT.getName(), new Date().toString());

        if (json.createJson(String.valueOf(counter),data)==0){
            System.out.println(name+"Задача (id "+counter+") создана.");
            return 0;
        }else {
            System.out.println(name+"Не удалось создать задачу.");
            return 1;
        }
    }

    public static int commUpdate(String id, String value){
        Json json = new Json();
        if (json.updateJson(id,Enum.TASK.getName(), value)==0){
            System.out.println(name+"Задача (id "+id+") обновлена.");
            return 0;
        }else {
            System.out.println(name+"Не удалось обновить задачу.");
            return 1;
        }
    }

    public static int commMark(String id,String value){
        Json json = new Json();
        if (Objects.equals(value,"to do")||Objects.equals(value,"in-process")||Objects.equals(value,"done")){
            if (json.updateJson(id,Enum.STATUS.getName(), value)==0){
                System.out.println(name+"Задача (id "+id+") обновлена.");
                return 0;
            }else{
                System.out.println(name+"Не удалось обновить задачу.");
                return 1;
            }
        }else{
            System.out.println(name+"Введен неправильный статус, задача не обновлена.");
            return 1;
        }
    }

    public static int commViewList(){
        ArrayList<Json> data = parseAllTasks();
        if (!data.isEmpty()){
            for (Json json : data){
                System.out.println(name+"\n\tЗадача: "+json.getOutArr().get(1).get(Enum.TASK.getName())+
                        ", \n\tId задачи: "+json.getOutArr().get(0).get(Enum.ID.getName())+
                        ", \n\tСтатус задачи: "+json.getOutArr().get(2).get(Enum.STATUS.getName())+
                        ", \n\tСоздана "+json.getOutArr().get(3).get(Enum.CREATEDAT.getName())+
                        ", \n\tПоследнее обновление информации: "+json.getOutArr().get(4).get(Enum.UPDATEAT.getName())+".\n");
            }
            return 0;
        }else{
            return 1;
        }
    }

    //возвращает массив jsonов всех файлов в папке Tasks, распаршенных в хэшмап
    private static ArrayList<Json> parseAllTasks(){
        ArrayList<Json> data = new ArrayList<>();
        File folder = new File("Tasks");
        File[] files = folder.listFiles();
        try{
            for (File file : files) {
                if (file.getName().endsWith(".json")) {
                    Json json = new Json();
                    if (json.parseJson(file.getPath())==0){
                        data.add(json);
                    }
                }
            }
            if (data.isEmpty()){
                System.out.println("Лист задач пуст.");
            }

        }catch (Exception e){
            System.out.println("Лист задач пуст.");

        }

        return data;
    }


    public static void main(String[] args) throws IOException {
        while (true){
            System.out.println(name+"Введите команду:");
            String command = scanner.nextLine();

            if (command.startsWith("exit")) break;
            else if(command.startsWith("add")){
                String[]str = command.split(" ",2);
                commCreateTask(str[1]);
            }else if(command.startsWith("update")) {
                String[] str = command.split(" ", 3);
                commUpdate(str[1], str[2]);
            }else if(command.startsWith("mark")) {
                String[] str = command.split(" ", 3);
                commMark(str[1], str[2]);
            }else if(command.startsWith("list")){
                commViewList();
            }
        }
    }
}