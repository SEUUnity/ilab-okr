package com.industics.ilab.okr.dal.manager;

import com.industics.ilab.okr.dal.dao.mapper.BonusMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class BonusManager {
    private BonusMapper bonusMapper;
    @Autowired
    public void setBonusMapper(BonusMapper bonusMapper){this.bonusMapper=bonusMapper;}

    public void addBonus(String bonus_type,int amount){
        bonusMapper.addBonus(bonus_type,amount);
    }
    public void addGrantBonus(String admin_id,String approval_id,String bonus_type,int amount){
        bonusMapper.addGrantBonus(admin_id,approval_id,bonus_type,amount);
    }

    public void updateBonus(String bonus_type,int amount){
        bonusMapper.updateBonus(bonus_type,amount);
    }

    public void deleteBonus(String position_id){
        bonusMapper.deleteBonus(position_id);
    }

    public List<Map<String,Object>> getBonus(){
        return bonusMapper.getBonus();
    }
    public List<Map<String,Object>> getAmountInfo(){
        return bonusMapper.getAmountInfo();
    }

    public Map<String,Object> getBonusAmountByMonth(String start,String end){
        List<String>d1=new ArrayList<>();
        List<Integer>d2=new ArrayList<>();
        List<Integer>d3=new ArrayList<>();
        Map<String,Object>xAxis=new HashMap<>();
        List<Map<String,Object>>series=new ArrayList<>();
        Map<String,Object>mD2=new HashMap<>();
        Map<String,Object>mD3=new HashMap<>();
        Map<String,Object>res=new HashMap<>();
        int rpNum=0;
        int y1=Integer.parseInt(start.substring(0,4));
        int y2=Integer.parseInt(end.substring(0,4));
        int m1=Integer.parseInt(start.substring(5,7));
        int m2=Integer.parseInt(end.substring(5,7));

        int y=Integer.parseInt(end.substring(0,4));
        int m=Integer.parseInt(end.substring(5,7));
        String r=end.substring(7);
        m++;
        if(m>12){
            y++;
            m=1;
        }
        if(m<10){
            end=y+"-"+"0"+m+r;
        }else{
            end=y+"-"+m+r;
        }
        List<Map<String,Object>>rp=bonusMapper.getBonusAmountByMonth(start,end);

        while (y1<y2||(y1==y2&&m1<=m2)){
            if(m1>12){
                m1=1;
                y1++;
            }
            String drp="000000";
            if(rpNum<rp.size()){
                drp=rp.get(rpNum).getOrDefault("date","000000").toString();
            }
            int ty=Integer.parseInt(drp.substring(0,4));
            int tm=Integer.parseInt(drp.substring(4,6));
            if(ty==y1&&tm==m1){
                d1.add(y1+"???"+m1+"???");
                d2.add(Integer.parseInt(rp.get(rpNum).getOrDefault("sum","0").toString()));
                d3.add(Integer.parseInt(rp.get(rpNum).getOrDefault("count","0").toString()));
                rpNum++;
            }else{
                d1.add(y1+"???"+m1+"???");
                d2.add(0);
                d3.add(0);
            }
            m1++;
        }
        mD2.put("data",d2);
        mD3.put("data",d3);
        series.add(mD2);
        series.add(mD3);
        xAxis.put("data",d1);
        res.put("xAxis",xAxis);
        res.put("series",series);
        return res;
    }


    public List<Map<String,Object>> getBonusTypeNum(String start,String end){
        String r=end.substring(7);
        int m=Integer.parseInt(end.substring(5,7));
        int y=Integer.parseInt(end.substring(0,4));
        m++;
        if(m>12){
            y++;
            m=1;
        }
        if(m<10){
            end=y+"-"+"0"+m+r;
        }else{
            end=y+"-"+m+r;
        }
        return bonusMapper.getBonusTypeNum(start,end);
    }

    public List<String> getBonusType(){
        return bonusMapper.getBonusType();
    }
    public Map<String,Object> getBonusByID(String Bonus_type){
        return bonusMapper.getBonusByID(Bonus_type);
    }

    public void exportList(HttpServletResponse httpResponse) throws Exception {

        List<Map<String,Object>> resultList = getAmountInfo(); //????????????????????????????????????????????????????????????
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("????????????");
        int rowNo = 0;
        int sum=0;

        HSSFRow titleRow = sheet.createRow(rowNo++);

        for (int i = 0; i < excelFileCols.length; i++) {
            HSSFCell titleCell = titleRow.createCell(i);
            titleCell.setCellValue(excelFileCols[i]);
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        for (Map<String,Object> item : resultList){
            HSSFRow row = sheet.createRow(rowNo++);
            for (int i = 0; i < excelFileCols.length; i++) {
                HSSFCell cell = row.createCell(i);
                switch (excelFileCols[i]) {
                    case "????????????":
                        cell.setCellValue(item.getOrDefault("bl_name","").toString());
                        break;
                    case "????????????":
                        cell.setCellValue(item.getOrDefault("work_num","").toString());
                        break;
                    case "?????????":
                        cell.setCellValue(item.getOrDefault("applicant_name","").toString());
                        break;
                    case "??????":
                        cell.setCellValue(item.getOrDefault("position_name","").toString());
                        break;
                    case "?????????":
                        cell.setCellValue(item.getOrDefault("admin_name","").toString());
                        break;
                    case "??????":
                        cell.setCellValue(item.getOrDefault("amount","0").toString());
                        sum+=Integer.parseInt(item.getOrDefault("amount","0").toString());
                        break;
                    case "????????????":
                        cell.setCellValue(item.getOrDefault("grant_time","").
                                toString().replace('T',' ').replace(".0",""));
                        break;
                    default:
                        break;
                }
            }
        }

        HSSFRow row = sheet.createRow(rowNo++);
        for (int i = 0; i < excelFileCols.length; i++) {
            HSSFCell cell = row.createCell(i);
            switch (excelFileCols[i]) {
                case "????????????":
                    cell.setCellValue("??????");
                    break;
                case "??????":
                    cell.setCellValue(String.valueOf(sum));
                    break;
                default:
                    break;
            }
        }

        try{
            String fileName = URLEncoder.encode( "??????????????????.xls","utf-8");
            httpResponse.setContentType("application/x-msdownload; charset=UTF-8");
            httpResponse.setHeader("Content-disposition", "attachment; filename=" + fileName);
            OutputStream os = httpResponse.getOutputStream();
            workbook.write(os);
            os.flush();
            os.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private static final String[] excelFileCols = {
            "????????????",
            "????????????",
            "?????????",
            "??????",
            "?????????",
            "??????",
            "????????????"
    };
}
