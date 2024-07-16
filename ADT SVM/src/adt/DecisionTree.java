/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package adt;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Random;
import libsvm.svm;
import libsvm.svm_model;

import weka.classifiers.Evaluation;
import weka.core.Instances;
import weka.core.converters.CSVLoader;

import weka.classifiers.trees.J48;


/**
 *
 * @author admin
 */
public class DecisionTree 
{
    Details dt=new Details();
    
  
  
     ArrayList newCls=new ArrayList();
    public void construct()
    {
        try
        {
            CSVLoader csv1=new CSVLoader();
            csv1.setSource(new File("train1.csv"));
		 
            Instances trdata=csv1.getDataSet();
            trdata.setClassIndex(trdata.numAttributes() - 1);
            J48 nb=new J48();
            nb.buildClassifier(trdata);
            
            CSVLoader csv2=new CSVLoader();
            csv2.setSource(new File("test1.csv"));
		 
            Instances tedata=csv2.getDataSet();
            tedata.setClassIndex(tedata.numAttributes() - 1);           
           
                       
            Evaluation eval = new Evaluation(trdata);
            eval.evaluateModel(nb, trdata);//, null);          
            
            for(int i=0;i<tedata.numInstances();i++)
            {
                int ind=(int)nb.classifyInstance(tedata.instance(i));
                newCls.add(ind);
               // int it=(int)tedata.instance(i).classValue();
               // int ind=(int)nb.classifyInstance(tedata.instance(i));
              //  System.out.println(it+" : "+ind);
            }
            
            
            ocSVM();
            System.out.println(eval.toClassDetailsString());            
            ADTocSVM();
            
            
            
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    
   public void ocSVM()
   {
       try
       {
            SVMData svm1=new SVMData();
            svm1.readTrData("train2.csv");
            svm1.convertTrData("train1.txt");
            
            SVMTrain svmtr=new SVMTrain();
            svmtr.run();
            
            readData("test2.csv");
            
            
            
            int i, predict_probability=0;
            SVMPredict sm=new SVMPredict();
            BufferedReader input = new BufferedReader(new FileReader("test1.txt"));
             
            DataOutputStream output = new DataOutputStream(new BufferedOutputStream(new FileOutputStream("Res1.txt")));
            svm_model model = svm.svm_load_model("train1.model");
            if(predict_probability == 1)
            {
		if(svm.svm_check_probability_model(model)==0)
            	{
                    System.err.print("Model does not support probabiliy estimates\n");
                    System.exit(1);
		}
            }
            else
            {
		if(svm.svm_check_probability_model(model)!=0)
		{
                    System.out.print("Model supports probability estimates, but disabled in prediction.\n");
		}
            }
            String res=sm.predict(input,output,model,predict_probability);
            input.close();
            output.close();
       }       
       catch(Exception e)
       {
           e.printStackTrace();
       }
   }
   
   public void ADTocSVM()
   {
       try
       {
            SVMData svm1=new SVMData();
            svm1.readTrData("train2.csv");
            svm1.convertTrData("train1.txt");
            
            SVMTrain svmtr=new SVMTrain();
            svmtr.run();
            
            
            convertData2("test2.csv");
            
            
            int i, predict_probability=0;
            SVMPredict sm=new SVMPredict();
             BufferedReader input = new BufferedReader(new FileReader("newtest1.txt"));
            DataOutputStream output = new DataOutputStream(new BufferedOutputStream(new FileOutputStream("Res1.txt")));
            svm_model model = svm.svm_load_model("train1.model");
            if(predict_probability == 1)
            {
		if(svm.svm_check_probability_model(model)==0)
            	{
                    System.err.print("Model does not support probabiliy estimates\n");
                    System.exit(1);
		}
            }
            else
            {
		if(svm.svm_check_probability_model(model)!=0)
		{
                    System.out.print("Model supports probability estimates, but disabled in prediction.\n");
		}
            }
            String res=sm.predict(input,output,model,predict_probability);
            input.close();
            output.close();
       }       
       catch(Exception e)
       {
           e.printStackTrace();
       }
   }
   
    public void readData(String pp)
    {
        try
        {
              String dSet[][];
                int nData[][];
            ArrayList cls=new ArrayList();
            ArrayList clsCnt=new ArrayList();
            String colName[];
            String colType[];
            
            File fe=new File(pp);
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
                   dSet[i-2][j]=sg2[j]; //org
                
                }
                String c1=sg2[sg2.length-1].trim();
                
                if(!cls.contains(c1))
                    cls.add(c1);
            }
            System.out.println("cls "+cls);           
            System.out.println("clsCnt "+clsCnt);
            System.out.println("dset = "+dSet.length+" : "+dSet[0].length);
            
        
            for(int i=0;i<colType.length;i++)
            {
                if(colType[i].trim().equals("dis"))
                {
                    ArrayList at=new ArrayList();
                    for(int j=0;j<dSet.length;j++)
                    {
                        //System.out.println("i=== "+i+" : "+j+" = "+dSet[j][i]);
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
                    //g1=g1+nData[i][j]+"\t";
                }
                txt1=txt1+g1.trim()+"\n";
                
            }
            System.out.println(txt1);
            
            File fe2=new File("test1.txt");
            FileOutputStream fos=new FileOutputStream(fe2);
            fos.write(txt1.getBytes());
            fos.close();
            
            
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    
    public void convertData2(String pp)
    {
        try
        {
              String dSet[][];
                int nData[][];
            ArrayList cls=new ArrayList();
            ArrayList clsCnt=new ArrayList();
            String colName[];
            String colType[];
            
            File fe=new File(pp);
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
                   dSet[i-2][j]=sg2[j]; //org
                
                }
                String c1=sg2[sg2.length-1].trim();
                
                if(!cls.contains(c1))
                    cls.add(c1);
            }
            System.out.println("cls "+cls);           
            System.out.println("clsCnt "+clsCnt);
            System.out.println("dset = "+dSet.length+" : "+dSet[0].length);
            
            for(int i=0;i<colType.length;i++)
            {
                if(colType[i].trim().equals("dis"))
                {
                    ArrayList at=new ArrayList();
                    for(int j=0;j<dSet.length;j++)
                    {
                        //System.out.println("i=== "+i+" : "+j+" = "+dSet[j][i]);
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
                //String g1=String.valueOf(nData[i][nData[0].length-1]);
                String g1=newCls.get(i).toString();
                //String g1="";
                for(int j=0;j<nData[0].length-1;j++)
                {
                    g1=g1+"\t"+nData[i][j];
                    //g1=g1+nData[i][j]+"\t";
                }
                g1=g1+newCls.get(nData[0].length-1);
                txt1=txt1+g1.trim()+"\n";
                
            }
            System.out.println(txt1);
            
            File fe2=new File("newtest1.txt");
            FileOutputStream fos=new FileOutputStream(fe2);
            fos.write(txt1.getBytes());
            fos.close();
            
            
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}
