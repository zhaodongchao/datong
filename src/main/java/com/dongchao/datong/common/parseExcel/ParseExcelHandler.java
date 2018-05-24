package com.dongchao.datong.common.parseExcel;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

@RestController
public class ParseExcelHandler {
    //处理文件上传
    @RequestMapping(value = "upload", method = RequestMethod.POST)
    public @ResponseBody
    String uploadImg(@RequestParam("file") MultipartFile file, HttpServletRequest request) throws IOException {
        String contentType = file.getContentType();
        String fileName = file.getOriginalFilename();
        System.out.println("fileName-->" + fileName);
        System.out.println("getContentType-->" + contentType);

        //检查文件信息
        String title = "123";
        Workbook workBook = null;
        if (fileName.endsWith(".xlsx")) {
            workBook = new XSSFWorkbook(file.getInputStream());
        } else if (fileName.endsWith(".xls")) {
            workBook = new HSSFWorkbook(file.getInputStream());
        } else {
            throw new IllegalArgumentException("请上传excel文件");
        }
        String filePath = "C:\\work\\document\\sql"+System.currentTimeMillis()+".sql" ;
        parseSheet(workBook,filePath);

        return "export success";
    }

    private void parseSheet(Workbook workBook,String filePath) {
        int total = workBook.getNumberOfSheets();
        Sheet sheet;

        for (int i = 0; i < total; i++) {
            try{

                sheet = workBook.getSheetAt(i);
                int totalRows = sheet.getPhysicalNumberOfRows();
                String tableName = sheet.getRow(0).getCell(2).getStringCellValue() == null ?"无法读取":sheet.getRow(0).getCell(2).getStringCellValue(); //表名
                String tableComment = sheet.getRow(2).getCell(1).getStringCellValue();
                String pkey = sheet.getRow(4).getCell(1).getStringCellValue();//主键
                String pkeyComment = sheet.getRow(4).getCell(4).getStringCellValue();

                StringBuffer sql_table =  new StringBuffer("CREATE TABLE IF NOT EXISTS ");
                sql_table.append(tableName.toLowerCase()) ;
                sql_table.append(" ( ");

                for (int n = 6; n < totalRows; n++) {  //开始读取表结构
                    Row row = sheet.getRow(n);
                    int totalCells = 5; //读取到是否为空这一列
                    // System.out.println("序号："+row.getCell(0).getNumericCellValue());
                    try{
                        if (row.getCell(0).getNumericCellValue() == 0) {
                            break;
                        }
                    }catch (Exception e){
                        break;
                    }
                    sql_table.append(row.getCell(1).getStringCellValue().toLowerCase());  //字段名
                    sql_table.append(" ");
                    sql_table.append(convertType(row.getCell(3).getStringCellValue()));  //类型

                    String isNull = row.getCell(5).getStringCellValue().equals("N") ? " not null " : " ";
                    sql_table.append(isNull); //是否为空

                    sql_table.append("comment ");
                    sql_table.append("'"+row.getCell(2).getStringCellValue()+"'");
                    sql_table.append(" ,");
                }

                //添加主键
                String[] primaryKeys = pkey.split(",");
                sql_table.append("primary key (");

                if (primaryKeys.length==1){
                    sql_table.append(primaryKeys[0]);
                }else {
                    for (int t=0 ; t<primaryKeys.length ; t++){
                        sql_table.append(primaryKeys[t]);
                        if (t < primaryKeys.length-1){
                            sql_table.append(",");
                        }
                    }

                }
                sql_table.append(") ) ");
                sql_table.append("ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='");
                sql_table.append(tableComment);
                sql_table.append("' ;\n");
                //System.out.println(sql_table.toString());
                writeToFile(sql_table.toString(),filePath);
            }catch (Exception e){
                System.out.println("出现错误，序号----->"+i+"--工作簿名称："+workBook.getSheetAt(i).getSheetName());
            }

        }
    }


    private void writeToFile(String str,String filePath){
        FileOutputStream fos ;
        try {
            fos = new FileOutputStream(filePath,true);
            fos.write(str.getBytes());
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String convertType(String type){
        if (null == type || "".equals(type)){
            return "error type" ;
        }
        if (type.startsWith("VARCHAR2")){
            return "varchar"+type.substring(type.indexOf("("),type.length() );
        }
        if (type.startsWith("NUMBER")){
            return type.replace("NUMBER","numeric");
        }
        return type ;
    }

}
