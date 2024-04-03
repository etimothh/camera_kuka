import java.net.*;
import java.io.*;
import java.awt.image.*;
import javax.imageio.ImageIO;

public class receiver {
    private static final int WIDTH = 640;
    private static final int HEIGHT = 480;

    public static void main(String[] args) {
        // TCP/IP connection settings
        int port = 12345; // Example port number

        try {
            // Create server socket
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("Server listening on port " + port);

            while (true) {
                // Accept incoming connection
                Socket clientSocket = serverSocket.accept();
                System.out.println("Connected by " + clientSocket.getInetAddress() + ":" + clientSocket.getPort());

                // Read image data size
                DataInputStream dis = new DataInputStream(clientSocket.getInputStream());
                int imageSize = dis.readInt();

                // Read image data
                byte[] imageBytes = new byte[imageSize];
                dis.readFully(imageBytes);

                // Convert image data to BufferedImage
                BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
                for (int y = 0; y < HEIGHT; y++) {
                    for (int x = 0; x < WIDTH; x++) {
                        int index = (y * WIDTH + x) * 3;
                        int r = imageBytes[index] & 0xFF;
                        int g = imageBytes[index + 1] & 0xFF;
                        int b = imageBytes[index + 2] & 0xFF;
                        int rgb = (r << 16) | (g << 8) | b;
                        image.setRGB(x, y, rgb);
                    }
                }

                // Display image
                ImageIO.write(image, "jpg", new File("received_image.jpg"));

                // Close connection
                clientSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
