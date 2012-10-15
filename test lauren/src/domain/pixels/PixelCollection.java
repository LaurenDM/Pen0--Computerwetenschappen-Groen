package domain.pixels;

import java.util.List;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

public class PixelCollection {
	List<Pixel> pixelArrayList;
	public PixelCollection(Pixel[] pixelArray){
	this.pixelArrayList=Arrays.asList(pixelArray);
	}
	public List<Pixel> getPixels(){
		return Collections.unmodifiableList(pixelArrayList);
	}
}
