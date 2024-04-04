import java.io.*;
import java.net.*;

/**
 * Point Cloud Receiver
 * 
 * This program connects to a trigger server to receive point cloud data over TCP/IP. 
 * Upon successful connection, it sends a trigger command to initiate the data transfer.
 * The received point cloud data is then saved to a file named "point_cloud.pcd".
 * 
 * Author: [Timothé Kobak]
 * Date: [Date]
 */

public class receive_pcd {
    public static void main(String[] args) {
        String host = "127.0.0.1";
        int port = 12345;

        try {
            // Connect to the trigger server
            System.out.println("Trying to connect to trigger server at " + host + ":" + port);
            Socket socket = new Socket(host, port);
            System.out.println("Connected to trigger server at " + host + ":" + port);

            // Send trigger command
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            System.out.println("Sending trigger command...");
            out.println("trigger");

            // Close the connection
            socket.close();
            System.out.println("Connection closed");
            
            // Receive point cloud data
            System.out.println("Waiting for point cloud data...");
            int dataSize;
            byte[] dataBuffer = new byte[1024];
            InputStream inputStream = socket.getInputStream();
            DataInputStream dataInputStream = new DataInputStream(inputStream);

            // Read the size of the data
            dataSize = dataInputStream.readInt();
            System.out.println("Received data size: " + dataSize);

            // Read the data itself
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            while (dataSize > 0) {
                int bytesRead = inputStream.read(dataBuffer, 0, Math.min(dataSize, dataBuffer.length));
                byteArrayOutputStream.write(dataBuffer, 0, bytesRead);
                dataSize -= bytesRead;
            }

            // Save the point cloud data to a file
            String filePath = "point_cloud.pcd";
            FileOutputStream fileOutputStream = new FileOutputStream(filePath);
            byteArrayOutputStream.writeTo(fileOutputStream);
            fileOutputStream.close();
            System.out.println("Point cloud data saved to " + filePath);

        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
