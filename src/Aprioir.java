import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;


public class Aprioir {
	static int support=5000;
	static int split=-1;
	public static void main(String[] args)throws IOException{
		
		HashMap<Integer, ArrayList<Integer>> C1data =new HashMap<Integer, ArrayList<Integer>>();
		ArrayList<Integer> peoples = new ArrayList<Integer>();
		long time=System.currentTimeMillis();//計時開始
		//用fileinputStream來讀取二進制
		FileInputStream fi = new FileInputStream("./T15I6N0.5KD1000K.txt");
		byte[] b = new byte[fi.available()];
		fi.read(b);
		//位元長度
		System.out.println("Data.size="+b.length);
		System.out.println("readtime="+(System.currentTimeMillis()-time)/1000f+" m秒 ");
		int Intitem = 0;//轉換的item暫存
		int enter=1;//行數
		for(int i=0;i<b.length;i++){
			if((b[i])-47>0){//ASCII48="0"
				Intitem=Intitem*10+(int)(b[i])-48;
			}else if(b[i]==44 || b[i]==13){//ASCII48=","
				peoples=C1data.get(Intitem);
				if (peoples == null) {				//myList假如為空，則新增
					peoples = new ArrayList<Integer>();
				}
				peoples.add(enter);
				C1data.put(Intitem,peoples);
				Intitem=0;
				if(b[i]==13){//ASCII48="換行"
					enter++;
				}
			}
			
		}
		fi.close();
		System.out.println("L1.size="+C1data.size());
		//System.out.println("C1data="+C1data);
		C1remix(C1data);
		C1data.clear();
	    System.out.println("執行耗時= "+(System.currentTimeMillis()-time)/1000f+" 秒 ");//結束的時間	    
	}
	public static ArrayList<Integer> compare(ArrayList<Integer> a,ArrayList<Integer> b)
	{ 
		ArrayList<Integer> commendpeople = new ArrayList<Integer>();
		int countA=split+1;
		int countB=split+1;
		while(countA <a.size() && countB < b.size()){//&& (a.size()-countA)+commendpeople.size()>=support && (b.size()-countB)+commendpeople.size()>=support
			//countA <a.size() && countB < b.size()
			if(((a.size()-countA)+commendpeople.size())<support && ((b.size()-countB)+commendpeople.size())<support){
				return commendpeople;
			}else{
				if(a.get(countA).equals(b.get(countB))){
					commendpeople.add(a.get(countA));
					countA++;
					countB++;

				}
				else if(a.get(countA)>b.get(countB)){
					countB++;	
				}
				else{
					countA++;	
				}
				
			}
		}
		return commendpeople;
	}
	public static void C1remix(HashMap<Integer, ArrayList<Integer>> C1data){
		
		HashMap<Integer, ArrayList<Integer>> alldata =new HashMap<Integer, ArrayList<Integer>>();
		int i,j,count=0,size=C1data.size();
		ArrayList<Integer> temp = new ArrayList<Integer>();
		for (i=0;i<size;i++){
			if(C1data.get(i)==null|| C1data.get(i).size()<support){
				continue;
			}
			for (j=i+1;j<size;j++){
				if(C1data.get(j)==null|| C1data.get(j).size()<support){
					continue;
				}
				if(C1data.get(i).size() >support && C1data.get(j).size() >support && (temp=compare(C1data.get(i),C1data.get(j))).size()>support){
					temp.add(0, i);
					temp.add(1, j);
					alldata.put(count++,temp);
					
				}
			}
			C1data.remove(i);
		}
		split=1;
		System.out.println("L2.size="+alldata.size());
		//System.out.println("L2="+alldata);
		/*for(i=0;i<alldata.size();i++){
			System.out.println(alldata.get(i));  
		}*/
		datacompare(alldata);
	}

	public static void datacompare(HashMap<Integer, ArrayList<Integer>> data){
		HashMap<Integer, ArrayList<Integer>> alldata2 =new HashMap<Integer, ArrayList<Integer>>();
		ArrayList<Integer> temp = new ArrayList<Integer>();
		int i,j,q,count=0,size=data.size();
		
		for(i=0;i<size;i++){
			for(j=i+1;j<size;j++){
				if(level(data.get(i),data.get(j))){
					if((temp=compare(data.get(i),data.get(j))).size()>support){
						for(q=0;q<split;q++){
							temp.add(q, data.get(i).get(q));
						}
						temp.add(split, data.get(i).get(split));
						temp.add(split+1, data.get(j).get(split));
						alldata2.put(count++, temp);
						System.out.println(temp);
					}
				}else{
					break;
				}
			}
				data.remove(i);	
		}
		
		System.out.println("L"+(split+2)+".size="+alldata2.size()); 
		System.out.println("L"+(split+2)+"="+alldata2); 
		if(alldata2.size()>0){
			data.clear();
			split++;
			datacompare(alldata2);
		}else{
			System.out.println("split="+(split+1));
			System.out.println("support="+support);
		}
	}

	
	public static boolean level(ArrayList<Integer> a,ArrayList<Integer> b){
		
		int bit=0;
		while(a.get(bit) == b.get(bit)){
			bit++;
		}
		return (bit==split);
	}
	
}
