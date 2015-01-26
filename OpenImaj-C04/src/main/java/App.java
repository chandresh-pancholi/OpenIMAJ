import java.net.URL;
import java.util.ArrayList;

import org.openimaj.feature.DoubleFVComparison;
import org.openimaj.image.ImageUtilities;
import org.openimaj.image.pixel.statistics.HistogramModel;
import org.openimaj.math.statistics.distribution.MultidimensionalHistogram;



public class App {
	public static void main(String args[] ) throws Exception{
		final URL[] imageURLs = new URL[] {
								new URL("http://users.ecs.soton.ac.uk/dpd/projects/openimaj/tutorial/hist1.jpg"),
								new URL("http://users.ecs.soton.ac.uk/dpd/projects/openimaj/tutorial/hist2.jpg"),
								new URL("http://users.ecs.soton.ac.uk/dpd/projects/openimaj/tutorial/hist3.jpg")
						};
		 ArrayList<MultidimensionalHistogram> histograms = new ArrayList<MultidimensionalHistogram>();
		 HistogramModel model = new HistogramModel(4, 4, 4);
		 for( URL u : imageURLs ) {
			    model.estimateModel(ImageUtilities.readMBF(u));
			    histograms.add( model.histogram.clone() );
			}
		 for (int i = 0; i < histograms.size(); i++) {
			 for(int j = i; j<histograms.size(); j++){
				 final double distance = histograms.get(i).compare(histograms.get(j), DoubleFVComparison.EUCLIDEAN);
				 System.out.println(distance);
			 }
		 }
	}
}
