import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class PointCloudMerge {

    public static void main(String[] args) {
        // Load or generate point clouds from each corner
        List<PointCloud> pointClouds = new ArrayList<>();
        pointClouds.add(loadPointCloud("corner1.pcd")); // First point cloud at (0, 0)
        pointClouds.add(loadPointCloud("corner2.pcd")); // Second point cloud at (5, 0)
        pointClouds.add(loadPointCloud("corner3.pcd")); // Third point cloud at (5, 5)
        pointClouds.add(loadPointCloud("corner4.pcd")); // Fourth point cloud at (0, 5)

        // Merge point clouds
        PointCloud mergedCloud = mergePointClouds(pointClouds);

        // Save merged point cloud to file
        savePointCloud(mergedCloud, "merged_cloud.pcd");
    }

    private static PointCloud loadPointCloud(String filename) {
        PointCloud cloud = new PointCloud();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(" ");
                double x = Double.parseDouble(parts[0]);
                double y = Double.parseDouble(parts[1]);
                double z = Double.parseDouble(parts[2]);
                cloud.addPoint(new Point(x, y, z));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return cloud;
    }

    private static void savePointCloud(PointCloud cloud, String filename) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(filename))) {
            for (Point point : cloud.getPoints()) {
                pw.println(point.getX() + " " + point.getY() + " " + point.getZ());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static PointCloud mergePointClouds(List<PointCloud> pointClouds) {
        PointCloud mergedCloud = new PointCloud();
        for (PointCloud cloud : pointClouds) {
            for (Point point : cloud.getPoints()) {
                mergedCloud.addPoint(point);
            }
        }
        return mergedCloud;
    }
}

class Point {
    private double x;
    private double y;
    private double z;

    public Point(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }
}

class PointCloud {
    private List<Point> points = new ArrayList<>();

    public void addPoint(Point point) {
        points.add(point);
    }

    public List<Point> getPoints() {
        return points;
    }
}
