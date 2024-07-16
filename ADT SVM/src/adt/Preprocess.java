/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package adt;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 *
 * @author admin
 */
public class Preprocess 
{
    Details dt=new Details();
    
    Preprocess()
    {
        
    }
    
    public void readData()
    {
         try
        {
            File fe=new File(dt.inPath);
            FileInputStream fis=new FileInputStream(fe);
            byte bt[]=new byte[fis.available()];
            fis.read(bt);
            fis.close();
            
            System.out.println("=====================  Data Set ======================= ");
            System.out.println(new String(bt));
            String g=new String(bt).trim();
            
            String g1[]=g.split("\n");
            String gs=g1[0];
            dt.cname=gs;
            dt.ctype=g1[1];
            
            dt.colType=g1[1].split(",");
            
            dt.colName=gs.split(",");
            for(int i=0;i<dt.colName.length;i++)
                dt.colList.add(dt.colName[i].trim());
                
            dt.orgData=new String[g1.length-2][dt.colName.length];
          
            for(int i=2;i<g1.length;i++)
            {
                dt.dataList.add(g1[i]);
                String g2[]=g1[i].split(",");
                for(int j=0;j<g2.length;j++)
                {
                    dt.orgData[i-2][j]=g2[j];
                }
            }
                  
            
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }       
    }
    
    public void splitTrainTest()
    {
        try
        {
            int tr1=(int)(0.7*dt.orgData.length);
            
            int te1=(int)(0.3*dt.orgData.length);
            
            
            
            String tr=dt.cname;
            
            for(int i=0;i<tr1;i++)
            {
                tr=tr+"\n"+dt.dataList.get(i).toString().trim();
            }
            
            File fe1=new File("train1.csv");
            FileOutputStream fos1=new FileOutputStream(fe1);
            fos1.write(tr.getBytes());
            fos1.close();
            
            
            String tr2=dt.cname+"\n"+dt.ctype;
            
            for(int i=0;i<tr1;i++)
            {
                tr2=tr2+"\n"+dt.dataList.get(i).toString().trim();
            }
            
            File fe11=new File("train2.csv");
            FileOutputStream fos11=new FileOutputStream(fe11);
            fos11.write(tr2.getBytes());
            fos11.close();
            
            
            System.out.println(tr1+"  : "+te1);
            
            String te=dt.cname;
            for(int i=tr1;i<dt.dataList.size();i++)
            {
                te=te+"\n"+dt.dataList.get(i).toString().trim();
            }
            
            File fe2=new File("test1.csv");
            FileOutputStream fos2=new FileOutputStream(fe2);
            fos2.write(te.getBytes());
            fos2.close();
            
            String te2=dt.cname+"\n"+dt.ctype;
            for(int i=tr1;i<dt.dataList.size();i++)
            {
                te2=te2+"\n"+dt.dataList.get(i).toString().trim();
            }
            
            File fe21=new File("test2.csv");
            FileOutputStream fos21=new FileOutputStream(fe21);
            fos21.write(te2.getBytes());
            fos21.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}
