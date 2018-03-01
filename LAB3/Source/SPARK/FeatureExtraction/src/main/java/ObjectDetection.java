import Jama.Matrix;
import org.openimaj.feature.local.list.LocalFeatureList;
import org.openimaj.feature.local.matcher.FastBasicKeypointMatcher;
import org.openimaj.feature.local.matcher.consistent.ConsistentLocalFeatureMatcher2d;
import org.openimaj.image.DisplayUtilities;
import org.openimaj.image.FImage;
import org.openimaj.image.ImageUtilities;
import org.openimaj.image.MBFImage;
import org.openimaj.image.colour.RGBColour;
import org.openimaj.image.colour.Transforms;
import org.openimaj.image.feature.local.engine.DoGSIFTEngine;
import org.openimaj.image.feature.local.keypoints.Keypoint;
import org.openimaj.image.renderer.MBFImageRenderer;
import org.openimaj.math.geometry.point.Point2d;
import org.openimaj.math.geometry.transforms.HomographyModel;
import org.openimaj.math.geometry.transforms.HomographyRefinement;
import org.openimaj.math.geometry.transforms.MatrixTransformProvider;
import org.openimaj.math.geometry.transforms.check.TransformMatrixConditionCheck;
import org.openimaj.math.geometry.transforms.estimation.RobustHomographyEstimator;
import org.openimaj.math.model.fit.RANSAC;
import org.openimaj.video.Video;
import org.openimaj.video.xuggle.XuggleVideo;

import java.awt.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
//import org.openimaj.math.geometry.shape.Polygon;

/**
 * Created by Naga on 16-09-2016.
 */
public class ObjectDetection {

    public static void main(String args[]) throws IOException {
        ObjectMainDetection objectMainDetection = new ObjectMainDetection();
    }
}

class ObjectMainDetection {
    private ConsistentLocalFeatureMatcher2d<Keypoint> matcher1;
    private ConsistentLocalFeatureMatcher2d<Keypoint> matcher2;
    private ConsistentLocalFeatureMatcher2d<Keypoint> matcher3;
    final DoGSIFTEngine engine;

    private MBFImage modelImage1, modelImage2, modelImage3;


    public ObjectMainDetection() throws IOException {
        this.engine = new DoGSIFTEngine();
        this.engine.getOptions().setDoubleInitialImage(true);
        this.matcher1 = new ConsistentLocalFeatureMatcher2d<Keypoint>(
                new FastBasicKeypointMatcher<Keypoint>(8));
        this.matcher2 = new ConsistentLocalFeatureMatcher2d<Keypoint>(
                new FastBasicKeypointMatcher<Keypoint>(8));
        this.matcher3 = new ConsistentLocalFeatureMatcher2d<Keypoint>(
                new FastBasicKeypointMatcher<Keypoint>(8));
        final RobustHomographyEstimator ransac = new RobustHomographyEstimator(0.5, 1500,
                new RANSAC.PercentageInliersStoppingCondition(0.6), HomographyRefinement.NONE,
                new TransformMatrixConditionCheck<HomographyModel>(10000));
        this.matcher1.setFittingModel(ransac);
        this.matcher2.setFittingModel(ransac);
        this.matcher3.setFittingModel(ransac);
        LoadReferenceObject();
        StartVideo();
    }

    public void StartVideo() throws IOException {
        //Video<MBFImage> video = new XuggleVideo(new File("data/videoplayback.mkv"));
        int count1 = 0, count2 = 0, count3 = 0;
        String o1 = "output/happy.txt";
        String o2 = "output/angry.txt";
        String o3 = "output/contempt.txt";
        String o4 = "output/disgust.txt";
        String o5 = "output/excited.txt";
        String o6 = "output/fear.txt";
        String o7 = "output/neutral.txt";
        String o8 = "output/sad.txt";
        String o9 = "output/scared.txt";
        String o10 = "output/surprise.txt";

        FileWriter fw = new FileWriter(o1);
        FileWriter fw = new FileWriter(o2);
        FileWriter fw = new FileWriter(o3);
        FileWriter fw = new FileWriter(o4);
        FileWriter fw = new FileWriter(o5);
        FileWriter fw = new FileWriter(o6);
        FileWriter fw = new FileWriter(o7);
        FileWriter fw = new FileWriter(o8);
        FileWriter fw = new FileWriter(o9);
        FileWriter fw = new FileWriter(o10);

        BufferedWriter bw = new BufferedWriter(fw);
        for (MBFImage mbfImage : video) {
            final LocalFeatureList<Keypoint> kpl = this.engine.findFeatures(Transforms.calculateIntensityNTSC(mbfImage));
            final MBFImageRenderer renderer = mbfImage.createRenderer();
            renderer.drawPoints(kpl, RGBColour.MAGENTA, 3);

            if (this.matcher1.findMatches(kpl)
                    && ((MatrixTransformProvider) this.matcher1.getModel()).getTransform().cond() < 1e6 ) {
                try {
                    final Matrix boundsToPoly = ((MatrixTransformProvider) this.matcher1.getModel()).getTransform()
                            .inverse();

                    if (modelImage1.getBounds().transform(boundsToPoly).isConvex()) {

                        renderer.drawShape(this.modelImage1.getBounds().transform(boundsToPoly), 3, RGBColour.RED);

                        if(count1 <= 10){
                            List<Point2d> vertices = this.modelImage1.getBounds().transform(boundsToPoly).asPolygon().getVertices();
                            int x[] = new int[4], y[] = new int[4];
                            for (int i = 0; i < vertices.size(); i++) {
                                x[i] = (int) vertices.get(i).getX();
                                y[i] = (int) vertices.get(i).getY();
                            }
                            Polygon polygon = new Polygon(x, y, 4);
                            for (int i = 0; i < kpl.size(); i++) {
                                if (polygon.contains(kpl.get(i).getX(), kpl.get(i).getY())) {
                                    double c[] = kpl.get(i).getFeatureVector().asDoubleVector();
                                    bw.write("0,");
                                    for (int j = 0; j < c.length; j++) {
                                        bw.write(c[j] + " ");
                                    }
                                    bw.newLine();
                                }
                            }

                            count1++;
                        }

                    }
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }

            }
            if (this.matcher2.findMatches(kpl)
                    && ((MatrixTransformProvider) this.matcher2.getModel()).getTransform().cond() < 1e6 ) {
                try {
                    final Matrix boundsToPoly = ((MatrixTransformProvider) this.matcher2.getModel()).getTransform()
                            .inverse();

                    renderer.drawShape(this.modelImage2.getBounds().transform(boundsToPoly), 3, RGBColour.RED);

                    if(count2 <= 10){
                        if (modelImage2.getBounds().transform(boundsToPoly).isConvex()) {
                            List<Point2d> vertices = this.modelImage2.getBounds().transform(boundsToPoly).asPolygon().getVertices();
                            int x[] = new int[4], y[] = new int[4];
                            for (int i = 0; i < vertices.size(); i++) {
                                x[i] = (int) vertices.get(i).getX();
                                y[i] = (int) vertices.get(i).getY();
                            }
                            Polygon polygon = new Polygon(x, y, 4);
                            for (int i = 0; i < kpl.size(); i++) {
                                if (polygon.contains(kpl.get(i).getX(), kpl.get(i).getY())) {
                                    double c[] = kpl.get(i).getFeatureVector().asDoubleVector();
                                    bw.write("1,");
                                    for (int j = 0; j < c.length; j++) {
                                        bw.write(c[j] + " ");
                                    }
                                    bw.newLine();
                                }
                            }

                            count2++;
                    }

                    }
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }

            }
            if (this.matcher3.findMatches(kpl)
                    && ((MatrixTransformProvider) this.matcher3.getModel()).getTransform().cond() < 1e6) {
                try {
                    final Matrix boundsToPoly = ((MatrixTransformProvider) this.matcher3.getModel()).getTransform()
                            .inverse();

                    if (modelImage3.getBounds().transform(boundsToPoly).isConvex()) {

                        renderer.drawShape(this.modelImage3.getBounds().transform(boundsToPoly), 3, RGBColour.RED);
                        if (count3 <= 10){
                            List<Point2d> vertices = this.modelImage3.getBounds().transform(boundsToPoly).asPolygon().getVertices();
                            int x[] = new int[4], y[] = new int[4];
                            for (int i = 0; i < vertices.size(); i++) {
                                x[i] = (int) vertices.get(i).getX();
                                y[i] = (int) vertices.get(i).getY();
                            }
                            Polygon polygon = new Polygon(x, y, 4);
                            for (int i = 0; i < kpl.size(); i++) {
                                if (polygon.contains(kpl.get(i).getX(), kpl.get(i).getY())) {
                                    double c[] = kpl.get(i).getFeatureVector().asDoubleVector();
                                    bw.write("2,");
                                    for (int j = 0; j < c.length; j++) {
                                        bw.write(c[j] + " ");
                                    }
                                    bw.newLine();
                                }
                            }

                            count3++;
                        }

                    }
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }

            }
            DisplayUtilities.displayName(mbfImage, "Image");
        }
        bw.close();
    }

    public void LoadReferenceObject() {

        final DoGSIFTEngine engine = new DoGSIFTEngine();
        engine.getOptions().setDoubleInitialImage(true);

        try {
            modelImage1 = ImageUtilities.readMBF(new File("data/train/angry/image_0001.jpg"));
            modelImage2 = ImageUtilities.readMBF(new File("data/train/contempt/image_0043.jpg"));
            modelImage3 = ImageUtilities.readMBF(new File("data/train/disgust/image_0085.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        FImage modelF1 = Transforms.calculateIntensityNTSC(modelImage1);
        this.matcher1.setModelFeatures(engine.findFeatures(modelF1));

        FImage modelF2 = Transforms.calculateIntensityNTSC(modelImage2);
        this.matcher2.setModelFeatures(engine.findFeatures(modelF2));

        FImage modelF3 = Transforms.calculateIntensityNTSC(modelImage3);
        this.matcher3.setModelFeatures(engine.findFeatures(modelF3));

    }


}
