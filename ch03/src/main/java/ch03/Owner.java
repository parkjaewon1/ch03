package ch03;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
@Data
public class Owner {
	private String ownerName;
	private List<Pet> petList = new ArrayList<Pet>();
}