package org.example;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class Json {
    //сюда сохраняется прочитанный json
    private ArrayList<HashMap<String,String>> outArr = new ArrayList<>();

    //создает строку под стиль json
    private String createLine(int counter, String key,ArrayList<HashMap<String,String>> data,boolean isEnd){
        return isEnd?"\t\""+key+"\":\""+data.get(counter)+"\"\n" : "\t\""+key+"\":\""+data.get(counter)+"\",\n";
    }

    //создает нужный путь к файлу (тк все задачи названы по номерам)
    private String createFilePath(String id){
        return "Tasks/"+id+".json";
    }

    //создает или заменяет уже существующий json
    public int createJson(String id, ArrayList<HashMap<String,String>>data) throws IOException {
        try(BufferedWriter bw = new BufferedWriter(new FileWriter(createFilePath(id)))){
            bw.write("{");
            bw.write(createLine(0,Enum.ID.getName(),data,false));
            bw.write(createLine(1,Enum.TASK.getName(),data,false));
            bw.write(createLine(2,Enum.STATUS.getName(),data,false));
            bw.write(createLine(3,Enum.CREATEDAT.getName(),data,false));
            bw.write(createLine(4,Enum.UPDATEAT.getName(),data,false));
            bw.write("}");
            return 0;
        }catch (Exception e){
            e.getLocalizedMessage();
            return 1;
        }
    }
    //парсит в хэштаблицу json
    public int parseJson(String filePath){
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))){
            String line;
            while((line=br.readLine())!=null){
                if (!line.equals("{")&&!line.equals("}")){
                    String[] arrStr = line.split(":",2);
                    for (int i = 0;i<arrStr.length;i++){
                        arrStr[i] = arrStr[i].replace("\t","");
                        arrStr[i] = arrStr[i].replace("\"","");
                        arrStr[i] = arrStr[i].replace(",","");
                    }
                    HashMap<String,String> hm = new HashMap<>();
                    hm.put(arrStr[0],arrStr[1]);
                    outArr.add(hm);
                }
            }
            return 0;
        }catch (Exception e){
            e.getLocalizedMessage();
            return 1;
        }
    }

    //по данному ключу заменяет его данные и обновляет время
    public int updateJson(String id, String key, String value) {
        try {
            if (parseJson(createFilePath(id)) == 0) {
                for (HashMap<String, String> map : outArr) {
                    if (map.containsKey(key)) {
                        map.remove(key);
                        map.put(key, value);
                        break;
                    }
                    outArr.get(4).remove(Enum.UPDATEAT.getName());
                    outArr.get(4).put(Enum.UPDATEAT.getName(),new Date().toString());
                    createJson(id,outArr);
                    return 0;
                }
            }else {
                return 1;
            }
        } catch (Exception e) {
            e.getLocalizedMessage();
            return 1;
        }
        return 1;
    }
    //удаляет файл json
    public int deleteJson(String id){
        File file = new File(createFilePath(id));
        if (file.delete()){
            return 0;
        }else{
            return 1;
        }
    }
}
