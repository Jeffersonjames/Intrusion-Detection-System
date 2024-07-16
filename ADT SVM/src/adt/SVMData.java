/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package adt;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;

/**
 *
 * @author admin
 */
public class SVMData 
{
    
    String dSet[][];
     int nData[][];
     ArrayList cls=new ArrayList();
    ArrayList clsCnt=new ArrayList();
     String colName[];
     String colType[];
    
    SVMData()
    {
        
    }
    
    public void readTrData(String path)
    {
        try
        {
            cls=new ArrayList();
            clsCnt=new ArrayList();
            
            File fe=new File(path);
            FileInputStream fis=new FileInputStream(fe);
            byte data[]=new byte[fis.available()];
            fis.read(data);
            fis.close();
            
            String sg1[]=new String(data).split("\n");
            String col[]=sg1[0].split(",");
            String colty[]=sg1[1].split(",");
            colName=new String[col.length];
            colType=new String[col.length];
            
            for(int i=0;i<col.length;i++)
            {
                colName[i]=col[i];
                colType[i]=colty[i];                
            }
            
            dSet=new String[sg1.length-2][col.length];
            nData=new int[sg1.length-2][col.length];
            
            for(int i=2;i<sg1.length;i++)
            {
                String sg2[]=sg1[i].split(",");
                for(int j=0;j<sg2.length;j++)
                {
                    dSet[i-2][j]=sg2[j];
                }
                String c1=sg2[sg2.length-1].trim();
                if(!cls.contains(c1))
                    cls.add(c1);
            }
            System.out.println("cls "+cls);           
           
            
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    
    
    public void convertTrData(String name)
    {
        try
        {
            for(int i=0;i<colType.length;i++)
            {
                if(colType[i].trim().equals("dis"))
                {
                    ArrayList at=new ArrayList();
                    for(int j=0;j<dSet.length;j++)
                    {
                        String g1=dSet[j][i].trim();
                        if(!at.contains(g1))
                            at.add(g1);                            
                    }
                    
                    for(int j=0;j<dSet.length;j++)
                    {
                        String g1=dSet[j][i].trim();
                        nData[j][i]=at.indexOf(g1);
                    }
                }
                else
                {
                    for(int j=0;j<dSet.length;j++)
                    {
                        dSet[j][i]=String.valueOf(Math.round(Double.parseDouble(dSet[j][i])));
                        nData[j][i]=Integer.parseInt(dSet[j][i]);
                    }     
                }
            }
            String txt1="";
            for(int i=0;i<nData.length;i++)
            {
               // String g1="";
                String g1=String.valueOf(nData[i][nData[0].length-1]);
                for(int j=0;j<nData[0].length-1;j++)
                {
                    g1=g1+"\t"+nData[i][j];
                   // g1=g1+nData[i][j]+"\t";
                }
                txt1=txt1+g1.trim()+"\n";
                
            }
            System.out.println(txt1);
            
            File fe=new File(name);
            FileOutputStream fos=new FileOutputStream(fe);
            fos.write(txt1.getBytes());
            fos.close();
            
            
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    
   
    
}
