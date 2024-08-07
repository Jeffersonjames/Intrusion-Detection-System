/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package adt;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;
import libsvm.svm;
import libsvm.svm_model;
import libsvm.svm_node;
import libsvm.svm_parameter;

/**
 *
 * @author admin
 */
public class SVMPredict 
{
    private static double atof(String s)
    {
	return Double.valueOf(s).doubleValue();
    }

    private static int atoi(String s)
    {
	return Integer.parseInt(s);
    }

    public static String predict(BufferedReader input, DataOutputStream output, svm_model model, int predict_probability) throws IOException
    {
        String res="";
	int correct = 0;
	int total = 0;
	int err=0;
	double error = 0;
	double sumv = 0, sumy = 0, sumvv = 0, sumyy = 0, sumvy = 0;
	int svm_type=svm.svm_get_svm_type(model);
	int nr_class=svm.svm_get_nr_class(model);
	double[] prob_estimates=null;
        int k=0;
	if(predict_probability == 1)
	{
            if(svm_type == svm_parameter.EPSILON_SVR ||
               svm_type == svm_parameter.NU_SVR)
            {
		System.out.print("Prob. model for test data: target value = predicted value + z,\nz: Laplace distribution e^(-|z|/sigma)/(2sigma),sigma="+svm.svm_get_svr_probability(model)+"\n");
            }
            else
            {
		int[] labels=new int[nr_class];
		svm.svm_get_labels(model,labels);
		prob_estimates = new double[nr_class];
		output.writeBytes("labels");
		for(int j=0;j<nr_class;j++)
                    output.writeBytes(" "+labels[j]);
		output.writeBytes("\n");
            }
	}
	while(true)
	{
            String line = input.readLine();
            if(line == null) break;
		StringTokenizer st = new StringTokenizer(line," \t\n\r\f:");
            double target = atof(st.nextToken());
            int m = st.countTokens()/2;
            svm_node[] x = new svm_node[m];
            for(int j=0;j<m;j++)
            {
		x[j] = new svm_node();
		x[j].index = atoi(st.nextToken());
		x[j].value = atof(st.nextToken());
            }
            double v;
            if (predict_probability==1 && (svm_type==svm_parameter.C_SVC || svm_type==svm_parameter.NU_SVC))
            {
                v = svm.svm_predict_probability(model,x,prob_estimates);                
		output.writeBytes(v+" ");
		for(int j=0;j<nr_class;j++)
                    output.writeBytes(prob_estimates[j]+" ");
		output.writeBytes("\n");
            }
            else
            {
		v = svm.svm_predict(model,x);
		output.writeBytes(v+"\n");
            }
            //if(target==0.0)
              //  target=-1;
            if(v == target)
            {
		++correct;
		System.out.println(k+" : "+"correct : "+v+" : "+target);
                res=res+"correct\n";
            }
            else
            {
		err++;
		System.out.println(k+" : --- : "+v+" : "+target);
                res=res+"wrong\n";
            }	
		k++;	
            error += (v-target)*(v-target);
            sumv += v;
            sumy += target;
            sumvv += v*v;
            sumyy += target*target;
            sumvy += v*target;
            ++total;
	}
	if(svm_type == svm_parameter.EPSILON_SVR ||
	   svm_type == svm_parameter.NU_SVR)
	{
            System.out.print("Mean squared error = "+error/total+" (regression)\n");
            System.out.print("Squared correlation coefficient = "+
			 ((total*sumvy-sumv*sumy)*(total*sumvy-sumv*sumy))/
			 ((total*sumvv-sumv*sumv)*(total*sumyy-sumy*sumy))+
			 " (regression)\n");
	}
	else
	{
            System.out.print("Accuracy = "+(double)correct/total*100+
			 "% ("+correct+"/"+total+") (classification)\n");
            System.out.println(correct+" : "+error+" : "+err)	 ;
	}	
        
        return res;
    }

    public static void main(String argv[]) throws Exception
    {
    	int i, predict_probability=0;
        try 
	{
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
            predict(input,output,model,predict_probability);
            input.close();
            output.close();
	} 
	catch(Exception e) 
	{
	
	}
		
    }
}
