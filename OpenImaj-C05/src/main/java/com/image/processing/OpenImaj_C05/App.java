package com.image.processing.OpenImaj_C05;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

import org.openimaj.feature.local.list.LocalFeatureList;
import org.openimaj.feature.local.matcher.BasicMatcher;
import org.openimaj.feature.local.matcher.FastBasicKeypointMatcher;
import org.openimaj.feature.local.matcher.LocalFeatureMatcher;
import org.openimaj.feature.local.matcher.MatchingUtilities;
import org.openimaj.feature.local.matcher.consistent.ConsistentLocalFeatureMatcher2d;
import org.openimaj.image.DisplayUtilities;
import org.openimaj.image.ImageUtilities;
import org.openimaj.image.MBFImage;
import org.openimaj.image.colour.RGBColour;
import org.openimaj.image.feature.local.engine.DoGSIFTEngine;
import org.openimaj.image.feature.local.keypoints.Keypoint;
import org.openimaj.math.geometry.transforms.estimation.RobustAffineTransformEstimator;
import org.openimaj.math.model.fit.RANSAC;



public class App 
{
    public static void main( String[] args )
    {
    	try {
			MBFImage query = ImageUtilities.readMBF(new File("query.jpg"));
			MBFImage target = ImageUtilities.readMBF(new File("target.jpg"));
			DoGSIFTEngine engine = new DoGSIFTEngine();
			LocalFeatureList<Keypoint> queryKeypoints = engine.findFeatures(query.flatten());
			LocalFeatureList<Keypoint> targetKeypoints = engine.findFeatures(target.flatten());
			LocalFeatureMatcher<Keypoint> matcher = new BasicMatcher<Keypoint>(80);
			matcher.setModelFeatures(queryKeypoints);
			matcher.findMatches(targetKeypoints);
			MBFImage basicMatches = MatchingUtilities.drawMatches(query, target, matcher.getMatches(), RGBColour.RED);
			DisplayUtilities.display(basicMatches);
			
			RobustAffineTransformEstimator modelFitter = new RobustAffineTransformEstimator(5.0, 1500,
					  new RANSAC.PercentageInliersStoppingCondition(0.5));
					matcher = new ConsistentLocalFeatureMatcher2d<Keypoint>(
					  new FastBasicKeypointMatcher<Keypoint>(8), modelFitter);

					matcher.setModelFeatures(queryKeypoints);
					matcher.findMatches(targetKeypoints);

					MBFImage consistentMatches = MatchingUtilities.drawMatches(query, target, matcher.getMatches(), 
					  RGBColour.RED);

					DisplayUtilities.display(consistentMatches);
					
					target.drawShape(
							  query.getBounds().transform(modelFitter.getModel().getTransform().inverse()), 3,RGBColour.BLUE);
							DisplayUtilities.display(target);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
