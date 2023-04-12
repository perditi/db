package helper_stuff;

import java.util.*;

public class Kill_Me {
	String[] amenities = {"\'TV\'", "\'AC\'", "\'Fridge\'"};
	String[] capacities = {"\'Single\'", "\'Double\'", "\'Triple\'", "\'Quad\'"};

	String[] viewTypes = {"\'Sea View\'", "\'Mountain View\'"};

	public Kill_Me() {
	}


	public String[] getAmenities(int n) throws Exception {
		if (n > 3 || n < 0) {
			throw new Exception("invalid n");
		}
		return Arrays.copyOfRange(amenities, 0, n);
	}

	public String getCapacity() {
		int num = (int) (Math.random() * 3);
		return capacities[num];
	}

	public double getPrice(){//say price $50 < x < $300
		double temp = (Math.random() * 250) + 50;
		String tempString = String.format("%.2f", temp);
		return Double.parseDouble(tempString);
	}

	public String getViewType(){
		int num = (int) (Math.random() * 2);
		return viewTypes[num];
	}

	public String getExtendable(){
		int num = (int) (Math.random() * 2);
		if (num == 0){
			return "true";
		} else {
			return "false";
		}
	}

	public static void main(String[] args) throws Exception{
		Kill_Me f = new Kill_Me();

		for (int i = 0; i < 40; i++) {//40
			for (int j = 0; j < 15; j++) {//15
				StringBuffer str = new StringBuffer();
				str.append("(");
				str.append( (i * 15) + (j + 1) );//roomID
				str.append(", ");
				str.append(i + 1);//hotelID
				str.append(", ");
				str.append((i/8) + 1);//hotelChainID;
				str.append(", ");
				str.append(f.getPrice());//price
				str.append(", ");
				str.append(f.getCapacity());//cap
				str.append(", ");
				str.append(f.getViewType());//view
				str.append(", ARRAY[");
				String[] temp = f.getAmenities((int) (Math.random()*4));
				for (int k = 0; k < temp.length; k++){
					str.append(temp[k]);
					if (k < temp.length - 1){
						str.append(", ");
					}
				}
				str.append("]::AMENITY[], ");
				str.append(f.getExtendable());
				str.append(", \'no\'),");
				System.out.println(str.toString());
			}
		}
	}
}