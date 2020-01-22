package server;

import java.util.ArrayList;
import java.util.List;

public class NumeronAI {
	List<String> new_can_list = new ArrayList<>();//candidate list
	String can_num;//candidate number


	//考えられる候補数に絞る
	public NumeronAI Squeeze(int eat,int bite,String pred_num,String ca_num,List<String> old_list){
		NumeronAI squeeze = new NumeronAI();
		List<String> can_list = new ArrayList<>();
		List<String> li = new ArrayList<>();

		if(eat == 0 && bite == 0){
			//System.out.println("--------" + String.valueOf(ca_num.length()));
			for(int i = 0; i<ca_num.length();i++){
				if(ca_num.charAt(i) != pred_num.charAt(0) && ca_num.charAt(i) != pred_num.charAt(1) && ca_num.charAt(i) != pred_num.charAt(2)){
					li.add(String.valueOf(ca_num.charAt(i)));
				}
			}
			ca_num ="";
			StringBuilder builder = new StringBuilder();
			for(String num : li){
				builder.append(num);
			}
			ca_num = builder.substring(0,builder.length());

			for(int i = 0;i<ca_num.length();i++){
				for(int j = 0;j<ca_num.length();j++){
					for(int k = 0;k<ca_num.length();k++){
						if(ca_num.charAt(i)!=ca_num.charAt(j) && ca_num.charAt(i)!=ca_num.charAt(k) && ca_num.charAt(j)!=ca_num.charAt(k)){
							can_list.add(String.valueOf(ca_num.charAt(i))+String.valueOf(ca_num.charAt(j))+String.valueOf(ca_num.charAt(k)));
						}
					}
				}
			}
		}else if(eat ==0 && bite ==1){
			for(int i = 0;i<ca_num.length();i++){
				for(int j = 0;j<ca_num.length();j++){
					if(ca_num.charAt(i) != ca_num.charAt(j) && ca_num.charAt(i)!=pred_num.charAt(0) && ca_num.charAt(i)!=pred_num.charAt(1) && ca_num.charAt(i)!=pred_num.charAt(2)){
						can_list.add(String.valueOf(ca_num.charAt(i))+String.valueOf(pred_num.charAt(0))+String.valueOf(ca_num.charAt(j)));
						can_list.add(String.valueOf(ca_num.charAt(i))+String.valueOf(ca_num.charAt(j))+String.valueOf(pred_num.charAt(0)));
						can_list.add(String.valueOf(pred_num.charAt(1))+String.valueOf(ca_num.charAt(i))+String.valueOf(ca_num.charAt(j)));
						can_list.add(String.valueOf(ca_num.charAt(i))+String.valueOf(ca_num.charAt(j))+String.valueOf(pred_num.charAt(1)));
						can_list.add(String.valueOf(pred_num.charAt(2))+String.valueOf(ca_num.charAt(i))+String.valueOf(ca_num.charAt(j)));
						can_list.add(String.valueOf(ca_num.charAt(i))+String.valueOf(pred_num.charAt(2))+String.valueOf(ca_num.charAt(j)));
					}

				}
			}
		}else if(eat ==0 && bite ==2){
			for(int i = 0;i<ca_num.length();i++){
				if(ca_num.charAt(i)!=pred_num.charAt(0) && ca_num.charAt(i)!=pred_num.charAt(1) && ca_num.charAt(i)!=pred_num.charAt(2)){
					can_list.add(String.valueOf(ca_num.charAt(i))+String.valueOf(pred_num.charAt(0))+String.valueOf(pred_num.charAt(1)));
					can_list.add(String.valueOf(ca_num.charAt(i))+String.valueOf(pred_num.charAt(2))+String.valueOf(pred_num.charAt(0)));
					can_list.add(String.valueOf(ca_num.charAt(i))+String.valueOf(pred_num.charAt(2))+String.valueOf(pred_num.charAt(1)));
					can_list.add(String.valueOf(pred_num.charAt(1))+String.valueOf(pred_num.charAt(0))+String.valueOf(ca_num.charAt(i)));
					can_list.add(String.valueOf(pred_num.charAt(1))+String.valueOf(ca_num.charAt(i))+String.valueOf(pred_num.charAt(0)));
					can_list.add(String.valueOf(pred_num.charAt(1))+String.valueOf(pred_num.charAt(2))+String.valueOf(ca_num.charAt(i)));
					can_list.add(String.valueOf(pred_num.charAt(2))+String.valueOf(ca_num.charAt(i))+String.valueOf(pred_num.charAt(0)));
					can_list.add(String.valueOf(pred_num.charAt(2))+String.valueOf(ca_num.charAt(i))+String.valueOf(pred_num.charAt(1)));
					can_list.add(String.valueOf(pred_num.charAt(2))+String.valueOf(pred_num.charAt(0))+String.valueOf(ca_num.charAt(i)));
				}
			}
		}else if(eat == 0 && bite ==3){
			can_list.add(String.valueOf(pred_num.charAt(1))+String.valueOf(pred_num.charAt(2))+String.valueOf(pred_num.charAt(0)));
			can_list.add(String.valueOf(pred_num.charAt(2))+String.valueOf(pred_num.charAt(0))+String.valueOf(pred_num.charAt(1)));

		}else if(eat == 1 && bite ==0){
			for(int i = 0;i<ca_num.length();i++){
				for(int j = 0;j<ca_num.length();j++){
					if(ca_num.charAt(i)!=ca_num.charAt(j) && ca_num.charAt(i)!=pred_num.charAt(0) && ca_num.charAt(i)!=pred_num.charAt(1) &&
							ca_num.charAt(i)!=pred_num.charAt(2) && ca_num.charAt(j)!=pred_num.charAt(0) &&
							ca_num.charAt(j)!=pred_num.charAt(1) && ca_num.charAt(j)!=pred_num.charAt(2)){
					can_list.add(String.valueOf(pred_num.charAt(0))+String.valueOf(ca_num.charAt(i))+String.valueOf(ca_num.charAt(j)));
					can_list.add(String.valueOf(ca_num.charAt(i))+String.valueOf(pred_num.charAt(1))+String.valueOf(ca_num.charAt(j)));
					can_list.add(String.valueOf(ca_num.charAt(i))+String.valueOf(ca_num.charAt(j))+String.valueOf(pred_num.charAt(2)));
					}
				}
			}

		}else if(eat ==1 && bite ==1){
			for(int i = 0;i<ca_num.length();i++){
				if(ca_num.charAt(i)!=pred_num.charAt(0) && ca_num.charAt(i)!=pred_num.charAt(1) && ca_num.charAt(i)!=pred_num.charAt(2)){
					can_list.add(String.valueOf(pred_num.charAt(0))+String.valueOf(ca_num.charAt(i))+String.valueOf(pred_num.charAt(1)));
					can_list.add(String.valueOf(pred_num.charAt(0))+String.valueOf(pred_num.charAt(2))+String.valueOf(ca_num.charAt(i)));
					can_list.add(String.valueOf(ca_num.charAt(i))+String.valueOf(pred_num.charAt(1))+String.valueOf(pred_num.charAt(0)));
					can_list.add(String.valueOf(pred_num.charAt(2))+String.valueOf(pred_num.charAt(1))+String.valueOf(ca_num.charAt(i)));
					can_list.add(String.valueOf(pred_num.charAt(1))+String.valueOf(ca_num.charAt(i))+String.valueOf(pred_num.charAt(2)));
					can_list.add(String.valueOf(ca_num.charAt(i))+String.valueOf(pred_num.charAt(0))+String.valueOf(pred_num.charAt(2)));
				}
			}

		}else if(eat ==1 && bite ==2){
			for(int i = 0;i<ca_num.length();i++){
				can_list.add(String.valueOf(pred_num.charAt(0))+String.valueOf(pred_num.charAt(2))+String.valueOf(pred_num.charAt(1)));
				can_list.add(String.valueOf(pred_num.charAt(2))+String.valueOf(pred_num.charAt(1))+String.valueOf(pred_num.charAt(0)));
				can_list.add(String.valueOf(pred_num.charAt(1))+String.valueOf(pred_num.charAt(0))+String.valueOf(pred_num.charAt(2)));
			}

		}else if(eat ==2 && bite ==0){
			for(int i = 0;i<ca_num.length();i++){
				if(ca_num.charAt(i)!=pred_num.charAt(0) && ca_num.charAt(i)!=pred_num.charAt(1) && ca_num.charAt(i)!=pred_num.charAt(2)){
					can_list.add(String.valueOf(pred_num.charAt(0))+String.valueOf(pred_num.charAt(1))+String.valueOf(ca_num.charAt(i)));
					can_list.add(String.valueOf(pred_num.charAt(0))+String.valueOf(ca_num.charAt(i))+String.valueOf(pred_num.charAt(2)));
					can_list.add(String.valueOf(ca_num.charAt(i))+String.valueOf(pred_num.charAt(1))+String.valueOf(pred_num.charAt(2)));
				}
			}

		}else if(eat ==3 && bite ==0){
			can_list.add(pred_num);
		}

		if(old_list.size()!=0){
			for(String num : can_list){
				if(old_list.contains(num)){
					squeeze.new_can_list.add(num);
					squeeze.can_num =ca_num;
				}
			}

		}else{
			squeeze.new_can_list = can_list;
			squeeze.can_num = ca_num;
		}
		//System.out.println(can_num);
		return squeeze;
	}


	//期待候補数を計算する
	public double count_cand(String pred_num,String ca_num,List<String> ca_list){
		double ave_ca = 0;
		int[][] info_list = {{0,0},{0,1},{0,2},{0,3},{1,0},{1,1},{1,2},{2,1},{3,0}};
		int sum_ex = 0;
		int sum_ex2 = 0;
		List<String> old_count_list = new ArrayList<>(ca_list);
		String ca_count_num = ca_num;
		NumeronAI squeeze2 = new NumeronAI();
		for(int[] info :info_list){
			squeeze2 = Squeeze(info[0],info[1],pred_num,ca_count_num,old_count_list);
			sum_ex=sum_ex+squeeze2.new_can_list.size();
			sum_ex2=sum_ex2+squeeze2.new_can_list.size()^2;
		}
		if(sum_ex!=0){
			ave_ca=sum_ex2/sum_ex;
		}
		return ave_ca;
	}


	//期待候補数が最小の数値を選択する
	public String choice(List<String> ca_list,String ca_num){
		List<Double> ave_list = new ArrayList<>();
		int min_index =0;
		try{
			for(String num :ca_list){
				double ave_ca = count_cand(num,ca_num,ca_list);
				ave_list.add(ave_ca);
			}
			double min =ave_list.get(0);
			for(int i =0;i<ave_list.size();i++){
				double val = ave_list.get(i);
				if(min > val){
					min = val;
					min_index = i;
				}
			}
			return ca_list.get(min_index);
		}catch(Exception e){
		System.out.println("チョイスミス:" + e);
		return "111";
	}
	}


}
